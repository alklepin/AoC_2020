package day01;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public long processGroup(LinesGroup group)
    {
        long sum = 0;
        for (var line : group)
        {
            var l = Long.parseLong(line);
            sum += l;
        }
        return sum;
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());
        
        long result = 0;
        for (LinesGroup group : groups)
        {
            var l = group.processGroup(this::processGroup);
            if (result < l)
                result = l;
        }
        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
//        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
}
