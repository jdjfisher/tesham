package com.graphics.mesh;

import java.util.Objects;

import static com.graphics.mesh.FaceMI.IndexSet.NULL_INDEX;

public class FaceMI {
    private final IndexSet[] indexSets;

    public FaceMI(IndexSet[] indexSets){
//        if(indexSets.length < 3){
//            throw new Exception("Face order less than 3"); TODO: Handle this
//        }
        this.indexSets = indexSets;
    }

    public FaceMI(
            int vA,
            int vB,
            int vC
    ){
        this(
                new IndexSet[]{
                        new IndexSet(vA),
                        new IndexSet(vB),
                        new IndexSet(vC)
                }
        );
    }

    public FaceMI(
            int vA,
            int vB,
            int vC,
            int vD
    ){
        this(
                new IndexSet[]{
                        new IndexSet(vA),
                        new IndexSet(vB),
                        new IndexSet(vC),
                        new IndexSet(vD)
                }
        );
    }

    public FaceMI(
            int vA,
            int vB,
            int vC,
            int vD,
            int vE
    ){
        this(
                new IndexSet[]{
                        new IndexSet(vA),
                        new IndexSet(vB),
                        new IndexSet(vC),
                        new IndexSet(vD),
                        new IndexSet(vE)
                }
        );
    }

    public FaceMI(
            int vA, int tA,
            int vB, int tB,
            int vC, int tC
    ){
        this(
                new IndexSet[]{
                        new IndexSet(vA, NULL_INDEX, tA),
                        new IndexSet(vB, NULL_INDEX, tB),
                        new IndexSet(vC, NULL_INDEX, tC)
                }
        );
    }

    public FaceMI(
            int vA, int tA,
            int vB, int tB,
            int vC, int tC,
            int vD, int tD
    ){
        this(
                new IndexSet[]{
                        new IndexSet(vA, NULL_INDEX, tA),
                        new IndexSet(vB, NULL_INDEX, tB),
                        new IndexSet(vC, NULL_INDEX, tC),
                        new IndexSet(vD, NULL_INDEX, tD)
                }
        );
    }


    public IndexSet[] getIndexSets() {
        return indexSets;
    }

    public int getFaceOrder(){
        return indexSets.length;
    }

    public int getPrimtiveFaceCount(){
        return getFaceOrder() - 2;
    }

    public static class IndexSet{
        public static final int NULL_INDEX = -1;

        private final int vertexPositionIndex;
        private final int textureCoordinateIndex;
        private final int vertexNormalIndex;

        public IndexSet(int vertexPositionIndex){
            this(vertexPositionIndex, NULL_INDEX, NULL_INDEX);
        }

        public IndexSet(int vertexPositionIndex, int vertexNormalIndex, int textureCoordinateIndex){
            this.vertexPositionIndex = vertexPositionIndex;
            this.vertexNormalIndex = vertexNormalIndex;
            this.textureCoordinateIndex = textureCoordinateIndex;
        }

        public int getVertexPositionIndex() {
            return vertexPositionIndex;
        }

        public int getVertexNormalIndex() {
            return vertexNormalIndex;
        }

        public int getTextureCoordinateIndex() {
            return textureCoordinateIndex;
        }

        @Override
        public boolean equals(Object obj) {
            IndexSet indexSet = (IndexSet)obj;
            return indexSet.vertexPositionIndex == vertexPositionIndex && indexSet.vertexNormalIndex == vertexNormalIndex && indexSet.textureCoordinateIndex == textureCoordinateIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertexPositionIndex, vertexNormalIndex, textureCoordinateIndex);
        }
    }
}
