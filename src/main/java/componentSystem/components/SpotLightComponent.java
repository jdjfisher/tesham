package componentSystem.components;

import componentSystem.Component;
import engine.items.Attenuation;
import engine.items.Light;
import maths.Quaternion;
import maths.vectors.Vector3f;

public class SpotLightComponent extends Component
{
    private Light light;
    private Attenuation attenuation;
    private float innerCutOff;
    private float outerCutOff;

    public SpotLightComponent(Light light, Attenuation attenuation, float innerCutOff, float outerCutOff)
    {
        this.light = light;
        this.attenuation = attenuation;
        this.innerCutOff = innerCutOff;
        this.outerCutOff = outerCutOff;
    }

    public Light getLight()
    {
        return light;
    }

    public void setLight(Light light)
    {
        this.light = light;
    }

    public Attenuation getAttenuation()
    {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation)
    {
        this.attenuation = attenuation;
    }

    public float getInnerCutOff()
    {
        return innerCutOff;
    }

    public void setInnerCutOff(float innerCutOff)
    {
        this.innerCutOff = innerCutOff;
    }

    public float getOuterCutOff()
    {
        return outerCutOff;
    }

    public void setOuterCutOff(float outerCutOff)
    {
        this.outerCutOff = outerCutOff;
    }

    public Vector3f getPosition()
    {
        return transformComponent.getPosition();
    }

    public Quaternion getRotation()
    {
        return transformComponent.getRotation();
    }
}
