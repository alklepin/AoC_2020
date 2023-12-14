package day14;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

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
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        for (var y = 0; y < board.getHeigth(); y++)
        {
            for (var x = 0; x < board.getWidth(); x++)
            {
                var cell = IntPair.of(x, y);
                if (board.getCharAtXY(cell) == 'O')
                {
                    var next = cell.add(IntPair.DOWN);
                    while (board.containsXY(next) && board.getCharAtXY(next) == '.')
                    {
                        next = next.add(IntPair.DOWN);
                    }
                    next = next.add(IntPair.UP);
                    if (!cell.equals(next)) 
                    {
                        board.setCharAtXY(cell, '.');
                        board.setCharAtXY(next, 'O');
                    }
                }
            }
        }
        board.printAsStrings(System.out);
        
        long result = 0;
        for (var cell : board.allCellsXY())
        {
            if (board.getCharAtXY(cell) == 'O')
            {
                result += (board.getHeigth() - cell.getY());
            }
        }        
        System.out.println(result);
        
    }
}
