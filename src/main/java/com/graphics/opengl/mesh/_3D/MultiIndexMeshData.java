package com.graphics.opengl.mesh._3D;

import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.graphics.opengl.mesh._3D.FaceMI.IndexSet.NULL_INDEX;

public class MultiIndexMeshData {
    private ArrayList<Vector3f> vertexPositions;
    private ArrayList<Vector3f> vertexNormals;
    private ArrayList<Vector2f> textureCoords;
    private ArrayList<FaceMI> faces;

    public MultiIndexMeshData(Vector3f[] vertexPositions, Vector3f[] vertexNormals, Vector2f[] textureCoords, FaceMI[] indexs){
        this(vertexPositions == null ? null : new ArrayList<>(Arrays.asList(vertexPositions)), vertexNormals == null ? null : new ArrayList<>(Arrays.asList(vertexNormals)), textureCoords == null ? null : new ArrayList<>(Arrays.asList(textureCoords)), indexs == null ? null : new ArrayList<>(Arrays.asList(indexs)));
    }

    public MultiIndexMeshData(ArrayList<Vector3f> vertexPositions, ArrayList<Vector3f> vertexNormals, ArrayList<Vector2f> textureCoords, ArrayList<FaceMI> faces){
        this.vertexPositions = vertexPositions;
        this.vertexNormals = vertexNormals;
        this.textureCoords = textureCoords;
        this.faces = faces;
    }

    public ArrayList<Vector3f> getVertexPositions() {
        return vertexPositions;
    }

    public ArrayList<Vector3f> getVertexNormals() {
        return vertexNormals;
    }

    public ArrayList<Vector2f> getTextureCoords() {
        return textureCoords;
    }

    public ArrayList<FaceMI> getFaces() {
        return faces;
    }

    public SingleIndexMeshData toSingleIndexMeshData(){
        return ToSingleIndexMeshData(this);
    }

    private static SingleIndexMeshData ToSingleIndexMeshData(MultiIndexMeshData meshData){ // TODO: rework
        ArrayList<Vertex3D> vertices = new ArrayList<>();
        ArrayList<FaceSI> faces = new ArrayList<>();
        HashMap<FaceMI.IndexSet, Integer> map = new HashMap<>();

        int indexOffset = 0;

        for(FaceMI faceMI : meshData.getFaces()){
            int[] faceSIIndexs = new int[faceMI.getFaceOrder()];

            for(int i = 0; i < faceMI.getFaceOrder(); i++){
                FaceMI.IndexSet indexSet = faceMI.getIndexSets()[i];

                if(!map.containsKey(indexSet)) {
                    map.put(indexSet, indexOffset);
                    faceSIIndexs[i] = indexOffset;
                    indexOffset++;

                    final int vertexPositionIndex = indexSet.getVertexPositionIndex();
                    final int vertexNormalIndex = indexSet.getVertexNormalIndex();
                    final int textureCoordinateIndex = indexSet.getTextureCoordinateIndex();

                    vertices.add(new Vertex3D(
                            meshData.getVertexPositions().get(vertexPositionIndex),
                            vertexNormalIndex == NULL_INDEX || meshData.getVertexNormals() == null ? null : meshData.getVertexNormals().get(vertexNormalIndex),
                            textureCoordinateIndex == NULL_INDEX || meshData.getTextureCoords() == null  ? null : meshData.getTextureCoords().get(textureCoordinateIndex)
                    ));
                }else {
                    faceSIIndexs[i] = map.get(indexSet);
                }
            }

            faces.add(new FaceSI(faceSIIndexs));
        }

        return new SingleIndexMeshData(vertices, faces);
    }
}
