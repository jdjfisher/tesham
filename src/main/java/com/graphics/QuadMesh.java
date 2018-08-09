package com.graphics;

import com.utils.DataUtils;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class QuadMesh implements IResource
{
    private final int vao;
    private final int vbo;
    private final int ibo;
    private boolean disposed;
    
    public QuadMesh()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, DataUtils.toFloatBuffer(new float[]{
                -1, 1, 0, 1,
                -1,-1, 0, 0,
                 1,-1, 1, 0,
                 1, 1, 1, 1
        }), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.toIntBuffer(new int[]{
                0, 1, 2,
                2, 3, 0
        }), GL_STATIC_DRAW);

        // vertex positions
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
        // texture coordinates
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);

        glBindVertexArray(0);


        disposed = false;
    }

    public void render()
    {
        if(disposed)
        {
            throw new RuntimeException("QuadMesh is disposed");
        }

        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
    }
    
    @Override
    public void dispose() 
    {
        if(disposed)
        {
            throw new RuntimeException("QuadMesh is already disposed");
        }
        
        // Disable
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // Delete the VBOs
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
        
        disposed = true;
    }


}
