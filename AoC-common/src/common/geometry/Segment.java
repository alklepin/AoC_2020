package common.geometry;

import common.boards.IntPair;

public class Segment
{
    private final Vect2D m_point1;
    private final Vect2D m_point2;

    public static Segment of(IntPair point1, IntPair point2)
    {
        return new Segment(point1.asVector(), point2.asVector());
    }
    
    public Segment(Vect2D point1, Vect2D point2)
    {
        this.m_point1 = point1;
        this.m_point2 = point2;
    }
    
    public Segment(IntPair point1, IntPair point2)
    {
        this.m_point1 = point1.asVector();
        this.m_point2 = point2.asVector();
    }
    
    public Vect2D getPoint1()
    {
        return m_point1;
    }
    
    public Vect2D getPoint2()
    {
        return m_point2;
    }
    
    public boolean contains(Vect2D point)
    {
        Vect2D direction = m_point2.minus(m_point1);
        double directionLength = direction.length();
        double t = point.minus(m_point1).scalarMult(direction)/(directionLength*directionLength);
        return (t >= 0 && t <= 1);
    }
    
    public Vect2D intersect(Segment other)
    {
        Line l1 = Line.bySegment(this);
        Line l2 = Line.bySegment(other);
        Vect2D p = l1.intersectWith(l2);
        if (p == null)
            return null;
        if (!this.contains(p) || !other.contains(p))
            return null;
        return p;
    }

    public Vect2D asVector()
    {
        return m_point2.minus(m_point1);
    }
    
    @Override
    public String toString()
    {
        return "Segment [m_point1=" + m_point1 + ", m_point2=" + m_point2 + "]";
    }
}
