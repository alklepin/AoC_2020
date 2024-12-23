package day19;

import java.util.regex.Pattern;

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
        
        long result = 0;
        for (var p : lines.get(1))
        {
            if (regex.matcher(p).find())
            {
                result += countVariabts(p, patterns);
            }            
        }
        
        System.out.println(result);
        
    }
    
    public long countVariabts(String p, String[] patterns)
    {
        long[] counters = new long[p.length()+1];
        counters[0] = 1;
        for (int pos = 0; pos < p.length(); pos++)
        {
            for (int pIdx = 0; pIdx < patterns.length; pIdx++)
            {
                var pattern = patterns[pIdx];
                if (p.startsWith(pattern, pos))
                {
                    counters[pos + pattern.length()] += counters[pos];
                }
            }
        }
        return counters[p.length()];
    }
}
