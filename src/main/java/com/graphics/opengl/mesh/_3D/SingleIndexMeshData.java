package com.graphics.component.mesh._3D;

import java.util.ArrayList;

public class SingleIndexMeshData {
    private ArrayList<Vertex3D> vertices;
    private ArrayList<FaceSI> faces;

    public SingleIndexMeshData(ArrayList<Vertex3D> vertices, ArrayList<FaceSI> faces){
        set(vertices, faces);
    }

    public void set(ArrayList<Vertex3D> vertices, ArrayList<FaceSI> faces){
        this.vertices = vertices;
        this.faces = faces;
    }

    public ArrayList<Vertex3D> getVertices() {
        return vertices;
    }

    public ArrayList<FaceSI> getFaces() {
        return faces;
    }
}
