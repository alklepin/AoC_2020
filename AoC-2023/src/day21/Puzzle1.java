package day21;

import java.util.HashSet;

import common.LinesGroup;
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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        
        var startCell = board.allCellsXY().where(c -> board.getCharAtXY(c) == 'S').single(0);
        var queue = new HashSet<IntPair>();
        queue.add(startCell);
        var nextQueue = new HashSet<IntPair>();
        for (int idx = 0; idx < 64; idx++)
        {
            for (var node : queue)
            {
                for (var nextNode : board.neighbours4XY(node).where(c -> board.getCharAtXY(c) != '#'))
                {
                    nextQueue.add(nextNode);
                }
            }
            var tmp = queue;
            queue = nextQueue;
            nextQueue = tmp;
            nextQueue.clear();
        }
        
        System.out.println(queue.size());
        
    }
}
