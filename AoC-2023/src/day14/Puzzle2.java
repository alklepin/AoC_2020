package day14;

import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

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
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);

        HashMap<Board2D, Integer> cache = new HashMap<>();
        cache.put(board.clone(), 0);
        for (var step = 0; step < 1000000000; step++)
        {
            tiltBoard(board, IntPair.DOWN);
//            board.printAsStrings(System.out);
//            System.out.println();
            tiltBoard(board, IntPair.LEFT);
//            board.printAsStrings(System.out);
//            System.out.println();
            tiltBoard(board, IntPair.UP);
//            board.printAsStrings(System.out);
//            System.out.println();
            tiltBoard(board, IntPair.RIGHT);
//            board.printAsStrings(System.out);
//            System.out.println();
            var s = cache.get(board);
            if (s != null)
            {
                int n = (1000000000 - s) / (step - s);
                step = s + n * (step - s);
//                System.out.println("!"+s + " - " + step);
//                break;
            }
            else
                cache.put(board.clone(), step);
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
    
    public void tiltBoard(Board2D board, IntPair dir)
    {
        if (dir.equals(IntPair.DOWN))
        {
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
        }
        if (dir.equals(IntPair.UP))
        {
            for (var y = board.getHeigth()-1; y >= 0; y--)
            {
                for (var x = 0; x < board.getWidth(); x++)
                {
                    var cell = IntPair.of(x, y);
                    if (board.getCharAtXY(cell) == 'O')
                    {
                        var next = cell.add(IntPair.UP);
                        while (board.containsXY(next) && board.getCharAtXY(next) == '.')
                        {
                            next = next.add(IntPair.UP);
                        }
                        next = next.add(IntPair.DOWN);
                        if (!cell.equals(next)) 
                        {
                            board.setCharAtXY(cell, '.');
                            board.setCharAtXY(next, 'O');
                        }
                    }
                }
            }
        }
        if (dir.equals(IntPair.RIGHT))
        {
            for (var x = board.getWidth()-1; x >= 0; x--)
            {
                for (var y = 0; y < board.getHeigth(); y++)
                {
                    var cell = IntPair.of(x, y);
                    if (board.getCharAtXY(cell) == 'O')
                    {
                        var next = cell.add(IntPair.RIGHT);
                        while (board.containsXY(next) && board.getCharAtXY(next) == '.')
                        {
                            next = next.add(IntPair.RIGHT);
                        }
                        next = next.add(IntPair.LEFT);
                        if (!cell.equals(next)) 
                        {
                            board.setCharAtXY(cell, '.');
                            board.setCharAtXY(next, 'O');
                        }
                    }
                }
            }
        }
        if (dir.equals(IntPair.LEFT))
        {
            for (var x = 0; x < board.getWidth(); x++)
            {
                for (var y = 0; y < board.getHeigth(); y++)
                {
                    var cell = IntPair.of(x, y);
                    if (board.getCharAtXY(cell) == 'O')
                    {
                        var next = cell.add(IntPair.LEFT);
                        while (board.containsXY(next) && board.getCharAtXY(next) == '.')
                        {
                            next = next.add(IntPair.LEFT);
                        }
                        next = next.add(IntPair.RIGHT);
                        if (!cell.equals(next)) 
                        {
                            board.setCharAtXY(cell, '.');
                            board.setCharAtXY(next, 'O');
                        }
                    }
                }
            }
        }
    }
}
