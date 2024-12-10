package day10;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.Graph;
import common.graph.ImplicitGraph;

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
        var board = Board2D.parseAsChars(lines);
        long result = 0l;
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
                        var counts = new HashMap<IntPair, Integer>();
                        counts.put(c, 1);
                        
                        var queue = new LinkedList<IntPair>();
                        queue.add(c);
                        
                        while (queue.size() > 0)
                        {
                            var cur = queue.poll();
                            for (var prev : board.neighbours4RC(cur)
                                .where(prev -> (searchResult.visited().contains(prev) || searchResult.starts().contains(prev))
                                    && board.getCharAt(prev) == board.getCharAt(cur)-1))
                            {
                                var count = counts.get(prev);
                                count = ((count == null) ? 0 : count) + counts.get(cur);
                                counts.put(prev, count);
                                if (!queue.contains(prev))
                                    queue.add(prev);
                            }
                        }

                        var scoreDelta = counts.get(cell);
                        score += scoreDelta;
                            
                    }
                }
//                System.out.println(""+cell+" -> "+score);
                result += score; 
            }
        }
        
        System.out.println(result);
        
    }
}
