package day10;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
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
        Board2D board = Board2D.parseAsCharsXY(lines);
        IntPair start = null;
        for (var cell : board.allCellsXY())
        {
            if (board.getCharAtXY(cell) == 'S')
            {
                start = cell;
                break;
            }
        }
        
        var result = 0;
        var searchResult = ImplicitGraph.DFS(start, null, node -> nextCells(board, node));
        for (var end : nextCells(board, start))
        {
            if (searchResult.visited().contains(end))
            {
                var path = searchResult.getPath(end);
                result = Math.max(result, (path.size() + 1) / 2);
            }
        }
        
        System.out.println(result);
        
    }
    
    
    
    public Iterable<IntPair> nextCells(Board2D board, IntPair cell)
    {
        ArrayList<IntPair> moves = new ArrayList<>();
        var current = board.getCharAtXY(cell);
        
        IntPair next; 

        next = cell.add(IntPair.DOWN);
        if (board.containsXY(next) && "S|LJ".indexOf(current) >= 0 && "S|7F".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }

        next = cell.add(IntPair.UP);
        if (board.containsXY(next) && "S|7F".indexOf(current) >= 0 && "S|LJ".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }

        next = cell.add(IntPair.LEFT);
        if (board.containsXY(next) && "S-J7".indexOf(current) >= 0 && "S-LF".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }

        next = cell.add(IntPair.RIGHT);
        if (board.containsXY(next) && "S-LF".indexOf(current) >= 0 && "S-7J".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }
        return moves;
    }
}
