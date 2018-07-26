package com.engine.core;

import static com.engine.core.Options.*;
import com.engine.input.Cursor;
import com.engine.input.Keyboard;
import com.engine.input.MouseButtons;
import com.engine.input.MouseWheel;
import com.maths.Matrix4f;
import com.maths.vectors.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL43.GL_MIPMAP;

public class Window {

    private long windowHandle;
    private String title;
    private int refreshRate;

    private int windowedWidth;
    private int windowedHeight;

    private int fullScreenWidth;
    private int fullScreenHeight;

    private IResizeCallback resizeCallback;
    private boolean fullScreen;

    private Matrix4f perspectiveMatrix;
    private Matrix4f orthographic2DMatrix;

    public Window(String title, int width, int height) {
        this.windowedWidth = width;
        this.windowedHeight = height;
        this.title = title;
        this.resizeCallback = null;
        fullScreen = false;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFWVidMode primaryMonitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
        refreshRate = primaryMonitor.refreshRate();
        fullScreenWidth = primaryMonitor.width();
        fullScreenHeight = primaryMonitor.height();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, refreshRate);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowHandle = glfwCreateWindow(windowedWidth, windowedHeight, title, NULL, NULL);

        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        centreWindow();

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            if(!fullScreen){
                windowedWidth = width;
                windowedHeight = height;
            }

            if(resizeCallback != null) {
                resizeCallback.invoke(width, height);
            }
        });

        glfwSetKeyCallback(windowHandle, new Keyboard());
        glfwSetMouseButtonCallback(windowHandle, new MouseButtons());
        glfwSetCursorPosCallback(windowHandle, new Cursor(this));
        glfwSetScrollCallback(windowHandle, new MouseWheel());

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glFrontFace(GL_CCW);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_MIPMAP);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glfwShowWindow(windowHandle);
    }

    public void preRender(){
        if (isVSyncEnabled()) {
            glfwSwapInterval(1);
        }else {
            glfwSwapInterval(0);
        }

        setViewPort();

        perspectiveMatrix = Matrix4f.Perspective(getFOV(), getAspectRatio(), getViewDistanceNear(), getViewDistanceFar());
        orthographic2DMatrix = Matrix4f.Orthographic2D(0, getWidth(), getHeight(), 0);
    }

    public void pollEvents() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void dispose(){
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean state){
        if(fullScreen != state) {
            fullScreen = state;
            glfwHideWindow(windowHandle);

            glfwSetWindowMonitor(
                    windowHandle,
                    state ? glfwGetPrimaryMonitor() : NULL,
                    (fullScreenWidth - getWidth()) / 2,
                    (fullScreenHeight - getHeight()) / 2,
                    getWidth(),
                    getHeight(),
                    refreshRate
            );

            glfwShowWindow(windowHandle);
            glfwFocusWindow(windowHandle);
        }
    }

    public void toggleFullScreen(){
        setFullScreen(!isFullScreen());
    }

    public void setVisibility(boolean state){
        if(state) {
            glfwShowWindow(windowHandle);
        }else {
            glfwHideWindow(windowHandle);
        }
    }

    public void centreWindow(){
        glfwSetWindowPos(windowHandle, (fullScreenWidth - getWidth()) / 2, (fullScreenHeight - getHeight()) / 2);
    }

    public void changeWindowPosition(Vector2i deltaPosition){
        Vector2i windowPosition = Vector2i.Add(getWindowPosition(), deltaPosition);
        glfwSetWindowPos(windowHandle, windowPosition.getX(), windowPosition.getY());
    }

    public Vector2i getWindowPosition(){
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowPos(windowHandle, x, y);
        return new Vector2i(x[0], y[0]);
    }

    public void setViewPort(){
        glViewport(0,0, getWidth(), getHeight());
    }

    public Matrix4f getOrthographic2DMatrix() {
        return orthographic2DMatrix;
    }

    public Matrix4f getPerspectiveMatrix() {
        return perspectiveMatrix;
    }

    public long getHandle(){
        return windowHandle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
        glfwSetWindowTitle(windowHandle, title);
    }

    public void appendToTitle(String string){
        glfwSetWindowTitle(windowHandle, String.format("%s (%s)", title, string));
    }

    public int getWidth(){
        return fullScreen ? fullScreenWidth : windowedWidth;
    }

    public  int getHeight(){
        return fullScreen ? fullScreenHeight : windowedHeight;
    }

    public float getAspectRatio(){
        return getWidth() / (float)getHeight();
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setResizeCallback(IResizeCallback resizeCallback){
        this.resizeCallback = resizeCallback;
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(windowHandle);
    }

    public boolean isMinimized() {
        return glfwGetWindowAttrib(windowHandle, GLFW_ICONIFIED) == 1;
    }

    public boolean isFocused(){
        return glfwGetWindowAttrib(windowHandle, GLFW_FOCUSED) == 1;
    }
}