package utils.functionalInterfaces;

@FunctionalInterface
public interface TriCallback<T1, T2, T3>
{
    void invoke(T1 object1, T2 object2, T3 object3);
}
