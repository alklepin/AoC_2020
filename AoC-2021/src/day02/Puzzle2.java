package day02;

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
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        long pos = 0;
        long depth = 0;
        long aim = 0;
        for (String line : lines)
        {
            if (line.trim().length() == 0)
                break;
            
            var parts = line.split(" ");
            int v = parseInt(parts[1]);
            switch (parts[0].trim())
            {
                case "forward": 
                {
                    pos += v;
                    depth += aim * v;
                    break;
                }
                case "up":
                {
                    aim -= v;
                    break;
                }
                case "down":
                {
                    aim += v;
                    break;
                }
            }
        }
        System.out.println(pos * depth);
        
    }
}
