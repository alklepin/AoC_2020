package common.queries;

import java.util.Arrays;
import java.util.Iterator;

public class Query<T> implements Iterable<T>
{
    private Iterable<T> m_source;
    
    public Query(Iterable<T> source)
    {
        m_source = source;
    }
    
    public Iterator<T> iterator()
    {
        return m_source.iterator();
    }

    public static <Type> Query<Type> wrap(Iterable<Type> source)
    {
        return new Query<Type>(source);
    }
    
    public static <Type> Query<Type> wrapArray(Type[] array)
    {
        return new Query<Type>(Arrays.asList(array));
    }
    
    public <TTarget> Query<TTarget> select(Converter<? super T, ? extends TTarget> converter)
    {
        return new Query<TTarget>(new ConvertingIterable<T, TTarget>(m_source, converter));
    }
    
    public Query<T> where(Predicate<? super T> predicate)
    {
        return new Query<T>(new FilteringIterable<T>(m_source, predicate));
    }

    public int count()
    {
        int counter = 0;
        for (@SuppressWarnings("unused") T element : this)
        {
            counter++;
        }
        return counter;
    }

    public T first()
    {
        return iterator().next();
    }
    
//    private Iterable<T> unwrap()
//    {
//        return m_source;
//    }
}
