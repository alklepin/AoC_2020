package common.queries;

import java.util.Iterator;

public class FirstNotEmptyIterable<TType> implements Iterable<TType>
{
    private Iterable<TType>[] sources;
    
    @SafeVarargs
    public FirstNotEmptyIterable(Iterable<TType>... sources)
    {
        this.sources = sources;
    }
    
    public Iterator<TType> iterator()
    {
        if (sources != null)
        {
            for (var source : sources)
            {
                var current = source.iterator();
                if (current.hasNext())
                    return current;
            }
        }
        return EmptyIterator.instance();
    }
}
