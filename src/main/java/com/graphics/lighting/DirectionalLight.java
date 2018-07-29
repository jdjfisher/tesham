package com.graphics.lighting;

import com.engine.items.World;
import com.maths.Quaternion;
import com.maths.vectors.Vector3f;

import java.awt.*;

public class DirectionalLight extends Light {
    private Quaternion rotation;

    public DirectionalLight(Color colour, float intensity){
        this(colour, intensity, new Quaternion());
    }

    public DirectionalLight(Color colour, float intensity, Quaternion rotation){
        super(colour, intensity);
        this.rotation = rotation;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Vector3f getDirection(){
        return Vector3f.Multiply(World.FORWARD_VECTOR, rotation);
    }
}
