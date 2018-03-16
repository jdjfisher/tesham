package com.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by fisherj16 on 18/10/2017.
 */
public class FrameBuffer {

    private static final Exception FRAMEBUFFER_NOT_COMPLETE = new Exception("Framebuffer not complete!");
    private static final ByteBuffer NULL = (ByteBuffer)null;
    public static final int default_FBO = 0;

    private final int frameBufferId;

    private final HashMap<String, Texture2DAttachment> texture2DAttachments;
    private final HashMap<String, RenderBufferAttachment> renderBufferAttachments;

    public FrameBuffer(){
        frameBufferId = glGenFramebuffers();

        texture2DAttachments = new HashMap<>();
        renderBufferAttachments = new HashMap<>();
    }

    public void test() throws Exception{
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw FRAMEBUFFER_NOT_COMPLETE;
        }
    }

    public void resizeAttachments(int width, int height){
        for (Texture2DAttachment texture2DAttachment : texture2DAttachments.values()){
            texture2DAttachment.create(width, height);
        }

        for (RenderBufferAttachment renderBufferAttachment : renderBufferAttachments.values()){
            renderBufferAttachment.create(width, height);
        }
    }

    public void setReadBuffer(int buffers){
        glReadBuffer(buffers);
    }

    public void setDrawBuffers(int... buffers){
        glDrawBuffers(buffers);
    }

    public void bindRead(){
        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferId);
    }

    public void bindDraw(){
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferId);
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
    }

    public void unbind(){
        bindDefaultFramebuffer();
    }

    public void dispose(){
        for (Texture2DAttachment texture2DAttachment : texture2DAttachments.values()){
            texture2DAttachment.dispose();
        }

        for (RenderBufferAttachment renderBufferAttachment : renderBufferAttachments.values()){
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

    public void addTexture2DAttachment(String name, int internalFormat, int format, int type, int attachment, int width, int height, TexParameterI texParameters){
        addTexture2DAttachment(name, new Texture2DAttachment(internalFormat, format, type, attachment, width, height, texParameters));
    }

    public void addTexture2DAttachment(String name, int internalFormat, int format, int type, int attachment, int width, int height){
        addTexture2DAttachment(name, new Texture2DAttachment(internalFormat, format, type, attachment, width, height));
    }

    public void addTexture2DAttachment(String name, Texture2DAttachment texture2DAttachment){
        texture2DAttachments.put(name, texture2DAttachment);
    }

    public Texture2DAttachment getTexture2DAttachment(String name){
        return texture2DAttachments.get(name);
    }

    public class Texture2DAttachment {
        private final int id;
        private final int internalFormat;
        private final int format;
        private final int type;

        public Texture2DAttachment(int internalFormat, int format, int type, int attachment, int width, int height, TexParameterI texParameters){
            this(internalFormat, format, type, attachment, width, height);
            setTexParameters(texParameters);
        }

        public Texture2DAttachment(int internalFormat, int format, int type, int attachment, int width, int height){
            this.id = glGenTextures();
            this.internalFormat = internalFormat;
            this.format = format;
            this.type = type;
            create(width, height);
            attach(attachment);
        }

        protected void create(int width, int height){
            bind();
            glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, NULL);
        }

        private void attach(int attachment){
            FrameBuffer.this.bind();
            bind();
            glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, id, 0);
        }

        public void setTexParameters(TexParameterI texParameters){
            bind();
            texParameters.setParameters();
        }

        private void bind(){
            glBindTexture(GL_TEXTURE_2D, id);
        }

        public int getId() {
            return id;
        }

        public void dispose(){
            glDeleteTextures(id);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addRenderBufferAttachment(String name, int internalFormat, int attachment, int width, int height){
        addRenderBufferAttachment(name, new RenderBufferAttachment(internalFormat, attachment, width, height));
    }

    public void addRenderBufferAttachment(String name, RenderBufferAttachment renderBufferAttachment){
        renderBufferAttachments.put(name, renderBufferAttachment);
    }

    public RenderBufferAttachment getRenderBufferAttachment(String name){
        return renderBufferAttachments.get(name);
    }

    public class RenderBufferAttachment{
        private final int id;
        private final int internalFormat;

        public RenderBufferAttachment(int internalFormat, int attachment, int width, int height){
            this.id = glGenRenderbuffers();
            this.internalFormat = internalFormat;
            create(width, height);
            attach(attachment);
        }

        protected void create(int width, int height){
            bind();
            glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);
        }

        private void attach(int attachment){
            FrameBuffer.this.bind();
            bind();
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, id);
        }

        private void bind(){
            glBindRenderbuffer(GL_RENDERBUFFER, id);
        }

        public int getId() {
            return id;
        }

        public void dispose(){
            glDeleteRenderbuffers(id);
        }
    }
}
