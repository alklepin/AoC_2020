package day03;

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
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        int result = 0;
        int[] ones = new int[12];
        int[] zeroes = new int[12];
        for (String line : lines)
        {
            for (int i = 0; i < line.length(); i++)
            {
                if (line.charAt(i) == '1')
                {
                    ones[i]++;
                }
                else
                {
                    zeroes[i]++;
                }
            }
        }
        int value = 0;
        int inv = 0;
        for (int i = 0; i < 12; i++)
        {
            value *= 2;
            inv *= 2;
            if (ones[i] > zeroes[i])
                value +=  1;
            else
                inv += 1;
        }
        System.out.println(value);
        System.out.println(inv);
        System.out.println(value * inv);
        
    }
}
