package common.queries;

import java.util.Iterator;

public class SkipIterable<TSource> implements Iterable<TSource>
{
    private Iterable<TSource> m_source;
    private int m_itemsToSkip;
    
    public SkipIterable(Iterable<TSource> source, int itemsToSkip)
    {
        m_source = source;
        m_itemsToSkip = itemsToSkip;
    }
    
    public Iterator<TSource> iterator()
    {
        return new SkipIterator<TSource>(m_source.iterator(), m_itemsToSkip);
    }
    
    private static class SkipIterator<TSource> implements Iterator<TSource>
    {
        private Iterator<TSource> m_iterator;
        private boolean m_skipDone;
        private int m_itemsToSkip;

        public SkipIterator(Iterator<TSource> iterator, int itemsToSkip)
        {
            m_iterator = iterator;
            m_itemsToSkip = itemsToSkip;
            m_skipDone = false;
        }

        private void doSkip()
        {
            while (m_iterator.hasNext() && m_itemsToSkip > 0)
            {
                m_iterator.next();
                m_itemsToSkip--;
            }
            m_skipDone = true;
        }

        @Override
        public boolean hasNext()
        {
            if (!m_skipDone)
            {
                doSkip();
            }
            return m_iterator.hasNext();
        }

        @Override
        public TSource next()
        {
            if (!m_skipDone)
            {
                doSkip();
            }
            return m_iterator.next();
        }
    }
    
}
