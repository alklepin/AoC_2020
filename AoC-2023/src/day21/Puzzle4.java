package day21;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;

public class Puzzle4 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle4().solve();
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
        var dims = new IntPair(board.getWidth(), board.getHeigth());

        var result = 0;
        var bound = 26501365;
        int dx = bound;
        int dy = 0;
        while (dx > 0)
        {
            var cell = Pair.of(dx, dx).add(startCell);
            var cm = cell.componentModulo(dims);
            if (board.getCharAtXY(cm) != '#')
            {
                result++;
            }
            dx--;
            dy++;
        }
        dx = -bound;
        dy = 0;
        while (dx < 0)
        {
            var cell = Pair.of(dx, dx).add(startCell);
            var cm = cell.componentModulo(dims);
            if (board.getCharAtXY(cm) != '#')
            {
                result++;
            }
            dx++;
            dy--;
        }
        dx = 0;
        dy = bound;
        while (dy > 0)
        {
            var cell = Pair.of(dx, dx).add(startCell);
            var cm = cell.componentModulo(dims);
            if (board.getCharAtXY(cm) != '#')
            {
                result++;
            }
            dx--;
            dy--;
        }
        dx = 0;
        dy = -bound;
        while (dy < 0)
        {
            var cell = Pair.of(dx, dx).add(startCell);
            var cm = cell.componentModulo(dims);
            if (board.getCharAtXY(cm) != '#')
            {
                result++;
            }
            dx++;
            dy++;
        }
        System.out.println(result);
        
    }
}
