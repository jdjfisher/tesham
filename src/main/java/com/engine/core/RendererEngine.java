package com.engine.core;


import com.engine.items.World;
import com.graphics.opengl.*;
import com.graphics.opengl.mesh.Mesh;
import com.graphics.lighting.Attenuation;
import com.graphics.lighting.DirectionalLight;
import com.graphics.lighting.PointLight;
import com.graphics.lighting.SpotLight;
import com.maths.Matrix4f;
import com.maths.RNG;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import com.utils.DataUtils;
import com.utils.GenerateMesh;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.engine.core.Options.getAmbientLightBrightness;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

public class RendererEngine {
    private boolean disposed;

    private ShaderProgram basicShader;
    private ShaderProgram sceneGeometryShader;
    private ShaderProgram deferredLighitngShader;
    private ShaderProgram lampShader;
    private ShaderProgram blurShader;
    private ShaderProgram toneMappingShader;
    private ShaderProgram fxaaShader;

    private FrameBuffer gBuffer;
    private FrameBuffer preToneMappingBuffer;
    private FrameBuffer horizontalBlurBuffer;
    private FrameBuffer verticalBlurBuffer;
    private FrameBuffer preFxaaBuffer;

    private QuadMesh quadMesh;

    //Temp (Move to component system)

    private Mesh sphereMesh;
    private Mesh pyramidMesh;
    private DirectionalLight directionalLight;
    private HashMap<String, PointLight> pointLights;
    private HashMap<String, SpotLight> spotLights;
    private ArrayList<Vector3f> randomPositions;

    //

    public RendererEngine()
    {
        disposed = true;
    }

    public void init(Window window) throws Exception
    {
        Texture.DEFAULT_TEXTURE = Texture.fromSolidColor(Color.DARK_GRAY, 32, 32);

        basicShader = new ShaderProgram("basic", "basic", "basic");
        sceneGeometryShader = new ShaderProgram("sceneGeometry", "sceneGeometry", "sceneGeometry");
        deferredLighitngShader = new ShaderProgram("deferredLighitng", "texturedQuad", "deferredLighting");
        lampShader = new ShaderProgram("lamp", "basic", "lamp");
        blurShader = new ShaderProgram("gaussianBlur", "texturedQuad", "gaussianBlur");
        toneMappingShader = new ShaderProgram("toneMapping", "texturedQuad", "toneMapping");
        fxaaShader = new ShaderProgram("fxaa", "texturedQuad", "fxaa");

        int initialWidth = window.getWidth(), initialHeight = window.getHeight();

        gBuffer = new FrameBuffer();
        gBuffer.addTextureAttachment("fragmentPosition_W_Texture", GL_COLOR_ATTACHMENT0,  new Texture(GL_RGBA16F, GL_RGB, GL_FLOAT, initialWidth, initialHeight, GL_NEAREST, GL_NEAREST));
        gBuffer.addTextureAttachment("fragmentNormal_W_Texture", GL_COLOR_ATTACHMENT1, new Texture(GL_RGBA16F, GL_RGBA, GL_FLOAT, initialWidth, initialHeight, GL_NEAREST, GL_NEAREST));
        gBuffer.addTextureAttachment("diffuseComponent_Texture", GL_COLOR_ATTACHMENT2, new Texture(GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, initialWidth, initialHeight, GL_NEAREST, GL_NEAREST));
        gBuffer.addRenderBufferAttachment("depth_RenderBuffer", GL_DEPTH_ATTACHMENT, new RenderBuffer(GL_DEPTH_COMPONENT, initialWidth, initialHeight));
        gBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2);
        gBuffer.test();


