package day04;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;

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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines); 
        
        Board2D nextBoard;
        Board2D[] current = new Board2D[1];
        current[0] = board;
        int result = 0;
        boolean hasChanges = true;
        while (hasChanges)
        {
            hasChanges = false;
            nextBoard = current[0].clone();
            for (var cell : board.allCellsXY().where(p -> current[0].getAtXY(p) == '@'))
            {
                var num = board.neighbours8XY(cell).where(p -> current[0].getAtXY(p) == '@').count();
                if (num < 4)
                {
    //                board.setAtXY(cell, 'X');
                    result++;
                    hasChanges = true;
                    nextBoard.setAtXY(cell, '.');
                }
            }
            current[0] = nextBoard.clone();
        }
        System.out.println(result);
        
//        board.printAsStrings(System.out);
        
    }
}
