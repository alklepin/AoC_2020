package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeIterableLong implements Iterable<Long>
{
    private long m_start;
    private long m_count;
    
    public RangeIterableLong(long start, long count)
    {
        m_start = start;
        m_count = count;
    }
    
    @Override
    public Iterator<Long> iterator()
    {
        return new RangeIterator(m_start, m_count);
    }

    public static class RangeIterator
        implements Iterator<Long>
    {
        
        private long m_start;
        private long m_count;

        public RangeIterator(long start, long count)
        {
            m_start = start;
            m_count = count;
        }

        @Override
        public boolean hasNext()
        {
            return m_count > 0;
        }
        
        @Override
        public Long next()
        {
            if (m_count > 0)
            {
                Long result = m_start++;
                m_count--;
                return result;
            }
            throw new NoSuchElementException();
        }
        
    }
    
}
