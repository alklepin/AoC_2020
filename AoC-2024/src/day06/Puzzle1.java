package day06;

import java.util.HashSet;

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
        
        LinesGroup lines = readAllLines(inputFile);
        var board = Board2D.parseAsCharsXY(lines);
        var guardStart = board.findCharXY('^').first();
        var guardDir = IntPair.DOWN;

        System.out.println("guard*:"+guardStart);
        System.out.println("guardDir*:"+guardDir);
        
        board.setAtXY(guardStart, '.');
        var visited = new HashSet<IntPair>();
        visited.add(guardStart);
        var stepsCount = 0;
        var guard = guardStart; 
        while (board.containsXY(guard))
        {
            var nextDir = guardDir;
            for (var attempt = 0; attempt < 4; attempt++)
            {
                var next = guard.add(nextDir);
                if (board.containsXY(next))
                {
                    if (board.getCharAtXY(next) == '.')
                    {
                        stepsCount++;
                        guard = next;
                        guardDir = nextDir;
                        visited.add(guard);

                        System.out.println("guard:"+guard);
                        System.out.println("guardDir:"+guardDir);
                        break;
                    }
                    else
                    {
                        nextDir = nextDir.rotateAntiClockwise90();
                        System.out.println("rotate");
                    }
                }
                else
                {
                    guard = next;
                    break;
                }
            }
        }
        
        System.out.println(visited.size());
        System.out.println(stepsCount);
        
    }
}
