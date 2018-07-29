package com.graphics.component.mesh._3D;

import java.util.ArrayList;
import java.util.Arrays;

public class FaceSI {
    private final int[] indexes;


    public FaceSI(int[] indexes) {
//        if(indexes.length < 3){
//            throw new Exception("Face order less than 3"); TODO: Handle this
//        }
        this.indexes = indexes;
    }

    public FaceSI(
            int iA,
            int iB,
            int iC
    ) {
        this(
                new int[]{
                        iA,
                        iB,
                        iC
                }
        );
    }

    public FaceSI(
            int iA,
            int iB,
            int iC,
            int iD
    ) {
        this(
                new int[]{
                        iA,
                        iB,
                        iC,
                        iD
                }
        );
    }

    public int[] getIndexes() {
        return indexes;
    }

    public Vertex3D[] getVertices(ArrayList<Vertex3D> vertices) {
        Vertex3D[] faceVertices = new Vertex3D[getFaceOrder()];

        for(int i = 0; i < getFaceOrder(); i++){
            faceVertices[i] = vertices.get(indexes[i]);
        }

        return faceVertices;
    }

    public int getFaceOrder(){
        return indexes.length;
    }

    public int getPrimtiveFaceCount(){
        return getFaceOrder() - 2;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(indexes);
    }
}
