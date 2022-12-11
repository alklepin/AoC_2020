package day06;

import java.util.ArrayList;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1New().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashSet<Character> chars = new HashSet<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                chars.add(line.charAt(i));
            }
        }
        return chars.size();
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
