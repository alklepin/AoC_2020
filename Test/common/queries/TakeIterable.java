package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TakeIterable<TSource> implements Iterable<TSource>
{
    private Iterable<TSource> m_source;
    private int m_itemsToTake;
    
    public TakeIterable(Iterable<TSource> source, int itemsToTake)
    {
        m_source = source;
        m_itemsToTake = itemsToTake;
    }
    
    public Iterator<TSource> iterator()
    {
        return new TakeIterator<TSource>(m_source.iterator(), m_itemsToTake);
    }

    
    private static class TakeIterator<TSource> implements Iterator<TSource>
    {
        private Iterator<TSource> m_iterator;
        private int m_itemsToTake;

        public TakeIterator(Iterator<TSource> iterator, int itemsToTake)
        {
            m_iterator = iterator;
            m_itemsToTake = itemsToTake;
        }

        public boolean hasNext()
        {
            return m_itemsToTake > 0 && m_iterator.hasNext();
        }

        @Override
        public TSource next()
        {
            if (m_itemsToTake > 0)
            {
                TSource result = m_iterator.next();
                m_itemsToTake--;
                return result;
            }
            throw new NoSuchElementException();
        }
    }
}
