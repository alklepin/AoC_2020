package day03;

import java.util.ArrayList;
import java.util.HashMap;

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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        int result1 = 0;
        int result2 = 0;
        
        ArrayList<Integer> data = new ArrayList<Integer>();
        {
            ArrayList<String> filteredLines = new ArrayList<>();
            filteredLines.addAll(lines);
            int pos = 0;
            while (filteredLines.size() > 1 && pos < 12)
            {
                int ones = 0;
                int zeroes = 0;
                for (String line : filteredLines)
                {
                    if (line.charAt(pos) == '1')
                    {
                        ones++;
                    }
                    else
                    {
                        zeroes++;
                    }
                }
                char selected = ones >= zeroes ? '1' : '0';
                ArrayList<String> next = new ArrayList<String>();
                for (String line : filteredLines)
                {
                    if (line.charAt(pos) == selected)
                    {
                        next.add(line);
                    }
                }
                filteredLines = next;
                pos++;
            }
            result1 = Integer.parseInt(filteredLines.get(0), 2);
            System.out.println(filteredLines.get(0));
            System.out.println(filteredLines.size());
        }        
        {
            ArrayList<String> filteredLines = new ArrayList<>();
            filteredLines.addAll(lines);
            int pos = 0;
            while (filteredLines.size() > 1 && pos < 12)
            {
                int ones = 0;
                int zeroes = 0;
                for (String line : filteredLines)
                {
                    if (line.charAt(pos) == '1')
                    {
                        ones++;
                    }
                    else
                    {
                        zeroes++;
                    }
                }
                char selected = zeroes <= ones ? '0' : '1';
                ArrayList<String> next = new ArrayList<String>();
                for (String line : filteredLines)
                {
                    if (line.charAt(pos) == selected)
                    {
                        next.add(line);
                    }
                }
                filteredLines = next;
                pos++;
            }
            result2 = Integer.parseInt(filteredLines.get(0), 2);
            System.out.println(filteredLines.get(0));
            System.out.println(filteredLines.size());
        }  
        System.out.println(result1 * result2);
    }
}
