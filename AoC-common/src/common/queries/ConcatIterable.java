package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcatIterable<T> implements Iterable<T>
{
    private Iterable<? extends T>[] m_sources;
    
    @SafeVarargs
    public ConcatIterable(Iterable<? extends T>... sources)
    {
        m_sources = sources;
    }
    
    public Iterator<T> iterator()
    {
        return new ConcatIterator();
    }
    
    private class ConcatIterator implements Iterator<T>
    {
        private Iterator<? extends T> m_currentIterator = EmptyIterator.instance();
        private int pos;

        public ConcatIterator()
        {
            pos = -1;
            m_currentIterator = tryLocateNonEmpty();
        }
        
        private Iterator<? extends T> tryLocateNonEmpty()
        {
            pos++;
            while (pos < m_sources.length)
            {
                var iter = m_sources[pos].iterator();
                if (iter.hasNext())
                    return iter;
                pos++;
            }
            return EmptyIterator.instance();
        }

        @Override
        public boolean hasNext()
        {
            return m_currentIterator.hasNext();
        }

        @Override
        public T next()
        {
            if (m_currentIterator.hasNext())
            {
                T result = m_currentIterator.next();
                if (!m_currentIterator.hasNext())
                    m_currentIterator = tryLocateNonEmpty();
                return result;
            }
            throw new NoSuchElementException();
        }
    }
    
}
