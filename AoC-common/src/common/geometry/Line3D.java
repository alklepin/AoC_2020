package common.geometry;

public class Line3D
{
    public static double EPS = 1E-12;
    private final Vect3D m_point;
    private final Vect3D m_direction;

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
    }
    
    public Vect3D getPoint()
    {
        return m_point;
    }

    public Vect3D getDirection()
    {
        return m_direction;
    }

    public LineLayout classifyWith(Line3D other)
    {
        var norm = m_direction.vectorMult(other.m_direction);
        if (norm.length() <= EPS)
            return LineLayout.Parallel;
        
        var v = m_point.minus(other.m_point);
        var s1 = norm.scalarMult(v) / v.length();
        if (Math.abs(s1) <= EPS)
        {
            return LineLayout.Intersecting;
        }
        return LineLayout.Crossing;
    }
    
    
    public Vect3D intersectWith(Line3D other)
    {
        var norm = m_direction.vectorMult(other.m_direction);
        if (norm.length() <= EPS)
        {
            if (contains(other.m_point))
                return other.m_point;
            else 
                return null;
        }
        
        var v = m_point.minus(other.m_point);
        var s1 = norm.scalarMult(v) / v.length();
        if (Math.abs(s1) <= EPS)
        {
            double sin = norm.length();
            double t = other.m_point.minus(m_point).vectorMult(other.m_direction).length() / sin;
            Vect3D result = m_point.add(m_direction.mult(t));
            return result;
        }
        return null;
    }
    
    public boolean contains(Vect3D point)
    {
        var v1 = m_point.add(m_direction);
        var v2 = v1.minus(m_point); 
        var v = point.minus(m_point);
        var s1 = m_direction.scalarMult(v)
            / v.length();
        return Math.abs(s1) >= (1 - EPS);
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
    
    public enum LineLayout
    {
        Parallel,
        Intersecting,
        Crossing
    }
}
