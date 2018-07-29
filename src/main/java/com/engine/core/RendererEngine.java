package com.engine.core;


import com.componentSystem.TransformComponentManager;
import com.engine.items.World;
import com.graphics.opengl.RenderBuffer;
import com.graphics.opengl.Texture;
import com.graphics.opengl.mesh._2D.Mesh2D;
import com.graphics.opengl.mesh._3D.Mesh3D;
import com.graphics.lighting.Attenuation;
import com.graphics.lighting.DirectionalLight;
import com.graphics.lighting.PointLight;
import com.graphics.lighting.SpotLight;
import com.graphics.opengl.FrameBuffer;
import com.graphics.opengl.ShaderProgram;
import com.maths.Matrix4f;
import com.maths.vectors.Vector3f;
import com.utils.DataUtils;
import com.utils.GenerateMesh;

import java.awt.*;
import java.util.HashMap;

import static com.engine.core.Options.getAmbientLightBrightness;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

public class RendererEngine {
    private boolean disposed; //Implement

    private ShaderProgram basicShader;
    private ShaderProgram sceneGeometryShader;
    private ShaderProgram deferredLighitngShader;

    private FrameBuffer gBuffer;

    private Mesh3D testMesh;
    private Mesh2D quad;

    //Temp (Move to component system)

    DirectionalLight directionalLight;
    HashMap<String, PointLight> pointLights;
    HashMap<String, SpotLight> spotLights;

    public RendererEngine()
    {
        disposed = true;
    }

    public void init() throws Exception
    {
        Texture.DEFAULT_TEXTURE = Texture.fromSolidColor(Color.DARK_GRAY, 32, 32);

        basicShader = new ShaderProgram("basic", "basic", "basic");
        sceneGeometryShader = new ShaderProgram("sceneGeometryShader", "sceneGeometry", "sceneGeometry");
        deferredLighitngShader = new ShaderProgram("deferredLighitngShader", "texturedQuad", "deferredLighting");


        int initialWidth = 1, initialHeight = 1;
        gBuffer = new FrameBuffer();

        Texture fragmentWorldViewPosition_Texture = new Texture(GL_RGBA16F, GL_RGB, GL_FLOAT, initialWidth, initialHeight);
        fragmentWorldViewPosition_Texture.setFilter(GL_NEAREST, GL_NEAREST);
        gBuffer.addTextureAttachment("fragmentWorldViewPosition_Texture", GL_COLOR_ATTACHMENT0, fragmentWorldViewPosition_Texture);

        Texture fragmentWorldViewNormal_Texture = new Texture(GL_RGBA16F, GL_RGBA, GL_FLOAT, initialWidth, initialHeight);
        fragmentWorldViewNormal_Texture.setFilter(GL_NEAREST, GL_NEAREST);
        gBuffer.addTextureAttachment("fragmentWorldViewNormal_Texture", GL_COLOR_ATTACHMENT1, fragmentWorldViewNormal_Texture);

        Texture diffuseComponent_Texture = new Texture(GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, initialWidth, initialHeight);
        diffuseComponent_Texture.setFilter(GL_NEAREST, GL_NEAREST);
        gBuffer.addTextureAttachment("diffuseComponent_Texture", GL_COLOR_ATTACHMENT2, diffuseComponent_Texture);

        gBuffer.addRenderBufferAttachment("depth_RenderBuffer", GL_DEPTH_ATTACHMENT, new RenderBuffer(GL_DEPTH_COMPONENT, initialWidth, initialHeight));

        gBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2);
        gBuffer.test();


        testMesh = GenerateMesh.sphere(1, 50);
        quad = GenerateMesh.screenRenderQuad();

        //Temp

        directionalLight = new DirectionalLight(Color.WHITE, 0.1f);
        directionalLight.getRotation().set(World.FORWARD_VECTOR, new Vector3f(0,1,0));
        pointLights = new HashMap<>();
        pointLights.put("p1", new PointLight(new Color(233, 99, 22), 0.3f, new Attenuation(1.0f, 0.22f, 0.20f)));
        spotLights = new HashMap<>();

        disposed = false;
    }



    public void render(Window window, World world) throws Exception
    {
        if(disposed)
        {
            throw new RuntimeException("Render engine has been disposed");
        }

        world.getCamera().preRender();

//        basicRender(window, world);
        litRender(window, world);
    }

    private void basicRender(Window window, World world) throws Exception
    {
        FrameBuffer.DEFAULT_FRAMEBUFFER.bind();
        FrameBuffer.clearBuffers();

        basicShader.setUniform("PV_Matrix", Matrix4f.Multiply(window.getPerspectiveMatrix(), world.getCamera().getViewMatrix()));

        for(TransformComponentManager.TransformComponent tc : world.getTransformComponentManager().getComponents()) {
            basicShader.setUniform("W_Matrix", Matrix4f.Translation(tc.getPosition()));

            testMesh.render();
        }
    }

    private void litRender(Window window, World world) throws Exception
    {
        gBuffer.bind();
        gBuffer.resizeAttachments(window.getWidth(), window.getHeight());
        FrameBuffer.clearBuffers();


        sceneGeometryShader.setUniform("hasNormalMap", false);
        sceneGeometryShader.setUniform("hasDiffuseTexture", false);
        sceneGeometryShader.setUniform("hasSpecularMap", false);
        sceneGeometryShader.setUniform("reflectance", 16);
        sceneGeometryShader.setUniform("diffuseColour", DataUtils.toVector3f(Color.ORANGE));
        sceneGeometryShader.setUniform("projectionMatrix", window.getPerspectiveMatrix());

        for(TransformComponentManager.TransformComponent tc : world.getTransformComponentManager().getComponents())
        {
            sceneGeometryShader.setUniform("worldViewMatrix", Matrix4f.Multiply(world.getCamera().getViewMatrix(), tc.getTransformation()));

            testMesh.render();
        }

        FrameBuffer.DEFAULT_FRAMEBUFFER.bind();
        FrameBuffer.clearBuffers();
        
        deferredLighitngShader.setUniform("gFragmentWorldViewPosition", 0);
        deferredLighitngShader.setUniform("gFragmentWorldViewNormal", 1);
        deferredLighitngShader.setUniform("gDiffuseComponent", 2);
        
        gBuffer.getTexture2DAttachment("fragmentWorldViewPosition_Texture").bindToUnit(0);
        gBuffer.getTexture2DAttachment("fragmentWorldViewNormal_Texture").bindToUnit(1);
        gBuffer.getTexture2DAttachment("diffuseComponent_Texture").bindToUnit(2);


        deferredLighitngShader.setUniform("ambientLightBrightness", getAmbientLightBrightness());
        deferredLighitngShader.setUniform("viewMatrix", world.getCamera().getViewMatrix());

        deferredLighitngShader.setUniform("directionalLight", directionalLight);

        deferredLighitngShader.setUniform("activePointLights", pointLights.size());
        deferredLighitngShader.setUniform("pointLights", pointLights.values().toArray(new PointLight[pointLights.values().size()]));

        deferredLighitngShader.setUniform("activeSpotLights", spotLights.size());
        deferredLighitngShader.setUniform("spotLights", spotLights.values().toArray(new SpotLight[spotLights.values().size()]));

        quad.render();
    }


    public void dispose()
    {
        if(disposed)
        {
            throw new RuntimeException("Render engine has already been disposed");
        }

        basicShader.dispose();
        sceneGeometryShader.dispose();
        deferredLighitngShader.dispose();

        gBuffer.dispose();

        testMesh.dispose();
        quad.dispose();

        disposed = true;
    }
}
