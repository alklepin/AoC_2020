package day11;

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
    
    static long [] multipliers; 
    
    public void solve()
        throws Exception
    {
        multipliers = new long[20];
        var m = 1; 
        for (var idx = 0; idx < multipliers.length; idx++)
        {
            multipliers[idx] = m;
            m *= 10;
        }
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var stones = lines.lineParser(0).listOfLongs();
        var stonesBase = new ArrayList<>(stones);
        for (var step = 0; step < 25; step++)
        {
            for (var idx = 0; idx < stones.size(); idx++)
            {
                var stone = stones.get(idx);
                if (stone == 0)
                {
                    stones.set(idx, 1l);
                    continue;
                }
                var numOfDigits = digitsCount(stone); 
                if (numOfDigits % 2 == 0)
                {
                    stones.set(idx, stone / multipliers[numOfDigits/2]);
                    stones.add(idx+1, stone % multipliers[numOfDigits/2]);
                    idx++;
                    continue;
                }
                stones.set(idx, stone * 2024l);
            }
        }
        System.out.println(stones.size());
        
    }

    private int digitsCount(long value)
    {
        int res = 1;
        while (value >= 10)
        {
            res++;
            value /= 10;
        }
        return res;
    }

}
