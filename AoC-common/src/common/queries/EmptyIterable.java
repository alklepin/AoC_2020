package common.queries;

import java.util.Iterator;

public class EmptyIterable<T> implements Iterable<T>
{

    @SuppressWarnings("rawtypes")
    private static EmptyIterable st_instance = new EmptyIterable();
    
    @SuppressWarnings("unchecked")
    public static <TType> Iterable<TType> instance()
    {
        return (Iterable<TType>)st_instance;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return EmptyIterator.instance();
    }

}
