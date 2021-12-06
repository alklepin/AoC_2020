package day06;

import java.util.ArrayList;
import java.util.HashMap;

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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
//        for (String line : lines)
//        {
//        }
        ArrayList<Integer> current = new ArrayList<>();
        String[] input = lines.get(0).split(",");
        for (var s : input)
        {
            current.add(parseInt(s));
        }
        for (int i = 0; i < 80; i++)
        {
            int countToAdd = 0;
            for (int iIdx = 0; iIdx < current.size(); iIdx++)
            {
                int nextV = current.get(iIdx) - 1;
                if (nextV < 0)
                {
                    nextV = 6;
                    countToAdd++;
                }
                current.set(iIdx, nextV);
            }
            while (countToAdd > 0)
            {
                current.add(8);
                countToAdd--;
            }
        }
        
        System.out.println(current.size());
        
    }
}
