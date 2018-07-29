package com.graphics.lighting;


import com.graphics.opengl.mesh._3D.Mesh3D;
import com.maths.RNG;
import com.utils.GenerateMesh;

import java.awt.*;

public class PointLight extends Lamp {

    private static final Mesh3D POINT_LIGHT_MESH = GenerateMesh.sphere(1f, 30);

    public PointLight(float intensity, Attenuation attenuation) {
        super(RNG.Colour(), intensity, attenuation);
    }

    public PointLight(Color color, float intensity, Attenuation attenuation) {
        super(color, intensity, attenuation);
    }

    @Override
    public Mesh3D getMesh() {
        return POINT_LIGHT_MESH;
    }
}