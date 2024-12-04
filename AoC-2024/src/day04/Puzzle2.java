package day04;

import java.util.HashSet;

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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        var result = 0;
        for (var startPoint : board.findAllRC(p -> board.getCharAt(p) == 'A'))
        {
            for (var p1 : board.neighbours4DiagRC(startPoint))
            {
                for (var p2 : board.neighbours4DiagRC(startPoint))
                {
                    var dir1 = startPoint.minus(p1);
                    var dir2 = startPoint.minus(p2); 
                    if (dir1.isOrthogonalTo(dir2))
                    {
                        var w1 = board.readWordRC(p1, dir1, 3);
                        var w2 = board.readWordRC(p2, dir2, 3);
                        if (w1.equals("MAS") && w2.equals("MAS"))
                        {
//                                System.out.println("Found at: "+startPoint);
                            result++;
                        }
                    }
                }
            }
        }
        
        System.out.println(result/2);
//        board1.printAsStrings(System.out);
    }
}
