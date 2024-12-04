package day09;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;

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
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input2.txt");
        
        int result = 0;
        int height = 0;
        int width = 0;
        for (String line : lines)
        {
            height++;
            if (line.length() > width)
            {
                width = line.length();
            }
        }
        Board2D board = new Board2D(width, height);
        int row = 0;
        for (String line : lines)
        {
            var col = 0;
            for (var c : line.split(""))
            {
                board.setAtRC(row, col, parseInt(c));
                col++;
            }
            row++;
        }
        
        ArrayList<IntPair> mins = new ArrayList<>();
        for (int iRow = 0; iRow < height; iRow++)
        {
            for (int iCol = 0; iCol < width; iCol++)
            {
                var found = true;
                var val = board.getAtRC(iRow, iCol);
                for (var pair : board.neighbours4RC(Pair.of(iRow, iCol)))
                {
                    if (board.getAtRC(pair) <= val)
                    {
                        found = false;
                        break;
                    }
                }
                if (found)
                {
                    result += val + 1;
                    mins.add(Pair.of(iRow, iCol));
                }
            }
        }

        Board2D used = new Board2D(width, height);
        ArrayList<Integer> sizes = new ArrayList<>();
        for (var start : mins)
        {
            if (used.getAtRC(start) == 0)
            {
                LinkedList<IntPair> front = new LinkedList<>();
                int size = 1;
                front.add(start);
                IntPair current;
                while ((current = front.poll()) != null)
                {
                    for (var pair : board.neighbours4RC(current))
                    {
                        if (board.getAtRC(pair) >= board.getAtRC(current) && used.getAtRC(pair) == 0 && board.getAtRC(pair) != 9)
                        {
                            used.setAtRC(pair, 1);
                            front.add(pair);
                            size++;
                        }
                    }
                }
                sizes.add(size);
            }
        }
        
        Collections.sort(sizes, (a,b) -> Integer.compare(b, a));
        result = sizes.get(0) * sizes.get(1) * sizes.get(2); 
        
        System.out.println(result);
        
    }
}
