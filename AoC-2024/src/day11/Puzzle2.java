package day11;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.LongPair;

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
//        var inputFile = "input1_test1.txt";
//        var inputFile = "input1_test2.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var stones = lines.lineParser(0).listOfLongs();
        var stonesBase = new ArrayList<>(stones);
        long result = 0;
        int targetStep = 75;
        for (var idx = 0; idx < stones.size(); idx++)
        {
            var delta = getNumOfStonesAtStep(stones.get(idx), targetStep);
            result += delta;
        }
        System.out.println(result);
    }

    HashMap<LongPair, Long> knownAnswers = new HashMap<>(); 
    
    long getNumOfStonesAtStep(long stone, int step)
    {
        var pair = new LongPair(stone, step);
        var result = knownAnswers.get(pair);
        if (result != null)
            return result;
        
        if (step == 0)
            return 1;
        
        if (stone == 0)
        {
            result = getNumOfStonesAtStep(1, step-1);
        }
        else
        {
            var numOfDigits = digitsCount(stone); 
            if (numOfDigits % 2 == 0)
            {
                var p1 = getNumOfStonesAtStep(stone / multipliers[numOfDigits/2], step - 1);
                var p2 = getNumOfStonesAtStep(stone % multipliers[numOfDigits/2], step - 1);
                result = p1 + p2;
            }
            else
            {
                result = getNumOfStonesAtStep(stone * 2024l, step-1);
            }
        }
        knownAnswers.put(pair,  result);
        return result;
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
