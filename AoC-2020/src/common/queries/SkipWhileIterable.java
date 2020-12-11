package common.queries;

import java.util.Iterator;
import java.util.function.Predicate;

public class SkipWhileIterable<TSource> implements Iterable<TSource>
{
    private Iterable<TSource> m_source;
    private Predicate<? super TSource> m_predicate;
    
    public SkipWhileIterable(Iterable<TSource> source, Predicate<? super TSource> predicate)
    {
        m_source = source;
        m_predicate = predicate;
    }
    
    public Iterator<TSource> iterator()
    {
        return new SkipWhileIterator(m_source.iterator());
    }
    
    private class SkipWhileIterator implements Iterator<TSource>
    {
        private Iterator<TSource> m_iterator;
        private TSource m_prefetched;
        private boolean m_hasPrefetched;

        public SkipWhileIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
            prefetch();
        }

        private void prefetch()
        {
            m_hasPrefetched = false;
            while (m_iterator.hasNext())
            {
                TSource next = m_iterator.next();
                if (!m_predicate.test(next))
                {
                    m_prefetched = next;
                    m_hasPrefetched = true;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext()
        {
            return m_hasPrefetched || m_iterator.hasNext();
        }

        @Override
        public TSource next()
        {
            if (m_hasPrefetched)
            {
                m_hasPrefetched = false;
                TSource result = m_prefetched;
                m_prefetched = null;
                return result;
            }
            return m_iterator.next();
        }
    }
    
}
