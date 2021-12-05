package common.geometry;

import common.boards.IntPair;

public class Vect2D
{
    private final double m_x;
    private final double m_y;
    
    public static Vect2D vector(double x, double y)
    {
        return new Vect2D(x, y);
    }
    
    public Vect2D(double x, double y)
    {
        m_x = x;
        m_y = y;
    }
    
    public double getX()
    {
        return m_x;
    }

    public double getY()
    {
        return m_y;
    }

    public IntPair asIntPair()
    {
        return new IntPair((int)Math.round(m_x), (int)Math.round(m_y));
    }
    
    public Vect2D add(Vect2D other)
    {
        return new Vect2D(m_x + other.m_x, m_y + other.m_y);
    }

    public Vect2D minus(Vect2D other)
    {
        return new Vect2D(m_x - other.m_x, m_y - other.m_y);
    }

    public Vect2D mult(double value)
    {
        return new Vect2D(m_x * value, m_y * value);
    }

    public Vect2D divideBy(double value)
    {
        return new Vect2D(m_x / value, m_y / value);
    }

    public double scalarMult(Vect2D other)
    {
        return m_x * other.m_x +  m_y * other.m_y;
    }

    public double vectorMult(Vect2D other)
    {
        return m_x * other.m_y -  m_y * other.m_x;
    }
    
    public Vect2D rotateLeft()
    {
        return new Vect2D(-m_y, m_x);
    }
    
    public Vect2D rotateRight()
    {
        return new Vect2D(m_y, -m_x);
    }
    
    public Vect2D rotate(double angle)
    {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return new Vect2D(m_x*cos - m_y*sin, m_x*sin + m_y*cos);
    }
    
    public double length()
    {
        return Math.sqrt(m_x*m_x + m_y*m_y);
    }

    public boolean inRectangle(Vect2D min, Vect2D max)
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
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(m_x);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m_y);
        result = prime * result + (int)(temp ^ (temp >>> 32));
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
        Vect2D other = (Vect2D)obj;
        if (Double.doubleToLongBits(m_x) != Double.doubleToLongBits(other.m_x))
            return false;
        if (Double.doubleToLongBits(m_y) != Double.doubleToLongBits(other.m_y))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "(x: " + m_x + ", y: " + m_y + ")";
    }


    
}
