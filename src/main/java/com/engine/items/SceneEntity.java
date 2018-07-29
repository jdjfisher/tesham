package com.engine.items;

import com.graphics.opengl.mesh._3D.Mesh3D;
import com.maths.TransformationSet;
import com.maths.vectors.Vector3f;

import java.util.ArrayList;

/**
 * Created by fisherj16 on 5/16/2017.
 */

public class SceneEntity {
    private String name;
    private ArrayList<Model> models;
    private boolean visible;
    private boolean frozen;
    private final TransformationSet transformationSet;
    private final Vector3f velocity;
    private final Vector3f acceleration;

    public SceneEntity(String name, Mesh3D mesh, Material material){
        this(name);
        this.models.add(new Model(mesh, material));
    }

    public SceneEntity(String name, Mesh3D mesh){
        this(name);
        this.models.add(new Model(mesh));
    }

    public SceneEntity(String name, Model model){
        this(name);
        this.models.add(model);
    }

    public SceneEntity(String name, ArrayList<Model> models){
        this(name);
        this.models.addAll(models);
    }

    public SceneEntity(String name){
        this.name = name;
        this.models = new ArrayList<>();
        this.visible = true;
        this.frozen = false;
        this.transformationSet = new TransformationSet();
        this.velocity = new Vector3f();
        this.acceleration = new Vector3f();
    }

    public void update(float interval){
        if(!frozen) {
            getVelocity().add(Vector3f.Multiply(acceleration, interval));
            transformationSet.getPosition().add(Vector3f.Multiply(velocity, interval));
        }
    }

    @Override
    public String toString(){
        return name;
    }

    public void dispose(){
        for(Model model : models) {
            model.dispose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Model> getModels(){
        return models;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isVisible() {
        return visible;
    }

    public void setVisibility(boolean state){
        visible = state;
    }

    public void toggleVisibility(){
        visible = !visible;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isFrozen(){
        return frozen;
    }

    public void setFrozen(boolean state){
        frozen = state;
    }

    public void toggleFreeze(){
        frozen = !frozen;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public TransformationSet getTransformationSet() {
        return transformationSet;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getVelocity(){
        return velocity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getAcceleration(){
        return acceleration;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
