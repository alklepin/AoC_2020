package day18;

import java.util.ArrayList;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
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
            var dims = new IntPair(71, 71);
            var bytesCount= 1024;
//        var inputFile = "input1_test.txt";
//        var dims = new IntPair(7, 7);
//        var bytesCount= 12;
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<IntPair> bytes = new ArrayList<>();
        int result = 0;
        for (var line : lines.parsers())
        {
            var data = line.listOfInts();
            var pair = IntPair.of(data.get(0), data.get(1));
            bytes.add(pair);
        }
        var start = new IntPair(0,0);
        var end = dims.add(IntPair.of(-1, -1));
        HashSet<IntPair> occupied = new HashSet<>();
        for (var idx = 0; idx < Math.min(bytes.size(), bytesCount); idx++)
        {
            occupied.add(bytes.get(idx));
        }
        Board2D board = new Board2D(dims.getX(), dims.getY());
        var searchResult = ImplicitGraph.BFS(start, end, node -> board.neighbours4XY(node).where(n -> !occupied.contains(n)));
        
        board.setAll('.');
        for (var n : occupied)
        {
            if (board.containsXY(n))
                board.setCharAtXY(n, '#');
        }
        
//        for (var n : searchResult.visited())
//        {
//            board.setCharAtXY(n, 'O');
//        }

//        for (var n : searchResult.getPath())
//        {
//            board.setCharAtXY(n, 'O');
//        }
        
        board.printAsStrings(System.out);
        
        System.out.println(searchResult.getPath().size()-1);
        
    }
}
