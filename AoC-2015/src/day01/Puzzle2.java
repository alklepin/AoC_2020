package day01;

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
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        long result = 0;
        int pos = 0;
        for (var c : lines.get(0).toCharArray())
        {
            pos++;
            if (c == '(')
            {
                result++;
            }
            else if (c == ')')
            {
                result--;
            }
            if (result < 0)
                break;
        }
        System.out.println(pos);
        
    }
}
