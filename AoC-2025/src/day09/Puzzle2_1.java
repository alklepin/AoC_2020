package day09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.geometry.Line;
import common.geometry.PathInt;

public class Puzzle2_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        PathInt path = new PathInt();
        ArrayList<IntPair> points = new ArrayList<>();
        TreeSet<Integer> splitX = new TreeSet<>();
        TreeSet<Integer> splitY = new TreeSet<>();
        for (String line : lines)
        {
            var point = IntPair.from(line);
            points.add(point);
            path.add(point);
            splitX.add(point.getX());
            splitY.add(point.getY());
        }
        
        HashMap<IntPair, Boolean> isGreen = new HashMap<>();
        
        for (var x : splitX)
        {
            for (var y : splitY)
            {
                var pair = IntPair.of(x+1, y+1);
                var green = path.isInside(pair);
                isGreen.put(pair,  green);
            }
        }
        
        
        
        var testPath = new PathInt();
        testPath.add(IntPair.of(0, 0));
        testPath.add(IntPair.of(10, 0));
        testPath.add(IntPair.of(10, 10));
        testPath.add(IntPair.of(0, 10));
        var test = testPath.isInside(IntPair.of(10, 7));

//        var board = new Board2D(14, 9);
//        board.setAll('.');
//        for (var p : board.allCellsXY())
//        {
//            if (isInside(p, points))
//                board.setCharAtXY(p, 'X');
//        }
//        board.printAsStrings(System.out);
//        System.exit(0);
        

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
                        path.isInside(IntPair.of(minX, minY))
                        && path.isInside(IntPair.of(maxX, minY))
                        && path.isInside(IntPair.of(minX, maxY))
                        && path.isInside(IntPair.of(maxX, maxY))
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
