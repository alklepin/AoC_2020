package day09;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
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
        ArrayList<IntPair> points = new ArrayList<>();
        for (String line : lines)
        {
            var point = IntPair.from(line);
            points.add(point);
        }

        long result = 0;
        for (var idx1 = 0; idx1 < points.size(); idx1++)
        {
            var p1 = points.get(idx1);
            for (var idx2 = idx1+1; idx2 < points.size(); idx2++)
            {
                var p2 = points.get(idx2);
                var area = (Math.abs((long)p1.getX()-p2.getX())+1)
                    * (Math.abs(p1.getY()-p2.getY())+1);
                if (area > result)
                    result = area;
            }
            
        }
        System.out.println(result);
        
    }
}
