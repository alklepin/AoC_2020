package day13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.Pair;

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
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());

        var points = new ArrayList<IntPair>();
        var pointsGroup = groups.get(0);
        for (var line : pointsGroup)
        {
            var parts = line.split(",");
            points.add(Pair.of (parseInt(parts[0].trim()), parseInt(parts[1].trim() )));
        }
        
        HashSet<IntPair> pointsSet = new HashSet<>();
        pointsSet.addAll(points);
        
        var foldingGroup = groups.get(1);
        var line = foldingGroup.get(0);
        
        var parts = line.split("=");
        var horiz = parts[0].endsWith("x");
        var position = parseInt(parts[1].trim());
        
        HashSet<IntPair> next = new HashSet<>();
        for (var point : pointsSet)
        {
            IntPair newPoint = point;
            if (horiz)
            {
                if (point.getX() > position)
                {
                    newPoint = Pair.of(position * 2 -point.getX(), point.getY());
                }
            }
            else
            {
                if (point.getY() > position)
                {
                    newPoint = Pair.of(point.getX(), position * 2 -point.getY());
                }
            }
            next.add(newPoint);
        }
        
        System.out.println(next.size());
        
        
    }
}
