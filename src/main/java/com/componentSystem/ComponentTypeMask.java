package com.componentSystem;

import java.util.ArrayList;
import java.util.HashSet;

public class ComponentTypeMask
{
    private final HashSet<ComponentType> filter;
    private final HashSet<ComponentType> blacklist;

    public ComponentTypeMask()
    {
        filter = new HashSet<>();
        blacklist = new HashSet<>();
    }

    public boolean test(Entity e)
    {
        for(ComponentType componentType : blacklist)
        {
            if(e.hasComponent(componentType))
            {
                return false;
            }
        }

        for(ComponentType componentType : filter)
        {
            if(!e.hasComponent(componentType))
            {
                return false;
            }
        }

        return true;
    }

    public HashSet<ComponentType> getFilter() {
        return filter;
    }

    public HashSet<ComponentType> getBlacklist() {
        return blacklist;
    }
}
