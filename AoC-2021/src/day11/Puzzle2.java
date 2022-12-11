package day11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import common.IntValue;
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

        Board2D board = new Board2D(10,10);
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input2.txt");
        int result = 0;
        int row = 0;
        for (String line : lines)
        {
            for (int i = 0; i < line.length(); i++)
            {
                board.setAtRC(row, i, parseInt(line.substring(i, i+1)));
            }
            row++;
        }
        LinkedList<IntPair> flashes = new LinkedList<IntPair>();
        for (int step = 1; step <= 2000; step++)
        {
            IntValue count = new IntValue(0);
            board.modifyEachCellRC((cell, value) -> {
                var newValue = value + 1;
                if (newValue > 9)
                {
                    newValue = 0;
                    flashes.add(cell);
                    count.inc();
                }
                return newValue;
            });
            
            IntPair f = null;
            while ((f = flashes.poll()) != null)
            {
                for (var next : board.neighbours8RC(f))
                {
                    var value = board.getAtRC(next);
                    if (value > 0)
                    {
                        var newValue = value + 1;
                        board.setAtRC(next, newValue);
                        if (newValue > 9)
                        {
                            flashes.add(next);
                            board.setAtRC(next, 0);
                            count.inc();
                        }
                    }
                }
            }
//            System.out.println("Step: "+step);
//            board.printAsInts(System.out);
            if (count.getValue() == 100)
            {
                result = step;
                break;
            }
        }
        
        System.out.println(result);
        
    }
}
