package com.graphics.lighting;


import com.graphics.component.mesh._3D.Mesh3D;
import com.maths.RNG;
import com.utils.GenerateMesh;

import java.awt.*;

public class PointLight extends Lamp {

    private static final Mesh3D POINT_LIGHT_MESH = GenerateMesh.sphere(1f, 30);

    public PointLight(String name, float intensity, Attenuation attenuation) {
        super(name, RNG.Colour(), intensity, attenuation);
    }

    public PointLight(String name, Color color, float intensity, Attenuation attenuation) {
        super(name, color, intensity, attenuation);
    }

    @Override
    public Mesh3D getMesh() {
        return POINT_LIGHT_MESH;
    }

    public PointLight(PointLight pointLight) { //copy
        this(
                pointLight.toString() + "#",
                new Color(pointLight.getColor().getRed(), pointLight.getColor().getGreen(), pointLight.getColor().getBlue()),
                pointLight.getIntensity(),
                new Attenuation(pointLight.getAttenuation())
        );
    }
}