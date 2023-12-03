package common.boards;

import common.geometry.Vect2D;

public class IntPair
{
    public static final IntPair ZERO = Pair.of(0,  0);
    
    public static final IntPair LEFT = IntPair.of(-1, 0);
    public static final IntPair RIGHT = IntPair.of(1, 0);
    public static final IntPair UP = IntPair.of(0, 1);
    public static final IntPair DOWN = IntPair.of(0, -1);

    public static final IntPair UP_LEFT = IntPair.of(-1, 1);
    public static final IntPair UP_RIGHT = IntPair.of(1, 1);
    public static final IntPair DOWN_LEFT = IntPair.of(-1, -1);
    public static final IntPair DOWN_RIGHT = IntPair.of(1, -1);
    
    private int m_x;
    private int m_y;

    public static IntPair of(int x, int y)
    {
        return new IntPair(x, y);
    }

    public static IntPair of(String x, String y)
    {
        return new IntPair(Integer.parseInt(x), Integer.parseInt(y));
    }
    
    public static IntPair from(String s)
    {
        var parts = s.split(",");
        return new IntPair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
    
    public static IntPair decodeDirection(char c)
    {
        return switch (c) 
        {
            case '^' -> IntPair.UP;
            case 'v' -> IntPair.DOWN;
            case '>' -> IntPair.RIGHT;
            case '<' -> IntPair.LEFT;
            default -> null;
        };
    }
    
    public static IntPair decodeDirectionVInv(char c)
    {
        return switch (c) 
        {
            case '^' -> IntPair.DOWN;
            case 'v' -> IntPair.UP;
            case '>' -> IntPair.RIGHT;
            case '<' -> IntPair.LEFT;
            default -> null;
        };
    }    
    
    public static char asDirectionChar(IntPair p)
    {
        p = p.signum();
        if (p.equals(UP))
            return '^';
        else if (p.equals(DOWN))
            return 'v';
        else if (p.equals(RIGHT))
            return '>';
        else if (p.equals(LEFT))
            return '<';
        else
            return '\0';
    }
    
    public static char asDirectionCharVInv(IntPair p)
    {
        p = p.signum();
        if (p.equals(DOWN))
            return '^';
        else if (p.equals(UP))
            return 'v';
        else if (p.equals(RIGHT))
            return '>';
        else if (p.equals(LEFT))
            return '<';
        else
            return '\0';
    }
    
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
    
    public IntPair copy()
    {
        return new IntPair(m_x, m_y);
    }
    
    public int getX()
    {
        return m_x;
    }
    
    public int getY()
    {
        return m_y;
    }
    public IntPair add(IntPair other)
    {
        return new IntPair(m_x + other.m_x, m_y + other.m_y);
    }

    public IntPair addBounded(IntPair other, Bounds bounds)
    {
        return minus(bounds.min())
            .add(other)
            .componentModulo(bounds.dim())
            .add(bounds.min());
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
    
    public IntPair nextRight()
    {
        return add(RIGHT);
    }

    public IntPair nextLeft()
    {
        return add(LEFT);
    }

    public IntPair nextUp()
    {
        return add(UP);
    }

    public IntPair nextDown()
    {
        return add(DOWN);
    }
    
    /**
     * Returns Manhattan length
     * @return
     */
    public int lengthManh()
    {
        return Math.abs(m_x) + Math.abs(m_y);
    }

    /**
     * Returns Chebyshev length
     * @return
     */
    public int lengthCheb()
    {
        return Math.max(Math.abs(m_x), Math.abs(m_y));
    }

    /**
     * Returns Euclidean length
     * @return
     */
    public double length()
    {
        return Math.sqrt(m_x * m_x + m_y * m_y);
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

    /**
     * Limits coordinates by given rectangle 
     */
    public IntPair toRectangle(IntPair min, IntPair max)
    {
        int x = m_x;
        int y = m_y;
        if (min != null)
        {
            x = Math.max(x, min.m_x);
            y = Math.max(y, min.m_y);
        }
        if (max != null)
        {
            x = Math.min(x, max.m_x);
            y = Math.min(y, max.m_y);
        }
        return Pair.of(x, y);
    }
    
    private static int signum(int value)
    {
        return value > 0 ? 1 : value < 0 ? -1 : 0;
    }
    
    public IntPair signum()
    {
        return Pair.of(signum(m_x), signum(m_y));
    }

    @Override
    public String toString()
    {
        return "(" + m_x + ", " + m_y + ")";
    }

    public boolean componentLessEq(IntPair next)
    {
        return m_x <= next.getX() && m_y <= next.getY();
    }

    public IntPair componentMin(IntPair other)
    {
        return Pair.of(Math.min(m_x, other.m_x), Math.min(m_y, other.m_y));
    }

    public IntPair componentMax(IntPair other)
    {
        return Pair.of(Math.max(m_x, other.m_x), Math.max(m_y, other.m_y));
    }

    public IntPair componentModulo(IntPair dims)
    {
        var x = m_x % dims.getX();
        if (x < 0)
        {
            x += dims.getX();  
        }
        var y = m_y % dims.getY();
        if (y < 0)
        {
            y += dims.getY();  
        }
        return new IntPair(x, y);
    }
}
