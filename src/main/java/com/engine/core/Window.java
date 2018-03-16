package com.engine.core;

import com.engine.input.Cursor;
import com.engine.input.Keyboard;
import com.engine.input.MouseButtons;
import com.engine.input.MouseWheel;
import com.maths.Matrix4f;
import com.maths.vectors.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static com.engine.core.Options.*;
import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_MIPMAP;

public class Window {

    private long windowHandle;
    private final String title;
    private int refreshRate;

    private int windowedWidth;
    private int windowedHeight;

    private int fullScreenWidth;
    private int fullScreenHeight;

    private int currentWidth;
    private int currentHeight;

    private boolean hasResized;
    private boolean minimized;

    private Matrix4f perspectiveMatrix;
    private Matrix4f orthographic2DMatrix;

    public Window(String title, int width, int height) {
        this.windowedWidth = width;
        this.windowedHeight = height;
        this.title = title;
        currentWidth = windowedWidth;
        currentHeight = windowedHeight;
        hasResized = false;
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
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowHandle = glfwCreateWindow(windowedWidth, windowedHeight, title, NULL, NULL);

        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        centreWindow();

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            hasResized = true;
            if(isWindowFullScreen()){
                currentWidth = width;
                currentHeight = height;
            }else if(!minimized){
                currentWidth = windowedWidth = width;
                currentHeight = windowedHeight = height;
            }
        });

        glfwSetWindowIconifyCallback(windowHandle, (window, state) -> minimized = state);

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
        if(isWindowFullScreen() && (glfwGetWindowMonitor(windowHandle) != glfwGetPrimaryMonitor())){
            setFullScreen(true);
        }else if(!isWindowFullScreen() && glfwGetWindowMonitor(windowHandle) == glfwGetPrimaryMonitor()){
            setFullScreen(false);
        }

        if (isVSyncEnabled()) {
            glfwSwapInterval(1);
        }else {
            glfwSwapInterval(0);
        }

        setViewPort();

        perspectiveMatrix = Matrix4f.Perspective(getFOV(), getAspectRatio(), getViewDistanceNear(), getViewDistanceFar());
        orthographic2DMatrix = Matrix4f.Orthographic2D(0, getWidth(), getHeight(), 0);
    }

    public void update() {
        hasResized = false;
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void dispose(){
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void setFullScreen(boolean state){
        glfwHideWindow(windowHandle);

        glfwSetWindowMonitor(
                windowHandle,
                state ? glfwGetPrimaryMonitor() : NULL,
                (fullScreenWidth - currentWidth) / 2 ,
                (fullScreenHeight - currentHeight) / 2,
                state ? fullScreenWidth : windowedWidth,
                state ? fullScreenHeight : windowedHeight,
                refreshRate
        );

        if(!state){
            centreWindow();
        }

        glfwShowWindow(windowHandle);
        glfwFocusWindow(windowHandle);
    }

    public void centreWindow(){
        glfwSetWindowPos(windowHandle, (fullScreenWidth - currentWidth) / 2, (fullScreenHeight - currentHeight) / 2);
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
        glViewport(0,0, currentWidth, currentHeight);
    }

    public Matrix4f getOrthographic2DMatrix() {
        return orthographic2DMatrix;
    }

    public Matrix4f getPerspectiveMatrix() {
        return perspectiveMatrix;
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(windowHandle);
    }

    public boolean hasResized() {
        return hasResized;
    }

    public boolean isMinimized() {
        return minimized;
    }

    public long getHandle(){
        return windowHandle;
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }

    public String getTitle() {
        return title;
    }

    public int getWidth(){
        return currentWidth;
    }

    public  int getHeight(){
        return currentHeight;
    }

    public float getAspectRatio(){
        return getWidth() / (float)getHeight();
    }

    public int getRefreshRate() {
        return refreshRate;
    }
}
