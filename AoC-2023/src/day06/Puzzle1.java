package day06;

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
        var times = lines.lineParser(0).skipTill(":").listOfInts();
        var distance = lines.lineParser(1).skipTill(":").listOfInts();
        var results = new int[times.size()];
        for (var idx = 0; idx < results.length; idx++)
        {
            results[idx] = solve(times.get(idx), distance.get(idx));
        }
        int result = 1;
        for (var idx = 0; idx < results.length; idx++)
        {
            result *= results[idx];
        }
        System.out.println(result);
    }
    
    public int solve(int time, int distance)
    {
        var result = 0;
        for (var pushTime = 0; pushTime < time; pushTime++)
        {
            var d = (time - pushTime) * pushTime;
            if (d > distance)
                result++;
        }
        return result;
    }
}
