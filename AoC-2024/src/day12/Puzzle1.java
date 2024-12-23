package day12;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Perimeters;
import common.graph.ImplicitGraph;
import common.queries.Query;

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
        var board = Board2D.parseAsChars(lines);
        
        
        long area = 0;
        long perimiter = 0;
        
        var processed = new HashSet<IntPair>();
        int result = 0;
        for (var cell : board.allCellsRC())
        {
            if (processed.contains(cell))
                continue;
            var searchResult = ImplicitGraph.BFS(cell, null, 
                c -> board.neighbours4RC(c).where(next -> board.getAtRC(cell)==board.getAtRC(next)));
            var count = 0;
            var fenceCount = 0;
            for (var c : Query.concat(Query.wrap(cell), searchResult.visited()))
            {
                for (var dir : IntPair.FOUR_CROSS_DIRECTIONS)
                {
                    var next = c.add(dir);
                    if (!board.containsRC(next)
                        || (board.getAtRC(cell) != board.getAtRC(next))
                        )
                    {
                        fenceCount++;
                    }
                }
                count++;
                processed.add(c);
            }
            
            result += fenceCount*count;
        }
        
        System.out.println(result);
        
    }
}
