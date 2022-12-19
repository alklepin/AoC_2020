package day19;

import java.util.ArrayList;
import java.util.Arrays;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntTriple;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    static class Blueprint
    {
        public IntTriple[] robotCosts;
        
        public Blueprint()
        {
            robotCosts = new IntTriple[4];
            Arrays.fill(robotCosts, IntTriple.ZERO);
        }
        
        public String toString()
        {
            var result = new StringBuilder();
            for (var v : robotCosts)
            {
                result.append(v).append(' ');
            }
            return result.toString();    
        }
    }
    
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var blueprints = new ArrayList<Blueprint>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            var blueprint = new Blueprint();
            
            var parts = line.split("Each (ore|clay|obsidian|geode) robot costs");
            for (var idx = 1; idx < parts.length; idx++)
            {
                var resStrings = parse(" (\\d* ore)?.*(\\d+ clay)?.*(\\d+ obsidian)?\\. ?", parts[idx]);
                var res = IntTriple.ZERO;
                for (var resIdx = 1; resIdx < resStrings.length; resIdx++)
                {
                    var s = resStrings[resIdx];
                    if (s == null)
                        continue;
                    var p = s.split(" ");
                    var amount = parseInt(p[0]);
                    switch (p[1])
                    {
                        case "ore":
                            res = res.add(IntTriple.of(1, 0, 0).mult(amount));
                            break;
                        case "clay":
                            res = res.add(IntTriple.of(0, 1, 0).mult(amount));
                            break;
                        case "obsidian":
                            res = res.add(IntTriple.of(0, 0, 1).mult(amount));
                            break;
                    }
                }
                blueprint.robotCosts[idx-1] = res;
            }
            blueprints.add(blueprint);
            System.out.println(blueprint);
        }
        System.out.println(result);
        
    }
}
