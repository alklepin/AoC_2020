package common.boards;

public class IntTriple
{
    
    public final static IntTriple X_PLUS = IntTriple.of(1, 0, 0);  
    public final static IntTriple X_MINUS = IntTriple.of(-1, 0, 0);
    public final static IntTriple Y_PLUS = IntTriple.of(0, 1, 0);  
    public final static IntTriple Y_MINUS = IntTriple.of(0, -1, 0);
    public final static IntTriple Z_PLUS = IntTriple.of(0, 0, 1);  
    public final static IntTriple Z_MINUS = IntTriple.of(0, 0, -1);
    
    public final static IntTriple[] NEIGHBOURS6 = 
        new IntTriple[]{X_PLUS, X_MINUS, Y_PLUS, Y_MINUS, Z_PLUS, Z_MINUS};
    
    private int m_x;
    private int m_y;
    private int m_z;

    public static IntTriple of(int x, int y, int z)
    {
        return new IntTriple(x, y, z);
    }

    public static IntTriple of(String x, String y, String z)
    {
        return new IntTriple(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
    }
    
    public static IntTriple from(String s)
    {
        var parts = s.split(",");
        return new IntTriple(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
    
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

    public int getY()
    {
        return m_y;
    }

    public int getZ()
    {
        return m_z;
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
    
    public double length()
    {
        return Math.sqrt(m_x*(double)m_x + m_y*(double)m_y + m_z*(double)m_z); 
    }

    public long lengthSquared()
    {
        return m_x*(long)m_x + m_y*(long)m_y + m_z*(long)m_z;
    }

    /**
     * Returns Manhattan length
     * @return
     */
    public long lengthManh()
    {
        return Math.abs(m_x) + Math.abs(m_y) + Math.abs(m_z);
    }

    /**
     * Returns Chebyshev length
     * @return
     */
    public long lengthCheb()
    {
        return Math.max(Math.max(Math.abs(m_x), Math.abs(m_y)), Math.abs(m_z));
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
//        return "IntPair [m_x=" + m_x + ", m_y=" + m_y +", m_z=" + m_z + "]";
        return String.format("(%s, %s, %s)", m_x, m_y, m_z);
    }

    private static int signum(int value)
    {
        return value > 0 ? 1 : value < 0 ? -1 : 0;
    }
    
    public IntTriple signum()
    {
        return new IntTriple(signum(m_x), signum(m_y), signum(m_z));
    }
    
    public boolean componentLessEq(IntTriple point1)
    {
        return m_x <= point1.m_x 
            && m_y <= point1.m_y
            && m_z <= point1.m_z
            ;
    }
    
    public boolean componentGreaterEq(IntTriple point1)
    {
        return m_x >= point1.m_x 
            && m_y >= point1.m_y
            && m_z >= point1.m_z
            ;
    }

    public IntTriple componentMax(IntTriple point1)
    {
        return new IntTriple(Math.max(m_x, point1.m_x),
            Math.max(m_y, point1.m_y),
            Math.max(m_z, point1.m_z)
            );
    }

    public IntTriple componentMin(IntTriple point1)
    {
        return new IntTriple(Math.min(m_x, point1.m_x),
            Math.min(m_y, point1.m_y),
            Math.min(m_z, point1.m_z)
            );
    }

    public boolean inRectangle(IntTriple min, IntTriple max)
    {
        if ((min != null) && !componentGreaterEq(min))
        {
            return false;
        }
        if ((max != null) && !componentLessEq(max))
        {
            return false;
        }
        return true;
    }

    
}
