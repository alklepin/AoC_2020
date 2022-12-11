package day06;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;

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
        
        ArrayList<String> lines = readAllLines("input1.txt");
        
        var line = lines.get(0);
        var queue = new LinkedList<Character>();
        var set = new HashSet<Character>();
        
        var result = -1;
        int count = 0;
        for (var c : line.toCharArray())
        {
            count++;
            queue.add(c);
            set.add(c);
            if (queue.size() == 14)
            {
                set.clear();
                for (var c1 : queue)
                {
                    set.add(c1);
                }
                if (set.size() == 14)
                {
                    result = count;
                    break;
                }
                queue.poll();
            }
        }
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
        System.out.println(result);
        
    }
}
