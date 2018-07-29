package com.graphics.component;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;

public class RenderBuffer { //TODO: add disposed, add bind check
    private final int id;
    private final int internalFormat;

    public RenderBuffer(int internalFormat, int width, int height){
        this.id = glGenRenderbuffers();
        this.internalFormat = internalFormat;

        create(width, height);
    }

    public void create(int width, int height){
        bind();
        glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);
    }

    public void bind(){
        glBindRenderbuffer(GL_RENDERBUFFER, id);
    }

    public int getId() {
        return id;
    }

    public void dispose(){
        glDeleteRenderbuffers(id);
    }
}