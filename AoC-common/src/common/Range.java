package common;

public class Range<T extends Comparable<? super T>> implements Comparable<Range<T>>
{
    private T start; // inclusive
    private T end; // exclusive
    
    public Range(T start, T end)
    {
        this.start = start;
        this.end = end;
    }
    
    public T start()
    {
        return start;
    }
    
    public T end()
    {
        return end;
    }

    @Override
    public int compareTo(Range<T> r)
    {
        var result = start.compareTo(r.start);
        if (result == 0)
            result = end.compareTo(r.end);
        return result;
    }
}
