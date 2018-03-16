package com.maths;

import com.maths.vectors.Vector3f;

public class TransformationSet {
    private Vector3f position;
    private Quaternion rotation;
    private float scale;

    public TransformationSet(){
        this.position = new Vector3f();
        this.rotation = new Quaternion();
        this.scale = 1f;
    }

    public Matrix4f getGlobalMatrix(){
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

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        if (scale <= 0){
            this.scale = 0;
        }else {
            this.scale = scale;
        }
    }

    public void changeScale(float delta){
        setScale(getScale() + delta);
    }
}
