package day10;

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
        ArrayList<String> lines = readAllLines("input1.txt");
        int result = 0;
        ArrayList<Integer> values = new ArrayList<>();
        for (String line : lines)
        {
            values.add(Integer.parseInt(line));
        }
        Collections.sort(values);
        int diff1Count = 0;
        int diff2Count = 0;
        int diff3Count = 0;
        for (int i = 1; i < values.size(); i++)
        {
            int value1 = values.get(i-1);
            int value2 = values.get(i);
            if (value2 - value1 == 1)
                diff1Count++;
            if (value2 - value1 == 2)
                diff2Count++;
            if (value2 - value1 == 3)
                diff3Count++;
        }
        result = (diff1Count+1)*(diff3Count+1);
        
        System.out.println(diff1Count);
        System.out.println(diff2Count);
        System.out.println(diff3Count);
        System.out.println(result);
        
    }
}
