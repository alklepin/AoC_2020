package common.boards;

public class IntQuadruple
{
    public static final IntQuadruple ZERO = of(0,0,0,0);
    
    private int m_x;
    private int m_y;
    private int m_z;
    private int m_k;

    public static IntQuadruple of(int x, int y, int z, int k)
    {
        return new IntQuadruple(x, y, z, k);
    }

    public IntQuadruple(int x, int y, int z, int k)
    {
        m_x = x;
        m_y = y;
        m_z = z;
        m_k = k;
    }
    
    public int getX()
    {
        return m_x;
    }

    public int getY()
    {
        return m_y;
    }
    
    public int getZ()
    {
        return m_z;
    }
    
    public int getK()
    {
        return m_k;
    }
    
    public IntQuadruple add(IntQuadruple other)
    {
        return new IntQuadruple(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z, m_k + other.m_k);
    }

    public IntQuadruple minus(IntQuadruple other)
    {
        return new IntQuadruple(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z, m_k - other.m_k);
    }

    public IntQuadruple mult(int number)
    {
        return new IntQuadruple(m_x * number, m_y * number, m_z * number, m_k * number);
    }
    
    public double length()
    {
        return Math.sqrt(m_x*(double)m_x + m_y*(double)m_y + m_z*(double)m_z + m_k*(double)m_k); 
    }

    public long lengthSquared()
    {
        return m_x*(long)m_x + m_y*(long)m_y + m_z*(long)m_z + m_k*(long)m_k;
    }

    
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_x;
        result = prime * result + m_y;
        result = prime * result + m_z;
        result = prime * result + m_k;
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
        IntQuadruple other = (IntQuadruple)obj;
        if (m_x != other.m_x)
            return false;
        if (m_y != other.m_y)
            return false;
        if (m_z != other.m_z)
            return false;
        if (m_k != other.m_k)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("(%s, %s, %s, %s)", m_x, m_y, m_z, m_k);
    }

    public boolean componentLessEq(IntQuadruple point1)
    {
        return m_x <= point1.m_x 
            && m_y <= point1.m_y
            && m_z <= point1.m_z
            && m_k <= point1.m_k
            ;
    }
    
    public boolean componentGreaterEq(IntQuadruple point1)
    {
        return m_x >= point1.m_x 
            && m_y >= point1.m_y
            && m_z >= point1.m_z
            && m_k >= point1.m_k
            ;
    }

    public IntQuadruple componentMax(IntQuadruple point1)
    {
        return new IntQuadruple(Math.max(m_x, point1.m_x),
            Math.max(m_y, point1.m_y),
            Math.max(m_z, point1.m_z),
            Math.max(m_k, point1.m_k)
            );
    }

    public IntQuadruple componentMin(IntQuadruple point1)
    {
        return new IntQuadruple(Math.min(m_x, point1.m_x),
            Math.min(m_y, point1.m_y),
            Math.min(m_z, point1.m_z),
            Math.min(m_k, point1.m_k)
            );
    }
}
