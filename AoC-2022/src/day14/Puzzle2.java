package day14;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
import common.boards.IntPair;

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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        int maxX = 0;
        int maxY = 0;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        var segments = new ArrayList<ArrayList<IntPair>>();
        for (String line : lines)
        {
            var segment = new ArrayList<IntPair>();
            var parts = line.split(" -> ");
            for (var s : parts)
            {
                var pair = IntPair.from(s);
                segment.add(pair);
                maxX = Math.max(maxX, pair.getX());
                maxY = Math.max(maxY, pair.getY());
                minX = Math.min(minX, pair.getX());
                minY = Math.min(minY, pair.getY());
            }
            segments.add(segment);
        }
        
        var sandSource = new IntPair(500, 0);
        maxX = Math.max(maxX, sandSource.getX());
        maxY = Math.max(maxY, sandSource.getY());
        minX = Math.min(minX, sandSource.getX());
        minY = Math.min(minY, sandSource.getY());
        
        var addSegment = new ArrayList<IntPair>();
        addSegment.add(IntPair.of(sandSource.getX() - maxY - 2, maxY + 2));
        addSegment.add(IntPair.of(sandSource.getX() + maxY + 2, maxY + 2));
        maxY = maxY + 2;
        maxX = Math.max(maxX, sandSource.getX() + maxY + 2);
        minX = Math.min(minX, sandSource.getX() - maxY - 2);
        
        segments.add(addSegment);
        var base = new IntPair(minX, minY);
        
        sandSource = sandSource.minus(base);

        
        var board = new Board2D(maxX-minX+1, maxY-minY+1);
        board.setAll('.');
        for (var segment : segments)
        {
            var p = segment.get(0).minus(base);
            board.setAtXY(p, '#');
            for (var idx = 1; idx < segment.size(); idx++)
            {
                var pNext = segment.get(idx).minus(base);
                var diff = pNext.minus(p);
                for (var cell : Generators.ray(p, diff.signum(), diff.lengthManh()))
                {
                    board.setAtXY(cell, '#');
                }
                p = pNext;
            }
        }
        board.setAtXY(sandSource, '+');
        board.printAsStrings(System.out);
        System.out.println();
//        System.out.println(result);
        
        var dir1 = IntPair.of(0, 1);
        var dir2 = IntPair.of(-1, 1);
        var dir3 = IntPair.of(1, 1);
        
        var count = 0;
        var running = true;
        while (running)
        {
            var sand = sandSource.copy();
            var steps = 0;
            var sandMoves = true;
            while (sandMoves)
            {
                sandMoves = false;
                
                var next = sand.add(dir1);
                if (board.containsXY(next))
                {
                    if (board.getAtXY(next) == '.')
                    {
                        sand = next;
                        sandMoves = true;
                        steps++;
                        continue;
                    }
                }
                else
                {
                    sandMoves = false;
                    running = false;
                }

                next = sand.add(dir2);
                if (board.containsXY(next))
                {
                    if (board.getAtXY(next) == '.')
                    {
                        sand = next;
                        sandMoves = true;
                        steps++;
                        continue;
                    }
                }
                else
                {
                    sandMoves = false;
                    running = false;
                }
            
                next = sand.add(dir3);
                if (board.containsXY(next))
                {
                    if (board.getAtXY(next) == '.')
                    {
                        sand = next;
                        sandMoves = true;
                        steps++;
                        continue;
                    }
                }
                else
                {
                    sandMoves = false;
                    running = false;
                }
            }
            if (steps == 0)
                running = false;
            if (running)
            {
                count++;
                board.setAtXY(sand, 'o');
            }
        }
        
        board.printAsStrings(System.out);
        System.out.println();
        System.out.println(count+1);
    }
}
