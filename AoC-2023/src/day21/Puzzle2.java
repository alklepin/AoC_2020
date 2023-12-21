package day21;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

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
//        var inputFile = "input1_test2.txt";
//        var inputFile = "input1_test4.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        
        var startCell = board.allCellsXY().where(c -> board.getCharAtXY(c) == 'S').single(0);
        var queue = new HashSet<IntPair>();
        queue.add(startCell);
        var visited = new HashMap<IntPair, Integer>();
        var dirs = new IntPair[] {IntPair.UP, IntPair.DOWN, IntPair.LEFT, IntPair.RIGHT};
        var dims = new IntPair(board.getWidth(), board.getHeigth());
//        for (int idx = 0; idx < 26501365; idx++)
//        var bound = 196;
//        var bound = 327;
        var bound = 1113;
        for (int idx = 1; idx <= bound; idx++)
        {
            System.out.println("Step: "+idx + " Size: "+visited.size() + " QSize: "+queue.size());
            
            var nextQueue = new HashSet<IntPair>();
            for (var node : queue)
            {
                for (var dir : dirs)
                {
                    var nextNode = node.add(dir);
                    var testNode = nextNode.componentModulo(dims);
                    if (board.getCharAtXY(testNode) != '#')
                    {
                        if (!visited.containsKey(nextNode))
                        {
                            nextQueue.add(nextNode);
                            visited.put(nextNode, idx);
                        }
                    }
                }
            }
            queue = nextQueue;

            var r = 0;
            for (var e : visited.entrySet())
            {
                var s = e.getValue();
                if (s % 2 == idx % 2)
                    r++;
            }
            System.out.println(r);
        }
        
        var result = 0;
        for (var e : visited.entrySet())
        {
            var s = e.getValue();
            if (s % 2 == bound % 2)
                result++;
        }
        System.out.println(result);
        
    }
}
