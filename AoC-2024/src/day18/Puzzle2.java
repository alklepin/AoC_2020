package day18;

import java.util.ArrayList;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
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
            var dims = new IntPair(71, 71);
            var bytesCount= 1024;
//        var inputFile = "input1_test.txt";
//        var dims = new IntPair(7, 7);
//        var bytesCount= 12;
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<IntPair> bytes = new ArrayList<>();
        for (var line : lines.parsers())
        {
            var data = line.listOfInts();
            var pair = IntPair.of(data.get(0), data.get(1));
            bytes.add(pair);
        }
        var start = new IntPair(0,0);
        var end = dims.add(IntPair.of(-1, -1));
        Board2D board = new Board2D(dims.getX(), dims.getY());
        var limit = bytesCount;
        for (limit = bytesCount; limit < bytes.size(); limit++)
        {
            HashSet<IntPair> occupied = new HashSet<>();
            for (var idx = 0; idx < Math.min(bytes.size(), limit); idx++)
            {
                occupied.add(bytes.get(idx));
            }
            var searchResult = ImplicitGraph.BFS(start, end, node -> board.neighbours4XY(node).where(n -> !occupied.contains(n)));
            var path = searchResult.getPath();
            if (searchResult.getPath() == null)
            {
                break;
            }

            limit++;
            var visited = new HashSet<>(path);
            while (limit < bytes.size() && !visited.contains(bytes.get(limit-1)))
            {
                limit++;
            }
            limit--;
        }

        System.out.println(limit);
        System.out.println(bytes.get(limit-1));
        
    }
}
