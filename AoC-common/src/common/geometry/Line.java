package common.geometry;

public class Line
{
    private final Vect2D m_point;
    private final Vect2D m_direction;
    private final Vect2D m_norm;

    public static Line byPoints(Vect2D point1, Vect2D point2)
    {
        return new Line(point1, point2.minus(point1));
    }

    public static Line byPointAndNorm(Vect2D point, Vect2D norm)
    {
        return new Line(point, norm.rotateRight());
    }

    public static Line bySegment(Segment segment)
    {
        return Line.byPoints(segment.getPoint1(), segment.getPoint2());
    }
    
    public Line(Vect2D point, Vect2D direction)
    {
        m_point = point;
        m_direction = direction.divideBy(direction.length());
        m_norm = m_direction.rotateLeft();
    }
    
    public Vect2D getPoint()
    {
        return m_point;
    }

    public Vect2D getDirection()
    {
        return m_direction;
    }
    
    public double signedDistanceTo(Vect2D point)
    {
        return point.minus(m_point).scalarMult(m_norm);
    }

    public double distanceTo(Vect2D point)
    {
        return Math.abs(signedDistanceTo(point));
    }
    
    public Vect2D intersectWith(Line other)
    {
        double sin = m_direction.vectorMult(other.m_direction);
        if (sin == 0)
            return null;
        double t = other.m_point.minus(m_point).vectorMult(other.m_direction) / sin;
        Vect2D result = m_point.add(m_direction.mult(t));
        return result;
    }
}
