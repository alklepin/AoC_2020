package day11;

import java.util.ArrayList;
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
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }

    public char[][] buildArray(int width, int height)
    {
        char [][] data = new char[height][];
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            data[rowIdx] = new char[width];
        }
        return data;
    }

    public void printArray(char [][] data)
    {
        System.out.println("==========================================");
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++)
        {
            System.out.println(new String(data[rowIdx]));
        }
        System.out.println("==========================================");
    }
    
    public boolean equals(char [][] current, char [][] next)
    {
        int height = current.length;
        int width = current[0].length;
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                if (current[rowIdx][colIdx] != next[rowIdx][colIdx])
                    return false;
            }
        }
        return true;
    }
    
    public int countOccupied(char [][] current)
    {
        int height = current.length;
        int width = current[0].length;
        int result = 0;
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                if (current[rowIdx][colIdx] == '#')
                    result++;
            }
        }
        return result;
    }    
    
    
    public void modelStep(char [][] current, char [][] next)
    {
        int height = current.length;
        int width = current[0].length;
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                int occupiedCount = 0;
                int dx,dy;
                dx = 1; dy = 1;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = 1; dy = 0;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = 1; dy = -1;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = 0; dy = -1;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = -1; dy = -1;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = -1; dy = 0;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = -1; dy = 1;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                dx = 0; dy = 1;
                if (rowIdx+dx >= 0 && rowIdx+dx < height && colIdx+dy >= 0 && colIdx+dy < width && current[rowIdx+dx][colIdx+dy] == '#')
                    occupiedCount++;
                char nextState = current[rowIdx][colIdx];
                switch (nextState)
                {
                    case 'L':
                    {
                        if (occupiedCount == 0)
                            nextState = '#';
                        break;
                    }
                    case '#':
                    {
                        if (occupiedCount >= 4)
                            nextState = 'L';
                        break;
                    }
                }
                next[rowIdx][colIdx] = nextState;
            }
        }
    }
    
    public void solve()
        throws Exception
    {
        
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int height = lines.size();
        int width = lines.get(0).length();
        char [][] dataCurrent = buildArray(width, height);
        char [][] dataNext = buildArray(width, height);
        
        int result = 0;
        int rowCount = 0;
        for (String line : lines)
        {
            for (int i = 0; i < line.length(); i++)
            {
                dataCurrent[rowCount][i] = line.charAt(i);
            }
            rowCount++;
        }

        printArray(dataCurrent);
        
        boolean changed = true;
        int counter = 37;
        
        while (changed)
        {
            changed = false;
            modelStep(dataCurrent, dataNext);
//            printArray(dataNext);
            changed = !equals(dataCurrent, dataNext);
            char [][] temp = dataCurrent;
            dataCurrent = dataNext;
            dataNext = temp;
//            counter--;
//            if (counter == 0) 
//            {
//                break;
//            }
        }
        
        result = countOccupied(dataCurrent);
            
        
//        printArray(dataCurrent);
        
//        modelStep(dataCurrent, dataNext);
        
        System.out.println(result);
        
    }
}
