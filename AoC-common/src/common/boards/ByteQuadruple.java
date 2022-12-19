package common.boards;

public class ByteQuadruple
{
    public static final ByteQuadruple ZERO = of(0,0,0,0);
    
    private byte m_x;
    private byte m_y;
    private byte m_z;
    private byte m_k;

    public static ByteQuadruple of(byte x, byte y, byte z, byte k)
    {
        return new ByteQuadruple(x, y, z, k);
    }

    public static ByteQuadruple of(int x, int y, int z, int k)
    {
        return new ByteQuadruple((byte)x, (byte)y, (byte)z, (byte)k);
    }

    public ByteQuadruple(int x, int y, int z, int k)
    {
        this((byte)x, (byte)y, (byte)z, (byte)k);
    }
    
    public ByteQuadruple(byte x, byte y, byte z, byte k)
    {
        m_x = x;
        m_y = y;
        m_z = z;
        m_k = k;
    }
    
    public byte getX()
    {
        return m_x;
    }

    public byte getY()
    {
        return m_y;
    }
    
    public byte getZ()
    {
        return m_z;
    }
    
    public byte getK()
    {
        return m_k;
    }
    
    public ByteQuadruple add(ByteQuadruple other)
    {
        return new ByteQuadruple(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z, m_k + other.m_k);
    }

    public ByteQuadruple minus(ByteQuadruple other)
    {
        return new ByteQuadruple(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z, m_k - other.m_k);
    }

    public ByteQuadruple mult(int number)
    {
        return new ByteQuadruple(m_x * number, m_y * number, m_z * number, m_k * number);
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
        ByteQuadruple other = (ByteQuadruple)obj;
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

    public boolean componentLessEq(ByteQuadruple point1)
    {
        return m_x <= point1.m_x 
            && m_y <= point1.m_y
            && m_z <= point1.m_z
            && m_k <= point1.m_k
            ;
    }
    
    public boolean componentGreaterEq(ByteQuadruple point1)
    {
        return m_x >= point1.m_x 
            && m_y >= point1.m_y
            && m_z >= point1.m_z
            && m_k >= point1.m_k
            ;
    }

    public ByteQuadruple componentMax(ByteQuadruple point1)
    {
        return new ByteQuadruple(Math.max(m_x, point1.m_x),
            Math.max(m_y, point1.m_y),
            Math.max(m_z, point1.m_z),
            Math.max(m_k, point1.m_k)
            );
    }

    public ByteQuadruple componentMin(ByteQuadruple point1)
    {
        return new ByteQuadruple(Math.min(m_x, point1.m_x),
            Math.min(m_y, point1.m_y),
            Math.min(m_z, point1.m_z),
            Math.min(m_k, point1.m_k)
            );
    }
}
