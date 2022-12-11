package day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        HashMap<Integer, Long> memory = new HashMap<>();
        long andMask = 0;
        long orMask = 0;
        
        LinesGroup lines = readAllLines("input1.txt");
//        LinesGroup lines = readAllLines("test.txt");
        for (String line : lines)
        {
            String[] parts = line.split(" = ");
            if (parts[0].equals("mask"))
            {
                andMask = Long.parseLong(parts[1].replace('X', '1'), 2);
                orMask = Long.parseLong(parts[1].replace('X', '0'), 2);
            }
            else
            {
                String[] parts1 = parse("mem\\[([0-9]+)\\]", parts[0]);
                int addr = parseInt(parts1[1], -1);
                int newValue = parseInt(parts[1], -1);
                Long value = memory.get(addr);
                if (value == null)
                {
                    value = 0L;
                }
                long current = newValue;
                current |= orMask;
                current &= andMask;
                memory.put(addr,  current);
            }
        }

        long result = 0;
        for (Map.Entry<Integer, Long> entry : memory.entrySet())
        {
            result += entry.getValue();
        }
        System.out.println(result);
        
    }
}
