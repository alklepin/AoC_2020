package day10;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.Graph;
import common.graph.ImplicitGraph;

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
        var result = 0l;
        for (var cell : board.allCellsRC())
        {
            if (board.getCharAt(cell) == '0')
            {
                var score = 0;
                var searchResult = ImplicitGraph.BFS(cell, null, 
                    cur -> board.neighbours4RC(cur)
                        .where(next -> board.getCharAt(next) == board.getCharAt(cur)+1));
                for (var c : searchResult.visited())
                {
                    if (board.getCharAt(c) == '9')
                    {
                        score++;
                    }
                }
                result += score; 
            }
        }
        
        System.out.println(result);
        
    }
}
