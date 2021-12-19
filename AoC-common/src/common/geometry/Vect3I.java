package common.geometry;

import java.util.ArrayList;

public class Vect3I
{
    public static final ArrayList<Vect3I> NEIGHBOURS = generateNeighbours(); 

    private int m_x;
    private int m_y;
    private int m_z;

    
    public static Vect3I of(int x, int y, int z)
    {
        return new Vect3I(x, y, z);
    }

    public static Vect3I vector(int x, int y, int z)
    {
        return new Vect3I(x, y, z);
    }

    private static ArrayList<Vect3I> generateNeighbours()
    {
        ArrayList<Vect3I> result = new ArrayList<>();
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                {
                    if (x != 0 || y != 0 || z != 0)
                    {
                        result.add(Vect3I.of(x, y, z));
                    }
                }
        return result;
    }
    
    public Vect3I(int x, int y, int z)
    {
        m_x = x;
        m_y = y;
        m_z = z;
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
    
    public Vect3I add(Vect3I other)
    {
        return new Vect3I(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z);
    }

    public Vect3I minus(Vect3I other)
    {
        return new Vect3I(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z);
    }

    public Vect3I mult(int number)
    {
        return new Vect3I(m_x * number, m_y * number, m_z * number);
    }

    public long scalarMult(Vect3I other)
    {
        return m_x * other.m_x +  m_y * other.m_y + m_z * other.m_z;
    }
    

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
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
        Vect3I other = (Vect3I)obj;
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
        return String.format("(%s, %s, %s)", m_x, m_y, m_z);
    }

    public static int comparatorXYZ(Vect3I v1, Vect3I v2)
    {
        if (v1.m_x < v2.m_x)
            return -1;
        if (v1.m_x > v2.m_x)
            return 1;
        if (v1.m_y < v2.m_y)
            return -1;
        if (v1.m_y > v2.m_y)
            return 1;
        if (v1.m_z < v2.m_z)
            return -1;
        if (v1.m_z > v2.m_z)
            return 1;
        return 0;
    }

    public Vect3I negate()
    {
        return new Vect3I(-m_x, -m_y, -m_z);
    }

    public int lengthManhattan()
    {
        return Math.abs(m_x) + Math.abs(m_y) + Math.abs(m_z);
    }
    
}
