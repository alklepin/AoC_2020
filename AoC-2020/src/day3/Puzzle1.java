package day3;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

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
        ArrayList<String> lines = new ArrayList<>();
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
            {
                while (scanner.hasNextLine()) 
                {
                    lines.add(scanner.nextLine().trim());
                }
            }
        }
//        for (String line : lines)
//        {
//            System.out.println(line);
//        }
//        System.out.println(lines.size());
        int gridHeight = lines.size();
        int gridWidth = lines.get(1).length();
        int treeCount = 0;
        int currentX = 0;
        int currentY = 0;
        int deltaX = 3;
        int deltaY = 1;
        while (currentY < gridHeight)
        {
            if (lines.get(currentY).charAt(currentX % gridWidth) == '#')
                treeCount++;
            currentX += deltaX;
            currentY += deltaY;
        }
        System.out.println(treeCount);
    }
}
