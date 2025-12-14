package day06;

import java.nio.channels.IllegalSelectorException;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    enum Operation
    {
        TurnOn, TurnOff, Toggle;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input2_test.txt";

        var board = new Board2D(1000, 1000);
        board.fill(0);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        for (String line : lines)
        {
            var parts = line.split("turn on|turn off|toggle|through");
            Operation op;
            if (line.startsWith("turn on"))
                op = Operation.TurnOn;
            else if (line.startsWith("turn off"))
                op = Operation.TurnOff;
            else if (line.startsWith("toggle"))
                op = Operation.Toggle;
            else
                throw new IllegalStateException();
            var start = IntPair.from(parts[1].trim(), ",");
            var end = IntPair.from(parts[2].trim(), ",");
            for (var y = start.getY(); y <= end.getY(); y++)
            {
                for (var x = start.getX(); x <= end.getX(); x++)
                {
                    switch (op)
                    {
                        case Toggle:
                        {
                            var c = board.getAtXY(x, y);
                            board.setAtXY(x, y, c+2);
                            break;
                        }
                        case TurnOff:
                        {
                            var c = board.getAtXY(x, y);
                            board.setAtXY(x, y, Math.max(c-1, 0));
                            break;
                        }
                        case TurnOn:
                        {
                            var c = board.getAtXY(x, y);
                            board.setAtXY(x, y, c+1);
                            break;
                        }
                        default:
                            break;
                        
                    }
                }
            }
//            board.printAsStrings(System.out);
//            System.out.println("----");
        }
        long result = 0;
        for (var c : board.allCellsXY())
        {
            result += board.getAtXY(c);
        }
        System.out.println(result);
        
    }
}
