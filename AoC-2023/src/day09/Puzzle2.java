package day09;

import java.util.ArrayList;
import java.util.Collections;

import common.LineParser;
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
        ArrayList<ArrayList<Long>> series = new ArrayList<>();
        for (String line : lines)
        {
            series.add(new LineParser(line).listOfLongs());
        }
        
        long result = 0;
        for (var idx = 0; idx < series.size(); idx++)
        {
            var v = processSeries(series.get(idx));
            result += v;    
        }
        System.out.println(result);
    }

    private long processSeries(ArrayList<Long> list)
    {
        ArrayList<ArrayList<Long>> stack = new ArrayList<>();
        while (!isZeroes(list))
        {
            stack.add(list);
            list = diff(list);
        }
        long p = 0;
        Collections.reverse(stack);
        for (var s : stack)
        {
            p = -p + s.get(0);
        }
        return p;
//        var result = new ArrayList<>();
//        result.addAll(list);
//        result.add(p);
//        return result;
    }
    
    private boolean isZeroes(ArrayList<Long> list)
    {
        for (var v : list)
            if (v != 0)
                return false;
        return true;
    }

    private ArrayList<Long> diff(ArrayList<Long> list)
    {
        var result = new ArrayList<Long>();
        for (var idx = 1; idx < list.size(); idx++)
        {
            result.add(list.get(idx) - list.get(idx-1));
        }
        return result;
    }
}
