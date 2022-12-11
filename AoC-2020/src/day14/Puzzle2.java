package day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HashMap<Long, Long> memory = new HashMap<>();
        
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test2.txt");
        String mask = ""; 
        for (String line : lines)
        {
            String[] parts = line.split(" = ");
            if (parts[0].equals("mask"))
            {
                mask = parts[1];
            }
            else
            {
                List<String> addressMask = generateAddresses(mask);
                
                String[] parts1 = parse("mem\\[([0-9]+)\\]", parts[0]);
                int addr = parseInt(parts1[1], -1);
                long newValue = parseInt(parts[1], -1);
                for (String ad : addressMask)
                {
                    long andMask = Long.parseLong(ad.replace('X', '1'), 2);
                    long orMask = Long.parseLong(ad.replace('X', '0'), 2);
                    long newAddr = addr;
                    newAddr |= orMask;
                    newAddr &= andMask;
                    
                    Long value = memory.get(newAddr);
                    if (value == null)
                    {
                        value = 0L;
                    }
                    memory.put(newAddr,  newValue);
                }
            }
        }

        long result = 0;
        for (Map.Entry<Long, Long> entry : memory.entrySet())
        {
            result += entry.getValue();
        }
        System.out.println(result);
        
    }
    
    private List<String> generateAddresses(String mask)
    {
        if (mask.length() == 0)
            return Arrays.asList("");
        char c = mask.charAt(0);
        String remainder = mask.substring(1);
        List<String> remainders = generateAddresses(remainder);
        ArrayList<String> result = new ArrayList<String>();
        switch (c)
        {
            case '0':
            {
                for (String s : remainders)
                {
                    result.add('X'+s);
                }
                break;
            }
            case '1':
            {
                for (String s : remainders)
                {
                    result.add('1'+s);
                }
                break;
            }
            case 'X':
            {
                for (String s : remainders)
                {
                    result.add('1'+s);
                    result.add('0'+s);
                }
                break;
            }
        }
        return result;
    }
}
