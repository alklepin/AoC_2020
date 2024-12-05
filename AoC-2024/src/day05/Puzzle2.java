package day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

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
        
        LinesGroup lines = readAllLines(inputFile);
        
        var groups = readAllLineGroups(inputFile);
        
        var ruleLines = groups.get(0);
        var pageLines = groups.get(1);
        
        var rules = new ArrayList<IntPair>();
        var rulesSet = new HashSet<IntPair>();
        for (var line : ruleLines)
        {
            var parts = line.split("\\|");
            var x = parseInt(parts[0]);
            var y = parseInt(parts[1]);
            var pair = IntPair.of(x, y); 
            rules.add(pair);
            rulesSet.add(pair);
        }
        
        var result = 0;
        for (var line : pageLines.parsers())
        {
            var pages = line.listOfInts();
            var pagesSaved = new ArrayList<>(pages);
            var valid = true;
            var hasChanges = true;
            while (hasChanges)
            {
                hasChanges = false;
                for (var idx1 = 0; idx1 < pages.size(); idx1++)
                {
                    for (var idx2 = idx1 + 1; idx2 < pages.size(); idx2++)
                    {
                        var page1 = pages.get(idx1);
                        var page2 = pages.get(idx2);
                        var pair = IntPair.of(page2, page1);
                        if (rulesSet.contains(pair))
                        {
                            valid = false;
                            hasChanges = true;
                            pages.set(idx1,page2);
                            pages.set(idx2,page1);
                        }
                    }
                }
            }
            if (!valid)
            {
                System.out.println(pagesSaved);
                System.out.println(pages);
                
                var forbidden = new HashSet<Integer>();
                var v = true;
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
                
                System.out.println(v);
                System.out.println("---");
                
                
                //Collections.sort(pages);
                var middle = pages.get(pages.size()/2);
                result += middle;
            }
            
        }
        System.out.println(result);
        
    }
}
