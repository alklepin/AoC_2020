package day01;

import java.util.ArrayList;
import java.util.Collections;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

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
        
        ArrayList<IntPair> first = new ArrayList<>();
        ArrayList<IntPair> second = new ArrayList<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int idx = 0;
        for (String line : lines)
        {
            var parts = line.split("\\s+");
            var pair1 = IntPair.of(parseInt(parts[0]), idx);
            var pair2 = IntPair.of(parseInt(parts[1]), idx);
            first.add(pair1);
            second.add(pair2);
            idx++;
        }
        
        Collections.sort(first, Puzzle1::comparer);
        Collections.sort(second, Puzzle1::comparer);
        
        
        long result = 0;
        for (idx = 0; idx < first.size(); idx++)
        {
            result += Math.abs(first.get(idx).getX() - second.get(idx).getX());
        }
        
        System.out.println(result);
        
    }
    
    public static int comparer(IntPair p1, IntPair p2)
    {
        return Integer.compare(p1.getX(), p2.getX());
    }
}
