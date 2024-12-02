package common;

import java.util.HashMap;
import java.util.Iterator;

import common.queries.Query;

public class Counters<TValue> implements Iterable<Tuple<TValue, Integer>>
{
    private final HashMap<TValue, Integer> counters = new HashMap<>();
    
    public Counters()
    {
    }
    
    public Counters(Iterable<TValue> values)
    {
        for (var v : values)
        {
            count(v);
        }
    }
    
    public void count(TValue value)
    {
        var counter = counters.get(value);
        if (counter != null)
            counter = counter+1;
        else
            counter = 1;
        counters.put(value, counter);
    }
    
    public Iterator<Tuple<TValue, Integer>> iterator()
    {
        return Query.wrap(counters.entrySet())
            .select(e -> new Tuple<>(e.getKey(), e.getValue()))
            .iterator();
    }
    
}

