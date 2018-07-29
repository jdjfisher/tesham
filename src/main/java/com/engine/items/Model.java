package com.engine.items;

import com.graphics.opengl.mesh._3D.Mesh3D;

public class Model {
    private Mesh3D mesh;
    private Material material;

    public Model(Mesh3D mesh){
        this(mesh, new Material());
    }

    public Model(Mesh3D mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    public void render() throws Exception{
        mesh.render();
    }

    public Mesh3D getMesh() {
        return mesh;
    }

    public void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean supportsTexture(){
        return material.hasDiffuseTexture() && mesh.supportsTexture();
    }

    public boolean supportsSpecularMap(){
        return material.hasSpecularMap() && mesh.supportsTexture();
    }

    public boolean supportsNormalMap(){
        return material.hasNormalMap() && mesh.supportsTexture();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void dispose(){
        mesh.dispose();
    }
}
