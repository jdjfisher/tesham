package com.engine.items;

import com.componentSystem.*;
import com.engine.input.Keyboard;
import com.maths.vectors.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Jordan Fisher on 29/06/2017.
 */
public class World {

    public static final Vector3f X_AXIS = new Vector3f(1,0,0);
    public static final Vector3f Y_AXIS = new Vector3f(0,1,0);
    public static final Vector3f Z_AXIS = new Vector3f(0,0,1);
    public static final Vector3f FORWARD_VECTOR = Z_AXIS;

    private final Camera camera;

    private final EntityManager entityManager;
    private final TransformComponentManager transformComponentManager;
    private final MeshComponentManager meshComponentManager;

    public World() {
        camera = new Camera(new Vector3f(0,0,5));

        entityManager = new EntityManager();
        transformComponentManager = new TransformComponentManager();
        meshComponentManager = new MeshComponentManager();
    }

    public void init(){
        Entity e1 = entityManager.create();

        transformComponentManager.subscribe(e1);
        meshComponentManager.subscribe(e1);


        transformComponentManager.getComponent(e1).getPosition().set(0, -4, 0);
    }

    public void handleInput(){
        camera.handleInput();

        if (Keyboard.isKeyTapped(GLFW_KEY_ENTER)){
            Entity e2 = entityManager.create();

            transformComponentManager.subscribe(e2);

            transformComponentManager.getComponent(e2).getPosition().set(1,2,3);
        }
    }

    public void updateLogic(float interval){
        camera.update(interval);

        transformComponentManager.update(interval);
        meshComponentManager.update(interval);

        transformComponentManager.garbageCollector(entityManager);
        meshComponentManager.garbageCollector(entityManager);
    }

    public void dispose(){

    }

    public Camera getCamera() {
        return camera;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public TransformComponentManager getTransformComponentManager() {
        return transformComponentManager;
    }

    public MeshComponentManager getMeshComponentManager() {
        return meshComponentManager;
    }
}