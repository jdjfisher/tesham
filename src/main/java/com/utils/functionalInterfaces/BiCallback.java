
package com.utils.functionalInterfaces;

@FunctionalInterface
public interface BiCallback<T1, T2>
{
    void invoke(T1 object1, T2 object2);
}
