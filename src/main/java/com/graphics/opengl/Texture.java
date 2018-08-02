package com.graphics.opengl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by Jordan Fisher on 04/06/2017.
 */
public class Texture implements IResource{
    public static Texture DEFAULT_TEXTURE;   //TODO move init
    private static Texture BOUND_TEXTURE;
    private static int ACTIVE_UNIT;

    private final int id;
    private final int internalFormat;
    private final int format;
    private final int type;
    private boolean disposed;
    private int width;
    private int height;

    public static Texture fromImage(BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        // Load texture contents into a byte buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for(int y = height - 1; y > 0; --y) {
            for(int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));// Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF)) ;// Green component
                buffer.put((byte) (pixel & 0xFF));        // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));// Alpha component
            }
        }

        buffer.flip();

        return new Texture(GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, width, height, GL_LINEAR, GL_LINEAR, GL_REPEAT, GL_REPEAT, buffer);
    }

    public static Texture fromSolidColor(Color colour, int width, int height)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();

        g2d.setBackground(colour);
        g2d.clearRect(0, 0, width, height);

        return fromImage(image);
    }

    public Texture(int internalFormat, int format, int type, int width, int height){
        this(internalFormat, format, type, width, height, null);
    }

    public Texture(int internalFormat, int format, int type, int width, int height, int min, int mag){
        this(internalFormat, format, type, width, height, null);

        setFilter(min, mag);
    }

    public Texture(int internalFormat, int format, int type, int width, int height, int min, int mag, int s, int t){
        this(internalFormat, format, type, width, height, null);

        setFilter(min, mag);
        setWrapping(s, t);
    }

    public Texture(int internalFormat, int format, int type, int width, int height, int min, int mag, int s, int t, ByteBuffer buffer){
        this(internalFormat, format, type, width, height, buffer);

        setFilter(min, mag);
        setWrapping(s, t);
    }

    public Texture(int internalFormat, int format, int type, int width, int height, ByteBuffer buffer){
        this.id = glGenTextures();
        this.internalFormat = internalFormat;
        this.format = format;
        this.type = type;
        this.width = width;
        this.height = height;
        this.disposed = false;

        create(width, height, buffer);
    }



    public void create(int width, int height)
    {
        create(width, height,null);
    }

    public void create(int width, int height, ByteBuffer buffer)
    {
        bind();

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, buffer);
    }

    public void setFilter(int min_and_mag)
    {
        setFilter(min_and_mag, min_and_mag);
    }

    public void setFilter(int min, int mag)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag);
    }

    public void setWrapping(int s_and_t)
    {
        setWrapping(s_and_t, s_and_t);
    }

    public void setWrapping(int s, int t)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, t);
    }

    public void generateMipMaps()
    {
        bind();

        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void bind()
    {
        if(BOUND_TEXTURE == this)
        {
            return;
        }

        if(disposed)
        {
            throw new RuntimeException(String.format("Texture: %d has been disposed", id));
        }

        glBindTexture(GL_TEXTURE_2D, id);

        BOUND_TEXTURE = this;
    }

    public void bindToUnit(int textureUnit)
    {
        setActiveUnit(textureUnit);
        bind();
    }

    public static int getActiveUnit()
    {
        return ACTIVE_UNIT;
    }

    public static void setActiveUnit(int textureUnit)
    {
        if (textureUnit == ACTIVE_UNIT)
        {
            return;
        }

        if(0 <= textureUnit && textureUnit <=  glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS))
        {
            glActiveTexture(GL_TEXTURE0 + textureUnit);

            ACTIVE_UNIT = textureUnit;
        }
    }

    @Override
    public boolean equals(Object o)
    {
        return o != null && getId() == ((Texture) o).getId();
    }

    @Override
    public void dispose()
    {
        DEFAULT_TEXTURE.bindToUnit(ACTIVE_UNIT);

        if(disposed)
        {
            throw new RuntimeException(String.format("Texture: %d has already been disposed", id));
        }

        glDeleteTextures(id);
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

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
