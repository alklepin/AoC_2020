package day11;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import common.graph.ImplicitGraph.SearchState;
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
    
    Board2D board;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D sourceboard = Board2D.parseAsCharsXY(lines);
        board = sourceboard.clone();
        
        for (var x = 0; x < board.getWidth(); x++)
        {
            var isEmpty = Query.wrap(board.colCellsXY(x))
                .all(cell -> board.getCharAtXY(cell) != '#');
            if (isEmpty)
            {
                for (var cell : board.colCellsXY(x))
                    board.setCharAtXY(cell, '#');                
            }
//            var isEmpty = true;
//            for (var y = 0; y < board.getHeigth(); y++)
//            {
//                if (board.getCharAtXY(x, y) == '#')
//                {
//                    isEmpty = false;
//                    break;
//                }
//            }
//            if (isEmpty)
//            {
//                for (var y = 0; y < board.getHeigth(); y++)
//                {
//                    board.setCharAtXY(x, y, '+');
//                }
//            }
        }

        for (var y = 0; y < board.getHeigth(); y++)
        {
            var isEmpty = Query.wrap(board.rowCellsXY(y))
                .all(cell -> board.getCharAtXY(cell) != '#');
            if (isEmpty)
            {
                for (var cell : board.rowCellsXY(y))
                    board.setCharAtXY(cell, '#');                
            }
//            var isEmpty = true;
//            for (var x = 0; x < board.getWidth(); x++)
//            {
//                if (board.getCharAtXY(x, y) == '#')
//                {
//                    isEmpty = false;
//                    break;
//                }
//            }
//            if (isEmpty)
//            {
//                for (var x = 0; x < board.getWidth(); x++)
//                {
//                    board.setCharAtXY(x, y, '+');
//                }
//            }
            
        }
        HashSet<IntPair> galaxies = new HashSet<>();
        for (var cellId : board.allCellsXY())
        {
            if (board.getCharAtXY(cellId) == '#')
                galaxies.add(cellId);
        }
        
        int result = 0;
        for (var source : galaxies)
        {
            var searchState = ImplicitGraph.Dijkstra(SearchState.of(source, 0), null, this::nextNodes);
            for (var galaxy : galaxies)
            {
                result += searchState.distanceTo(galaxy);
            }
        }
        
//        board.printAsStrings(System.out);
        
        System.out.println(result/2);
        
    }
    
    public Iterable<SearchState<IntPair, Integer>> nextNodes(SearchState<IntPair, Integer> current)
    {
        return Query.wrap(board.neighbours4XY(current.getNode()))
            .select((n) -> 
                SearchState.of(n, 
                    switch (board.getCharAtXY(n))
                    {
                        case '.' -> 1 + current.getDistance();
                        case '#' -> 1 + current.getDistance();
                        case '+' -> 2 + current.getDistance();
                        default -> throw new IllegalStateException();
                    })
            );
    }
    
}
