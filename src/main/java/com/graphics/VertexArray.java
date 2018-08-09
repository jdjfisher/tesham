package com.graphics;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

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
        if(BOUND_VERTEX_ARRAY == this)
        {
            return;
        }

        if(disposed)
        {
            throw new RuntimeException(String.format("VertexArray: %d has been disposed", id));
        }

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
        pointAttribute(index, count, type,false, stride, offset, buffer);
    }

    public void pointAttribute(int index, int count, int type, boolean normalized, int stride, long offset, BufferGl buffer)
    {
        this.bind();
        buffer.bind();
        glVertexAttribPointer(index, count, type, normalized, stride, offset);
    }

    @Override
    public boolean equals(Object o)
    {
        return o != null && getId() == ((VertexArray) o).getId();
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
