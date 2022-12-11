package day01;

import java.util.ArrayList;
import java.util.HashMap;

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

        
        LinesGroup lines = readAllLines("input1.txt");
        int result = 0;
        ArrayList<Integer> data = new ArrayList<Integer>();
        for (String line : lines)
        {
            data.add(parseInt(line));
        }
        
        int prevValue = Integer.MAX_VALUE;
        for (int i = 2; i < data.size(); i++)
        {
            int v = data.get(i-2) + data.get(i-1) + data.get(i);
            if (v > prevValue)
            {
                result++;
                System.out.println(i);
            }
            prevValue = v;
        }

        System.out.println(result);
        
    }
}
