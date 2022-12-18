package common.boards;

import java.util.Iterator;
import java.util.NoSuchElementException;

import common.Numbers;
import common.geometry.SegmentInt;
import common.geometry.Vect2D;
import common.queries.Query;

public class Generators
{
    public static Iterable<IntPair> neighbours8(int startX, int startY, int minX, int minY, int maxX, int maxY)
    {
        return new Neighbours8Generator(new IntPair(startX, startY), new IntPair(minX,  minY), new IntPair(maxX, maxY));
    }
    
    public static Iterable<IntPair> neighbours8(IntPair start, IntPair min, IntPair max)
    {
        return new Neighbours8Generator(start, min, max);
    }
    
    public static Iterable<IntPair> neighbours4(int startX, int startY, int minX, int minY, int maxX, int maxY)
    {
        return new Neighbours4Generator(new IntPair(startX, startY), new IntPair(minX,  minY), new IntPair(maxX, maxY));
    }

    public static Iterable<IntPair> neighbours4(IntPair start, IntPair min, IntPair max)
    {
        return new Neighbours4Generator(start, min, max);
    }

    public static Query<IntTriple> neighbours6_3D(IntTriple start, IntTriple min, IntTriple max)
    {
        return Query.wrap(IntTriple.NEIGHBOURS6)
            .select(point -> point.add(start))
            .where(point -> point.inRectangle(min, max));
    }
    
    public static Iterable<IntPair> ray(int startX, int startY, int dx, int dy, int count)
    {
        return new RayGenerator(new IntPair(startX, startY), new IntPair(dx,  dy), count);
    }

    public static Iterable<IntPair> ray(IntPair start, IntPair delta, int count)
    {
        return new RayGenerator(start, delta, count);
    }
    
    public static Iterable<IntPair> ray(IntPair start, IntPair delta, int count, boolean includeStart)
    {
        return new RayGenerator(start, delta, count, includeStart);
    }
    
    public static Iterable<IntPair> rhomb(IntPair center, int radius)
    {
        return Query.concat(
            ray(center.add(IntPair.LEFT.mult(radius)), IntPair.UP_RIGHT, radius, true),
            ray(center.add(IntPair.UP.mult(radius)), IntPair.DOWN_RIGHT, radius, true),
            ray(center.add(IntPair.RIGHT.mult(radius)), IntPair.DOWN_LEFT, radius, true),
            ray(center.add(IntPair.DOWN.mult(radius)), IntPair.UP_LEFT, radius, true));
    }

    public static Iterable<IntPair> rect(IntPair topLeft, int width, int height)
    {
        return Query.concat(
            ray(topLeft, IntPair.RIGHT, width, true),
            ray(topLeft.add(IntPair.RIGHT.mult(width)), IntPair.DOWN, height, true),
            ray(topLeft.add(IntPair.RIGHT.mult(width)).add(IntPair.DOWN.mult(height)), IntPair.LEFT, width, true),
            ray(topLeft.add(IntPair.DOWN.mult(height)), IntPair.UP, height, true));
    }
    
    public static class RayGenerator
        implements Iterable<IntPair>
    {

        private IntPair m_start;
        private IntPair m_delta;
        private int m_count;
        private boolean m_includeStart;

        public RayGenerator(IntPair start, IntPair delta, int count)
        {
            this(start, delta, count, false);
        }
        
        public RayGenerator(IntPair start, IntPair delta, int count, boolean includeStart)
        {
            m_start = start;
            m_delta = delta;
            m_count = count;
            m_includeStart = includeStart;
        }

        public RayGenerator(SegmentInt segment)
        {
            this(segment.getPoint1(), segment.getPoint2());
        }

        public RayGenerator(IntPair from, IntPair to)
        {
            m_start = from;
            var delta = to.minus(from);
            m_count = Numbers.nod(delta.getX(), delta.getY());
            m_delta = delta.divideBy(m_count);
            m_includeStart = true;
        }
        
        
        @Deprecated
        public RayGenerator(Vect2D start, Vect2D delta, int count)
        {
            m_start = new IntPair(start);
            m_delta = new IntPair(delta);
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
                if (m_includeStart)
                {
                    m_toGenerate = m_count + 1;
                    m_current = m_start.minus(m_delta);
                }
                else
                {
                    m_toGenerate = m_count;
                    m_current = m_start;
                }
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
    
    public static class Neighbours4Generator 
        implements Iterable<IntPair>
    {

        private IntPair m_start;
        private IntPair m_min;
        private IntPair m_max;

        public Neighbours4Generator(IntPair intPair, IntPair min, IntPair max)
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
                    new IntPair(1,0),
                    new IntPair(0,-1),
                    new IntPair(-1,0),
                    new IntPair(0,1),
                })
                .select(pair -> pair.add(m_start))
                .where(pair -> pair.inRectangle(m_min, m_max)).iterator();
        }
    }
    
    public static class Neighbours6Generator3D 
        implements Iterable<IntTriple>
    {
        
        private IntTriple m_start;
        private IntTriple m_min;
        private IntTriple m_max;
        
        public Neighbours6Generator3D(IntTriple start, IntTriple min, IntTriple max)
        {
            m_start = start;
            m_min = min;
            m_max = max;
        }
        
        @Override
        public Iterator<IntTriple> iterator()
        {
            return Query.wrap(IntTriple.NEIGHBOURS6)
                .select(point -> point.add(m_start))
                .where(point -> point.inRectangle(m_min, m_max)).iterator();
        }
    }
    
}
