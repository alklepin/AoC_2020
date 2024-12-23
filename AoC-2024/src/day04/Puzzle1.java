package day04;

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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        var result = 0;
        for (var startPoint : board.findAllRC(p -> board.getCharAt(p) == 'X'))
        {
            for (var dir : IntPair.ALL_DIRECTIONS)
            {
                if (board.readWordRC(startPoint, dir, 4).equals("XMAS"))
                {
                    result++;
                }
            }
        }
        System.out.println(result);
    }
}
