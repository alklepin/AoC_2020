package day09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        ArrayList<Long> values = new ArrayList<>();
        int result = 0;
        for (String line : lines)
        {
            values.add(Long.parseLong(line));
        }
        
        for (int start = 0; start < values.size(); start++)
        {
            for (int end = start+1; end < values.size(); end++)
            {
                long sum = 0;
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                for (int idx = start; idx <= end; idx++)
                {
                    long value = values.get(idx); 
                    sum += value;
                    if (value < min)
                    {
                        min = value;
                    }
                    if (value > max)
                    {
                        max = value;
                    }
                }
                if (sum == 731031916)
                {
                    
                    System.out.printf("%d %d \n", start, end);
                    System.out.println(min + max);
                }
            }
        }
        
        
//        for (int i = 25; i < values.size(); i++)
//        {
//            if (check(i, values))
//                System.out.println(values.get(i));
//        }
        
//        System.out.println(result);
    }

    private boolean check(int checkPos, ArrayList<Long> values)
    {
        HashSet<Long> sums = new HashSet<>();
        for (int i = checkPos-25; i < checkPos; i++)
        {
            for (int j = checkPos-25; j < i; j++) 
            {
                long sum = values.get(i) + values.get(j);
                sums.add(sum);
            }
        }
        if (!sums.contains(values.get(checkPos)))
        {
            return true;
        }
        return false;
    }
    
}
