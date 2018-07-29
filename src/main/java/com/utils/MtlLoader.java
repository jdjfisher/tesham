package com.utils;

import com.engine.items.Material;
import com.graphics.opengl.Texture;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MtlLoader {
    private static final Pattern newMaterialPattern = Pattern.compile("\\s*newmtl\\s+\\w+");

    private MtlLoader(){}

    public static HashMap<String, Material> loadMaterials(String modelName) throws Exception{
        final HashMap<String, Material> materialMap = new HashMap<>();
        final ArrayList<String> rawMaterialData = new ArrayList<>();
        Matcher matcher;

        List<String> lines = ReasourceLoader.readAllLines(String.format("/models/%1$s/%1$s.mtl", modelName));

        for (String line : lines) {
            matcher = newMaterialPattern.matcher(line);

            if(matcher.find() && rawMaterialData.size() > 0){
                addMaterial(modelName, rawMaterialData, materialMap);
                rawMaterialData.clear();
            }

            rawMaterialData.add(line.replaceAll("\\t+", ""));
        }

        addMaterial(modelName, rawMaterialData, materialMap);
        rawMaterialData.clear();

        return materialMap;
    }

    private static void addMaterial(String modelName, ArrayList<String> rawMaterialData, HashMap<String, Material> materialMap) throws Exception{
        final Material material = new Material();

        final String name = rawMaterialData.get(0).split("\\s+")[1];

        for (int lineIndex = 1; lineIndex < rawMaterialData.size(); lineIndex++){
            final String[] tokens = rawMaterialData.get(lineIndex).split("\\s+");

            switch (tokens[0]){
                case "Kd":
                    material.setColour(new Color(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[3]), Float.parseFloat(tokens[3])));
                    break;
                case "map_Ka":
                case "map_Kd":
                    material.setDiffuseTexture(grabTexture(modelName, tokens[1]));
                    break;
                case "map_Ns":
                case "map_Ks":
                    material.setSpecularMap(grabTexture(modelName, tokens[1]));
                    break;
                case "Ns":
                    material.setReflectance(Float.parseFloat(tokens[1]));
                    break;
                case "map_bump":
                case "map_Bump":
                    material.setNormalMap(grabTexture(modelName, tokens[1]));
                    break;
                case "Ks":
                case "Ka":
                case "Ni":
                case "d":
                case "Tr":
                case "Tf":
                case "illum":
                case "Ke":
                case "map_d":
                    break;
            }
        }

        materialMap.put(name, material);
    }

    private static Texture grabTexture(String modelName, String token) throws Exception{
        return TextureLoader.load(String.format("/models/%s/%s", modelName, token.replaceAll("\\\\", "/")));
    }
}
