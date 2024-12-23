package day15;

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
        String line = lines.get(0);
        var parts = line.split(",");
        long result = 0;
        for (var part : parts)
        {
            result += hash(part);
        }
        System.out.println(result);
    }
    
    public long hash(String s)
    {
        int result = 0;
        for (var c : s.toCharArray())
        {
            result += c;
            result = (result * 17) % 256;
        }
        return result;
    }
}
