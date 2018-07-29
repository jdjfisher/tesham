package com.utils;

import com.engine.items.Model;
import com.engine.items.Material;
import com.graphics.opengl.mesh._3D.FaceMI;
import com.graphics.opengl.mesh._3D.Mesh3D;
import com.graphics.opengl.mesh._3D.MultiIndexMeshData;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.utils.MeshLoader.indexGroupsToFace;

public class ModelLoader {
    private static final String NULL = "NULL";

    private ModelLoader(){}

    public static ArrayList<Model> loadModels(String modelName) throws Exception {
        return loadModels(modelName, false);
    }

    public static ArrayList<Model> loadModels(String modelName, boolean flipFaces) throws Exception {

        HashMap<String, Material> materialMap = MtlLoader.loadMaterials(modelName);

        List<String> lines = ReasourceLoader.readAllLines(String.format("/models/%1$s/%1$s.obj", modelName));

        HashMap<String, MultiIndexMeshData> rawMeshs = new HashMap<>();
        ArrayList<Vector3f> vertexPositions = new ArrayList<>();
        ArrayList<Vector3f> vertexNormals = new ArrayList<>();
        ArrayList<Vector2f> textureCoords = new ArrayList<>();
        ArrayList<FaceMI> faces = new ArrayList<>();

        String[] tokens;
        final int lineCount = lines.size();

        String currentMaterial = NULL;

        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            tokens = lines.get(lineIndex).split("\\s+");
            switch (tokens[0]) {
                case "v":
                    // Geometric vertex

                    vertexPositions.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])
                            )
                    );
                    break;
                case "vn":
                    // Vertex normal

                    vertexNormals.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])
                            )
                    );
                    break;
                case "vt":
                    // Texture coordinate

                    textureCoords.add(
                            new Vector2f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2])
                            )
                    );
                    break;
                case "f":
                    // Face

                    faces.add(indexGroupsToFace(Arrays.copyOfRange(tokens, 1, tokens.length), flipFaces));
                    break;
                case "usemtl":
                    // Material grouping

                    if (!currentMaterial.equals(NULL)) {
                        method(currentMaterial, rawMeshs, vertexPositions, vertexNormals, textureCoords, faces);
                    }
                    faces.clear();
                    currentMaterial = tokens[1];
                    break;
                default:
                    break;
            }
        }

        method(currentMaterial, rawMeshs, vertexPositions, vertexNormals, textureCoords, faces);

        ArrayList<Model> models = new ArrayList<>();

        for(String key : rawMeshs.keySet()){
            models.add(new Model(new Mesh3D(rawMeshs.get(key)), materialMap.get(key)));
        }

        return models;
    }

    private static void method(String currentMaterial, HashMap<String, MultiIndexMeshData> rawMeshs, ArrayList<Vector3f> vertexPositions, ArrayList<Vector3f> vertexNormals, ArrayList<Vector2f> textureCoords, ArrayList<FaceMI> faces) {
        if (faces.size() > 0) {
            if (rawMeshs.containsKey(currentMaterial)) {
                rawMeshs.get(currentMaterial).getFaces().addAll(new ArrayList<>(faces));
            } else {
                rawMeshs.put(currentMaterial, new MultiIndexMeshData(vertexPositions, vertexNormals, textureCoords, new ArrayList<>(faces)));
            }
        }
    }

}
