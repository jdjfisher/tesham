package engine.items;

import maths.Quaternion;
import maths.vectors.Vector3f;

import static componentSystem.World.FORWARD_VECTOR;

public class DirectionalLight
{
    private Light light;
    private Quaternion rotation;

    public DirectionalLight(Light light)
    {
        this(light, new Quaternion());
    }

    public DirectionalLight(Light light, Quaternion rotation)
    {
        this.light = light;
        this.rotation = rotation;
    }

    public Light getLight()
    {
        return light;
    }

    public void setLight(Light light)
    {
        this.light = light;
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public Vector3f getDirection()
    {
        return Vector3f.Multiply(FORWARD_VECTOR, rotation);
    }
}
