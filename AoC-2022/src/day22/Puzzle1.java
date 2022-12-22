package day22;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.geometry.Vect2I;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile, true);
        // System.out.println(groups.size());
        
        var board = Board2D.parseAsCharsXY(groups.get(0));
        
//        board.printAsStrings(System.out);
        
        var board1 = board.clone();
        
        var initX = -1;
        
        for (var x = 0; x < board.getWidth(); x++)
        {
            if (board.getAtXY(x, 0) == '.')
            {
                initX = x;
                break;
            }
        }
        
        var position = Vect2I.of(initX, 0);
        var direction = Vect2I.RIGHT;

        var commandsLine = groups.get(1).line(0).trim();
        var commands = new ArrayList<String>();
        while (commandsLine.length() > 0)
        {
            var parts = parse("(\\d+|[RL])(.*)", commandsLine);
            commands.add(parts[1]);
            commandsLine = parts[2];
        }
        
        Vect2I dims = Vect2I.of(board.getWidth(), board.getHeigth()); 
        for (var c : commands)
        {
            switch (c)
            {
                case "R":
                {
                    direction = direction.rotateLeft();
                    break;
                }
                case "L":
                {
                    direction = direction.rotateRight();
                    break;
                }
                default:
                {
                    var count = parseInt(c);
                    while (count > 0)
                    {
                        count--;
                        var newPos = position.add(direction);
                        if (!board.containsXY(newPos) || board.getAtXY(newPos) == ' ')
                        {
                            newPos = newPos.componentModulo(dims);
                            while (board.getAtXY(newPos) == ' ')
                            {
                                newPos = newPos.add(direction);
                                newPos = newPos.componentModulo(dims);
                            }
                        }
                        if (board.getAtXY(newPos) == '.')
                        {
                            position = newPos;
                            board1.setAtXY(position, '*');
                        }
                        else 
                        {
                            break;
                        }
                    }
                }
            }
        }
        
        System.out.println(position);
        System.out.println(direction);

//        board1.printAsStrings(System.out);
        
        var result = 1000 * (position.getY()+1) + 4 * (position.getX()+1);
        if (direction.equals(Vect2I.LEFT))
            result += 2;
        else if (direction.equals(Vect2I.UP))
            result += 1;
        else if (direction.equals(Vect2I.DOWN))
            result += 3;
        
        System.out.println(result);
        
//        LinesGroup lines = readAllLines(inputFile);
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
    
    
}
