package engine.core;


import componentSystem.World;
import componentSystem.components.MaterialComponent;
import componentSystem.components.MeshComponent;
import componentSystem.components.PointLightComponent;
import componentSystem.components.SpotLightComponent;
import graphics.*;
import maths.Matrix4f;
import maths.vectors.Vector2f;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.ARBFramebufferObject.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;

public class RendererEngine
{
    private boolean disposed;
    private boolean exportBuffer;

    private ShaderProgram basicShader;
    private ShaderProgram sceneGeometryShader;
    private ShaderProgram deferredLightingShader;
    private ShaderProgram skyboxShader;
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

    public RendererEngine()
    {
        disposed = true;
        exportBuffer = false;
    }

    public void init(Window window) throws Exception
    {
        Texture.DEFAULT_TEXTURE = Texture.fromSolidColor(Color.DARK_GRAY, 32, 32);

        basicShader = new ShaderProgram("basic", "basic", "basic");
        sceneGeometryShader = new ShaderProgram("sceneGeometry", "sceneGeometry", "sceneGeometry");
        deferredLightingShader = new ShaderProgram("deferredLighitng", "texturedQuad", "deferredLighting");
        skyboxShader = new ShaderProgram("skybox", "basic", "skybox");
        lampShader = new ShaderProgram("lamp", "basic", "lamp");
        blurShader = new ShaderProgram("gaussianBlur", "texturedQuad", "gaussianBlur");
        toneMappingShader = new ShaderProgram("toneMapping", "texturedQuad", "toneMapping");
        fxaaShader = new ShaderProgram("fxaa", "texturedQuad", "fxaa");

        int initialWidth = window.getWidth(), initialHeight = window.getHeight();

        gBuffer = new FrameBuffer();
        gBuffer.addTextureAttachment("fragmentPosition_W_Texture", GL_COLOR_ATTACHMENT0, new Texture(GL_RGBA16F, GL_RGB, GL_FLOAT, initialWidth, initialHeight, GL_NEAREST, GL_NEAREST));
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

        quadMesh = new QuadMesh();

        disposed = false;
    }

