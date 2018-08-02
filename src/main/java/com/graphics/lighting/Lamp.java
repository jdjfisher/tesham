package com.graphics.lighting;

import com.graphics.opengl.mesh.Mesh;
import com.maths.TransformationSet;
import com.utils.DataUtils;

import java.awt.*;

public abstract class Lamp extends Light{
    private final TransformationSet transformationSet;
    private Attenuation attenuation;

    public Lamp(Color colour, float intensity, Attenuation attenuation) {
        super(colour, intensity);
        this.transformationSet = new TransformationSet();
        this.attenuation = attenuation;
    }

    public TransformationSet getTransformationSet() {
        return transformationSet;
    }

    public abstract Mesh getMesh();

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public float getRange(){
        return attenuation.getRange(getIntensity() * DataUtils.toVector3f(getColor()).getLargestComponent());
    }
}

