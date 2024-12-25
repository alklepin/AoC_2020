package day25;

import java.util.ArrayList;

import common.PuzzleCommon;
import common.boards.Board2D;
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
        var lineGroups = readAllLineGroups(inputFile); 

        
        var keys = new ArrayList<ArrayList<Integer>>();
        var locks = new ArrayList<ArrayList<Integer>>();
        
        for (var group : lineGroups)
        {
            var isKey = group.line(0).contains(".");
            var board = Board2D.parseAsCharsXY(group);
            var data = new ArrayList<Integer>();
            for (var idx = 0; idx < board.getWidth(); idx++)
            {
                var sharpCount = board.rayXY(IntPair.of(idx, 0), IntPair.UP)
                    .where(cell -> board.getCharAtXY(cell) == '#').count();
                if (isKey)
                    sharpCount--;
                data.add(sharpCount);
            }
            if (isKey)
                keys.add(data);
            else
                locks.add(data);
        }
        
        
        int result = 0;
        for (var lock : locks)
        {
            for (var key : keys)
            {
                var overlaps = false;
                for (var idx = 0; idx < key.size(); idx++)
                {
                    if (lock.get(idx) + key.get(idx) > 5)
                    {
                        overlaps = true;
                        break;
                    }
                }
                if (!overlaps)
                    result++;
            }
        }
        System.out.println(result);
        
    }
}
