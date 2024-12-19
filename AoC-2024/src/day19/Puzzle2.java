package day19;

import java.util.regex.Pattern;

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
        
        var lines = readAllLineGroups(inputFile);
        
        var firstLIne = lines.get(0).get(0);
        var patterns = firstLIne.split(", ");
        var regexPattern = new StringBuilder();
        regexPattern.append("^((");
        for (var p : patterns)
        {
            regexPattern.append(p).append(")|(");
        }
        regexPattern.setLength(regexPattern.length()-2);
        regexPattern.append(")+$");
        var regex = Pattern.compile(regexPattern.toString());
        
        var result = 0;
        for (var p : lines.get(1))
        {
            if (regex.matcher(p).find())
                result++;
        }
        
        System.out.println(result);
        
    }
}
