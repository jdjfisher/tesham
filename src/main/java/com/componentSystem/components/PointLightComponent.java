
package com.componentSystem.components;

import com.componentSystem.Component;
import com.engine.items.Attenuation;
import com.engine.items.Light;
import com.maths.vectors.Vector3f;
import com.utils.DataUtils;
import org.apache.commons.math3.util.FastMath;

public class PointLightComponent extends Component
{
    private Light light;
    private Attenuation attenuation;

    public PointLightComponent(Light light, Attenuation attenuation)
    {
        this.light = light;
        this.attenuation = attenuation;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public Vector3f getPosition(){
        return transformComponent.getPosition();
    }

    public float getRange(){
        return (-attenuation.getLinear() + (float) FastMath.sqrt(attenuation.getLinear() * attenuation.getLinear() - 4 * attenuation.getExponent() * (attenuation.getConstant() - light.getIntensity() * DataUtils.toVector3f(light.getColor()).getLargestComponent() / 0.01))) / (2f * attenuation.getExponent());
    }
}
