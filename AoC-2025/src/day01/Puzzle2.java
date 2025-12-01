package day01;

import common.LinesGroup;
import common.PuzzleCommon;

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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLines(inputFile);
        
        int result = 0;
        var pos = 50;
        for (String line : lines)
        {
            var v = parseInt(line.substring(1));
            while (v > 0)
            {
                if (line.startsWith("R"))
                {
                    pos = (pos + 1) % 100;
                }
                else
                {
                    pos = (pos + 100 - 1) % 100;
                }
                if (pos == 0)
                {
                    result++;
                }
                v--;
            }
        }
        System.out.println(result);
        
    }
}
