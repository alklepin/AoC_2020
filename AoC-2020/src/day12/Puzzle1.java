package day12;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;
import common.boards.IntPair;

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

    IntPair north = new IntPair(0, 1);
    IntPair south = new IntPair(0, -1);
    IntPair east = new IntPair(1, 0);
    IntPair west = new IntPair(-1, 0);
    
    public void solve()
        throws Exception
    {
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int result = 0;
        IntPair current = new IntPair(0,0);
        IntPair delta = new IntPair(1, 0);
        for (String line : lines)
        {
            String direction = line.substring(0, 1);
            int length = parseInt(line.substring(1), -1);
            switch (direction)
            {
                case "N":
                {
//                  delta = north;
                    current = current.add(north.mult(length));
                    break;
                }
                case "S":
                {
//                    delta = south;
                    current = current.add(south.mult(length));
                    break;
                }
                case "E":
                {
//                    delta = east;
                    current = current.add(east.mult(length));
                    break;
                }
                case "W":
                {
//                    delta = west;
                    current = current.add(west.mult(length));
                    break;
                }
                case "L":
                {
                    if (length >= 90)
                        delta = rotateLeft(delta);
                    if (length >= 180)
                        delta = rotateLeft(delta);
                    if (length >= 270)
                        delta = rotateLeft(delta);
//                    current = current.add(delta.mult(length));
                    break;
                }
                case "R":
                {
                    if (length >= 90)
                        delta = rotateRight(delta);
                    if (length >= 180)
                        delta = rotateRight(delta);
                    if (length >= 270)
                        delta = rotateRight(delta);
//                    current = current.add(delta.mult(length));
                    break;
                }
                case "F":
                {
                    current = current.add(delta.mult(length));
                    break;
                }
            }
            System.out.println(current);
        }
        result = Math.abs(current.getX()) + Math.abs(current.getY());
        System.out.println(result);
    }
    
    IntPair rotateLeft(IntPair pair)
    {
        if (pair.equals(north))
            return west;
        if (pair.equals(west))
            return south;
        if (pair.equals(south))
            return east;
        if (pair.equals(east))
            return north;
        throw new IllegalStateException();
    }
    
    IntPair rotateRight(IntPair pair)
    {
        if (pair.equals(north))
            return east;
        if (pair.equals(east))
            return south;
        if (pair.equals(south))
            return west;
        if (pair.equals(west))
            return north;
        throw new IllegalStateException();
    }
}
