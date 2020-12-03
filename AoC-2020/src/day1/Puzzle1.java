package day1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{
    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    public void solve()
        throws Exception
    {
        ArrayList<Integer> data = new ArrayList<>();
        HashSet<Integer> knownValues = new HashSet<>();
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF-8"))
            {
                while (scanner.hasNextInt())
                {
                    int value = scanner.nextInt();
                    data.add(value);
                    int diff = 2020 - value;
                    if (knownValues.contains(diff))
                    {
                        System.out.printf("Found: %d %d %d", value, diff, value * diff);
                    }
                    knownValues.add(value);
                }
            }
        }
    }
}
