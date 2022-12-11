package day12;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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
        IntPair waypoint = new IntPair(10, 1);
        for (String line : lines)
        {
            String direction = line.substring(0, 1);
            int length = parseInt(line.substring(1), -1);
            switch (direction)
            {
                case "N":
                {
                    waypoint = waypoint.add(north.mult(length));
                    break;
                }
                case "S":
                {
                    waypoint = waypoint.add(south.mult(length));
                    break;
                }
                case "E":
                {
                    waypoint = waypoint.add(east.mult(length));
                    break;
                }
                case "W":
                {
                    waypoint = waypoint.add(west.mult(length));
                    break;
                }
                case "L":
                {
                    if (length >= 90)
                        waypoint = rotateLeft(waypoint);
                    if (length >= 180)
                        waypoint = rotateLeft(waypoint);
                    if (length >= 270)
                        waypoint = rotateLeft(waypoint);
                    break;
                }
                case "R":
                {
                    if (length >= 90)
                        waypoint = rotateRight(waypoint);
                    if (length >= 180)
                        waypoint = rotateRight(waypoint);
                    if (length >= 270)
                        waypoint = rotateRight(waypoint);
                    break;
                }
                case "F":
                {
                    IntPair shift = waypoint.mult(length);
                    current = current.add(shift);
                    break;
                }
            }
            System.out.println("current: "+current+ "    waypoint: "+ waypoint);
        }
        result = Math.abs(current.getX()) + Math.abs(current.getY());
        System.out.println(result);
    }
    
    IntPair rotateLeft(IntPair pair)
    {
        IntPair delta = pair;
        delta = new IntPair(-delta.getY(), delta.getX());
        return delta;
    }
    
    IntPair rotateRight(IntPair pair)
    {
        IntPair delta = pair;
        delta = new IntPair(delta.getY(), -delta.getX());
        return delta;
    }
}
