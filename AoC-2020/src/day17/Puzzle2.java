package day17;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Vect4I;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    public HashMap<Vect4I, Integer> cloneMap(HashMap<Vect4I, Integer> m)
    {
        HashMap<Vect4I, Integer> result = new HashMap<Vect4I, Integer>();
        for (Map.Entry<Vect4I, Integer> entry : m.entrySet())
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public void solve()
        throws Exception
    {
        HashMap<Vect4I, Integer> board = new HashMap<>();
        HashMap<Vect4I, Integer> boardNext = new HashMap<>();
        
        LinesGroup lines = readAllLines("input1.txt");
//        LinesGroup lines = readAllLines("test.txt");
        int result = 0;
        int x,y,z;
        
        y = 0;
        for (String line : lines)
        {
            for (x = 0; x < line.length(); x++)
            {
                if (line.charAt(x) == '#')
                {
                    board.put(Vect4I.of(x, y, 0, 0), 1);
                }
            }
            y++;
        }

        printBoardState(board);
        
        for (int count = 0; count < 6; count++)
        {
            boardNext = cloneMap(board);
            HashSet<Vect4I> front = new HashSet<>();
            for (Vect4I cell : board.keySet())
            {
                front.add(cell);
                for (Vect4I testDiff : Vect4I.NEIGHBOURS)
                {
                    Vect4I testCell = cell.add(testDiff);
                    front.add(testCell);
                }
            }
            
            for (Vect4I cell : front)
            {
                int nCount = 0;
                for (Vect4I testDiff : Vect4I.NEIGHBOURS)
                {
                    Vect4I testCell = cell.add(testDiff);
                    if (board.containsKey(testCell))
                    {
                        nCount++;
                    }
                }
                boolean isActive = board.containsKey(cell);
                boolean newState;
                if (isActive)
                {
                    newState = (nCount >= 2 && nCount <= 3);
                }
                else
                {
                    newState = (nCount == 3);
                }
                if (newState)
                {
                    boardNext.put(cell, 1);
                }
                else
                {
                    boardNext.remove(cell);
                }
            }
            board = boardNext;
            printBoardState(board);
        }
        
        result = board.keySet().size();
        
        System.out.println(result);
        
    }
    
    public static void printBoardState(HashMap<Vect4I, Integer> board)
    {
        return;
//        
//        int minX = Integer.MAX_VALUE;
//        int maxX = Integer.MIN_VALUE;
//        int minY = Integer.MAX_VALUE;
//        int maxY = Integer.MIN_VALUE;
//        int minZ = Integer.MAX_VALUE;
//        int maxZ = Integer.MIN_VALUE;
//        int minV = Integer.MAX_VALUE;
//        int maxV = Integer.MIN_VALUE;
//        for (Vect4I cell : board.keySet())
//        {
//            minX = Math.min(minX, cell.getX());
//            maxX = Math.max(maxX, cell.getX());
//            minY = Math.min(minY, cell.getY());
//            maxY = Math.max(maxY, cell.getY());
//            minZ = Math.min(minZ, cell.getZ());
//            maxZ = Math.max(maxZ, cell.getZ());
//            minV = Math.min(minZ, cell.getV());
//            maxV = Math.max(maxZ, cell.getV());
//        }
        
//        for (int z = minZ; z <= maxZ; z++)
//        {
//            System.out.printf("z=%d\n", z);
//            for (int y = minY; y <= maxY; y++)
//            {
//                for (int x = minX; x <= maxX; x++)
//                {
//                    Vect3I cell = Vect3I.of(x, y, z);
//                    System.out.print(board.containsKey(cell) ? "#" : ".");
//                }
//                System.out.println();
//            }
//        }
//        System.out.println();
//        System.out.println();
//        
    }
}
