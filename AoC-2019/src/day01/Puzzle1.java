package day01;

import java.util.ArrayList;
import java.util.HashMap;

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
        ArrayList<String> lines = readAllLines("input1.txt");
        long result = 0;
        for (String line : lines)
        {
            int value = parseInt(line);
            while (value >= 0)
            {
                result += ((int)Math.floor(value / 3.0) - 2);
            }
        }
        System.out.println(result);
        
    }
}
