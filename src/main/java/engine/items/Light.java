package engine.items;

import java.awt.*;

public class Light
{
    private float intensity;
    private Color colour;
    private boolean enabled;

    public Light(float intensity, Color colour)
    {
        this.intensity = intensity;
        this.colour = colour;
        this.enabled = true;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public void changeIntensity(float deltaIntensity)
    {
        setIntensity(getIntensity() + deltaIntensity);
    }

    public Color getColor()
    {
        return colour;
    }

    public void setColour(Color colour)
    {
        this.colour = colour;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void toggleEnabled()
    {
        this.enabled = !this.enabled;
    }
}
