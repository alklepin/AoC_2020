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

    private static boolean isPwdValid(String pwd, char c, int num1, int num2)
    {
        int count = 0;
        for (int idx = 0; idx < pwd.length(); idx++)
        {
            char ch = pwd.charAt(idx);
            if (ch == c)
                count++;
        }
        return num1 <= count && count <= num2;
    }

//    Pattern pattern = Pattern.compile("([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)");
//    int validPwdCounter = 0;
//    while (scanner.hasNextLine())
//    {
//        String line = scanner.nextLine();
//        System.out.println(line);
//        Matcher m = pattern.matcher(line);
//        if (m.matches())
//        {
//            int num1 = Integer.parseInt(m.group(1));
//            int num2 = Integer.parseInt(m.group(2));
//            char c = m.group(3).charAt(0);
//            String pwd = m.group(4);
////            System.out.printf("%d-%d %s: %s\n", num1, num2, c, pwd);
//            if (isPwdValid(pwd, c, num1, num2))
//            {
//                validPwdCounter++;
//            }
//        }
//    }
//    System.out.printf("%d", validPwdCounter);
    
}
