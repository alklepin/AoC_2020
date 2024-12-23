package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleValueIterable <T> implements Iterable<T>
{
    private T m_value;

    public static <T> Iterable<T> wrap(T value)
    {
        return new SingleValueIterable<T>(value);
    }
    
    public SingleValueIterable(T value)
    {
        m_value = value;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new SingleValueIterator<T>(m_value);
    }

    public static class SingleValueIterator<T>
        implements Iterator<T>
    {
        private T m_value;
        private boolean m_processed;

        public SingleValueIterator(T value)
        {
            m_value = value;
            m_processed = false;
        }

        @Override
        public boolean hasNext()
        {
            return !m_processed;
        }
        
        @Override
        public T next()
        {
            if (!m_processed)
            {
                m_processed = true;
                return m_value;
            }
            throw new NoSuchElementException();
        }
        
    }

}
