package com.graphics.opengl.mesh;

import java.util.ArrayList;

public class SingleIndexMeshData {
    private ArrayList<Vertex> vertices;
    private ArrayList<FaceSI> faces;

    public SingleIndexMeshData(ArrayList<Vertex> vertices, ArrayList<FaceSI> faces){
        set(vertices, faces);
    }

    public void set(ArrayList<Vertex> vertices, ArrayList<FaceSI> faces){
        this.vertices = vertices;
        this.faces = faces;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<FaceSI> getFaces() {
        return faces;
    }
}
