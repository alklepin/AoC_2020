package day04;

import java.util.HashSet;

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
        var result = 0;
        for (String line : group)
        {
        }
        return result;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            
            var parts = line.split(":")[1].trim().split("\\|");
            var winning = parts[0].trim().split("\\s+");
            var numbers = parts[1].trim().split("\\s+");
            var hsWinn = new HashSet<Integer>();
            for (var s : winning)
            {
                var n = parseInt(s);
                hsWinn.add(n);
            }
            var cost = 1;
            for (var s : numbers)
            {
                var n = parseInt(s);
                if (hsWinn.contains(n))
                {
                    cost *= 2;
                }
            }
            if (cost > 1)
                cost = cost / 2;
            else 
                cost = 0;
            result += cost;
        }
        System.out.println(result);
        
    }
}
