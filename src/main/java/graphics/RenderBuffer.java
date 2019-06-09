package graphics;

import static org.lwjgl.opengl.GL30.*;

public class RenderBuffer implements IResource
{
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
        if (BOUND_RENDER_BUFFER == this)
        {
            return;
        }

        if (disposed)
        {
            throw new RuntimeException(String.format("RenderBuffer: %d has been disposed", id));
        }

        glBindRenderbuffer(GL_RENDERBUFFER, id);

        BOUND_RENDER_BUFFER = this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return getId() == ((RenderBuffer) obj).getId();
    }

    @Override
    public void dispose()
    {
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        if (disposed)
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