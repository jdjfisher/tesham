package com.engine.items;

import com.componentSystem.Entity;
import com.componentSystem.EntityManager;
import com.componentSystem.MeshComponentManager;
import com.componentSystem.TransformComponentManager;
import com.engine.core.Window;
import com.engine.input.Cursor;
import com.engine.input.Keyboard;
import com.engine.input.MouseButtons;
import com.graphics.RendererEngine;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * Created by Jordan Fisher on 29/06/2017.
 */
public class World {

    public static final Vector3f X_AXIS = new Vector3f(1,0,0);
    public static final Vector3f Y_AXIS = new Vector3f(0,1,0);
    public static final Vector3f Z_AXIS = new Vector3f(0,0,1);
    public static final Vector3f WORLD_FORWARD_VECTOR = Z_AXIS;

    private final Camera camera;
    final Vector3f deltaCamPos;

    private final EntityManager entityManager;
    private final TransformComponentManager transformComponentManager;
    private final MeshComponentManager meshComponentManager;

    public World() {
        camera = new Camera(new Vector3f(0,0,5));
        deltaCamPos = Vector3f.Identity();

        entityManager = new EntityManager();
        transformComponentManager = new TransformComponentManager();
        meshComponentManager = new MeshComponentManager();
    }

    public void init(){
        Entity e1 = entityManager.create();

        transformComponentManager.subscribe(e1);
        meshComponentManager.subscribe(e1);
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

    public void render(Window window, RendererEngine renderer) throws Exception{
        window.preRender();
        camera.preRender();

        renderer.render(window, camera);
    }

    public void dispose(){

    }
}