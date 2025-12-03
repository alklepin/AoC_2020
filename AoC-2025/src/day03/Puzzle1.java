package day03;

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
    
    public int maxJoltage(String bank)
    {
        char[] bankChars = bank.toCharArray();
        var max = -1;
        for (var i = 0; i < bankChars.length; i++)
        {
            for (var j = i+1; j < bankChars.length; j++)
            {
                var joltage = (bankChars[i] - '0') * 10 + bankChars[j] - '0';
                if (joltage > max)
                    max = joltage;
            }
        }
        return max;
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
