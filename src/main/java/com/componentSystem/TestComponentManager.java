package com.componentSystem;

public class TestComponentManager extends AbstractComponentManager<TestComponentManager.TestComponent> {

    public class TestComponent extends AbstractComponent{
        public TestComponent(Entity parent){
            super(parent);
        }
    }


    @Override
    public void update(float interval) {

    }
}
