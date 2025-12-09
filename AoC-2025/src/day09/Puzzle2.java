package day09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.geometry.Line;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
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
    
    public boolean isInsideBad(IntPair point, ArrayList<IntPair> points)
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
        if (angle >= Math.PI*2 || angle <= -Math.PI*2)
            angle = angle % (Math.PI * 2);
        return Math.abs(angle) > Math.PI / 16;
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
    
    public boolean isInside(IntPair point, ArrayList<IntPair> points)
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
    
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<IntPair> points = new ArrayList<>();
        TreeSet<Integer> splitX = new TreeSet<>();
        TreeSet<Integer> splitY = new TreeSet<>();
        for (String line : lines)
        {
            var point = IntPair.from(line);
            points.add(point);
            splitX.add(point.getX());
            splitY.add(point.getY());
        }
        
        HashMap<IntPair, Boolean> isGreen = new HashMap<>();
        
        for (var x : splitX)
        {
            for (var y : splitY)
            {
                var pair = IntPair.of(x+1, y+1);
                var green = isInside(pair, points);
                isGreen.put(pair,  green);
            }
        }
        
        
        
        var testPoints = new ArrayList<IntPair>();
        testPoints.add(IntPair.of(0, 0));
        testPoints.add(IntPair.of(10, 0));
        testPoints.add(IntPair.of(10, 10));
        testPoints.add(IntPair.of(0, 10));
        var test = isInside(IntPair.of(10, 7), points);

//        var board = new Board2D(14, 9);
//        board.setAll('.');
//        for (var p : board.allCellsXY())
//        {
//            if (isInside(p, points))
//                board.setCharAtXY(p, 'X');
//        }
//        board.printAsStrings(System.out);
//        System.exit(0);
        
        // (16418, 84769)
        // (83031, 15981)
//        var test1 = isInside(IntPair.of(16418, 84769), points);
//        var test2 = isInside(IntPair.of(83031, 15981), points);
//        var test3 = isInside(IntPair.of(16418, 15981), points);
//        var test4 = isInside(IntPair.of(83031, 84769), points);

        long result = 0;
        for (var idx1 = 0; idx1 < points.size(); idx1++)
        {
            var p1 = points.get(idx1);
            loop: 
            for (var idx2 = idx1+1; idx2 < points.size(); idx2++)
            {
                var p2 = points.get(idx2);
                var area = (Math.abs((long)p1.getX()-p2.getX())+1)
                    * (Math.abs(p1.getY()-p2.getY())+1);
                if (area > result)
                {
                    var minX = Math.min(p1.getX(), p2.getX());
                    var maxX = Math.max(p1.getX(), p2.getX());
                    var minY = Math.min(p1.getY(), p2.getY());
                    var maxY = Math.max(p1.getY(), p2.getY());
                    if (
                        isInside(IntPair.of(minX, minY), points)
                        && isInside(IntPair.of(maxX, minY), points)
                        && isInside(IntPair.of(minX, maxY), points)
                        && isInside(IntPair.of(maxX, maxY), points)
                        )
                    {
                        var x = splitX.floor(minX);
                        while (x < maxX)
                        {
                            var y = splitY.floor(minY);
                            while (y < maxY)
                            {
                                if (!isGreen.get(IntPair.of(x+1, y+1)))
                                    continue loop;
                                y = splitY.ceiling(y+1);
                            }
                            x = splitX.ceiling(x+1);
                        }
                        
                        result = area;
                        System.out.println(p1);
                        System.out.println(p2);
                        System.out.println("---");
                    }
                }
            }
            
        }
        System.out.println(result);
        
    }
}
