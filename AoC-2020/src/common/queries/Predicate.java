package common.queries;

public interface Predicate<TSource>
{
    public boolean evaluate(TSource source);
}
