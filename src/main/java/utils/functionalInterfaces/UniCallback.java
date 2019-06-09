package utils.functionalInterfaces;

@FunctionalInterface
public interface UniCallback<T>
{
    void invoke(T object);
}
