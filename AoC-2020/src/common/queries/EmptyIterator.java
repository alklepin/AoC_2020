package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator<T> implements Iterator<T>
{
    @SuppressWarnings("rawtypes")
    private static Iterator st_instance = new EmptyIterator();
    
    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> instance()
    {
        return (Iterator<T>)st_instance;
    }
    
    @Override
    public boolean hasNext()
    {
        return false;
    }

    @Override
    public T next()
    {
        throw new NoSuchElementException();
    }
    
}
