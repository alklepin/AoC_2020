package day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;

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
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        var line = lines.get(0);
        long result = 0;
        ArrayList<Integer> values = new ArrayList<>();
        for (var v : line.split(","))
        {
            values.add(parseInt(v));
        }
        Collections.sort(values);
        int target = values.get(values.size() / 2);
        for (var v : values)
        {
            result += Math.abs(v - target);
        }
           
//        for (String line : lines)
//        {
//        }
        System.out.println(target);
        System.out.println(result);
        
    }
}
