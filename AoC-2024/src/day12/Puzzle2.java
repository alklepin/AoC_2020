package day12;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Perimeters;
import common.graph.ImplicitGraph;
import common.queries.Query;

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
                var inAngle = 0;
                var outAngle = 0;
                for (var d : IntPair.FOUR_DIAGONALS)
                {
                    var nd = c.add(d);
                    var n1 = c.add(IntPair.of(0, d.getY()));
                    var n2 = c.add(IntPair.of(d.getX(), 0));
                    
                    if ((!board.containsRC(n1) || (board.getAtRC(cell) != board.getAtRC(n1)))
                        && (!board.containsRC(n2) || (board.getAtRC(cell) != board.getAtRC(n2)))
                        )
                    {
                        outAngle++;
                    }
                    
                    if (board.containsRC(nd)
                        && (board.getAtRC(cell) == board.getAtRC(n1))
                        && (board.getAtRC(cell) == board.getAtRC(n2))
                        && (board.getAtRC(cell) != board.getAtRC(nd))
                        )
                    {
                        inAngle++;
                    }
                }
                
//                System.out.println("Cell: "+c+" outAngle: "+outAngle + " inAngle: "+inAngle);
                
                fenceCount += inAngle + outAngle;
                count++;
                processed.add(c);
            }
            
            var f1 = Perimeters.sideCount(Query.concat(Query.wrap(cell), searchResult.visited()));
            if (f1 != fenceCount)
            {
                System.out.println("Error "+cell);
            }
//            System.out.println("---");
            result += fenceCount*count;
        }
        
        System.out.println(result);
        
    }
}
