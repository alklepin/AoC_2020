package day07;

import java.util.ArrayList;
import java.util.HashSet;

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
        long result = 0;
        for (var line : lines.parsers())
        {
            long testValue = parseLong(line.segment("[:\\s]").toString());
            var values = line.skipTill("[:]").listOfInts("[:\\s]");
            if (canCombinev2(values, testValue))
            {
//                if (!canCombine(values, testValue))
//                {
//                    System.out.println(line.toString());
//                }
                result += testValue;
            }
        }
        System.out.println(result);
        
        for (var i = 0; i <= 10000; i++)
        {
            System.out.println(""+i+" -> "+countVultiplier(i));
        }
        
    }

    private boolean canCombinev2(ArrayList<Integer> values, long testValue)
    {
        var step = new HashSet<Long>();
        step.add(values.get(0).longValue());
        for (var idx = 1; idx < values.size(); idx++)
        {
            long nextValue = values.get(idx);
            var nextStep = new HashSet<Long>();
            for (var v : step)
            {
                long op1 = v.longValue();
                long res = nextValue + op1;
                if (res > 0 && res <= testValue)
                    nextStep.add(res);
                
                res = nextValue * op1;
                if (res > 0 && res <= testValue)
                    nextStep.add(res);
                
                res = op1*countVultiplier(nextValue) + nextValue;
                if (res > 0 && res <= testValue)
                {
                    nextStep.add(res);
                }
            }
            step = nextStep;
        }
        return step.contains(testValue);
    }
    
    private long countVultiplier(long value)
    {
        long res = 10;
        while (value >= 10)
        {
            res *= 10;
            value /= 10;
        }
        return res;
    }
}
