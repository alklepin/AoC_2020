package day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
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
        
        LinesGroup lines = readAllLines(inputFile);
        
        var groups = readAllLineGroups(inputFile);
        
        var ruleLines = groups.get(0);
        var pageLines = groups.get(1);
        
        var rules = new ArrayList<IntPair>();
        for (var line : ruleLines)
        {
            var parts = line.split("\\|");
            var x = parseInt(parts[0]);
            var y = parseInt(parts[1]);
            rules.add(IntPair.of(x, y));
        }
        
        var result = 0;
        for (var line : pageLines.parsers())
        {
            var pages = line.listOfInts();
            var forbidden = new HashSet<Integer>();
            var valid = true;
            for (var page : pages)
            {
                if (forbidden.contains(page))
                {
                    valid = false;
                    break;
                }
                for (var rule : rules)
                {
                    if (page.equals(rule.getY()))
                        forbidden.add(rule.getX());
                }
            }
            if (valid)
            {
                //Collections.sort(pages);
                var middle = pages.get(pages.size()/2);
                result += middle;
            }
            
        }
        System.out.println(result);
        
    }
}
