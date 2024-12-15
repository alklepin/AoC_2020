package day15;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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
        
        
        var lineGroups = readAllLineGroups(inputFile, false);
        
        var lines = new ArrayList<String>();
        for (var line : lineGroups.get(0))
        {
            line = line.replace("#", "##").replace(".", "..").replace("O", "[]").replace("@", "@.");
            lines.add(line);
        }
        var board = Board2D.parseAsChars(lines);
        var movementsStr = lineGroups.get(1).joinLines();
        var movements = movementsStr.toCharArray();

        board.printAsStrings(System.out);
        
        var robot = board.findCharXY('@').first();
        for (var moveChar : movements)
        {
            var dir = IntPair.decodeDirectionVInv_XY(moveChar);
            
            var moves = new ArrayList<IntPair>();
            if (canMove(board, robot, dir, moves))
            {
                var processed = new HashSet<IntPair>();
                Collections.reverse(moves);
                for (var c : moves)
                {
                    if (processed.add(c))
                    {
                        var v = c.add(dir);
                        board.setCharAtXY(v, board.getCharAtXY(c));
                        board.setCharAtXY(c, '.');
                    }
                }
                board.setCharAtXY(robot, '.');
                robot = robot.add(dir);
            }
            
            
            board.printAsStrings(System.out);
        }
        
        long result = 0;
        for (var c : board.findCharXY('['))
        {
            result += c.getX() + c.getY()*100;
        }
        
        System.out.println(result);
        
    }
    
    private static boolean canMove(Board2D board, IntPair cell, IntPair dir, ArrayList<IntPair> movements)
    {
        movements.add(cell);
        var next = cell.add(dir);
        var nextChar = board.getCharAtXY(next); 
        if (nextChar == '.')
            return true;
        if (nextChar == '#')
            return false;
        
        if (dir.isHorizontal())
        {
            movements.add(next);
            return canMove(board, next.add(dir), dir, movements);
        }
        else
        {
            if (nextChar == '[')
                return canMove(board, next, dir, movements) 
                    && canMove(board, next.add(IntPair.RIGHT), dir, movements);
            if (nextChar == ']')
                return canMove(board, next, dir, movements) 
                    && canMove(board, next.add(IntPair.LEFT), dir, movements);
        }
        throw new IllegalStateException();
    }
}
