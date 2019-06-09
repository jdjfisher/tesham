package com.utils;

import com.graphics.mesh.FaceMI;
import com.graphics.mesh.Mesh;
import com.graphics.mesh.MultiIndexMeshData;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.graphics.mesh.FaceMI.IndexSet.NULL_INDEX;

public class MeshLoader {
    private static final HashMap<String, Mesh> loadedMeshMap = new HashMap<>();

    private MeshLoader(){}

    public static Mesh loadMesh(String filePath) throws Exception {
        return loadMesh(filePath, false);
    }

    public static Mesh loadMesh(String filePath, boolean flipFaces) throws Exception{
        if (filePath.endsWith(".obj")) {
            if(loadedMeshMap.containsKey(filePath)){
                return loadedMeshMap.get(filePath);
            }else {
                List<String> lines = ResourceLoader.readAllLines(filePath);

                ArrayList<Vector3f> vertexPositions = new ArrayList<>();
                ArrayList<Vector3f> vertexNormals = new ArrayList<>();
                ArrayList<Vector2f> textureCoords = new ArrayList<>();
                ArrayList<FaceMI> faces = new ArrayList<>();

                final int lineCount = lines.size();

                for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
                    String[] tokens = lines.get(lineIndex).split("\\s+");
                    switch (tokens[0]) {
                        case "v":
                            // Geometric vertex
                            vertexPositions.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                            break;
                        case "vn":
                            // Vertex normal
                            vertexNormals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                            break;
                        case "vt":
                            // Texture coordinate
                            textureCoords.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                            break;
                        case "f":
                            // Face
                            faces.add(indexGroupsToFace(Arrays.copyOfRange(tokens, 1, tokens.length), flipFaces));
                            break;
                        default:
                            break;
                    }
                }

                Mesh mesh = new Mesh(new MultiIndexMeshData(vertexPositions, vertexNormals, textureCoords, faces));
                loadedMeshMap.put(filePath, mesh);
                return mesh;
            }
        }else {
            throw new Exception(String.format("Cant load %s, file type not supported", filePath));
        }
    }

    public static FaceMI indexGroupsToFace(String[] indexGroups, boolean flipFace) {
        int faecOrder = indexGroups.length;
        FaceMI.IndexSet[] indexSets = new FaceMI.IndexSet[faecOrder];

        for(int i = 0; i < faecOrder; i++) {
            String[] indexs = indexGroups[flipFace ? faecOrder - i - 1 : i].split("/");

            int vertexPositionsIndex = Integer.parseInt(indexs[0]) - 1; //.Obj files indexs start at 1
            int vertexNormalsIndex = NULL_INDEX;
            int textureCoordinateIndex = NULL_INDEX;

            if(indexs.length > 1){
                if (indexs[1].matches("\\d+")) {
                    textureCoordinateIndex = Integer.parseInt(indexs[1]) - 1; //.Obj files indexs start at 1
                }
                if(indexs.length > 2) {
                    if (indexs[2].matches("\\d+")) {
                        vertexNormalsIndex = Integer.parseInt(indexs[2]) - 1; //.Obj files indexs start at 1
                    }
                }
            }

            indexSets[i] = new FaceMI.IndexSet(vertexPositionsIndex, vertexNormalsIndex, textureCoordinateIndex);
        }

        return new FaceMI(indexSets);
    }
}
