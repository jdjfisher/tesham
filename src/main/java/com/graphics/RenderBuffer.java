package com.graphics;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;

public class RenderBuffer implements IResource{ //TODO: add disposed, add bind check
    private static RenderBuffer BOUND_RENDER_BUFFER;

    private final int id;
    private final int internalFormat;
    private boolean disposed;

    public RenderBuffer(int internalFormat, int width, int height)
    {
        this.id = glGenRenderbuffers();
        this.internalFormat = internalFormat;
        this.disposed = false;

        create(width, height);
    }

    public void create(int width, int height)
    {
        bind();

        glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);
    }

    public void bind()
    {
        if(BOUND_RENDER_BUFFER == this)
        {
            return;
        }

        if(disposed)
        {
            throw new RuntimeException(String.format("RenderBuffer: %d has been disposed", id));
        }

        glBindRenderbuffer(GL_RENDERBUFFER, id);

        BOUND_RENDER_BUFFER = this;
    }

    @Override
    public boolean equals(Object o)
    {
        return o != null && getId() == ((RenderBuffer) o).getId();
    }

    @Override
    public void dispose()
    {
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        if(disposed)
        {
            throw new RuntimeException(String.format("RenderBuffer: %d has already been disposed", id));
        }

        glDeleteRenderbuffers(id);
        disposed = true;
    }

    public int getId()
    {
        return id;
    }

    public boolean isDisposed()
    {
        return disposed;
    }
}