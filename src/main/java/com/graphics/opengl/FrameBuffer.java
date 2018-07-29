package com.graphics;

import com.graphics.component.RenderBuffer;
import com.graphics.component.Texture;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by fisherj16 on 18/10/2017.
 */
public class FrameBuffer {
    private static final int default_FBO = 0;

    private final int frameBufferId;
    private boolean disposed;

    private final HashMap<String, Texture> textureAttachments;
    private final HashMap<String, RenderBuffer> renderBufferAttachments;

    public FrameBuffer()
    {
        frameBufferId = glGenFramebuffers();

        textureAttachments = new HashMap<>();
        renderBufferAttachments = new HashMap<>();
    }

    public void test() throws Exception
    {
        if(disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", frameBufferId));
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            throw new Exception("Framebuffer not complete!");
        }
    }

    public void resizeAttachments(int width, int height)
    {
        for (Texture texture2DAttachment : textureAttachments.values())
        {
            texture2DAttachment.create(width, height);
        }

        for (RenderBuffer renderBufferAttachment : renderBufferAttachments.values())
        {
            renderBufferAttachment.create(width, height);
        }
    }

    public void setReadBuffer(int buffers)
    {
        bindRead();
        glReadBuffer(buffers);
    }

    public void setDrawBuffers(int... buffers)
    {
        bindDraw();
        glDrawBuffers(buffers);
    }

    public void bindRead()
    {
        if(disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", frameBufferId));
        }

        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferId);
    }

    public void bindDraw()
    {
        if(disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", frameBufferId));
        }

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferId);
    }

    public void bind()
    {
        if(disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", frameBufferId));
        }

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
    }

    public void dispose()
    {
        if(disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has already been disposed", frameBufferId));
        }

        for (Texture texture2DAttachment : textureAttachments.values()){
            texture2DAttachment.dispose();
        }

        for (RenderBuffer renderBufferAttachment : renderBufferAttachments.values()){
            renderBufferAttachment.dispose();
        }

        glDeleteFramebuffers(frameBufferId);
    }

    public static void bindDefaultFramebuffer(){
        glBindFramebuffer(GL_FRAMEBUFFER, default_FBO);
    }

    public static void bindDefaultFramebufferRead(){
        glBindFramebuffer(GL_READ_FRAMEBUFFER, default_FBO);
    }

    public static void bindDefaultFramebufferDraw(){
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, default_FBO);
    }

    public static void clearBuffers(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void clearColourBuffer(){
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void clearDepthBuffer(){
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public static void blit(FrameBuffer source, FrameBuffer destination, int width, int height, int mask, int filter){
        source.bindRead();
        destination.bindDraw();
        glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, mask, filter);
        destination.bindRead();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addTextureAttachment(String name, int attachment, Texture textureAttachment)
    {
        FrameBuffer.this.bind();
        textureAttachment.bind();

        glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, textureAttachment.getId(), 0);

        textureAttachments.put(name, textureAttachment);
    }

    public Texture getTexture2DAttachment(String name)
    {
        return textureAttachments.get(name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addRenderBufferAttachment(String name, int attachment, RenderBuffer renderBufferAttachment)
    {
        FrameBuffer.this.bind();
        renderBufferAttachment.bind();

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderBufferAttachment.getId());

        renderBufferAttachments.put(name, renderBufferAttachment);
    }

    public RenderBuffer getRenderBufferAttachment(String name){
        return renderBufferAttachments.get(name);
    }
}
