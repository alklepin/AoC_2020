package day13;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;

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
        
        LinesGroup lines = readAllLines(inputFile);
        var groups = lines.split("\\s*");
        int result = 0;
        for (var group : groups)
        {
            Board2D board = Board2D.parseAsChars(group);
            result += processBoard(board);
        }
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
//        int result = 0;
//        for (String line : lines)
//        {
//        }
        System.out.println(result);
        
    }
    
    public int processBoard(Board2D board)
    {
        var result = 0;
        for (var x = 0; x < board.getWidth(); x++)
        {
            if (isMirroredAfterCol(board, x))
            {
                result += x + 1;
//                System.out.println("After col: "+(x));
                System.out.println("After col: "+(x+1));
                break;
            }
        }
        for (var y = 0; y < board.getHeigth(); y++)
        {
            if (isMirroredAfterRow(board, y))
            {
                result += (y + 1)*100;
//                System.out.println("After row: "+(y));
                System.out.println("After row: "+(y+1));
                break;
            }
        }
        return result;
    }
    
    public boolean isMirroredAfterCol(Board2D board, int x)
    {
        int range = Math.min(x + 1, board.getWidth() - x - 1);
        if (range == 0)
            return false;
        var isMirrored = true;
        for (var idx = 0; idx < range && isMirrored; idx++)
        {
            for (var y = 0; y < board.getHeigth(); y++)
            {
                if (board.getCharAtXY(x - idx, y) != board.getCharAtXY(x + 1 + idx, y))
                {
                    isMirrored = false;
                    break;
                }
            }
        }
        return isMirrored;
    }

    public boolean isMirroredAfterRow(Board2D board, int y)
    {
        int range = Math.min(y + 1, board.getHeigth() - y - 1);
        if (range == 0)
            return false;
        var isMirrored = true;
        for (var idx = 0; idx < range && isMirrored; idx++)
        {
            for (var x = 0; x < board.getWidth(); x++)
            {
                if (board.getCharAtXY(x, y - idx) != board.getCharAtXY(x, y + 1 + idx))
                {
                    isMirrored = false;
                    break;
                }
            }
        }
        return isMirrored;
    }
}
