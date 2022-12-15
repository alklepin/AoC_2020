package common.geometry;

public class Vect2I
{
    public static final Vect2I NORTH = new Vect2I(0, 1);
    public static final Vect2I SOUTH = new Vect2I(0, -1);
    public static final Vect2I EAST = new Vect2I(1, 0);
    public static final Vect2I WEST = new Vect2I(-1, 0);
    
    public static final Vect2I LEFT = Vect2I.of(-1, 0);
    public static final Vect2I RIGHT = Vect2I.of(1, 0);
    public static final Vect2I UP = Vect2I.of(0, 1);
    public static final Vect2I DOWN = Vect2I.of(0, -1);

    public static final Vect2I UP_LEFT = Vect2I.of(-1, 1);
    public static final Vect2I UP_RIGHT = Vect2I.of(1, 1);
    public static final Vect2I DOWN_LEFT = Vect2I.of(-1, -1);
    public static final Vect2I DOWN_RIGHT = Vect2I.of(1, -1);
    
    
    private int m_x;
    private int m_y;

    public static Vect2I of(int x, int y)
    {
        return new Vect2I(x, y);
    }
    
    public static Vect2I vector(int x, int y)
    {
        return new Vect2I(x, y);
    }
    
    public Vect2I(int x, int y)
    {
        m_x = x;
        m_y = y;
    }
    
    public int getX()
    {
        return m_x;
    }

    public int getY()
    {
        return m_y;
    }
    
    public Vect2I add(Vect2I other)
    {
        return new Vect2I(m_x + other.m_x, m_y + other.m_y);
    }

    public Vect2I minus(Vect2I other)
    {
        return new Vect2I(m_x - other.m_x, m_y - other.m_y);
    }

    public Vect2I mult(int number)
    {
        return new Vect2I(m_x * number, m_y * number);
    }

    public long scalarMult(Vect2I other)
    {
        return m_x * other.m_x +  m_y * other.m_y;
    }
    
    public Vect2I rotateLeft()
    {
        return new Vect2I(-m_y, m_x);
    }
    
    public Vect2I rotateRight()
    {
        return new Vect2I(m_y, -m_x);
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
        Vect2I other = (Vect2I)obj;
        if (m_x != other.m_x)
            return false;
        if (m_y != other.m_y)
            return false;
        return true;
    }

    public boolean inRectangle(Vect2I min, Vect2I max)
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
        return "(" + m_x + ", " + m_y + ")";
    }

    
}
