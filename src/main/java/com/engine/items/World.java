package com.engine.items;
import com.componentSystem.borrowed.Component;
import com.componentSystem.borrowed.Entity;
import com.componentSystem.borrowed.components.TransformComponent;
import com.maths.vectors.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordan Fisher on 29/06/2017.
 */
public class World {

    public static final Vector3f X_AXIS = new Vector3f(1,0,0);
    public static final Vector3f Y_AXIS = new Vector3f(0,1,0);
    public static final Vector3f Z_AXIS = new Vector3f(0,0,1);
    public static final Vector3f FORWARD_VECTOR = Z_AXIS;

    private final Camera camera;

    public World() {
        camera = new Camera(new Vector3f(-1,1,6));
        camera.facePoint(0,0,0);
    }

    public void init(){

    }

    public void handleInput(){
        camera.handleInput();


    }

    public void updateLogic(float interval){
        camera.update(interval);
    }

    public void dispose(){

    }

    public Camera getCamera() {
        return camera;
    }
}