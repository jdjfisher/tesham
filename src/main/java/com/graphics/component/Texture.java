package com.graphics.component;

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
public class Texture {

    private int id;

    private int width;

    private int height;

    public Texture(BufferedImage bufferedImage){
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();

        // Load texture contents into a byte buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);

        int[] pixels = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

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

        id = glGenTextures();

        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public Texture(int id) {
        this.id = id;
    }

    public void bind(){
        Bind(id);
    }

    public void bindToUnit(int textureUnit){
        BindToUnit(id, textureUnit);
    }

    public static void Bind(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void BindToUnit(Texture texture, int textureUnit){
        BindToUnit(texture.getId(), textureUnit);
    }

    public static void BindToUnit(int id, int textureUnit){
        ActivateUnit(textureUnit);
        Bind(id);
    }

    public static void ActivateUnit(int textureUnit){
        if(0 <= textureUnit && textureUnit <=  glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS)){
            glActiveTexture(GL_TEXTURE0 + textureUnit);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId(){
        return id;
    }

    public void dispose() {
        glDeleteTextures(id);
    }
}
