package com.graphics.component.mesh._3D;

import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import com.utils.DataUtils;
import org.apache.commons.math3.util.FastMath;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class Mesh3D {
    private static final int FLOAT_SIZE = 4;

    private final int indexedVertexCount;
    private final int vao;
    private final int vbo;
    private final int ibo;

    private final float boundingSphereRadius;
    private final boolean supportsTexture;

    public Mesh3D(MultiIndexMeshData meshData){
        this(meshData.toSingleIndexMeshData());
    }

    public Mesh3D(SingleIndexMeshData meshData){
        boolean needsNormals = false;
        boolean hasTextureCoords = true;
        boolean needsTangents = false;

        for (Vertex3D vertex : meshData.getVertices()) {
            if (!needsNormals && vertex.getNormal() == null) {
                needsNormals = true;
            }

            if (hasTextureCoords) {
                if (vertex.getTextureCoord() == null) {
                    hasTextureCoords = false;
                } else if (!needsTangents && (vertex.getTangent() == null || vertex.getBitangent() == null)) {
                    needsTangents = true;
                }
            }

            if(needsNormals && !hasTextureCoords){
                break;
            }
        }

        if(needsNormals){
            calcNormals(meshData);
        }

        supportsTexture = hasTextureCoords;

        if(needsTangents){
            calcTangents(meshData);
        }

        PrimitiveFaceSI[] primitiveFaces = triangulateFaces(meshData.getFaces());
        indexedVertexCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesToFloatBuffer(meshData.getVertices()), GL_STATIC_DRAW);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, primitveFacesToIndexBuffer(primitiveFaces), GL_STATIC_DRAW);

        // vertex positions
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, Vector3f.SIZE, GL_FLOAT, false, Vertex3D.SIZE * FLOAT_SIZE, 0);
        // vertex normals
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, Vector3f.SIZE, GL_FLOAT, false, Vertex3D.SIZE * FLOAT_SIZE, 3 * FLOAT_SIZE);
        // texture coords
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, Vector2f.SIZE, GL_FLOAT, false, Vertex3D.SIZE * FLOAT_SIZE, 6 * FLOAT_SIZE);
        // tangents
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3, Vector3f.SIZE, GL_FLOAT, false, Vertex3D.SIZE * FLOAT_SIZE, 8 * FLOAT_SIZE);
        // bitangents
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(4, Vector3f.SIZE, GL_FLOAT, false, Vertex3D.SIZE * FLOAT_SIZE, 11 * FLOAT_SIZE);

        glBindVertexArray(0);

        boundingSphereRadius = calcBoundingSphereRadius(meshData.getVertices());
    }

    public void render() throws Exception{
        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, indexedVertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
    }

    public void dispose() {
        // Disable and delete

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(ibo);

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    private FloatBuffer verticesToFloatBuffer(ArrayList<Vertex3D> vertices){
        final int floatCount = vertices.size() * Vertex3D.SIZE;

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(floatCount);

        for(Vertex3D vertex : vertices){
            DataUtils.putVector3f(floatBuffer, vertex.getPosition());
            DataUtils.putVector3f(floatBuffer, vertex.getNormal());
            DataUtils.putVector2f(floatBuffer, supportsTexture ? vertex.getTextureCoord() : Vector2f.Identity());
            DataUtils.putVector3f(floatBuffer, supportsTexture ? vertex.getTangent() : Vector3f.Identity());
            DataUtils.putVector3f(floatBuffer, supportsTexture ? vertex.getBitangent() : Vector3f.Identity());
        }

        floatBuffer.flip();

        return floatBuffer;
    }

    private static IntBuffer primitveFacesToIndexBuffer(PrimitiveFaceSI[] primitiveFaces){
        final int indiciesCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indiciesCount);

        for(PrimitiveFaceSI primitiveFace : primitiveFaces){
            indicesBuffer.put(primitiveFace.getI0());
            indicesBuffer.put(primitiveFace.getI1());
            indicesBuffer.put(primitiveFace.getI2());
        }

        indicesBuffer.flip();

        return indicesBuffer;
    }

    private static void calcNormalsFlat(ArrayList<Vertex3D> vertices, ArrayList<FaceSI> faces){
        ArrayList<Vertex3D> newVerts = new ArrayList<>();
        ArrayList<FaceSI> newFaces = new ArrayList<>();

        int j = 0;

        for(FaceSI face : faces){
            Vertex3D[] faceVertices = face.getVertices(vertices);
            int temp[] = new int[face.getFaceOrder()];

            for(int i = 0; i < face.getFaceOrder(); i++){
                temp[i] = j++;
            }

            Vector3f v0 = faceVertices[0].getPosition();
            Vector3f v1 = faceVertices[1].getPosition();
            Vector3f v2 = faceVertices[2].getPosition();

            Vector3f edge1 = Vector3f.Subtract(v1, v0);
            Vector3f edge2 = Vector3f.Subtract(v2, v0);

            Vector3f faceNormal = Vector3f.MultiplyCrossProduct(edge1, edge2);
            faceNormal.normalize();

            for(Vertex3D vertex : faceVertices){
                newVerts.add(new Vertex3D(vertex.getPosition(), faceNormal, vertex.getTextureCoord(), vertex.getTangent(), vertex.getBitangent()));
            }

            newFaces.add(new FaceSI(temp));
        }

        vertices.clear();
        vertices.addAll(newVerts);
        faces.clear();
        faces.addAll(newFaces);
    }

    private static void calcNormalsSmooth(ArrayList<Vertex3D> vertices, ArrayList<FaceSI> faces){ // https://www.youtube.com/watch?v=PMgjVJogIbc
        HashMap<Vector3f, Vector3f> normalMap = new HashMap<>();

        for(FaceSI face: faces){
            Vertex3D[] faceVertices = face.getVertices(vertices);

            Vector3f v0 = faceVertices[0].getPosition();
            Vector3f v1 = faceVertices[1].getPosition();
            Vector3f v2 = faceVertices[2].getPosition();

            Vector3f edge1 = Vector3f.Subtract(v1, v0);
            Vector3f edge2 = Vector3f.Subtract(v2, v0);

            Vector3f faceNormal = Vector3f.MultiplyCrossProduct(edge1, edge2);
            faceNormal.normalize();

            for(Vertex3D vertex : faceVertices){
                Vector3f vertexPosition = vertex.getPosition();

                if(normalMap.containsKey(vertexPosition)){
                    normalMap.put(vertexPosition, Vector3f.Add(faceNormal, normalMap.get(vertexPosition)));
                }else {
                    normalMap.put(vertexPosition, faceNormal);
                }
            }
        }

        for(Vector3f normal : normalMap.values()){
            normal.normalize();
        }

        for(Vertex3D vertex : vertices){
            vertex.setNormal(normalMap.get(vertex.getPosition()));
        }
    }

    private static float smoothNormalCap = (float) FastMath.cos(FastMath.toRadians(30));

    private static void calcNormals(SingleIndexMeshData meshData){
        ArrayList<Vertex3D> vertices = meshData.getVertices();
        ArrayList<FaceSI> faces = meshData.getFaces();

        HashMap<FaceSI, Vector3f> faceNormalMap = new HashMap<>();
        HashMap<Vector3f, HashSet<Vector3f>> vertexPositionToNormalSetMap = new HashMap<>();
        // a vertex position maps a hashset of all of the normals of faces that the vertex is a part of

        for(FaceSI face: faces){
            Vertex3D[] faceVertices = face.getVertices(vertices);

            Vector3f v0 = faceVertices[0].getPosition();
            Vector3f v1 = faceVertices[1].getPosition();
            Vector3f v2 = faceVertices[2].getPosition();

            Vector3f edge1 = Vector3f.Subtract(v1, v0);
            Vector3f edge2 = Vector3f.Subtract(v2, v0);

            Vector3f faceNormal = Vector3f.MultiplyCrossProduct(edge1, edge2);
            faceNormal.normalize();

            faceNormalMap.put(face, faceNormal);

            for(Vertex3D vertex : faceVertices){
                Vector3f vertexPosition = vertex.getPosition();

                if(vertexPositionToNormalSetMap.containsKey(vertexPosition)){
                    HashSet<Vector3f> set = vertexPositionToNormalSetMap.get(vertexPosition);
                    set.add(faceNormal);
                }else {
                    HashSet<Vector3f> set = new HashSet<>();
                    set.add(faceNormal);
                    vertexPositionToNormalSetMap.put(vertexPosition, set);
                }
            }
        }

        ArrayList<Vertex3D> newVertices = new ArrayList<>();
        ArrayList<FaceSI> newFaces = new ArrayList<>();
        int currentIndexPointer = 0;
        int[] currentNewFaceIndexes;

        for(FaceSI face: faces){
            Vertex3D[] faceVertices = face.getVertices(vertices);
            currentNewFaceIndexes = new int[face.getFaceOrder()];

            for(int i = 0; i < face.getFaceOrder(); i++){
                currentNewFaceIndexes[i] = currentIndexPointer++;
            }

            for(Vertex3D vertex : faceVertices){
                Vector3f faceNormal = faceNormalMap.get(face);
                Vector3f finalNormal = new Vector3f(faceNormal);

                for(Vector3f otherNormal : vertexPositionToNormalSetMap.get(vertex.getPosition())){
                    if(!otherNormal.equals(faceNormal)) {
                        float dotProduct = Vector3f.MultiplyDotProduct(faceNormal, otherNormal);

                        if (dotProduct >= smoothNormalCap && dotProduct <= 1) {
                            finalNormal.add(otherNormal);
                        }
                    }
                }

                finalNormal.normalize();

                newVertices.add(new Vertex3D(vertex.getPosition(), finalNormal, vertex.getTextureCoord(), vertex.getTangent(), vertex.getBitangent()));
            }

            newFaces.add(new FaceSI(currentNewFaceIndexes));
        }

        meshData.set(newVertices, newFaces);
    }

    private static void calcTangents(SingleIndexMeshData meshData){
        ArrayList<Vertex3D> vertices = meshData.getVertices();
        ArrayList<FaceSI> faces = meshData.getFaces();

        for(FaceSI face: faces) {
            Vertex3D[] faceVertices = face.getVertices(vertices);

            Vector3f v0 = faceVertices[0].getPosition();
            Vector3f v1 = faceVertices[1].getPosition();
            Vector3f v2 = faceVertices[2].getPosition();
            Vector2f t0 = faceVertices[0].getTextureCoord();
            Vector2f t1 = faceVertices[1].getTextureCoord();
            Vector2f t2 = faceVertices[2].getTextureCoord();

            Vector3f edge1 = Vector3f.Subtract(v1, v0);
            Vector3f edge2 = Vector3f.Subtract(v2, v0);
            Vector2f deltaUV1 = Vector2f.Subtract(t1, t0);
            Vector2f deltaUV2 = Vector2f.Subtract(t2, t0);

            float f = 1.0f / (deltaUV1.getX() * deltaUV2.getY() - deltaUV2.getX() * deltaUV1.getY());

            Vector3f tangent = new Vector3f(
                    f * (deltaUV2.getY() * edge1.getX() - deltaUV1.getY() * edge2.getX()),
                    f * (deltaUV2.getY() * edge1.getY() - deltaUV1.getY() * edge2.getY()),
                    f * (deltaUV2.getY() * edge1.getZ() - deltaUV1.getY() * edge2.getZ())
            );

            tangent.normalize();

            Vector3f bitangent = new Vector3f(
                    f * (-deltaUV2.getX() * edge1.getX() + deltaUV1.getX() * edge2.getX()),
                    f * (-deltaUV2.getX() * edge1.getY() + deltaUV1.getX() * edge2.getY()),
                    f * (-deltaUV2.getX() * edge1.getZ() + deltaUV1.getX() * edge2.getZ())
            );

            bitangent.normalize();

            for(Vertex3D vertex : faceVertices){
                vertex.setTangent(tangent);
                vertex.setBitangent(bitangent);
            }
        }
    }

    private static float calcBoundingSphereRadius(ArrayList<Vertex3D> vertices){
        float boundingSphereRadius = 0;

        for(Vertex3D vertex: vertices) {
            boundingSphereRadius = FastMath.max(boundingSphereRadius, vertex.getPosition().getLength());
        }

        return boundingSphereRadius;
    }

    public float getBoundingSphereRadius() {
        return boundingSphereRadius;
    }

    public boolean supportsTexture() {
        return supportsTexture;
    }

    private static PrimitiveFaceSI[] triangulateFaces(ArrayList<FaceSI> faces){
        ArrayList<PrimitiveFaceSI> Faces = new ArrayList<>();

        for(FaceSI face : faces) {
            for (int i = 1; i <= face.getPrimtiveFaceCount(); i++) {
                Faces.add(new PrimitiveFaceSI(
                        face.getIndexes()[0    ],
                        face.getIndexes()[i    ],
                        face.getIndexes()[i + 1]
                ));
            }
        }

        return Faces.toArray(new PrimitiveFaceSI[Faces.size()]);
    }

    private static class PrimitiveFaceSI {
        public static final int SIZE = 3;

        private int i0;
        private int i1;
        private int i2;

        public PrimitiveFaceSI(int i0, int i1, int i2){
            this.i0 = i0;
            this.i1 = i1;
            this.i2 = i2;
        }

        public int getI0() {
            return i0;
        }

        public int getI1() {
            return i1;
        }

        public int getI2() {
            return i2;
        }
    }
}
