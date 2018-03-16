package com.componentSystem;

public class Component {
    private Entity entity;

    public Component(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
