package common;

@FunctionalInterface
public interface Function2<T1, T2, R>
{
    R apply(T1 v1, T2 v2);
}
