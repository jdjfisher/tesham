/*
package com.wip.components;

import com.wip.core.AbstractComponent;
import com.wip.core.Entity;
import com.maths.Matrix4f;
import com.maths.Quaternion;
import com.maths.vectors.Vector3f;

public class TransformComponent extends AbstractComponent
{
    private Vector3f position;
    private Quaternion rotation;
    private float scale;

    public TransformComponent(Entity parent) {
        this(parent, Vector3f.Identity(), Quaternion.Identity(), 1);
    }

    public TransformComponent(Entity parent, Vector3f position, Quaternion rotation, float scale) {
        super(parent);
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Matrix4f getTransformation(){
        Matrix4f result = Matrix4f.Identity();

        if(!position.equals(Vector3f.Identity())){
            result.multiply(Matrix4f.Translation(position));
        }

        if(!rotation.equals(Quaternion.Identity())) {
            result.multiply(Matrix4f.QuaternionRotation(rotation));
        }

        if(scale != 1) {
            result.multiply(Matrix4f.Enlargement(scale));
        }

        return result;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}*/
