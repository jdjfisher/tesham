package com.graphics.lighting;

import java.awt.*;

/**
 * Created by Jordan Fisher on 18/07/2017.
 */

public class Light {
    private static final float intensityDEFAULT = 1f;
    private static final Color colourDEFAULT = Color.WHITE;

    private float intensity;
    private Color colour;
    private boolean active;

    public Light(){
        this(colourDEFAULT, intensityDEFAULT);
    }

    public Light(Color colour){
        this(colour,intensityDEFAULT);
    }

    public Light(float intensity){
        this(colourDEFAULT, intensity);
    }

    public Light(Color colour, float intensity){
        this.colour = colour;
        this.intensity = intensity;
        this.active = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Color getColor() {
        return colour;
    }

    public void setColour(Color colour){
        this.colour = colour;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void changeIntensity(float deltaIntensity){
        setIntensity(getIntensity() + deltaIntensity);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void toggleActive() {
        this.active = !this.active;
    }
}
