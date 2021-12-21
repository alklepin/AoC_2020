package common.boards;

import java.util.Objects;

import common.geometry.Vect2D;

public class LongPair
{
    public static LongPair ZERO = Pair.of(0L,  0L);
    
    private long m_x;
    private long m_y;

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
    
    
    public long lengthL1()
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
}
