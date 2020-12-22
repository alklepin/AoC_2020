package common.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
        m_source = source != null ? source : EmptyIterable.instance();
    }
    
    public Iterator<T> iterator()
    {
        return m_source.iterator();
    }

    public static Query<Integer> range(int start, int count)
    {
        return new Query<Integer>(new RangeIterable(start, count));
    }

    public static <Type> Query<Type> wrap(Iterable<Type> source)
    {
        return new Query<Type>(source);
    }
    
    public static <Type> Query<Type> wrap(Type... array)
    {
        return new Query<Type>(Arrays.asList(array));
    }
    
    public <TTarget> Query<TTarget> select(Converter<? super T, ? extends TTarget> converter)
    {
        return new Query<TTarget>(new SelectIterable<T, TTarget>(m_source, converter));
    }
    
    public <TTarget> Query<TTarget> selectMany(Converter<? super T, Iterable<? extends TTarget>> converter)
    {
        return new Query<TTarget>(new SelectManyIterable<T, TTarget>(m_source, converter));
    }
    
    public Query<T> where(Predicate<? super T> predicate)
    {
        return new Query<T>(new WhereIterable<T>(m_source, predicate));
    }
    
    public Query<T> skip(int itemsToSkip)
    {
        return new Query<T>(new SkipIterable<T>(m_source, itemsToSkip));
    }
    
    public Query<T> take(int itemsToTake)
    {
        return new Query<T>(new TakeIterable<T>(m_source, itemsToTake));
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
        return new Query<T>(new WhereIterable<T>(m_source, e -> e != null));
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

    public HashSet<T> toSet()
    {
        HashSet<T> result = new HashSet<T>();
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
    
    public boolean any(Predicate<? super T> condition)
    {
        for (T element : this)
        {
            if (condition.test(element))
                return true;
        }
        return false;
    }

    public boolean all(Predicate<? super T> condition)
    {
        for (T element : this)
        {
            if (!condition.test(element))
                return false;
        }
        return true;
    }

    
//    private Iterable<T> unwrap()
//    {
//        return m_source;
//    }
}
