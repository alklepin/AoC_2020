package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeIterable implements Iterable<Integer>
{
    private int m_start;
    private int m_count;
    
    public RangeIterable(int start, int count)
    {
        m_start = start;
        m_count = count;
    }
    
    @Override
    public Iterator<Integer> iterator()
    {
        return new RangeIterator(m_start, m_count);
    }

    public static class RangeIterator
        implements Iterator<Integer>
    {
        
        private int m_start;
        private int m_count;

        public RangeIterator(int start, int count)
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
        public Integer next()
        {
            if (m_count > 0)
            {
                Integer result = m_start++;
                m_count--;
                return result;
            }
            throw new NoSuchElementException();
        }
        
    }
    
}
