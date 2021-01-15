package common.geometry;

import java.util.ArrayList;

public class Vect4I
{
    public static final ArrayList<Vect4I> NEIGHBOURS = generateNeighbours(); 

    private int m_x;
    private int m_y;
    private int m_z;
    private int m_v;

    
    public static Vect4I of(int x, int y, int z, int v)
    {
        return new Vect4I(x, y, z, v);
    }

    public static Vect4I vector(int x, int y, int z, int v)
    {
        return new Vect4I(x, y, z, v);
    }

    private static ArrayList<Vect4I> generateNeighbours()
    {
        ArrayList<Vect4I> result = new ArrayList<>();
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                    for (int v = -1; v <= 1; v++)
                    {
                        if (x != 0 || y != 0 || z != 0 || v != 0)
                        {
                            result.add(Vect4I.of(x, y, z, v));
                        }
                    }
        return result;
    }
    
    public Vect4I(int x, int y, int z, int v)
    {
        m_x = x;
        m_y = y;
        m_z = z;
        m_v = v;
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
    
    public int getZ()
    {
        return m_z;
    }
    public void setZ(int z)
    {
        this.m_z = z;
    }
    
    public int getV()
    {
        return m_v;
    }
    public void setV(int v)
    {
        this.m_v = v;
    }
    
    public Vect4I add(Vect4I other)
    {
        return new Vect4I(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z, m_v + other.m_v);
    }

    public Vect4I minus(Vect4I other)
    {
        return new Vect4I(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z, m_v - other.m_v);
    }

    public Vect4I mult(int number)
    {
        return new Vect4I(m_x * number, m_y * number, m_z * number, m_v * number);
    }

    public long scalarMult(Vect4I other)
    {
        return m_x * other.m_x +  m_y * other.m_y + m_z * other.m_z + m_v * other.m_v;
    }
    


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_v;
        result = prime * result + m_x;
        result = prime * result + m_y;
        result = prime * result + m_z;
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
        Vect4I other = (Vect4I)obj;
        if (m_v != other.m_v)
            return false;
        if (m_x != other.m_x)
            return false;
        if (m_y != other.m_y)
            return false;
        if (m_z != other.m_z)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "IntPair [m_x=" + m_x + ", m_y=" + m_y + ", m_z=" + m_z + ", m_v=" + m_v + "]";
    }

    
}
