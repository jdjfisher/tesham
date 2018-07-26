package com.graphics;


import com.engine.core.Window;
import com.engine.items.Camera;
import com.graphics.component.mesh._3D.Mesh3D;
import com.maths.Matrix4f;
import com.utils.GenerateMesh;

import java.awt.*;

import static org.lwjgl.opengl.ARBFramebufferObject.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

public class RendererEngine {
    private ShaderProgram basicShader;
    private Mesh3D testMesh;
    private FrameBuffer gBuffer;

    public RendererEngine() {
    }

    public void init() throws Exception{
        basicShader = new ShaderProgram("basic", "basic", "basic");
        basicShader.bind();
//        basicShader.setColourRGBUniform("colour", Color.WHITE);

        testMesh = GenerateMesh.sphere(1, 50);


//        int initialWidth = 0; int initialHeight = 0;
//        gBuffer = new FrameBuffer();
//
//        gBuffer.addTexture2DAttachment("fragmentGlobalViewPosition_Texture", GL_RGBA16F, GL_RGB, GL_FLOAT, GL_COLOR_ATTACHMENT0, initialWidth, initialHeight, ()->{
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//        });
//        gBuffer.addTexture2DAttachment("fragmentGlobalViewNormal_Texture", GL_RGBA16F, GL_RGBA, GL_FLOAT, GL_COLOR_ATTACHMENT1, initialWidth, initialHeight, ()->{
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//        });
//        gBuffer.addTexture2DAttachment("diffuseComponent_Texture", GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, GL_COLOR_ATTACHMENT2, initialWidth, initialHeight, ()->{
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//        });
//        gBuffer.addRenderBufferAttachment("depth_RenderBuffer", GL_DEPTH_COMPONENT, GL_DEPTH_ATTACHMENT, initialWidth, initialHeight);
//
//        gBuffer.setDrawBuffers(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2);
//        gBuffer.test();
    }

    public void render(Window window, Camera camera) throws Exception{
        FrameBuffer.clearBuffers();

        basicShader.setMatrix4fUniform("PV_Matrix", Matrix4f.Multiply(window.getPerspectiveMatrix(), camera.getViewMatrix()));
        basicShader.setMatrix4fUniform("W_Matrix", Matrix4f.Identity());

        testMesh.render();
    }

    public void dispose(){
        basicShader.dispose();
        testMesh.dispose();
    }
}
