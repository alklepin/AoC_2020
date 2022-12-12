package day12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        IntPair start = null;
        IntPair end = null;
        for (var cell : board.allCellsXY())
        {
            var c = board.getCharAtXY(cell);
            
            if (c == 'S')
                start = cell;
            if (c == 'E')
                end = cell;
        }

        board.setAtXY(start, 'a');
        board.setAtXY(end, 'z');

        int min = Integer.MAX_VALUE;
        for (var cell : board.allCellsXY())
        {
            var c = board.getCharAtXY(cell);
            if (c != 'a')
                continue;
            
            start = cell;
            LinkedList<IntPair> queue = new LinkedList<>();
            HashSet<IntPair> visited = new HashSet<>();
            HashMap<IntPair, IntPair> visitedFrom = new HashMap<>();
            visited.add(start);
            queue.add(start);
            while (queue.size() > 0)
            {
                IntPair current = queue.poll();
                char currentHeight = board.getCharAtXY(current);
                for (var next : board.neighbours4XY(current))
                {
                    if (!board.containsXY(next) || visited.contains(next))
                        continue;
    
                    char nextHeight = board.getCharAtXY(next);
                    if (nextHeight - currentHeight > 1)
                        continue;
                    
                    visited.add(next);
                    visitedFrom.put(next, current);
                    if (next.equals(end))
                    {
                        break;
                    }
                    queue.add(next);
                }
            }
            
            if (!visited.contains(end))
                continue;
            
            ArrayList<IntPair> result = new ArrayList<>();
            IntPair current = end;
            while (current != null)
            {
                result.add(current);
                current = visitedFrom.get(current);
            }
            Collections.reverse(result);
            min = Math.min(min, result.size());
        }        
        
//        int result = 0;
//        for (String line : lines)
//        {
//        }
        System.out.println(min-1);
        
    }
}
