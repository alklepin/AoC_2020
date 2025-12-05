package day05;

import java.util.ArrayList;
import java.util.TreeSet;

import common.PuzzleCommon;
import common.RangeLong;
import common.RangeLongSet;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
        var lineGroups = readAllLineGroups(inputFile);
        var ranges = lineGroups.get(0);
        var rangeList = new ArrayList<RangeLong>();
        var rangeSet = new RangeLongSet(); 
        for (var line : ranges)
        {
            var parts = line.split("-");
            var start = parseLong(parts[0]);
            var end = parseLong(parts[1]);
            var range = new RangeLong(start, end+1);
            rangeList.add(range);
            rangeSet.addRange(range);
        }
        
        long result = 0;
        for (var r : rangeSet)
        {
            result += r.length();
            System.out.println(r);
        }
        
        System.out.println(result);
        
    }
}
