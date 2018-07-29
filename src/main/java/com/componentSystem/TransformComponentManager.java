package com.componentSystem;

import com.maths.Matrix4f;
import com.maths.Quaternion;
import com.maths.RNG;
import com.maths.vectors.Vector3f;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class TransformComponentManager implements IComponentManager{
    private final HashMap<Entity, Integer> indexMap;
    private final ArrayList<TransformComponent> components;

    public static class TransformComponent{
        private Entity parent;
        private Vector3f position;
        private Quaternion rotation;
        private float scale;

        public TransformComponent(Entity parent) {
            this(parent, Vector3f.Identity(), Quaternion.Identity(), 1);
        }

        public TransformComponent(Entity parent, Vector3f position, Quaternion rotation, float scale) {
            this.parent = parent;
            this.position = position;
            this.rotation = rotation;
            this.scale = scale;
        }

        public Matrix4f getTransformation(){
            Matrix4f result = Matrix4f.Identity();

            if(!position.equals(Vector3f.Identity())){
                result.multiply(Matrix4f.Translation(position));
            }

            if(!rotation.equals(Quaternion.Identity())) {
                result.multiply(Matrix4f.QuaternionRotation(rotation));
            }

            if(scale != 1) {
                result.multiply(Matrix4f.Enlargement(scale));
            }

            return result;
        }

        public Vector3f getPosition() {
            return position;
        }

        public Quaternion getRotation() {
            return rotation;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }
    }

    public TransformComponentManager() {
        indexMap = new HashMap<>();
        components = new ArrayList<>();
    }

    @Override
    public void subscribe(Entity entity){
        indexMap.put(entity, components.size());
        components.add(new TransformComponent(entity));
    }

    @Override
    public void destroy(Entity entity){
        int componentIndex = indexMap.get(entity);
        int lastComponentIndex = components.size() - 1;
        TransformComponent lastComponent = components.get(lastComponentIndex);
        Entity lastEntity = lastComponent.parent;

        components.add(componentIndex, lastComponent);
        components.remove(lastComponentIndex);
        indexMap.put(lastEntity, componentIndex);
        indexMap.remove(entity);
    }

    @Override
    public void update(float interval){
        for(TransformComponent component : components){
//            System.out.println(component.position);
        }
    }

    @Override
    public void garbageCollector(EntityManager entityManager) {
        int alive_in_row = 0;

        while (components.size() > 0 && alive_in_row < 4) {
            int i = RNG.Int(0, components.size() - 1);

            Entity parent = components.get(i).parent;

            if (entityManager.alive(parent)) {
                ++alive_in_row;
                continue;
            }

            alive_in_row = 0;
            destroy(parent);
        }
    }

    public TransformComponent getComponent(Entity entity){
        return components.get(indexMap.get(entity));
    }

    public ArrayList<TransformComponent> getComponents() {
        return components;
    }
}
