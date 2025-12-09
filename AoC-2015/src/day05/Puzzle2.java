package day05;

import java.util.Arrays;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    public boolean isNice(String s)
    {
        var chars = s.toCharArray();
        var req1 = false;
        for (var idx = 1; idx < chars.length; idx++)
        {
            var seg = s.substring(idx-1, idx+1);
            if (s.indexOf(seg, idx+1) >= 0)
            {
                req1 = true;
                break;
            }
        }
        
        var req2 = false;
        for (var idx = 2; idx < chars.length; idx++)
        {
            if (chars[idx-2] == chars[idx])
            {
                req2 = true;
                break;
            }
        }
        return req1 && req2;
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            if (isNice(line))
                result++;
        }
        System.out.println(result);
        
    }
}
