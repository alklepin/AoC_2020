package common.boards;

import java.util.Iterator;
import java.util.NoSuchElementException;

import common.queries.Query;

public class Generators
{
    public static Iterable<IntPair> neighbours8(int startX, int startY, int minX, int minY, int maxX, int maxY)
    {
        return new Neighbours8Generator(new IntPair(startX, startY), new IntPair(minX,  minY), new IntPair(maxX, maxY));
    }
    
    public static Iterable<IntPair> ray(int startX, int startY, int dx, int dy, int count)
    {
        return new RayGenerator(new IntPair(startX, startY), new IntPair(dx,  dy), count);
    }
    
    public static class RayGenerator
        implements Iterable<IntPair>
    {

        private IntPair m_start;
        private IntPair m_delta;
        private int m_count;

        public RayGenerator(IntPair start, IntPair delta, int count)
        {
            m_start = start;
            m_delta = delta;
            m_count = count;
        }

        public Iterator<IntPair> iterator()
        {
            return new RayIterator();
        }
        
        private class RayIterator implements Iterator<IntPair>
        {
            private int m_toGenerate;
            private IntPair m_current;

            public RayIterator()
            {
                m_toGenerate = m_count;
                m_current = m_start;
            }

            @Override
            public boolean hasNext()
            {
                return m_toGenerate > 0;
            }

            @Override
            public IntPair next()
            {
                if (hasNext())
                {
                    m_toGenerate--;
                    m_current = m_current.add(m_delta);
                    return m_current;
                    
                }
                throw new NoSuchElementException();
            }
            
        }
    }
    
    public static class Neighbours8Generator 
        implements Iterable<IntPair>
    {

        private IntPair m_start;
        private IntPair m_min;
        private IntPair m_max;

        public Neighbours8Generator(IntPair intPair, IntPair min, IntPair max)
        {
            m_start = intPair;
            m_min = min;
            m_max = max;
        }

        @Override
        public Iterator<IntPair> iterator()
        {
            return Query.wrap(
                new IntPair[] {
                    new IntPair(1,1),
                    new IntPair(1,0),
                    new IntPair(1,-1),
                    new IntPair(0,-1),
                    new IntPair(-1,-1),
                    new IntPair(-1,0),
                    new IntPair(-1,1),
                    new IntPair(0,1),
                })
                .select(pair -> pair.add(m_start))
                .where(pair -> pair.inRectangle(m_min, m_max)).iterator();
        }
        
    }
}
