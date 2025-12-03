package day03;

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
    
    public long maxJoltage(String bank)
    {
        long[] current = new long[13];
        char[] bankChars = bank.toCharArray();
        for (var i = 0; i < bankChars.length; i++)
        {
            long[] next = new long[13];
            var digit = bankChars[i] - '0';
            for (var j = 1; j < current.length; j++)
            {
                var joltage = current[j-1] * 10 + digit;
                next[j] = Math.max(current[j], joltage);
            }
            current = next;
        }
        return current[12];
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        long result = 0;
        for (String line : lines)
        {
            var joltage = maxJoltage(line);
            result += joltage;
        }
        System.out.println(result);
        
    }
}
