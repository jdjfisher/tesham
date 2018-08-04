package com.engine.items;

import com.engine.input.Cursor;
import com.engine.input.Keyboard;
import com.maths.Maths;
import com.maths.Matrix4f;
import com.maths.Quaternion;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import org.apache.commons.math3.util.FastMath;

import static com.componentSystem.World.X_AXIS;
import static com.componentSystem.World.Y_AXIS;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT;

/**
 * Created by Jordan Fisher on 19/05/2017.
 */
public class Camera {

    private Matrix4f viewMatrix;
    private Quaternion rotation;

    private Vector3f position;
    private Vector3f deltaCamPos;
    private float tilt; // Pitch
    private float pan; // Yaw

    private float cameraMovementSpeed;
    private float mouseSensitivity;

    public Camera(){
        this(Vector3f.Identity());
    }

    public Camera(Vector3f position){
        this.position = position;
        this.deltaCamPos = Vector3f.Identity();
        this.tilt = 0f;
        this.pan = 0f;

        preRender();

        this.cameraMovementSpeed = cameraMovementSpeed_DEFAULT;
        this.mouseSensitivity = mouseSensitivity_DEFAULT;
    }

    public void handleInput(){
        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            deltaCamPos.setZ(-1);
        } else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            deltaCamPos.setZ(1);
        } else{
            deltaCamPos.setZ(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            deltaCamPos.setX(-1);
        } else if(Keyboard.isKeyDown(GLFW_KEY_D)){
            deltaCamPos.setX(1);
        } else{
            deltaCamPos.setX(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_Q)){
            deltaCamPos.setY(1);
        } else if(Keyboard.isKeyDown(GLFW_KEY_E)){
            deltaCamPos.setY(-1);
        } else{
            deltaCamPos.setY(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            deltaCamPos.multiply(20);
        }else if(Keyboard.isKeyDown(GLFW_KEY_LEFT_ALT)) {
            deltaCamPos.multiply(0.1f);
        }

        if(!Cursor.isVisible()) {
            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
            deltaCursorPos.multiply(getMouseSensitivity());
            changeTilt(-deltaCursorPos.getY());
            changePan(deltaCursorPos.getX());
        }
    }

    public void update(float interval){
        changePositionRelativeToOrientation(Vector3f.Multiply(deltaCamPos,getCameraMovementSpeed() * interval));
    }

    public void preRender(){
        rotation = new Quaternion(-tilt, X_AXIS);
        rotation.multiply(pan, Y_AXIS);

        viewMatrix = Matrix4f.Identity();
        viewMatrix.multiply(Matrix4f.Translation(Vector3f.Negative(position)));
        viewMatrix.multiply(Matrix4f.QuaternionRotation(Quaternion.Conjugate(rotation)));
    }

    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }

    public void returnToOrigin(){
        this.position = new Vector3f();
        this.tilt = 0;
        this.pan = 0;
    }

    public Vector3f getDirection(){
        return Vector3f.Multiply(new Vector3f(0,0,-1), rotation);
    }

    public Quaternion getRotation(){
        return rotation;
    }

    public void facePoint(float x, float y, float z){
        facePoint(new Vector3f(x, y, z));
    }

    public void facePoint(Vector3f pointPositionVector){
        faceDirection(Vector3f.Subtract(pointPositionVector, position));
    }

    public void faceDirection(Vector3f direction){
        direction.normalize();
        pan = 90 + direction.getTheta();
        tilt = 90 - direction.getPhi();
    }

    @Override
    public String toString(){
        return String.format("Position %s\nDirection %s\nTilt: %f   Pan: %f\n", getPosition(), getDirection(), getTilt(), getPan());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getPosition(){
        return position;
    }

    public void changePositionRelativeToOrientation(Vector3f deltaPosition){
        changePositionRelativeToOrientation(deltaPosition.getX(), deltaPosition.getY(), deltaPosition.getZ());
    }

    public void changePositionRelativeToOrientation(float dx, float dy, float dz) {
        if(dx != 0){
            changePositionXRelativeToOrientation(dx);
        }
        if(dy != 0){
            changePositionYRelativeToOrientation(dy);
        }
        if(dz != 0){
            changePositionZRelativeToOrientation(dz);
        }
    }

    public void changePositionXRelativeToOrientation(float dx){
        position.addX(-dx * (float)FastMath.sin(FastMath.toRadians(pan - 90)));
        position.addZ(dx * (float) FastMath.cos(FastMath.toRadians(pan - 90)));
    }

    public void changePositionYRelativeToOrientation(float dy){
        position.addY(dy);
    }

    public void changePositionZRelativeToOrientation(float dz){
        position.addX(-dz * (float)FastMath.sin(FastMath.toRadians(pan)));
        position.addZ(dz * (float)FastMath.cos(FastMath.toRadians(pan)));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int MAX_TILT = 85;

    public float getTilt(){
        return tilt;
    }

    public void setTilt(float value){
        if(value > MAX_TILT){
            tilt = MAX_TILT;
        }else if(value < -MAX_TILT){
            tilt = -MAX_TILT;
        }else {
            tilt = value;
        }
    }

    public void changeTilt(float deltaTilt){
        setTilt(getTilt() + deltaTilt);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public float getPan(){
        return pan;
    }

    public void setPan(float value){
        pan = Maths.floorMod(value, 360f);
    }

    public void changePan(float deltaValue){
        setPan(getPan() + deltaValue);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final float cameraMovementSpeed_DEFAULT = 2f;
    private static final float cameraMovementSpeed_MINIMUM = 0.1f;
    private static final float cameraMovementSpeed_MAXIMUM = 20f;

    public void setCameraMovementSpeed(float value){
        if(value < cameraMovementSpeed_MINIMUM){
            cameraMovementSpeed = cameraMovementSpeed_MINIMUM;
        }else if(value > cameraMovementSpeed_MAXIMUM){
            cameraMovementSpeed = cameraMovementSpeed_MAXIMUM;
        }else {
            cameraMovementSpeed = value;
        }
    }

    public void changeCameraMovementSpeed(float value){
        setCameraMovementSpeed(getCameraMovementSpeed() + value);
    }

    public float getCameraMovementSpeed(){
        return cameraMovementSpeed;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final float mouseSensitivity_DEFAULT = 0.3f;
    public static final float mouseSensitivity_MINIMUM = 0.01f;
    public static final float mouseSensitivity_MAXIMUM = 5f;

    public void setMouseSensitivity(float value){
        if(value < mouseSensitivity_MINIMUM){
            mouseSensitivity = mouseSensitivity_MINIMUM;
        }else if(value > mouseSensitivity_MAXIMUM){
            mouseSensitivity = mouseSensitivity_MAXIMUM;
        }else {
            mouseSensitivity = value;
        }
    }

    public void changeMouseSensitivity(float value){
        setMouseSensitivity(getMouseSensitivity() + value);
    }

    public float getMouseSensitivity(){
        return mouseSensitivity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
