package com.componentSystem;

import com.maths.Quaternion;
import com.maths.RNG;
import com.maths.vectors.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class MeshComponentManager implements IComponentManager{
    private final HashMap<Entity, Integer> indexMap;
    private final ArrayList<MeshComponent> components;

    public static class MeshComponent{
        private Entity parent;

        public MeshComponent(Entity parent) {
            this.parent = parent;
        }
    }

    public MeshComponentManager() {
        indexMap = new HashMap<>();
        components = new ArrayList<>();
    }

    @Override
    public void subscribe(Entity entity){
        indexMap.put(entity, components.size());
        components.add(new MeshComponent(entity));
    }

    @Override
    public void destroy(Entity entity){
        int componentIndex = indexMap.get(entity);
        int lastComponentIndex = components.size() - 1;
        MeshComponent lastComponent = components.get(lastComponentIndex);
        Entity lastEntity = lastComponent.parent;

        components.add(componentIndex, lastComponent);
        components.remove(lastComponentIndex);
        indexMap.put(lastEntity, componentIndex);
        indexMap.remove(entity);
    }

    @Override
    public void update(float interval){
        for(MeshComponent component : components){

        }
    }

    @Override
    public void garbageCollector(EntityManager entityManager) {
        int aliveInARow = 0;

        while (components.size() > 0 && aliveInARow < 4) {
            int i = RNG.Int(0, components.size() - 1);

            Entity parent = components.get(i).parent;

            if (entityManager.alive(parent)) {
                ++aliveInARow;
                continue;
            }

            aliveInARow = 0;
            destroy(parent);
        }
    }

    public MeshComponent getComponent(Entity entity){
        return components.get(indexMap.get(entity));
    }
}
