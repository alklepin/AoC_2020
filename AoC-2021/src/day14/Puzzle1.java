package day14;

import java.util.ArrayList;
import java.util.HashMap;

import common.IntValue;
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
    
    private HashMap<String, String> substs = new HashMap<>();
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("input2.txt");
        // System.out.println(groups.size());
        
        var line = groups.get(0).get(0);
        
        for (var rule : groups.get(1))
        {
            var parts = rule.split("->");
            var from = parts[0].trim();
            var to = parts[1].trim();
            substs.put(from, to);
        }
        
        var current = line;
        for (int i = 0; i < 10; i++)
        {
            var next = new StringBuilder();
            for (int pos = 0; pos < current.length()-1; pos++)
            {
                next.append(current.charAt(pos));
                var pair = current.substring(pos, pos+2);
                var s = substs.get(pair);
                if (s != null)
                    next.append(s);
            }
            next.append(current.charAt(current.length()-1));
            current = next.toString();
        }
        
        HashMap<Character, IntValue> counters = new HashMap<>();
        for (var c : current.toCharArray())
        {
            IntValue counter = counters.computeIfAbsent(c, kay -> new IntValue(0));
            counter.inc();
        }
        int minCount = Integer.MAX_VALUE;
        int maxCount = Integer.MIN_VALUE;
        char maxChar = ' ';
        char minChar = ' ';
        for (var entry : counters.entrySet())
        {
            int value = entry.getValue().getValue(); 
            if (value < minCount)
            {
                minCount = value;
                minChar = entry.getKey();
            }
            if (value > maxCount)
            {
                maxCount = value;
                maxChar = entry.getKey();
            }
        }
        
        System.out.println(maxCount - minCount);
        
    }
}
