package day06;

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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var time = parseInt(lines.lineParser(0).skipTill(":").toString().replace(" ", ""));
        var distance = parseLong(lines.lineParser(1).skipTill(":").toString().replace(" ", ""));
        long result = solve(time, distance);
        System.out.println(result);
    }
    
    public long solve(long time, long distance)
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
