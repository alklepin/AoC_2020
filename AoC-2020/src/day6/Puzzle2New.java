package day6;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;

public class Puzzle2New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2New().solve();
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
        System.out.println(groups.size());
        
        int sum = 0;
        for (LinesGroup group : groups)
        {
            sum += group.processGroup(this::processGroup);
        }
        System.out.println(sum);
    }
}
