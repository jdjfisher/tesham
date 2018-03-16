package com.graphics.lighting;

import org.apache.commons.math3.util.FastMath;

/**
 * Created by Jordan Fisher on 20/07/2017.
 */
public class Attenuation {
    private static final double DARKNESS_CONSTANT = 0.01;

    private float constant;
    private float linear;
    private float exponent;

    public Attenuation(){
        this(0, 0, 0);
    }

    public Attenuation(Attenuation attenuation){
        this(attenuation.constant, attenuation.linear, attenuation.exponent);
    }

    public Attenuation(float constant, float linear, float exponent) {
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        return String.format("Attenuation: [Exponent %f, Linear %f, Constant %f ]", exponent, linear, constant);
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getExponent() {
        return exponent;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }

    public float getRange(float lightMaxBrightness){
        if(exponent != 0) {
            return (-linear + (float) FastMath.sqrt(linear * linear - 4 * exponent * (constant - lightMaxBrightness / DARKNESS_CONSTANT))) / (2f * exponent);
        }else if(linear != 0) {
            return (float)(lightMaxBrightness / DARKNESS_CONSTANT - constant) / linear;
        }else {
            return (float)(lightMaxBrightness / DARKNESS_CONSTANT - constant);
        }
    }
}
