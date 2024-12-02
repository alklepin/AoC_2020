package day02;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;

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
        int result = 0;
        for (String line : lines)
        {
            var list = new ArrayList<Integer>();
            for (var value : line.split("\\s+"))
                list.add(parseInt(value));
            var isSafe = true;
            if (list.get(0) < list.get(list.size()-1))
            {
                for (var idx = 1; idx < list.size(); idx++)
                {
                    var cur = list.get(idx-1);
                    var next = list.get(idx);;
                    isSafe &= cur < next && cur + 3 >= next;
                }
            }
            else
            {
                for (var idx = 1; idx < list.size(); idx++)
                {
                    var cur = list.get(idx-1);
                    var next = list.get(idx);;
                    isSafe &= cur > next && cur - 3 <= next;
                }
            }
            if (isSafe)
                result++;
        }
        System.out.println(result);
        
    }
}
