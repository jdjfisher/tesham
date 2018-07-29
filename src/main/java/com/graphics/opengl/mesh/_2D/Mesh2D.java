package com.graphics.component.mesh._2D;

import com.graphics.component.mesh._3D.FaceSI;
import com.maths.vectors.Vector2f;
import com.utils.DataUtils;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class Mesh2D {
    private static final int FLOAT_SIZE = 4;
    private final int indexedVertexCount;
    private final int vao;
    private final int vbo;
    private final int ibo;

    private final boolean supportsTexture;

    public Mesh2D(ArrayList<Vertex2D> vertices, ArrayList<FaceSI> faces){
        this(vertices.toArray(new Vertex2D[vertices.size()]), faces.toArray(new FaceSI[faces.size()]));
    }

    public Mesh2D(Vertex2D[] vertices, FaceSI[] faces){
        boolean hasTextureCoords = true;

        for (Vertex2D vertex : vertices) {
            if (vertex.getTextureCoord() == null) {
                hasTextureCoords = false;
                break;
            }
        }

        supportsTexture = hasTextureCoords;

        PrimitiveFaceSI[] primitiveFaces = triangulateFaces(faces);

        indexedVertexCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesToFloatBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, primitveFacesToIndexBuffer(primitiveFaces), GL_STATIC_DRAW);

        // vertex positions
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, Vertex2D.SIZE * FLOAT_SIZE, 0);
        // texture coords
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex2D.SIZE * FLOAT_SIZE, 2 * FLOAT_SIZE);

        glBindVertexArray(0);
    }

    public void render() throws Exception{
        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        if(supportsTexture) {
            glEnableVertexAttribArray(1);
        }

        glDrawElements(GL_TRIANGLES, indexedVertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        if(supportsTexture) {
            glDisableVertexAttribArray(1);
        }

        glBindVertexArray(0);
    }

    public void dispose() {
        // Disable
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // Delete the VBOs
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    private FloatBuffer verticesToFloatBuffer(Vertex2D[] vertices){
        final int floatCount = vertices.length * Vertex2D.SIZE;

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(floatCount);

        for(Vertex2D vertex : vertices){
            DataUtils.putVector2f(floatBuffer, vertex.getPosition());
            DataUtils.putVector2f(floatBuffer, supportsTexture ? vertex.getTextureCoord() : new Vector2f());
        }

        floatBuffer.flip();

        return floatBuffer;
    }

    private static IntBuffer primitveFacesToIndexBuffer(PrimitiveFaceSI[] primitiveFaces){
        final int indiciesCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indiciesCount);

        int[] indicesArray = new int[indiciesCount];

        for(int i = 0; i < primitiveFaces.length; i++){
            indicesArray[i * 3    ] = primitiveFaces[i].getI0();
            indicesArray[i * 3 + 1] = primitiveFaces[i].getI1();
            indicesArray[i * 3 + 2] = primitiveFaces[i].getI2();
        }

        indicesBuffer.put(indicesArray);
        indicesBuffer.flip();

        return indicesBuffer;
    }

    public boolean supportsTexture() {
        return supportsTexture;
    }

    private static PrimitiveFaceSI[] triangulateFaces(FaceSI[] faces){
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
