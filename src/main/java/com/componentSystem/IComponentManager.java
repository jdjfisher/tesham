package com.componentSystem;

public interface IComponentManager {

    void subscribe(Entity entity);

    void destroy(Entity entity);

    void update(float interval);

    void garbageCollector(EntityManager entityManager);

}
