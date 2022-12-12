package day04;

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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";

        var start = 165432;
        var end = 707912;
        
        var result = 0;
        for (int value = start; value <= end; value++)
        {
            var str = String.valueOf(value);
            var increasing = true;
            var hasDuplicates = false;
            for (var idx = 1; idx < str.length(); idx++)
            {
                var c1 = str.charAt(idx-1);
                var c2 = str.charAt(idx);
                hasDuplicates |= (c1 == c2);
                increasing &= (c1 <= c2);
            }
            if (increasing && hasDuplicates)
                result++;
        }   
        
        System.out.println(result);
        
    }
}
