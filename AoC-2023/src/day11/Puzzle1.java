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
        board = Board2D.parseAsCharsXY(lines);
        
        for (var x = 0; x < board.getWidth(); x++)
        {
            var isEmpty = Query.wrap(board.colCellsXY(x))
                .all(cell -> board.getCharAtXY(cell) != '#');
            if (isEmpty)
                board.fillColumnXY(x, '+');
        }

        for (var y = 0; y < board.getHeigth(); y++)
        {
            var isEmpty = Query.wrap(board.rowCellsXY(y))
                .all(cell -> board.getCharAtXY(cell) != '#');
            if (isEmpty)
                board.fillRowXY(y, '+');
        }
        
        HashSet<IntPair> galaxies = Query.wrap(
            board.findAllXY(cellId -> (board.getCharAtXY(cellId) == '#')))
            .toSet();
        
        int result = 0;
        for (var source : galaxies)
        {
            var searchState = ImplicitGraph.DijkstraLong(source, null, this::nextNodes);
            for (var galaxy : galaxies)
            {
                result += searchState.distanceTo(galaxy);
            }
        }
        
//        board.printAsStrings(System.out);
        
        System.out.println(result/2);
        
    }
    
    public Iterable<SearchState<IntPair, Long>> nextNodes(SearchState<IntPair, Long> current)
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
