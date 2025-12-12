package common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Lists
{
    public static <T, S extends T> ArrayList<T> asArrayList(S[] source)
    {
        var result = new ArrayList<T>(source.length);
        for (var value : source)
        {
            result.add(value);
        }
        return result;
    }

    public static <T> ArrayList<T> asArrayList(Iterable<? extends T> source)
    {
        var result = new ArrayList<T>();
        for (var value : source)
        {
            result.add(value);
        }
        return result;
    }
    
    public static <T> ArrayList<T> asArrayList(Iterator<? extends T> source)
    {
        var result = new ArrayList<T>();
        while (source.hasNext())
        {
            result.add(source.next());
        }
        return result;
    }

    public static <T> ArrayList<T> asSortedArrayList(Iterable<? extends T> source, Comparator<? super T> comparator)
    {
        ArrayList<T> result = asArrayList(source);
        result.sort(comparator);
        return result;
    }

    public static <T, S extends T> ArrayList<T> asSortedArrayList(S[] source, Comparator<? super T> comparator)
    {
        ArrayList<T> result = asArrayList(source);
        result.sort(comparator);
        return result;
    }

    public static <S, T extends S> List<S> addAll(List<S> list, T[] source)
    {
        for (var value : source)
        {
            list.add(value);
        }
        return list;
    }
    
    public static <S, T extends S> List<S> addAll(List<S> list, Iterable<T> source)
    {
        for (var value : source)
        {
            list.add(value);
        }
        return list;
    }
    
    /**
     * Creates a shallow copy of a given list
     * @param <T>
     * @param list
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static <T> ArrayList<T> duplicate(List<? extends T> list)
    {
        if (list instanceof ArrayList)
        {
            return (ArrayList<T>)(((ArrayList<? extends T>)list).clone());
        }
        
        ArrayList<T> result = new ArrayList<>(list.size());
        result.addAll(list);
        return result;
    }

    public static <T extends Comparable<? super T>> int compare(Iterable<? extends T> source1, Iterable<? extends T> source2)
    {
        if (source1 == null)
        {
            return (source2 == null) ? 0 : -1;
        }
        if (source2 == null)
        {
            return 1;
        }
        
        var iter1 = source1.iterator();
        var iter2 = source2.iterator();
        while (iter1.hasNext() && iter2.hasNext())
        {
            var val1 = iter1.next();
            var val2 = iter2.next();
            var compareResult = val1.compareTo(val2);
            if (compareResult != 0)
                return compareResult;
        }
        if (iter1.hasNext())
            return 1;
        if (iter2.hasNext())
            return -1;
        return 0;
        
    }
}
