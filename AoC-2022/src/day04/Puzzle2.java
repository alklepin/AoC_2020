package day04;

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
    
    
    IntPair parse(String s)
    {
        var parts = s.split("-");
        var v1 = parseInt(parts[0]);
        var v2 = parseInt(parts[1]);
        return new IntPair(v1, v2);
    }
    
    boolean fullyContains(IntPair p1, IntPair p2)
    {
        return (p1.getX() <= p2.getX() && p1.getY() >= p2.getY())
            || (p2.getX() <= p1.getX() && p2.getY() >= p1.getY());
        
    }

    boolean overlaps(IntPair p1, IntPair p2)
    {
        return (p1.getX() <= p2.getX() && p2.getX() <= p1.getY())
            || (p1.getX() <= p2.getY() && p2.getY() <= p1.getY())
            || (p2.getX() <= p1.getX() && p1.getX() <= p2.getY())
            || (p2.getX() <= p1.getY() && p1.getY() <= p2.getY());
            
    }
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        for (String line : lines)
        {
            var parts = line.split(",");
            var p1 = parse(parts[0]);
            var p2 = parse(parts[1]);
            if (overlaps(p1, p2))
                result++;
        }
        System.out.println(result);
        
    }
}
