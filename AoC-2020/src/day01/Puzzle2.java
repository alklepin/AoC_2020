package day01;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

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
        ArrayList<Integer> data = new ArrayList<>();
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF-8"))
            {
                while (scanner.hasNextInt())
                {
                    int value = scanner.nextInt();
                    data.add(value);
                }
            }
        }
        for (int i = 0; i < data.size(); i++)
        {
            int v1 = data.get(i);
            for (int j = 0; j < i; j++)
            {
                int v2 = data.get(j);
                for (int k = 0; k < j; k++)
                {
                    int v3 = data.get(k);
                    if (v1 + v2 + v3 == 2020)
                    {
                        System.out.printf("Found: %d %d %d Mul: %d", v1, v2, v3, (long)v1*v2*v3);
                    }
                }
            }
        }
    }
}
