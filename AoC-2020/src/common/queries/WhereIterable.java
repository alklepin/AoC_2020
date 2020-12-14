package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class WhereIterable<TSource> implements Iterable<TSource>
{
    private Iterable<TSource> m_source;
    private Predicate<? super TSource> m_predicate;
    
    public WhereIterable(Iterable<TSource> source, Predicate<? super TSource> predicate)
    {
        m_source = source;
        m_predicate = predicate;
    }
    
    public Iterator<TSource> iterator()
    {
        return new FilteringIterator(m_source.iterator());
    }
    
    private class FilteringIterator implements Iterator<TSource>
    {
        private Iterator<TSource> m_iterator;
        private TSource m_next;
        private boolean m_hasNext = true;

        public FilteringIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
            prefetch();
        }

        private void prefetch()
        {
            m_hasNext = false;
            while (m_iterator.hasNext())
            {
                TSource next = m_iterator.next();
                if (m_predicate.test(next))
                {
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
