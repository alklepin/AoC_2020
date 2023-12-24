package common.geometry;

public class Line3D
{
    private final Vect3D m_point;
    private final Vect3D m_direction;
//    private final Vect2D m_norm;

    public static Line3D byPoints(Vect3D point1, Vect3D point2)
    {
        return new Line3D(point1, point2.minus(point1));
    }

//    public static Line3D byPointAndNorm(Vect2D point, Vect2D norm)
//    {
//        return new Line3D(point, norm.rotateRight());
//    }

//    public static Line3D bySegment(Segment segment)
//    {
//        return Line3D.byPoints(segment.getPoint1(), segment.getPoint2());
//    }
    
    public Line3D(Vect3D point, Vect3D direction)
    {
        m_point = point;
        m_direction = direction.divideBy(direction.length());
//        m_norm = m_direction.rotateLeft();
    }
    
    public Vect3D getPoint()
    {
        return m_point;
    }

    public Vect3D getDirection()
    {
        return m_direction;
    }
    
//    public double signedDistanceTo(Vect2D point)
//    {
//        return point.minus(m_point).scalarMult(m_norm);
//    }
//
//    public double distanceTo(Vect2D point)
//    {
//        return Math.abs(signedDistanceTo(point));
//    }
    
//    public Vect3D intersectWith(Line3D other)
//    {
//        double sin = m_direction.vectorMult(other.m_direction);
//        if (sin == 0)
//            return null;
//        double t = other.m_point.minus(m_point).vectorMult(other.m_direction) / sin;
//        Vect2D result = m_point.add(m_direction.mult(t));
//        return result;
//    }
}
