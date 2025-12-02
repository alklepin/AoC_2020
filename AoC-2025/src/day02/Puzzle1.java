package day02;

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
    
    public boolean isInvalidId(String id)
    {
        if (id.length() % 2 != 0)
            return false;
        var mid = id.length() / 2;
        return id.substring(0, mid).equals(id.substring(mid));
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        long result = 0;
        LinesGroup lines = readAllLines(inputFile);
        var ranges = lines.get(0).split(",");
        for (int v = 0; v < 100000; v++)
        {
            var testLine = new StringBuilder().append(v).append(v).toString();
            var testVal = parseLong(testLine); 
            
            for (var range : ranges)
            {
                var parts = range.split("-");
                var rangeMin = parseLong(parts[0]);
                var rangeMax = parseLong(parts[1]);
                if (rangeMin <= testVal && testVal <= rangeMax)
                    result += parseLong(testLine);
            }
        }
            
        
        System.out.println(result);
        
    }
}
