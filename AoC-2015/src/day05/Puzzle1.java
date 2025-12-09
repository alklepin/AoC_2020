package day05;

import java.util.Arrays;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }

    public boolean isNice(String s)
    {
        var chars = s.toCharArray();
        var vowelCount = 0;
        for (var c : chars)
        {
            if ("aeiou".indexOf(c) >= 0)
                vowelCount++;
        }
        var req1 = vowelCount >= 3;
        
        var req3 = (s.indexOf("ab") < 0) && (s.indexOf("cd") < 0) && (s.indexOf("pq") < 0) && (s.indexOf("zy") < 0);
        var req2 = false;
        for (var idx = 1; idx < chars.length; idx++)
        {
            if (chars[idx-1] == chars[idx])
            {
                req2 = true;
                break;
            }
        }
        return req1 && req2 && req3;
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
