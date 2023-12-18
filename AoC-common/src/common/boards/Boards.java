package common.boards;

import common.geometry.Vect2D;
import common.queries.Query;

public class Boards
{
    /**
     * Calculates grid area covered by polyline given by points
     * For points (3,1), (5,1), (5,2), (6,2), (6,4), (2,4), (2,3), (3,3)
     * polyline is shown below 
     * ..###.
     * ..#.##
     * .##..#
     * .#####
     * and area is 17
     * @param points
     * @return
     */
    public static long areaOfGrid(Iterable<IntPair> points)
    {
        var pts = Query.wrap(points);
        pts = pts.concat(pts.take(2));
        
        
        long area = 0;
        long delta = 0;
        long deltaR = 0;
        long deltaL = 0;
        IntPair prevPoint = null;
        IntPair prevVect = null;
        for (var point : pts)
        {
            if (prevPoint == null)
            {
                prevPoint = point;
                continue;
            }
            if (prevVect == null)
            {
                prevVect = point.minus(prevPoint);
                prevPoint = point;
                continue;
            }
            var vect = point.minus(prevPoint);
            area += prevPoint.vectorMult(vect);
            delta += 2 * (vect.length()-1);
            var md = prevVect.vectorMult(vect);
            if (md > 0)
                deltaR += 3;
            else if (md < 0)
                deltaL += 1;
            else
                delta += 2;
            
            prevVect = vect;
            prevPoint = point;
        }
//        System.out.println("delta: "+delta/2);
//        System.out.println("deltaR: "+deltaR/3);
//        System.out.println("deltaL: "+deltaL);
//        System.out.println("areaL: "+area);
        
        return area / 2 + (delta+deltaR+deltaL) / 4;
    }

    public static double area(Iterable<Vect2D> points)
    {
        var pts = Query.wrap(points);
        pts = pts.concat(pts.take(2));

        
        double area = 0;
        Vect2D prevPoint = null;
        Vect2D prevVect = null;
        for (var point : pts)
        {
            if (prevPoint == null)
            {
                prevPoint = point;
                continue;
            }
            if (prevVect == null)
            {
                prevVect = point.minus(prevPoint);
                prevPoint = point;
                continue;
            }
            var vect = point.minus(prevPoint);
            area += prevPoint.vectorMult(vect);

            prevVect = vect;
            prevPoint = point;
        }
        
        return area / 2;
    }
}
