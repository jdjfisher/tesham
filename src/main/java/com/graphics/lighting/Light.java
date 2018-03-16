package com.graphics.lighting;

import java.awt.*;

/**
 * Created by Jordan Fisher on 18/07/2017.
 */

public class Light {
    private static final float intensityDEFAULT = 1f;
    private static final Color colourDEFAULT = Color.WHITE;

    private final String name;
    private float intensity;
    private Color colour;
    private boolean active;

    public Light(String name){
        this(name, colourDEFAULT, intensityDEFAULT);
    }

    public Light(String name, Color colour){
        this(name, colour,intensityDEFAULT);
    }

    public Light(String name, float intensity){
        this(name, colourDEFAULT, intensity);
    }

    public Light(String name, Color colour, float intensity){
        this.name = name;
        this.colour = colour;
        this.intensity = intensity;
        this.active = true;
    }

    @Override
    public String toString(){
        return name;
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
