package day15;

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
        
        
        var lineGroups = readAllLineGroups(inputFile, false);
        
        var board = Board2D.parseAsChars(lineGroups.get(0));
        var buf = new StringBuilder();
        for (var line : lineGroups.get(1))
        {
            buf.append(line);
        }
        var movementsStr = buf.toString();
        var movements = movementsStr.toCharArray();
        
        var robot = board.findCharXY('@').first();
        for (var moveChar : movements)
        {
            var dir = IntPair.decodeDirectionVInv(moveChar);
            var next = robot.add(dir);
            
            if (board.getCharAtXY(next) == '.')
            {
                board.setCharAtXY(robot, '.');
                board.setCharAtXY(next, '@');
                robot = next;
            }
            else if (board.getCharAtXY(next) == 'O')
            {
                var toSwap = board.rayXY(next, dir).where(c -> (board.getCharAtXY(c) != 'O')).first();
                if (board.getCharAtXY(toSwap) == '.')
                {
                    board.setCharAtXY(toSwap, 'O');
                    board.setCharAtXY(robot, '.');
                    board.setCharAtXY(next, '@');
                    robot = next;
                }
            }
            
            board.printAsStrings(System.out);
        }
        
        long result = 0;
        for (var c : board.findCharXY('O'))
        {
            result += c.getX() + c.getY()*100;
        }
        
//        LinesGroup lines = readAllLines(inputFile);
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
//        int result = 0;
//        for (String line : lines)
//        {
//        }
        System.out.println(result);
        
    }
}
