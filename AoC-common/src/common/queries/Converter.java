package common.queries;

public interface Converter <TSource, TTarget>
{
    public TTarget convert(TSource source);
}
