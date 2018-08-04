package com.componentSystem.components;

import com.componentSystem.Component;
import com.graphics.mesh.Mesh;

public class MeshComponent extends Component
{
    private final Mesh mesh;

    public MeshComponent(Mesh mesh)
    {
        this.mesh = mesh;
    }

    public Mesh getMesh()
    {
        return mesh;
    }
}
