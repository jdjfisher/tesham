/*
package com.wip.core;

import com.maths.RNG;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractComponentManager <ComponentT extends AbstractComponent> {
    private final HashMap<Entity, Integer> indexMap;
    private final ArrayList<ComponentT> components;

    public AbstractComponentManager() {
        indexMap = new HashMap<>();
        components = new ArrayList<>();
    }

    public abstract void subscribe(Entity entity);

    protected void subscribe(Entity entity, ComponentT component){
        indexMap.put(entity, components.size());
        components.add(component);
    }

    public void destroy(Entity entity){
        int componentIndex = indexMap.get(entity);
        int lastComponentIndex = components.size() - 1;
        ComponentT lastComponent = components.get(lastComponentIndex);
        Entity lastEntity = lastComponent.getParent();

        components.add(componentIndex, lastComponent);
        components.remove(lastComponentIndex);
        indexMap.put(lastEntity, componentIndex);
        indexMap.remove(entity);
    }

    public abstract void update(float interval);

    public void garbageCollector(EntityManager entityManager) {
        int alive_in_row = 0;

        while (components.size() > 0 && alive_in_row < 4) {
            int i = RNG.Int(0, components.size() - 1);

            Entity parent = components.get(i).getParent();

            if (entityManager.alive(parent)) {
                ++alive_in_row;
                continue;
            }

            alive_in_row = 0;
            destroy(parent);
        }
    }

    public ComponentT getComponent(Entity entity){
        return components.get(indexMap.get(entity));
    }

    public ArrayList<ComponentT> getComponents() {
        return components;
    }
}
*/
