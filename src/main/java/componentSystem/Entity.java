/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package componentSystem;


import componentSystem.components.TransformComponent;
import utils.functionalInterfaces.UniCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class Entity
{
    private static long nextID = 1;

    public final long id = nextID++;
    private boolean destroyed = false;
    public TransformComponent transformComponent;
    private final List<Component> components = new ArrayList<>();
    private final Map<ComponentType, List<Component>> componentsByType = new HashMap<>();

    public Entity()
    {
        addComponent(transformComponent = new TransformComponent());
    }

    public void addComponent(Component component)
    {
        if (destroyed)
            return;

        components.add(component);

        ComponentType componentType = ComponentType.of(component.getClass());
        List<Component> typedComponents = componentsByType.get(componentType);

        if (typedComponents == null)
            componentsByType.put(componentType, typedComponents = new ArrayList<>());

        typedComponents.add(component);

        component.setup(this);
    }

    public void removeComponent(Component component)
    {
        if (destroyed)
            return;

        components.remove(component);

        ComponentType componentType = ComponentType.of(component.getClass());
        List<Component> typedComponents = componentsByType.get(componentType);

        if (typedComponents != null)
            typedComponents.remove(component);

        component.onDestroyed();
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> klass)
    {
        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        if (componentsOfType == null || componentsOfType.size() == 0)
            return null;

        return (T) componentsOfType.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(Class<T> klass, List<T> list)
    {
        if (list == null)
            list = new ArrayList<>();

        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        if (componentsOfType != null)
            for (Component c : componentsOfType)
                list.add((T) c);

        return list;
    }

    public List<Component> getComponents(List<Component> list)
    {
        if (list == null)
            list = new ArrayList<>();

        list.addAll(components);
        return list;
    }

    public <T extends Component> boolean hasComponent(Class<T> klass)
    {
        return hasComponent(ComponentType.of(klass));
    }

    public <T extends Component> boolean hasComponent(ComponentType componentType)
    {
        List<Component> componentsOfType = componentsByType.get(componentType);

        return !(componentsOfType == null || componentsOfType.size() == 0);
    }

    public void forEachComponent(UniCallback<Component> callback)
    {
        for (Component c : components)
            callback.invoke(c);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> void forEachComponentOfType(Class<T> klass, UniCallback<T> callback)
    {
        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        if (componentsOfType != null)
            for (Component c : componentsOfType)
                callback.invoke((T) c);
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void destroy()
    {
        if (!destroyed)
            for (Component c : components)
                c.onDestroyed();

        components.clear();
        destroyed = true;
    }
}