    public void render(Window window, World world) throws Exception
    {
        if (disposed)
        {
            throw new RuntimeException("Render engine has been disposed");
        }

        if (Options.isCullingFacesEnabled())
        {
            if (!glIsEnabled(GL_CULL_FACE))
            {
                glEnable(GL_CULL_FACE);
            }
        }
        else
        {
            if (glIsEnabled(GL_CULL_FACE))
            {
                glDisable(GL_CULL_FACE);
            }
        }

        glPolygonMode(GL_FRONT_AND_BACK, Options.isWireframeMode() ? GL_LINE : GL_FILL);

        Matrix4f PV_Matrix = Matrix4f.Multiply(window.getPerspectiveMatrix(), world.getCamera().getViewMatrix());

        gBuffer.bind();
        FrameBuffer.clearBuffers();

        sceneGeometryShader.setUniform("PV_Matrix", PV_Matrix);
        sceneGeometryShader.setUniform("diffuseTexture_Sampler", 0);
        sceneGeometryShader.setUniform("normalMap_Sampler", 1);
        sceneGeometryShader.setUniform("specularMap_Sampler", 2);

        world.forEachEntity(e ->
        {
            if (e.hasComponent(MeshComponent.class) && e.hasComponent(MaterialComponent.class))// oi
            {
                MaterialComponent material = e.getComponent(MaterialComponent.class);

                sceneGeometryShader.setUniform("W_Matrix", e.transformComponent.getWorldTransform());
                sceneGeometryShader.setUniform("diffuseColour", material.getColour());
                sceneGeometryShader.setUniform("reflectance", material.getReflectance());

                if (material.hasDiffuseTexture())
                {
                    material.getDiffuseTexture().bindToUnit(0);
                    sceneGeometryShader.setUniform("hasDiffuseTexture", true); // Store bool for the current state of uniform and test that before updating uniform
                }
                else
                {
                    sceneGeometryShader.setUniform("hasDiffuseTexture", false);
                }

                if (material.hasNormalMap())
                {
                    material.getNormalMap().bindToUnit(1);
                    sceneGeometryShader.setUniform("hasNormalMap", true);
                }
                else
                {
                    sceneGeometryShader.setUniform("hasNormalMap", false);
                }

                if (material.hasSpecularMap())
                {
                    material.getSpecularMap().bindToUnit(2);
                    sceneGeometryShader.setUniform("hasSpecularMap", true);
                }
                else
                {
                    sceneGeometryShader.setUniform("hasSpecularMap", false);
                }

                e.getComponent(MeshComponent.class).getMesh().render();
            }
        });

        if (Options.isWireframeMode())
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        preToneMappingBuffer.bind();
        FrameBuffer.clearBuffers();

        deferredLightingShader.setUniform("fragmentPosition_W_Sampler", 3);
        deferredLightingShader.setUniform("fragmentNormal_W_Sampler", 4);
        deferredLightingShader.setUniform("diffuseComponent_Sampler", 5);

        gBuffer.getTexture2DAttachment("fragmentPosition_W_Texture").bindToUnit(3);
        gBuffer.getTexture2DAttachment("fragmentNormal_W_Texture").bindToUnit(4);
        gBuffer.getTexture2DAttachment("diffuseComponent_Texture").bindToUnit(5);

        deferredLightingShader.setUniform("cameraPosition", world.getCamera().getPosition());
        deferredLightingShader.setUniform("ambientLightBrightness", Options.getAmbientLightBrightness());
        deferredLightingShader.setUniform("directionalLight", world.getDirectionalLight());

        ArrayList<PointLightComponent> pointLights = new ArrayList<>();

        world.forEachEntityWithComponent(PointLightComponent.class, e ->
        {
            PointLightComponent plc = e.getComponent(PointLightComponent.class);

            if (plc.getLight().isEnabled())
            {
                pointLights.add(plc);
            }
        });

        deferredLightingShader.setUniform("activePointLights", pointLights.size());
        deferredLightingShader.setUniform("pointLights", pointLights.toArray(new PointLightComponent[pointLights.size()]));

        ArrayList<SpotLightComponent> spotLights = new ArrayList<>();

        world.forEachEntityWithComponent(SpotLightComponent.class, e ->
        {
            SpotLightComponent slc = e.getComponent(SpotLightComponent.class);

            if (slc.getLight().isEnabled())
            {
                spotLights.add(slc);
            }
        });

        deferredLightingShader.setUniform("activeSpotLights", spotLights.size());
        deferredLightingShader.setUniform("spotLights", spotLights.toArray(new SpotLightComponent[spotLights.size()]));

        quadMesh.render();

        FrameBuffer.blit(gBuffer, preToneMappingBuffer, window.getWidth(), window.getHeight(), GL_DEPTH_BUFFER_BIT, GL_NEAREST);


        lampShader.setUniform("PV_Matrix", PV_Matrix);


        world.forEachEntity(e ->
        {
            if (e.hasComponent(MeshComponent.class) && e.hasComponent(PointLightComponent.class))
            {
                PointLightComponent plc = e.getComponent(PointLightComponent.class);
                lampShader.setUniform("W_Matrix", e.transformComponent.getWorldTransform());
                lampShader.setUniform("lampIntensity", plc.getLight().getIntensity());
                lampShader.setUniform("lampColour", plc.getLight().getColor());

                e.getComponent(MeshComponent.class).getMesh().render();
            }
        });

        world.forEachEntity(e ->
        {
            if (e.hasComponent(MeshComponent.class) && e.hasComponent(SpotLightComponent.class))
            {
                SpotLightComponent slc = e.getComponent(SpotLightComponent.class);
                lampShader.setUniform("W_Matrix", e.transformComponent.getWorldTransform());
                lampShader.setUniform("lampIntensity", slc.getLight().getIntensity());
                lampShader.setUniform("lampColour", slc.getLight().getColor());

                e.getComponent(MeshComponent.class).getMesh().render();
            }
        });


        boolean horizontal = false;
        blurShader.setUniform("texture_Sampler", 6);
        preToneMappingBuffer.getTexture2DAttachment("bloomHighlights_Texture").bindToUnit(6);

        for (int i = 0; i < 10; i++)
        {
            blurShader.setUniform("horizontal", horizontal);
            (horizontal ? horizontalBlurBuffer : verticalBlurBuffer).bind();
            quadMesh.render();
            (horizontal ? horizontalBlurBuffer : verticalBlurBuffer).getTexture2DAttachment("preBlur_Texture").bindToUnit(6);

            horizontal = !horizontal;
        }

        preFxaaBuffer.bind();
        FrameBuffer.clearBuffers();

        toneMappingShader.setUniform("bloomHighlights_Sampler", 6);
        toneMappingShader.setUniform("primaryScene_Sampler", 7);
        toneMappingShader.setUniform("useHDR", true);
        toneMappingShader.setUniform("correctGamma", true);
        toneMappingShader.setUniform("exposure", 0.45f);
        toneMappingShader.setUniform("gamma", 0.8f);

        preToneMappingBuffer.getTexture2DAttachment("primaryScene_Texture").bindToUnit(7);

        quadMesh.render();


        FrameBuffer.DEFAULT_FRAMEBUFFER.bind();
        FrameBuffer.clearBuffers();

        fxaaShader.setUniform("enabled", true);
        fxaaShader.setUniform("screenResolution", new Vector2f(window.getWidth(), window.getHeight()));
        fxaaShader.setUniform("preFxaa_Sampler", 8);

        preFxaaBuffer.getTexture2DAttachment("preFxaa_Texture").bindToUnit(8);

        quadMesh.render();
    }

    public void takeScreenshot(){
        exportBuffer = true;
    }

    private void doScreenshot(Window window)
    {
        if(exportBuffer)
        {
            exportBuffer = false;

            ByteBuffer pixels = ByteBuffer.allocateDirect(4 * window.getWidth() * window.getHeight());

            glReadBuffer(GL_BACK);
            glReadPixels(0, 0, window.getWidth(), window.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        }
    }

    public void dispose()
    {
        if (disposed)
        {
            throw new RuntimeException("Render engine has already been disposed");
        }

        basicShader.dispose();
        sceneGeometryShader.dispose();
        deferredLightingShader.dispose();
        skyboxShader.dispose();
        lampShader.dispose();
        blurShader.dispose();
        toneMappingShader.dispose();
        fxaaShader.dispose();

        gBuffer.dispose();
        preToneMappingBuffer.dispose();
        horizontalBlurBuffer.dispose();
        verticalBlurBuffer.dispose();
        preFxaaBuffer.dispose();

        quadMesh.dispose();

        disposed = true;
    }
}
