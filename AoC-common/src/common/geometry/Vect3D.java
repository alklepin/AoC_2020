package common.geometry;

import common.boards.IntTriple;

public class Vect3D
{
    public static final Vect3D ZERO = Vect3D.of(0, 0, 0);
    
    private final double m_x;
    private final double m_y;
    private final double m_z;
    
    public static Vect3D vector(double x, double y, double z)
    {
        return new Vect3D(x, y, z);
    }
    
    public static Vect3D of(double x, double y, double z)
    {
        return new Vect3D(x, y, z);
    }
    
    public static Vect3D from(String s)
    {
        var parts = s.split("[\\s,]+");
        return new Vect3D(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
    
    public Vect3D(double x, double y, double z)
    {
        m_x = x;
        m_y = y;
        m_z = z;
    }
    
    public double getX()
    {
        return m_x;
    }

    public double getY()
    {
        return m_y;
    }
    
    public double getZ()
    {
        return m_z;
    }

    public IntTriple asIntTriple()
    {
        return new IntTriple((int)Math.round(m_x), (int)Math.round(m_y), (int)Math.round(m_z));
    }
    
    public Vect3D add(Vect3D other)
    {
        return new Vect3D(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z);
    }

    public Vect3D minus(Vect3D other)
    {
        return new Vect3D(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z);
    }

    public Vect3D mult(double value)
    {
        return new Vect3D(m_x * value, m_y * value, m_z * value);
    }

    public Vect3D divideBy(double value)
    {
        return new Vect3D(m_x / value, m_y / value, m_z / value);
    }

    public double scalarMult(Vect3D other)
    {
        return m_x * other.m_x +  m_y * other.m_y +  m_z * other.m_z;
    }

    public Vect3D vectorMult(Vect3D other)
    {
        var x = m_y * other.m_z - m_z * other.m_y;
        var y = -m_x * other.m_z + m_z * other.m_x;
        var z = m_x * other.m_y - m_y * other.m_x;
        
        return new Vect3D(x, y, z);
    }
    
//    public Vect3D rotateLeft()
//    {
//        return new Vect2D(-m_y, m_x);
//    }
//    
//    public Vect3D rotateRight()
//    {
//        return new Vect2D(m_y, -m_x);
//    }
    
//    public Vect3D rotate(double angle)
//    {
//        double sin = Math.sin(angle);
//        double cos = Math.cos(angle);
//        return new Vect2D(m_x*cos - m_y*sin, m_x*sin + m_y*cos);
//    }
    
    public double length()
    {
        return Math.sqrt(m_x*m_x + m_y*m_y + m_z*m_z);
    }

    public boolean inRectangle(Vect3D min, Vect3D max)
    {
        if ((min != null) && (m_x < min.m_x || m_y < min.m_y || m_z < min.m_z))
        {
            return false;
        }
        if ((max != null) && (m_x > max.m_x || m_y > max.m_y || m_z > max.m_z))
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
        temp = Double.doubleToLongBits(m_z);
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
        Vect3D other = (Vect3D)obj;
        if (Double.doubleToLongBits(m_x) != Double.doubleToLongBits(other.m_x))
            return false;
        if (Double.doubleToLongBits(m_y) != Double.doubleToLongBits(other.m_y))
            return false;
        if (Double.doubleToLongBits(m_z) != Double.doubleToLongBits(other.m_z))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Vect3D [m_x=" + m_x + ", m_y=" + m_y + ", m_z=" + m_z + "]";
    }

    
}
