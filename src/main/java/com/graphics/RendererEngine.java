package com.graphics;


import com.engine.core.Window;
import com.engine.items.Camera;
import com.graphics.component.mesh._3D.Mesh3D;
import com.maths.Matrix4f;
import com.utils.GenerateMesh;

import java.awt.*;

public class RendererEngine {
    private ShaderProgram basicShader;
    private Mesh3D testMesh;

    public RendererEngine() {
    }

    public void init(Window window) throws Exception{
        basicShader = new ShaderProgram("basic");
        basicShader.bind();
//        basicShader.setColourRGBUniform("colour", Color.WHITE);

        testMesh = GenerateMesh.sphere(1, 50);
    }

    public void render(Window window, Camera camera) throws Exception{
        FrameBuffer.clearBuffers();

        basicShader.setMatrix4fUniform("PV_Matrix", Matrix4f.Multiply(window.getPerspectiveMatrix(), camera.getViewMatrix()));
        basicShader.setMatrix4fUniform("W_Matrix", Matrix4f.Identity());

        testMesh.render();
    }

    public void dispose(){
        basicShader.dispose();
        testMesh.dispose();
    }
}
