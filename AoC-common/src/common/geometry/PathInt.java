package common.geometry;

import java.util.ArrayList;

import common.boards.IntPair;

public class PathInt
{
    private ArrayList<IntPair> points = new ArrayList<>();
    
    public int size()
    {
        return points.size();
    }

    public void clear()
    {
        points.clear();
    }
    
    public void add(IntPair point)
    {
        points.add(point);
    }

    public boolean isInside(IntPair point)
    {
        return isInsideByRay(point);
    }
    
    
    /**
     * Checks whether the point is inside the path by tracing a horizontal ray
     * @param point
     * @return
     */
    public boolean isInsideByRay(IntPair point)
    {
        var count = 0;
        for (var idx = 1; idx < points.size(); idx++)
        {
            var p1 = points.get(idx-1);
            var p2 = points.get(idx);
            
            var res = intersectHRay(point, p1, p2);
            if (res == IntersectionKind.OnSegment)
                return true;
            
            count += switch (res)
            {
                case IntersectsEndNegative -> -1;
                case IntersectsEndPositive -> 1;
                case IntersectsInsideNegative -> -2;
                case IntersectsInsidePositive -> 2;
                case IntersectsStartNegative -> -1;
                case IntersectsStartPositive -> 1;
                case NoIntersection -> 0;
                case OnSegment -> 0;
                case SegmentCovered -> 0;
                default -> throw new IllegalStateException();
            };
            
        }
        {
            var p1 = points.get(points.size()-1);
            var p2 = points.get(0);
            var res = intersectHRay(point, p1, p2);
            if (res == IntersectionKind.OnSegment)
                return true;
            
            count += switch (res)
            {
                case IntersectsEndNegative -> -1;
                case IntersectsEndPositive -> 1;
                case IntersectsInsideNegative -> -2;
                case IntersectsInsidePositive -> 2;
                case IntersectsStartNegative -> -1;
                case IntersectsStartPositive -> 1;
                case NoIntersection -> 0;
                case OnSegment -> 0;
                case SegmentCovered -> 0;
                default -> throw new IllegalStateException();
            };
        }
        return (count % 4 == 2);
    }
    
    enum IntersectionKind
    {
        NoIntersection, // horizontal ray does not intersect a segment 
        OnSegment, // ray start point is on a segment
        SegmentCovered, // ray covers a segment
        IntersectsStartPositive,
        IntersectsStartNegative,
        IntersectsEndPositive,
        IntersectsEndNegative,
        IntersectsInsidePositive,
        IntersectsInsideNegative,
    }
    
    public IntersectionKind intersectHRay(IntPair point, IntPair start, IntPair end)
    {
        var v = end.minus(start);
        
        if (v.getY() == 0)
        {
            // horizontal segment
            if (point.getY() == start.getY())
            {
                var segMinX = Math.min(start.getX(), end.getX());
                var segMaxX = Math.max(start.getX(), end.getX());
                if (point.getX() >= segMinX)
                    if (point.getX() <= segMaxX)
                        return IntersectionKind.OnSegment;
                    else
                        return IntersectionKind.NoIntersection;
                else
                    return IntersectionKind.SegmentCovered;
            }
            else
            {
                return IntersectionKind.NoIntersection;
            }
        }
        var segMinY = Math.min(start.getY(), end.getY());
        var segMaxY = Math.max(start.getY(), end.getY());
        
        if (point.getY() < segMinY || point.getY() > segMaxY)
            return IntersectionKind.NoIntersection;
        
        var line = Line.byPoints(start.asVector(), end.asVector());
        var ray = Line.byPointAndNorm(point.asVector(), IntPair.UP.asVector());
        var intersectPoint = line.intersectWith(ray);
        if (intersectPoint.getX() < point.getX())
            return IntersectionKind.NoIntersection;
        
        var d = line.signedDistanceTo(point.asVector());
        if (d == 0)
            return IntersectionKind.OnSegment;
        if (point.getY() == start.getY())
            return (d >= 0) ? IntersectionKind.IntersectsStartPositive : IntersectionKind.IntersectsStartNegative;
        if (point.getY() == end.getY())
            return (d >= 0) ? IntersectionKind.IntersectsEndPositive : IntersectionKind.IntersectsEndNegative;
                
        return (d >= 0) ? IntersectionKind.IntersectsInsidePositive : IntersectionKind.IntersectsInsideNegative;
    }
    
    public double angleBetween(IntPair v1, IntPair v2)
    {
        var v = (long)v1.getX()*v2.getY() - (long)v1.getY()*v2.getX();
        var s = (long)v1.getX()*v2.getX() + (long)v1.getY()*v2.getY();
        var l1 = v1.length();
        var l2 = v2.length();
        var lm = l1 * l2;
        var sinA = v != 0 ? v / lm : 0;
        var angle = Math.asin(sinA);
        if (s < 0)
        {
            if (angle >= 0)
                angle = Math.PI - angle;
            if (angle < 0)
                angle = -Math.PI - angle;
        }
        return angle;
    }
    
    public boolean isInsideByAngles(IntPair point)
    {
        double angle = 0;
        for (var idx = 1; idx < points.size(); idx++)
        {
            var p1 = points.get(idx-1);
            var p2 = points.get(idx);
            var v1 = p1.minus(point);
            var v2 = p2.minus(point);
            var a = angleBetween(v1, v2);
            angle += a;
            if (a == Math.PI)
                return true;
        }
        {
            var p1 = points.get(points.size()-1);
            var p2 = points.get(0);
            var v1 = p1.minus(point);
            var v2 = p2.minus(point);
            var a = angleBetween(v1, v2);
            angle += a;
            if (a == Math.PI)
                return true;
        }
//        if (angle >= Math.PI*2 || angle <= -Math.PI*2)
//            angle = angle % (Math.PI * 2);
        return Math.abs(angle) > Math.PI / 16;
    }
    
}
