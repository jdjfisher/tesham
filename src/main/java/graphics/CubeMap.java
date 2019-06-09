package graphics;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;

public class CubeMap implements IResource
{
    private static CubeMap BOUND_CUBEMAP;

    private final int id;
    private boolean disposed;

    public CubeMap()
    {
        this.id = glGenTextures();
    }

    public void bind()
    {
        if (BOUND_CUBEMAP == this)
        {
            return;
        }

        if (disposed)
        {
            throw new RuntimeException(String.format("Cubemap: %d has been disposed", id));
        }

        glBindTexture(GL_TEXTURE_CUBE_MAP, id);

        BOUND_CUBEMAP = this;
    }

    @Override
    public void dispose()
    {
        if (disposed)
        {
            throw new RuntimeException();
        }

        disposed = true;
    }
}
