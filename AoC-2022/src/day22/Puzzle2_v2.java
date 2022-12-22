package day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.geometry.Vect2I;

public class Puzzle2_v2 extends PuzzleCommon
{

    public static Vect2I UP = Vect2I.DOWN;
    public static Vect2I DOWN = Vect2I.UP;
    public static Vect2I RIGHT = Vect2I.RIGHT;
    public static Vect2I LEFT = Vect2I.LEFT;
    
    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_v2().solve();
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
        var direction = RIGHT;

        var commandsLine = groups.get(1).line(0).trim();
        var commands = new ArrayList<String>();
        while (commandsLine.length() > 0)
        {
            var parts = parse("(\\d+|[RL])(.*)", commandsLine);
            commands.add(parts[1]);
            commandsLine = parts[2];
        }
        
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
                    var savedCount = count;
                    var savedPos = position;
                    var savedDir = direction;
                    var translated = false;
                    while (count > 0)
                    {
                        count--;
                        var newPos = position.add(direction);
                        var newState = new State(newPos, direction, false);
                        if (!board.containsXY(newPos) || board.getAtXY(newPos) == ' ')
                        {
                            newState = translate(newPos, position, direction, board);
                            translated = newState.doTrace;
                        }
                        if (board.getAtXY(newState.position) == '.')
                        {
                            board1.setAtXY(position, dirChar(direction));
                            direction = newState.direction;
                            position = newState.position;
                            board1.setAtXY(position, '@');
                        }
                        else 
                        {
                            break;
                        }
                    }
                    if (translated)
                    {
                        System.out.printf("From %s in dir %s step count %s \n", savedPos, savedDir, savedCount);
                        board1.printAsStrings(System.out);
                        System.out.println("==========================");
                        System.out.println("==========================");
                        board1 = board.clone();
                    }
                }
            }
        }
        
        System.out.println(position);
        System.out.println(direction);

//        board1.printAsStrings(System.out);
        
        var result = 1000 * (position.getY()+1) + 4 * (position.getX()+1);
        if (direction.equals(LEFT))
            result += 2;
        else if (direction.equals(DOWN))
            result += 1;
        else if (direction.equals(UP))
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
    
    private static char dirChar(Vect2I dir)
    {
        if (dir.equals(UP))
            return '^';
        if (dir.equals(DOWN))
            return 'v';
        if (dir.equals(LEFT))
            return '<';
        if (dir.equals(RIGHT))
            return '>';
        throw new IllegalStateException();
    }

    class State
    {
        Vect2I position;
        Vect2I direction;
        boolean doTrace;
        
        public State(Vect2I position, Vect2I direction, boolean doTrace)
        {
            this.position = position;
            this.direction = direction;
            this.doTrace = doTrace;
        }
    }
    
    HashSet<IntPair> tracedTransitions = new HashSet<>();
    
    private State translate(Vect2I newPos, Vect2I position, Vect2I direction,
        Board2D board)
    {
        // cube layout:
        //  XX
        //  X 
        // XX 
        // X  
 
        //  01
        //  2 
        // 43 
        // 5  
        
        
        var x1 = newPos.getX() < 0 ? -1 : newPos.getX() / 50;
        var y1 = newPos.getY() < 0 ? -1 : newPos.getY() / 50;
        var tstate = Pair.of(x1, y1);
        var doTrace = tracedTransitions.add(tstate);
        
        if (x1 == -1)
        {
            if (y1 == 2)
            {   
                if (direction.equals(LEFT))
                {   // 4 -> 0
                    return new State(
                        new Vect2I(newPos.getX()+1+50, 49 - (newPos.getY() - 100) + 0),
                        RIGHT, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            if (y1 == 3)
            {
                if (direction.equals(LEFT))
                {   // 5 -> 0
                    return new State(
                        new Vect2I(newPos.getY() - 150 + 50, newPos.getX() + 1),
                        DOWN, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
        }
        if (x1 == 0)
        {
            if (y1 == 0)
            {
                if (direction.equals(LEFT))
                {   // 0 -> 4
                    return new State(
                        new Vect2I(newPos.getX()+1 - 50, 49 - (newPos.getY() - 0)  + 100),
                        RIGHT, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            if (y1 == 1)
            {
                if (direction.equals(LEFT))
                {   // 2 -> 4
                    return new State(
                        new Vect2I(newPos.getY() - 50, newPos.getX() + 1 + 50),
                        DOWN, doTrace);
                }
                if (direction.equals(UP))
                {   // 4 -> 2
                    return new State(
                        new Vect2I(newPos.getY() + 1 - 50, newPos.getX() + 50),
                        RIGHT, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            if (y1 == 4)
            {
                if (direction.equals(DOWN))
                {   // 5 -> 1
                    return new State(
                        new Vect2I(newPos.getX() + 100, newPos.getY() - 200),
                        DOWN, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
        }
        if (x1 == 1)
        {
            if (y1 == -1)
            {
                if (direction.equals(UP))
                {   // 0 -> 5
                    return new State(
                        new Vect2I(newPos.getY() + 1, newPos.getX() - 50 + 150),
                        RIGHT, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            if (y1 == 3)
            {
                if (direction.equals(DOWN))
                {   // 3 -> 5
                    return new State(
                        new Vect2I(newPos.getY()-1 - 100, newPos.getX() + 100),
                        LEFT, doTrace);
                }
                if (direction.equals(RIGHT))
                {   // 5 -> 3
                    return new State(
                        new Vect2I(newPos.getY() - 100, newPos.getX() - 1 + 100),
                        UP, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
        }
        if (x1 == 2)
        {
            if (y1 == -1)
            {
                if (direction.equals(UP))
                {   // 1 -> 5
                    return new State(
                        new Vect2I(newPos.getX() - 100, newPos.getY() + 200),
                        UP, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            if (y1 == 1)
            {
                if (direction.equals(DOWN))
                {   // 1 -> 2
                    return new State(
                        new Vect2I(newPos.getY() - 1 + 50, newPos.getX() - 50),
                        LEFT, doTrace);
                }
                if (direction.equals(RIGHT))
                {   // 2 -> 1
                    return new State(
                        new Vect2I(newPos.getY() + 50, newPos.getX() - 1 - 50),
                        UP, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            if (y1 == 2)
            {
                if (direction.equals(RIGHT))
                {   // 3 -> 1
                    return new State(
                        new Vect2I(newPos.getX()-1 + 50, 49 - (newPos.getY() - 100) + 0),
                        LEFT, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
        }
        if (x1 == 3)
        {
            if (y1 == 0)
            {
                if (direction.equals(RIGHT))
                {   // 1 -> 3
                    return new State(
                        new Vect2I(newPos.getX()-1 - 50, 49 - (newPos.getY() - 0) + 100),
                        LEFT, doTrace);
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
        }
        
        throw new IllegalStateException();
    }
    
    
}
