package com.graphics.lighting;

import com.maths.Quaternion;
import com.maths.vectors.Vector3f;

import java.awt.*;

import static com.engine.items.World.WORLD_FORWARD_VECTOR;

public class DirectionalLight extends Light {
    private Quaternion rotation;

    public DirectionalLight(String name, Color colour, float intensity){
        this(name, colour, intensity, new Quaternion());
    }

    public DirectionalLight(String name, Color colour, float intensity, Quaternion rotation){
        super(name, colour, intensity);
        this.rotation = rotation;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Vector3f getDirection(){
        return Vector3f.Multiply(WORLD_FORWARD_VECTOR, rotation);
    }
}
