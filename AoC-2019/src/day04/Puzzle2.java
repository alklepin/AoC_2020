package day04;

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
            for (var idx = 1; idx < str.length(); idx++)
            {
                var c1 = str.charAt(idx-1);
                var c2 = str.charAt(idx);
                increasing &= (c1 <= c2);
            }
            
            if (!increasing)
                continue;
            
            var str1 = str.replaceAll("0{3,6}|1{3,6}|2{3,6}|3{3,6}|4{3,6}|5{3,6}|6{3,6}|7{3,6}|8{3,6}|9{3,6}", " ");
            var hasDuplicates = false;
            for (var idx = 1; idx < str1.length(); idx++)
            {
                var c1 = str1.charAt(idx-1);
                var c2 = str1.charAt(idx);
                hasDuplicates |= (c1 == c2) && (c1 != ' ');
            }
            
            if (increasing && hasDuplicates)
                result++;
        }   
        
        System.out.println(result);
        
    }
}
