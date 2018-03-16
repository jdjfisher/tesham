package com.graphics.lighting;

import com.graphics.component.mesh._3D.Mesh3D;
import com.maths.TransformationSet;
import com.utils.DataUtils;

import java.awt.*;

public abstract class Lamp extends Light{
    private final TransformationSet transformationSet;
    private Attenuation attenuation;

    public Lamp(String name, Color colour, float intensity, Attenuation attenuation) {
        super(name, colour, intensity);
        this.transformationSet = new TransformationSet();
        this.attenuation = attenuation;
    }

    public TransformationSet getTransformationSet() {
        return transformationSet;
    }

    public abstract Mesh3D getMesh();

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

