package common.boards;

import java.util.Objects;

import common.geometry.Vect2D;

public class LongPair
{
    public static LongPair ZERO = Pair.of(0L,  0L);
    
    private long m_x;
    private long m_y;

    public static LongPair of(long x, long y)
    {
        return new LongPair(x, y);
    }
    
    public Vect2D asVector()
    {
        return new Vect2D(m_x, m_y);
    }
    
    public LongPair(long x, long y)
    {
        m_x = x;
        m_y = y;
    }

    public LongPair(Vect2D v)
    {
        m_x = (long)v.getX();
        m_y = (long)v.getY();
    }
    
    public long getX()
    {
        return m_x;
    }
    
    public void setX(long x)
    {
        this.m_x = x;
    }
    
    public long getY()
    {
        return m_y;
    }
    public void setY(long y)
    {
        this.m_y = y;
    }
    
    public LongPair add(LongPair other)
    {
        return new LongPair(m_x + other.m_x, m_y + other.m_y);
    }

    public LongPair minus(LongPair other)
    {
        return new LongPair(m_x - other.m_x, m_y - other.m_y);
    }

    public LongPair mult(long number)
    {
        return new LongPair(m_x * number, m_y * number);
    }

    public LongPair divideBy(long number)
    {
        return new LongPair(m_x / number, m_y / number);
    }
    
    
    /**
     * Returns Manhattan length
     * @return
     */
    public long lengthManh()
    {
        return Math.abs(m_x) + Math.abs(m_y);
    }

    /**
     * Returns Chebyshev length
     * @return
     */
    public long lengthCheb()
    {
        return Math.max(Math.abs(m_x), Math.abs(m_y));
    }

    public boolean inRectangle(LongPair min, LongPair max)
    {
        if ((min != null) && (m_x < min.m_x || m_y < min.m_y))
        {
            return false;
        }
        if ((max != null) && (m_x > max.m_x || m_y > max.m_y))
        {
            return false;
        }
        return true;
    }

    public LongPair toRectangle(LongPair min, LongPair max)
    {
        long x = m_x;
        long y = m_y;
        if (min != null)
        {
            x = Math.max(x, min.m_x);
            y = Math.max(y, min.m_y);
        }
        if (max != null)
        {
            x = Math.min(x, max.m_x);
            y = Math.min(y, max.m_y);
        }
        return Pair.of(x, y);
    }
    
    private static long signum(long value)
    {
        return value > 0 ? 1 : value < 0 ? -1 : 0;
    }
    
    public LongPair signum()
    {
        return Pair.of(signum(m_x), signum(m_y));
    }
    
    @Override
    public String toString()
    {
//        return "LongPair [m_x=" + m_x + ", m_y=" + m_y + "]";
        return "(" + m_x + ", " + m_y + ")";
    }

    public boolean componentLessEq(LongPair next)
    {
        return m_x <= next.getX() && m_y <= next.getY();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(m_x, m_y);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LongPair other = (LongPair)obj;
        return m_x == other.m_x && m_y == other.m_y;
    }

    public LongPair modulo(long modulo)
    {
        return of(m_x % modulo, m_y % modulo);
    }
    
    public LongPair componentModulo(LongPair dims)
    {
        var x = m_x % dims.getX();
        if (x < 0)
        {
            x += dims.getX();  
        }
        var y = m_y % dims.getY();
        if (y < 0)
        {
            y += dims.getY();  
        }
        return new LongPair(x, y);
    }

    public LongPair swapComponents()
    {
        return of(m_y, m_x);
    }
    
}
