package com.utils;

import com.graphics.component.Texture;

import javax.imageio.ImageIO;
import java.util.HashMap;

public class TextureLoader {
    private static final HashMap<String, Texture> loadedTextureMap = new HashMap<>();

    private TextureLoader(){}

    public static Texture load(String filePath) throws Exception{
        if (filePath.matches(".*\\.(png|jpg)")) {
            if(loadedTextureMap.containsKey(filePath)){
                return loadedTextureMap.get(filePath);
            }else {
                Texture texture = new Texture(ImageIO.read(TextureLoader.class.getResource(filePath)));
                loadedTextureMap.put(filePath, texture);
                return texture;
            }
        }else {
            throw new Exception(String.format("Cant load %s, file type not supported", filePath));
        }
    }
}
