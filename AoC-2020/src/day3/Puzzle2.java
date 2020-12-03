package day3;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
        long answer = 1;
        int numberOfTrees;

        numberOfTrees = getTreesWithSlope(lines, 1, 1);
        System.out.println(numberOfTrees);
        answer *= numberOfTrees;
        
        numberOfTrees = getTreesWithSlope(lines, 3, 1);
        System.out.println(numberOfTrees);
        answer *= numberOfTrees;
        
        numberOfTrees = getTreesWithSlope(lines, 5, 1);
        System.out.println(numberOfTrees);
        answer *= numberOfTrees;
        
        numberOfTrees = getTreesWithSlope(lines, 7, 1);
        System.out.println(numberOfTrees);
        answer *= numberOfTrees;
        
        numberOfTrees = getTreesWithSlope(lines, 1, 2);
        System.out.println(numberOfTrees);
        answer *= numberOfTrees;

        System.out.println(answer);
        
    }
    
    public int getTreesWithSlope(List<String> lines, int deltaX, int deltaY)
    {
        int gridHeight = lines.size();
        int gridWidth = lines.get(1).length();
        int treeCount = 0;
        int currentX = 0;
        int currentY = 0;
        while (currentY < gridHeight)
        {
            if (lines.get(currentY).charAt(currentX % gridWidth) == '#')
                treeCount++;
            currentX += deltaX;
            currentY += deltaY;
        }
        return treeCount;
    }
}
