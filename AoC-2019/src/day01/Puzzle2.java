package day01;

import java.util.ArrayList;
import java.util.HashMap;

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
        ArrayList<String> lines = readAllLines("input1.txt");
        long result = 0;
        for (String line : lines)
        {
            int value = parseInt(line);
            while (value >= 0)
            {
                int delta = (int)Math.floor(value / 3.0) - 2;
                if (delta < 0)
                    break;
                result += delta;
                value = delta;
            }
        }
        System.out.println(result);
        
    }
}
