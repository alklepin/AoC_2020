package day21;

import java.util.ArrayList;
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
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
        long result = 0;
        int pos1 = parseInt(parse("Player \\d starting position: (\\d+)", lines.get(0))[1]) - 1;
        int pos2 = parseInt(parse("Player \\d starting position: (\\d+)", lines.get(1))[1]) - 1;
        long score1 = 0;
        long score2 = 0;
        int dieState = 9;
        int count = 0;
        while (score1 < 1000 && score2 < 1000)
        {
            dieState = (dieState + 1) % 10;
            pos1 = (pos1 + dieState + 1) % 10;
            dieState = (dieState + 1) % 10;
            pos1 = (pos1 + dieState + 1) % 10;
            dieState = (dieState + 1) % 10;
            pos1 = (pos1 + dieState + 1) % 10;
            score1 += (pos1 + 1);
            count += 3;
            if (score1 >= 1000)
                break;
            
            dieState = (dieState + 1) % 10;
            pos2 = (pos2 + dieState + 1) % 10;
            dieState = (dieState + 1) % 10;
            pos2 = (pos2 + dieState + 1) % 10;
            dieState = (dieState + 1) % 10;
            pos2 = (pos2 + dieState + 1) % 10;
            score2 += (pos2 + 1);
            count += 3;
        }
        if (score1 > score2)
        {
            result = score2 * count;
        }
        else
        {   
            result = score1 * count;
        }
        System.out.println(result);
        
    }
}
