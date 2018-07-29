package com.graphics.lighting;

import com.graphics.opengl.mesh._3D.Mesh3D;
import com.utils.GenerateMesh;

import java.awt.*;

/**
 * Created by fisherj16 on 7/20/2017.
 */
public class SpotLight extends Lamp {

    private static final Mesh3D SPOT_LIGHT_MESH = GenerateMesh.semiSphere(0.2f, 30);

    private float cutOff;
    private float outerCutOff;

    public SpotLight(Color color, float intensity, float cutOff, float outerCutOff, Attenuation attenuation) {
        super(color, intensity, attenuation);
        this.cutOff = cutOff;
        this.outerCutOff = outerCutOff;
    }

    @Override
    public Mesh3D getMesh() {
        return SPOT_LIGHT_MESH;
    }

    public float getCutOff() {
        return cutOff;
    }

    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    public float getOuterCutOff() {
        return outerCutOff;
    }

    public void setOuterCutOff(float outerCutOff) {
        this.outerCutOff = outerCutOff;
    }
}