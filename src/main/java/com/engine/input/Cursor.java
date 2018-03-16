package com.engine.input;

import com.engine.core.Window;
import com.maths.vectors.Vector2d;
import com.maths.vectors.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class Cursor extends GLFWCursorPosCallback{
    private static Window window;

    private static Vector2d currentPosition;
    private static Vector2d previousPosition;

    private static boolean cameraMode;
    private static boolean onScreen;

    public Cursor(Window window){
        this.window = window;
        currentPosition = new Vector2d();
        previousPosition = new Vector2d();
        cameraMode = false;
    }

    public void invoke(long window, double x, double y) {
        currentPosition.setX(x);
        currentPosition.setY(y);
    }

    public static void update(){
        previousPosition.set(currentPosition);
        if(!cameraMode){
            onScreen = (currentPosition.getX() > 0 && currentPosition.getY() > 0 && currentPosition.getX() < window.getWidth() && currentPosition.getY() < window.getHeight());
        }
    }

    public static Vector2d getCurrentPosition(){
        return currentPosition;
    }

    public static Vector2f getDeltaPosition(){
        Vector2d deltaPosition = Vector2d.Subtract(currentPosition, previousPosition);
        return new Vector2f(
                (float) deltaPosition.getX(),
                (float) deltaPosition.getY()
        );
    }

    public static void toggleCameraMode(){
        setCameraMode(!cameraMode);
    }

    public static void setCameraMode(boolean state){
        cameraMode = state;
        if(state){
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }else{
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            glfwSetCursorPos(window.getHandle(), window.getWidth()/2, window.getHeight()/2);
        }
    }

    public static boolean inCameraMode(){
        return cameraMode;
    }

    public static boolean onScreen(){
        return onScreen;
    }
}
