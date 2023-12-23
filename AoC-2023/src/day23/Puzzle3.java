package day23;

import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.queries.Query;

public class Puzzle3 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle3().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    static IntPair end;
    static HashMap<IntPair, IntPair> visitedFrom = new HashMap<>();
    static HashMap<IntPair, Integer> distance = new HashMap<>();
    static Board2D board;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        board = Board2D .parseAsCharsXY(lines);
        var start = board.rowCellsXY(0).where(c -> board.getCharAtXY(c) == '.').first();
        end = board.rowCellsXY(board.getHeigth()-1).where(c -> board.getCharAtXY(c) == '.').first();
        
        distance.put(start, 0);
        visitedFrom.put(start, start);
        var result = maxDistance(start);
        
        System.out.println(result);
        
    }
    
    static int maxDist = 0;
    
    static int maxDistance(IntPair current)
    {
//        printPath();
        
        if (current.equals(end))
        {
            int d = distance.get(current);
            if (d > maxDist)
            {
                maxDist = d;
                printPath();
                System.out.println("Max:" + d);
            }
//            System.out.println("End:" + d);
            return distance.get(current);
        }
        var result = 0;
        
        var currentChar = board.getCharAtXY(current);
        Query<IntPair> nextCells;
//        if (currentChar != '.')
//        {
//            nextCells = Query.wrap(current.add(IntPair.decodeDirectionVInv(currentChar)));
//        }
//        else
//        {
            nextCells = board.neighbours4XY(current)
                .where(c -> board.getCharAtXY(c) != '#');
//        }
        var currentDistance = distance.get(current);
        var list = nextCells.toList();
        Collections.reverse(list);
        for (var next : list)
        {
//            var nextChar = board.getCharAtXY(next);
//            var nextDir = IntPair.decodeDirectionVInv(nextChar);
//            if (nextDir != null && nextDir.equals(next.minus(current).mult(-1)))
//                continue;
            
            var nextD = distance.get(next);
            if (nextD != null)
                continue;
            
            distance.put(next, currentDistance+1);
            visitedFrom.put(next, current);
            var dist = maxDistance(next);
            result = Math.max(result, dist);
            distance.remove(next);
            visitedFrom.remove(next);
        }
        return result;
    }
    
    static void printPath()
    {
        var b = board.clone();
        for (var c : distance.keySet())
        {
            b.setCharAtXY(c, '*');
        }
        b.printAsStrings(System.out);
        System.out.println("-----");
    }
}
