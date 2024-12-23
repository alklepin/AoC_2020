package day03;

import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;

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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (LineParser line : lines.parsers())
        {
            var instructions = line.listOf("mul\\(\\d{1,3},\\d{1,3}\\)");
            for (var inst : instructions)
            {
                var ints = new LineParser(inst).listOf("\\d{1,3}");
                result += parseInt(ints.get(0)) * parseInt(ints.get(1));
            }
        }
        System.out.println(result);
        
    }
}
