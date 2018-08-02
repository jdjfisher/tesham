package com.graphics.lighting;


import com.graphics.opengl.mesh.Mesh;
import com.maths.RNG;
import com.utils.GenerateMesh;

import java.awt.*;

public class PointLight extends Lamp {

    private static final Mesh POINT_LIGHT_MESH = GenerateMesh.sphere(1f, 30);

    public PointLight(float intensity, Attenuation attenuation) {
        super(RNG.Colour(), intensity, attenuation);
    }

    public PointLight(Color color, float intensity, Attenuation attenuation) {
        super(color, intensity, attenuation);
    }

    @Override
    public Mesh getMesh() {
        return POINT_LIGHT_MESH;
    }
}