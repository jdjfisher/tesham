package graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexArray implements IResource
{
    private static VertexArray BOUND_VERTEX_ARRAY;

    private final int id;
    private boolean disposed;

    public VertexArray()
    {
        this.id = glGenVertexArrays();
        this.disposed = false;
    }

    public void bind()
    {
        if (BOUND_VERTEX_ARRAY == this) return;

        if (disposed) throw new RuntimeException(String.format("VertexArray: %d has been disposed", id));

        glBindVertexArray(id);

        BOUND_VERTEX_ARRAY = this;
    }

    public void enableAttributeArray(int index)
    {
        bind();
        glEnableVertexAttribArray(index);
    }

    public void disableAttributeArray(int index)
    {
        bind();
        glDisableVertexAttribArray(index);
    }

    public void pointAttribute(int index, int count, int type, int stride, long offset, BufferGl buffer)
    {
        pointAttribute(index, count, type, false, stride, offset, buffer);
    }

    public void pointAttribute(int index, int count, int type, boolean normalized, int stride, long offset, BufferGl buffer)
    {
        this.bind();
        buffer.bind();
        glVertexAttribPointer(index, count, type, normalized, stride, offset);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return getId() == ((VertexArray) obj).getId();
    }

    public int getId()
    {
        return id;
    }

    @Override
    public void dispose()
    {
        glBindVertexArray(0);
        glDeleteVertexArrays(id);
        disposed = true;
    }
}
