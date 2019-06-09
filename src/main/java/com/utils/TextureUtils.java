package com.utils;

import com.graphics.Texture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_REPEAT;

public class TextureUtils
{
    public static ByteBuffer toByteBuffer(BufferedImage image)
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

        return buffer;
    }
}
