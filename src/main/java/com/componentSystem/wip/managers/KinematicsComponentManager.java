/*
package com.componentSystem.wip.managers;

import com.componentSystem.wip.components.KinematicsComponent;
import com.componentSystem.wip.core.AbstractComponentManager;
import com.componentSystem.wip.core.Entity;
import com.maths.vectors.Vector3f;

public class KinematicsComponentManager extends AbstractComponentManager<KinematicsComponent>
{
    @Override
    public void subscribe(Entity entity)
    {

    }

    @Override
    public void update(float interval)
    {
        for(KinematicsComponent component : getComponents())
        {
            component.getVelocity().add(Vector3f.Multiply(component.getAcceleration(), interval));
//            getPosition().add(Vector3f.Multiply(component.getVelocity(), interval));
        }
    }
}
*/
