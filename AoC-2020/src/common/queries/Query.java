package common.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    
//    public static <Type> Query<Type> wrapArray(Type[] array)
//    {
//        return new Query<Type>(Arrays.asList(array));
//    }

    public static <Type> Query<Type> wrapArray(Type... array)
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
    
    public Query<T> skipWhile(Predicate<? super T> predicate)
    {
        return new Query<T>(new SkipWhileIterable<T>(m_source, predicate));
    }
    
    public Query<T> takeWhile(Predicate<? super T> predicate)
    {
        return new Query<T>(new TakeWhileIterable<T>(m_source, predicate));
    }
    
    public Query<T> whereNotNull()
    {
        return new Query<T>(new FilteringIterable<T>(m_source, e -> e != null));
    }
    
    public ArrayList<T> toList()
    {
        ArrayList<T> result = new ArrayList<T>();
        for (T value : this)
        {
            result.add(value);
        }
        return result;
    }

    public Stream<T> stream()
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
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

    public int count(Predicate<? super T> condition)
    {
        int counter = 0;
        for (T element : this)
        {
            if (condition.test(element))
                counter++;
        }
        return counter;
    }

    public T first()
    {
        return iterator().next();
    }

    public T firstOrDefault()
    {
        Iterator<T> iter = iterator();
        if (iter.hasNext())
            return iterator().next();
        return null;
    }
    
//    private Iterable<T> unwrap()
//    {
//        return m_source;
//    }
}