        preToneMappingBuffer = new FrameBuffer();
        preToneMappingBuffer.addTextureAttachment("primaryScene_Texture", GL_COLOR_ATTACHMENT0, new Texture(GL_RGBA16F, GL_RGBA, GL_FLOAT, initialWidth, initialHeight, GL_LINEAR, GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE));
        preToneMappingBuffer.addTextureAttachment("bloomHighlights_Texture", GL_COLOR_ATTACHMENT1, new Texture(GL_RGBA16F, GL_RGBA, GL_FLOAT, initialWidth, initialHeight, GL_LINEAR, GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE));
        preToneMappingBuffer.addRenderBufferAttachment("depth_RenderBuffer", GL_DEPTH_ATTACHMENT, new RenderBuffer(GL_DEPTH_COMPONENT, initialWidth, initialHeight));
        preToneMappingBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1);
        preToneMappingBuffer.test();


        horizontalBlurBuffer = new FrameBuffer();
        horizontalBlurBuffer.addTextureAttachment("preBlur_Texture", GL_COLOR_ATTACHMENT0, new Texture(GL_RGBA16F, GL_RGB, GL_FLOAT, initialWidth, initialHeight, GL_LINEAR, GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE));
        horizontalBlurBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0);
        horizontalBlurBuffer.test();


        verticalBlurBuffer = new FrameBuffer();
        verticalBlurBuffer.addTextureAttachment("preBlur_Texture", GL_COLOR_ATTACHMENT0, new Texture(GL_RGBA16F, GL_RGB, GL_FLOAT, initialWidth, initialHeight, GL_LINEAR, GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE));
        verticalBlurBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0);
        verticalBlurBuffer.test();


        preFxaaBuffer = new FrameBuffer();
        preFxaaBuffer.addTextureAttachment("preFxaa_Texture", GL_COLOR_ATTACHMENT0, new Texture(GL_RGBA16F, GL_RGB, GL_FLOAT, initialWidth, initialHeight, GL_LINEAR, GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE));
        preFxaaBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0);
        preFxaaBuffer.test();


        window.setResizeCallback((width, height) ->
        {
            gBuffer.resizeAttachments(width, height);
            preToneMappingBuffer.resizeAttachments(width, height);
            horizontalBlurBuffer.resizeAttachments(width, height);
            verticalBlurBuffer.resizeAttachments(width, height);
            preFxaaBuffer.resizeAttachments(width, height);
        });


        sphereMesh = GenerateMesh.sphere(1, 50);
        pyramidMesh = GenerateMesh.cone(0.5f,3,1);
        quadMesh = new QuadMesh();

        //Temp

        directionalLight = new DirectionalLight(Color.WHITE, 0.01f);
        directionalLight.getRotation().set(World.FORWARD_VECTOR, new Vector3f(0,1,0));
        pointLights = new HashMap<>();
        pointLights.put("p1", new PointLight(new Color(233, 99, 22), 10f, new Attenuation(1.0f, 0.22f, 0.20f)));
        spotLights = new HashMap<>();
        spotLights.put("s1", new SpotLight(Color.WHITE, 5, 25f, 30f, new Attenuation(1.0f, 0.027f, 0.0028f)));

        final int k = 15;
        randomPositions = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            randomPositions.add(new Vector3f(RNG.Int(-k, k), RNG.Int(-k, k), RNG.Int(-k, k)));
        }

        disposed = false;
    }



    public void render(Window window, World world) throws Exception
    {
        if(disposed)
        {
            throw new RuntimeException("Render engine has been disposed");
        }

//        spotLights.get("s1").getTransformationSet().getRotation().set(world.getCamera().getRotation());
//        spotLights.get("s1").getTransformationSet().getPosition().set(world.getCamera().getPosition());

        world.getCamera().preRender();

//        basicRender(window, world);
        litRender(window, world);
    }

    private void basicRender(Window window, World world) throws Exception
    {
        FrameBuffer.DEFAULT_FRAMEBUFFER.bind();
        FrameBuffer.clearBuffers();

        basicShader.setUniform("PV_Matrix", Matrix4f.Multiply(window.getPerspectiveMatrix(), world.getCamera().getViewMatrix()));

        for(Vector3f pos : randomPositions)
        {
            basicShader.setUniform("W_Matrix", Matrix4f.Translation(pos));

            sphereMesh.render();
        }
    }

    private void litRender(Window window, World world) throws Exception
    {
        Matrix4f PV_Matrix = Matrix4f.Multiply(window.getPerspectiveMatrix(), world.getCamera().getViewMatrix());

        gBuffer.bind();
        FrameBuffer.clearBuffers();


        sceneGeometryShader.setUniform("hasNormalMap", false);
        sceneGeometryShader.setUniform("hasDiffuseTexture", false);
        sceneGeometryShader.setUniform("hasSpecularMap", false);
        sceneGeometryShader.setUniform("reflectance", 16);
        sceneGeometryShader.setUniform("diffuseColour", DataUtils.toVector3f(Color.ORANGE));

        sceneGeometryShader.setUniform("PV_Matrix", PV_Matrix);

        for (Vector3f vec : randomPositions)
        {
            sceneGeometryShader.setUniform("W_Matrix", Matrix4f.Translation(vec));

            sphereMesh.render();
        }

        preToneMappingBuffer.bind();
        FrameBuffer.clearBuffers();
        
        deferredLighitngShader.setUniform("fragmentPosition_W_Sampler", 0);
        deferredLighitngShader.setUniform("fragmentNormal_W_Sampler", 1);
        deferredLighitngShader.setUniform("diffuseComponent_Sampler", 2);
        
        gBuffer.getTexture2DAttachment("fragmentPosition_W_Texture").bindToUnit(0);
        gBuffer.getTexture2DAttachment("fragmentNormal_W_Texture").bindToUnit(1);
        gBuffer.getTexture2DAttachment("diffuseComponent_Texture").bindToUnit(2);

        deferredLighitngShader.setUniform("cameraPosition", world.getCamera().getPosition());
        deferredLighitngShader.setUniform("ambientLightBrightness", getAmbientLightBrightness());
        deferredLighitngShader.setUniform("directionalLight", directionalLight);
        deferredLighitngShader.setUniform("activePointLights", pointLights.size());
        deferredLighitngShader.setUniform("pointLights", pointLights.values().toArray(new PointLight[pointLights.values().size()]));
        deferredLighitngShader.setUniform("activeSpotLights", spotLights.size());
        deferredLighitngShader.setUniform("spotLights", spotLights.values().toArray(new SpotLight[spotLights.values().size()]));

        quadMesh.render();

//        FrameBuffer.blit(gBuffer, preToneMappingBuffer, window.getWidth(), window.getHeight(), GL_DEPTH_BUFFER_BIT, GL_NEAREST);

        lampShader.setUniform("PV_Matrix", PV_Matrix);

//        for()
//        {
//            lampShader.setUniform("W_Matrix", Matrix4f.Translation());
//            lampShader.setUniform("lampIntensity", );
//            lampShader.setUniform("lampColour", );
//            .render();
//        }

        blurShader.setUniform("texture_Sampler", 3);

        boolean horizontal = false;

        for(int i = 0; i < 10; i++)
        {
            horizontal = !horizontal;
            blurShader.setUniform("horizontal", horizontal);
            (horizontal ? horizontalBlurBuffer : verticalBlurBuffer).getTexture2DAttachment("preBlur_Texture").bindToUnit(3);
            quadMesh.render();
        }


        preFxaaBuffer.bind();
        FrameBuffer.clearBuffers();

        toneMappingShader.setUniform("primaryScene_Sampler", 4);
        toneMappingShader.setUniform("bloomHighlights_Sampler", 5);
        toneMappingShader.setUniform("useHDR", true);
        toneMappingShader.setUniform("correctGamma", true);
        toneMappingShader.setUniform("exposure", 0.45f);
        toneMappingShader.setUniform("gamma", 0.8f);

        preToneMappingBuffer.getTexture2DAttachment("primaryScene_Texture").bindToUnit(4);
        (horizontal ? horizontalBlurBuffer : verticalBlurBuffer).getTexture2DAttachment("preBlur_Texture").bindToUnit(5);

        quadMesh.render();


        FrameBuffer.DEFAULT_FRAMEBUFFER.bind();
        FrameBuffer.clearBuffers();

        fxaaShader.setUniform("enabled", true);
        fxaaShader.setUniform("screenResolution", new Vector2f(window.getWidth(), window.getHeight()));
        fxaaShader.setUniform("preFxaa_Sampler", 6);

        preFxaaBuffer.getTexture2DAttachment("preFxaa_Texture").bindToUnit(6);

        quadMesh.render();
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
        lampShader.dispose();
        blurShader.dispose();
        toneMappingShader.dispose();
        fxaaShader.dispose();

        gBuffer.dispose();
        preToneMappingBuffer.dispose();
        horizontalBlurBuffer.dispose();
        verticalBlurBuffer.dispose();
        preFxaaBuffer.dispose();

        sphereMesh.dispose();
        pyramidMesh.dispose();
        quadMesh.dispose();

        disposed = true;
    }
}
