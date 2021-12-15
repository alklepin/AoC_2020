package common.queries;

@FunctionalInterface
public interface Converter <TSource, TTarget>
{
    public TTarget convert(TSource source);
}
