package common.boards;

import common.geometry.Vect2D;

public class IntPair
{
    private int m_x;
    private int m_y;

    public Vect2D asVector()
    {
        return new Vect2D(m_x, m_y);
    }
    
    public IntPair(int x, int y)
    {
        m_x = x;
        m_y = y;
    }

    public IntPair(Vect2D v)
    {
        m_x = (int)v.getX();
        m_y = (int)v.getY();
    }
    
    public int getX()
    {
        return m_x;
    }
    
    public void setX(int x)
    {
        this.m_x = x;
    }
    
    public int getY()
    {
        return m_y;
    }
    public void setY(int y)
    {
        this.m_y = y;
    }
    
    public IntPair add(IntPair other)
    {
        return new IntPair(m_x + other.m_x, m_y + other.m_y);
    }

    public IntPair minus(IntPair other)
    {
        return new IntPair(m_x - other.m_x, m_y - other.m_y);
    }

    public IntPair mult(int number)
    {
        return new IntPair(m_x * number, m_y * number);
    }

    public IntPair divideBy(int number)
    {
        return new IntPair(m_x / number, m_y / number);
    }
    
    
    public int lengthL1()
    {
        return Math.max(Math.abs(m_x), Math.abs(m_y));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_x;
        result = prime * result + m_y;
        return result;
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
        IntPair other = (IntPair)obj;
        if (m_x != other.m_x)
            return false;
        if (m_y != other.m_y)
            return false;
        return true;
    }

    public boolean inRectangle(IntPair min, IntPair max)
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
        return "IntPair [m_x=" + m_x + ", m_y=" + m_y + "]";
    }

}
