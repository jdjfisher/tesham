package com.graphics.lighting;

import com.graphics.FrameBuffer;
import com.maths.Matrix4f;

import static org.lwjgl.opengl.ARBFramebufferObject.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

public class ShadowMap extends FrameBuffer {

    public static final int SHADOW_MAP_WIDTH = 4096;

    public static final int SHADOW_MAP_HEIGHT = 4096;

    private Matrix4f lightClipMatrix;

    public ShadowMap() throws Exception {
        super();

        addTexture2DAttachment("depthMap_Texture", GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_FLOAT, GL_DEPTH_ATTACHMENT, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, ()->{
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f });
        });

        setDrawBuffers(GL_NONE);
        setReadBuffer(GL_NONE);
        test();
    }

    public Matrix4f getLightClipMatrix() {
        return lightClipMatrix;
    }

    public void setLightClipMatrix(Matrix4f lightClipMatrix) {
        this.lightClipMatrix = lightClipMatrix;
    }

    @Override
    public void bind(){
        super.bind();
        setViewPort();
    }

    public static void setViewPort(){
        glViewport(0, 0, ShadowMap.SHADOW_MAP_WIDTH, ShadowMap.SHADOW_MAP_HEIGHT);
    }
}
