package day10;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input2_2_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        IntPair start = null;
        for (var cell : board.allCellsXY())
        {
            if (board.getCharAtXY(cell) == 'S')
            {
                start = cell;
                break;
            }
        }
        
        var result = 0;
        var searchResult = ImplicitGraph.DFS(start, null, node -> nextCells(board, node));
        HashSet<IntPair> pathCells = new HashSet<>();
        for (var end : nextCells(board, start))
        {
            if (searchResult.visited().contains(end))
            {
                var path = searchResult.getPath(end);
                var pathLength = path.size();
                if (result < pathLength)
                {
                    result = pathLength;
                    pathCells.clear();
                    pathCells.addAll(path);
                }
            }
        }
        
        Board2D modifiedBoard = board.clone();
        
        char startReplacement = '!';
        if (pathCells.contains(start.add(IntPair.DOWN)))
        {
            if (pathCells.contains(start.add(IntPair.RIGHT)))
            {
                startReplacement = 'L';
            }
            if (pathCells.contains(start.add(IntPair.LEFT)))
            {
                startReplacement = 'J';
            }
            if (pathCells.contains(start.add(IntPair.UP)))
            {
                startReplacement = '|';
            }
        }
        if (pathCells.contains(start.add(IntPair.UP)))
        {
            if (pathCells.contains(start.add(IntPair.RIGHT)))
            {
                startReplacement = 'F';
            }
            if (pathCells.contains(start.add(IntPair.LEFT)))
            {
                startReplacement = '7';
            }
            if (pathCells.contains(start.add(IntPair.UP)))
            {
                startReplacement = '|';
            }
        }
        if (pathCells.contains(start.add(IntPair.LEFT)))
        {
            if (pathCells.contains(start.add(IntPair.RIGHT)))
            {
                startReplacement = '-';
            }
        }
        modifiedBoard.setCharAtXY(start, startReplacement);
        
        
        HashSet<IntPair> internals = new HashSet<>();
        for (var cell : modifiedBoard.allCellsXY())
        {
            if (pathCells.contains(cell))
                continue;
            
            var count = 0;
            for (var rayCell : modifiedBoard.rayXY(cell, IntPair.UP))
            {
                if (pathCells.contains(rayCell))
                {
                    count += switch (modifiedBoard.getAtXY(rayCell))
                    {
                        case '-' -> 2;
                        case 'L' -> 1;
                        case '7' -> 1;
                        case 'J' -> -1; 
                        case 'F' -> -1;
                        default -> 0;
                    };
                }
            }
            
            if (Math.abs(count/2) % 2 == 1)
            {
                internals.add(cell);
            }
        }
        
//        Board2D copy = modifiedBoard.clone();
//        for (var cell : copy.allCellsXY())
//        {
//            if (!pathCells.contains(cell))
//                copy.setCharAtXY(cell, '@');
//        }
//        for (var cell : internals)
//        {
//            copy.setCharAtXY(cell, '#');
//        }
//        copy.printAsStrings(System.out);
        System.out.println(internals.size());
        
    }
    
    
    
    public Iterable<IntPair> nextCells(Board2D board, IntPair cell)
    {
        ArrayList<IntPair> moves = new ArrayList<>();
        var current = board.getCharAtXY(cell);
        
        IntPair next; 

        next = cell.add(IntPair.DOWN);
        if (board.containsXY(next) && "S|LJ".indexOf(current) >= 0 && "S|7F".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }

        next = cell.add(IntPair.UP);
        if (board.containsXY(next) && "S|7F".indexOf(current) >= 0 && "S|LJ".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }

        next = cell.add(IntPair.LEFT);
        if (board.containsXY(next) && "S-J7".indexOf(current) >= 0 && "S-LF".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }

        next = cell.add(IntPair.RIGHT);
        if (board.containsXY(next) && "S-LF".indexOf(current) >= 0 && "S-7J".indexOf(board.getCharAtXY(next)) >= 0)
        {
            moves.add(next);
        }
        return moves;
    }
}
