package day01;

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
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        long result = 0;
        for (var c : lines.get(0).toCharArray())
        {
            if (c == '(')
            {
                result++;
            }
            else if (c == ')')
            {
                result--;
            }
        }
        System.out.println(result);
        
    }
}
