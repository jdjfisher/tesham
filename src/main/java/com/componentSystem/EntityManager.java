package com.componentSystem;

import java.util.HashSet;

public class EntityManager {
    private final HashSet<Entity> entities;
    private final Entity nextEntity = new Entity();

    public EntityManager(){
        entities = new HashSet<>();
    }

    public Entity create(){
        nextEntity.id++;
        while (alive(nextEntity)){
            nextEntity.id++;
        }

        Entity newEntity = new Entity();
        newEntity.id = nextEntity.id;

        entities.add(newEntity);

        return newEntity;
    }

    public boolean alive(Entity entity){
        return entities.contains(entity);
    }

    public void destroy(Entity entity){
        entities.remove(entity);
    }
}