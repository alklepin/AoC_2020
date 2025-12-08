package common.queries;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UniqueIterable<TSource> implements Iterable<TSource>
{
    private Iterable<TSource> m_source;
    
    public UniqueIterable(Iterable<TSource> source)
    {
        m_source = source;
    }
    
    public Iterator<TSource> iterator()
    {
        return new UniqueIterator(m_source.iterator());
    }
    
    private class UniqueIterator implements Iterator<TSource>
    {
        private Iterator<TSource> m_iterator;
        private HashSet<TSource> m_hsProcessed;
        private TSource m_next;
        private boolean m_hasNext = true;

        public UniqueIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
            prefetchFirst();
        }

        // Fast path for a case when we wrap an iterator with the only item
        private void prefetchFirst()
        {
            if (!m_iterator.hasNext())
            {
                m_next = null;
                m_hasNext = false;
                return;
            }
            // Iterator is not empty, so we have at least one item, no filtering is needed so far 
            TSource next = m_iterator.next(); 
            
            if (m_iterator.hasNext())
            {
                // As we have more than one item in an iterator, do a preparation for a filtering
                // otherwise (single item) we may avoid allocation of HashSet.
                m_hsProcessed = new HashSet<TSource>();
                m_hsProcessed.add(next);
            }
            m_hasNext = true;
            m_next = next;
        }

        private void prefetch()
        {
            m_hasNext = false;
            while (m_iterator.hasNext())
            {
                TSource next = m_iterator.next();
                if (!m_hsProcessed.contains(next))
                {
                    m_hsProcessed.add(next);
                    m_next = next;
                    m_hasNext = true;
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
