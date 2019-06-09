/*
package wip.managers;

import wip.components.KinematicsComponent;
import wip.core.AbstractComponentManager;
import wip.core.Entity;
import maths.vectors.Vector3f;

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
