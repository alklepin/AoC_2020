package day03;

import common.CompositeRegex;
import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle3 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle3().solve();
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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        boolean mulEnabled = true;
        var pattern = CompositeRegex.of("mul\\(\\d{1,3},\\d{1,3}\\)", "do\\(\\)", "don't\\(\\)");
        for (String line : lines)
        {
            var instructions = pattern.allMatches(line);
            for (var inst : instructions)
            {
                if (inst.startsWith("mul(") )
                {
                    if (mulEnabled)
                    {
                        var ints = new LineParser(inst).listOf("\\d{1,3}");
                        result += parseInt(ints.get(0)) * parseInt(ints.get(1));
                    }
                }
                else if (inst.startsWith("don't("))
                {
                    mulEnabled = false;
                }
                else if (inst.startsWith("do("))
                {
                    mulEnabled = true;
                }
                else
                {
                    throw new IllegalStateException();
                }
                    
            }
        }
        System.out.println(result);
        
    }
}
