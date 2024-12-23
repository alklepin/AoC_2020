package common.queries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SubsetGenerator
{
    public static <TNode> Query<ArrayList<TNode>> subsets(ArrayList<TNode> elements, int size)
    {
        return Query.wrap(new SubsetsIterable<>(elements, size));
    }
    
    private static class SubsetsIterable <TNode> implements Iterable<ArrayList<TNode>>
    {
        private ArrayList<TNode> m_elements;
        private int m_size;

        public SubsetsIterable(ArrayList<TNode> elements, int size)
        {
            if (size <= 0)
                throw new IllegalArgumentException("size must be positive");
            m_elements = elements;
            m_size = size;
        }

        @Override
        public Iterator<ArrayList<TNode>> iterator()
        {
            if (m_elements.size() < m_size)
                return EmptyIterator.instance();

            return new SubsetsIterator<>(m_elements, m_size);
        }

        private static class SubsetsIterator <TNode> implements Iterator<ArrayList<TNode>>
        {
            public int[] m_state;
            private ArrayList<TNode> m_elements;
            private boolean m_hasNext;
            
            public SubsetsIterator(ArrayList<TNode> elements, int size)
            {
                m_elements = elements;
                m_state = new int[size];
                for (var idx = 0; idx < size; idx++)
                {
                    m_state[idx] = idx;
                }
                m_hasNext = true;
            }
            
            @Override
            public boolean hasNext()
            {
                return m_hasNext;
            }

            @Override
            public ArrayList<TNode> next()
            {
                final var stateSize = m_state.length;
                if (hasNext())
                {
                    var result = new ArrayList<TNode>();
                    for (var sidx = 0; sidx < stateSize; sidx++)
                    {
                        result.add(m_elements.get(m_state[sidx]));
                    }
                    
                    var idx = stateSize-1;
                    while (idx >= 0 && m_state[idx] >= m_elements.size() - stateSize + idx)
                    {
                        idx--;
                    }
                    if (idx >= 0)
                    {
                        m_state[idx]++;
                        var base = m_state[idx];
                        var delta = 0;
                        for (var next = idx+1; next < stateSize; next++)
                        {
                            delta++;
                            m_state[next] = base + delta;
                        }
                    }
                    else
                    {
                        m_hasNext = false;
                    }
                    
                    return result;
                }
                throw new NoSuchElementException();
            }
            
        }
    }
 }
