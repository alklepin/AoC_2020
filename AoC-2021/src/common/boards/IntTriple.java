package common.boards;

public class IntTriple
{
    private int m_x;
    private int m_y;
    private int m_z;
    
    public IntTriple(int x, int y, int z)
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
    
    public IntTriple add(IntTriple other)
    {
        return new IntTriple(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z);
    }

    public IntTriple minus(IntTriple other)
    {
        return new IntTriple(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z);
    }

    public IntTriple mult(int number)
    {
        return new IntTriple(m_x * number, m_y * number, m_z * number);
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
        IntTriple other = (IntTriple)obj;
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
        return "IntPair [m_x=" + m_x + ", m_y=" + m_y +", m_z=" + m_z + "]";
    }

    
}
