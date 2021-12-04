package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class Sequence<T> implements Iterable<T>
{

    public static <TType> Iterable<TType> of(int from, int to, Function<Integer, TType> func)
    {
        return new Sequence<TType>(from, to, func);
    }
    
    private int from;
    private int to;
    private Function<Integer, T> func;
    
    private Sequence(int from, int to, Function<Integer, T> func)
    {
        this.from = from;
        this.to = to;
        this.func = func;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new SequenceIterator();
    }
    
    private class SequenceIterator implements Iterator<T>
    {
        private int pos = from;

        @Override
        public boolean hasNext()
        {
            return pos < to;
        }

        @Override
        public T next()
        {
            if (hasNext())
            {
                return func.apply(pos++);
            }
            throw new NoSuchElementException();
        }
    }
}
