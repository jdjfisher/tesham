package componentSystem.components;

import componentSystem.Component;
import graphics.mesh.Mesh;

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
