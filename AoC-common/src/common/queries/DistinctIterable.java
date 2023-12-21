package common.queries;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class DistinctIterable<TSource> implements Iterable<TSource>
{
    private Iterable<TSource> m_source;
    
    public DistinctIterable(Iterable<TSource> source)
    {
        m_source = source;
    }
    
    public Iterator<TSource> iterator()
    {
        return new DistinctIterator(m_source.iterator());
    }
    
    private class DistinctIterator implements Iterator<TSource>
    {
        private HashSet<TSource> m_processed;
        private Iterator<TSource> m_iterator;
        private TSource m_next;
        private boolean m_hasNext = true;

        public DistinctIterator(Iterator<TSource> iterator)
        {
            m_processed = new HashSet<>();
            m_iterator = iterator;
            prefetch();
        }

        private void prefetch()
        {
            m_hasNext = false;
            while (m_iterator.hasNext())
            {
                TSource next = m_iterator.next();
                if (!m_processed.contains(next))
                {
                    m_next = next;
                    m_hasNext = true;
                    m_processed.add(next);
                    break;
                }
            }
        }

        @Override
        public boolean hasNext()
        {
            return m_hasNext;
        }

        @Override
        public TSource next()
        {
            if (m_hasNext)
            {
                TSource result = m_next;
                prefetch();
                return result;
            }
            throw new NoSuchElementException();
        }
    }
    
}

