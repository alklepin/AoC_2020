package day14;

import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.algebra.LoopDetector;
import common.boards.Board2D;
import common.boards.IntPair;

public class Puzzle2_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_1().solve();
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
//        var r = LoopDetector.findLoopWithCache(1, n -> 
//        {
//            return switch (n)
//            {
//                case 1 -> 2;
//                case 2 -> 3;
//                case 3 -> 4;
//                case 4 -> 5;
//                case 5 -> 6;
//                case 6 -> 3;
//                default -> throw new IllegalStateException();
//            };
//        }, 6);
//        
//        System.out.println(r);
//        System.exit(0);

//        var r = LoopDetector.simulateWithCache(1, n -> 
//        {
//            return switch (n)
//            {
//                case 1 -> 2;
//                case 2 -> 3;
//                case 3 -> 4;
//                case 4 -> 5;
//                case 5 -> 6;
//                case 6 -> 3;
//                default -> throw new IllegalStateException();
//            };
//        }, 3);
//        
//        System.out.println(r);
//        System.exit(0);
        
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);

        var res = LoopDetector.simulateWithCache(board, b -> 
        {
           b = b.clone();
           tiltBoard(b, IntPair.DOWN);
           tiltBoard(b, IntPair.LEFT);
           tiltBoard(b, IntPair.UP);
           tiltBoard(b, IntPair.RIGHT);
           return b;
        }, 1000000000);
        
        
        res.printAsStrings(System.out);
        
        long result = 0;
        for (var cell : res.allCellsXY())
        {
            if (res.getCharAtXY(cell) == 'O')
            {
                result += (res.getHeigth() - cell.getY());
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
