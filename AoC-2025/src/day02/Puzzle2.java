package day02;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;

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
     
        var foundIds = new HashSet<String>();
        
        long result = 0;
        LinesGroup lines = readAllLines(inputFile);
        var ranges = lines.get(0).split(",");
        for (int v = 0; v < 100000; v++)
        {
            String testLine = "";
            for (var repCount = 2; repCount < 11; repCount++)
            {
                var buf = new StringBuilder();
                for (var rc = 0; rc < repCount; rc++)
                {
                    buf.append(v);
                }
                testLine = buf.toString();
                if (testLine.length() >= 12)
                    continue;
                
                var testVal = parseLong(testLine); 
                
                for (var range : ranges)
                {
                    var parts = range.split("-");
                    var rangeMin = parseLong(parts[0]);
                    var rangeMax = parseLong(parts[1]);
                    if (rangeMin <= testVal && testVal <= rangeMax && !foundIds.contains(testLine))
                    {
                        result += testVal;
                        foundIds.add(testLine);
//                        System.out.println(testLine);
                        break;
                    }
                }
            }
        }
            
        
        System.out.println(result);
        
    }
}
