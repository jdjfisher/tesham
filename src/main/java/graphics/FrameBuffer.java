package graphics;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by fisherj16 on 18/10/2017.
 */
public class FrameBuffer implements IResource
{
    public static final FrameBuffer DEFAULT_FRAMEBUFFER = new FrameBuffer(0);
    private static FrameBuffer BOUND_READ_FRAMEBUFFER = DEFAULT_FRAMEBUFFER;
    private static FrameBuffer BOUND_DRAW_FRAMEBUFFER = DEFAULT_FRAMEBUFFER;

    private final int id;
    private final HashMap<String, Texture> textureAttachments;
    private final HashMap<String, RenderBuffer> renderBufferAttachments;
    private boolean disposed;

    public FrameBuffer()
    {
        this(glGenFramebuffers());
    }

    private FrameBuffer(int id)
    {
        this.id = id;

        textureAttachments = new HashMap<>();
        renderBufferAttachments = new HashMap<>();
        disposed = false;
    }

    public void test() throws Exception
    {
        bind();

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            throw new Exception("Framebuffer not complete!");
        }
    }

    public void resizeAttachments(int width, int height)
    {
        if (disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", id));
        }

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
        if (BOUND_READ_FRAMEBUFFER == this)
        {
            return;
        }

        if (disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", id));
        }

        glBindFramebuffer(GL_READ_FRAMEBUFFER, id);

        BOUND_READ_FRAMEBUFFER = this;
    }

    public void bindDraw()
    {
        if (BOUND_DRAW_FRAMEBUFFER == this)
        {
            return;
        }

        if (disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", id));
        }

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, id);

        BOUND_DRAW_FRAMEBUFFER = this;
    }

    public void bind()
    {
        if (BOUND_READ_FRAMEBUFFER == this && BOUND_DRAW_FRAMEBUFFER == this)
        {
            return;
        }

        if (disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has been disposed", id));
        }

        glBindFramebuffer(GL_FRAMEBUFFER, id);

        BOUND_READ_FRAMEBUFFER = BOUND_DRAW_FRAMEBUFFER = this;
    }

    @Override
    public void dispose()
    {
        DEFAULT_FRAMEBUFFER.bind();

        if (disposed)
        {
            throw new RuntimeException(String.format("FrameBuffer: %d has already been disposed", id));
        }

        for (Texture texture2DAttachment : textureAttachments.values())
        {
            texture2DAttachment.dispose();
        }

        for (RenderBuffer renderBufferAttachment : renderBufferAttachments.values())
        {
            renderBufferAttachment.dispose();
        }

        glDeleteFramebuffers(id);
        disposed = true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return getId() == ((FrameBuffer) obj).getId();
    }

    public int getId()
    {
        return id;
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public static void clearBuffers()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void clearColourBuffer()
    {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void clearDepthBuffer()
    {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public static void blit(FrameBuffer source, FrameBuffer destination, int width, int height, int mask, int filter)
    {
        source.bindRead();
        destination.bindDraw();
        glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, mask, filter);
        destination.bindRead();
    }

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

    public void addRenderBufferAttachment(String name, int attachment, RenderBuffer renderBufferAttachment)
    {
        FrameBuffer.this.bind();
        renderBufferAttachment.bind();

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderBufferAttachment.getId());

        renderBufferAttachments.put(name, renderBufferAttachment);
    }

    public RenderBuffer getRenderBufferAttachment(String name)
    {
        return renderBufferAttachments.get(name);
    }
}
