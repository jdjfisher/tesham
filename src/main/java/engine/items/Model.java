package engine.items;

import graphics.mesh.Mesh;

public class Model
{
    private Mesh mesh;
    private Material material;

    public Model(Mesh mesh)
    {
        this(mesh, new Material());
    }

    public Model(Mesh mesh, Material material)
    {
        this.mesh = mesh;
        this.material = material;
    }

    public void render() throws Exception
    {
        mesh.render();
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public void setMesh(Mesh mesh)
    {
        this.mesh = mesh;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public boolean supportsTexture()
    {
        return material.hasDiffuseTexture() && mesh.supportsTexture();
    }

    public boolean supportsSpecularMap()
    {
        return material.hasSpecularMap() && mesh.supportsTexture();
    }

    public boolean supportsNormalMap()
    {
        return material.hasNormalMap() && mesh.supportsTexture();
    }

    public void dispose()
    {
        mesh.dispose();
    }
}
