package common.queries;

@FunctionalInterface
public interface ConverterIndexed <TSource, TTarget>
{
    public TTarget convert(int index, TSource source);
}
