package day11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
import common.boards.IntPair;
import common.boards.Pair;

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
        
//        ArrayList<String> lines = readAllLines("input1.txt");

        Board2D board = new Board2D(10,10);
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
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
        IntPair min = Pair.of(0, 0);
        IntPair max = Pair.of(board.getHeigth()-1, board.getWidth()-1);
        
        for (int step = 1; step <= 100; step++)
        {
            for (row = 0; row < board.getHeigth(); row++)
            {
                for (var col = 0; col < board.getHeigth(); col++)
                {
                    IntPair current = Pair.of(row,  col);
                    var newValue = board.getAtRC(current) + 1;
                    board.setAtRC(current, newValue);
                    if (newValue > 9)
                    {
                        result++;
                        flashes.add(current);
                        board.setAtRC(current, 0);
                    }
                }
            }
            IntPair f = null;
            while ((f = flashes.poll()) != null)
            {
                board.setAtRC(f, 0);
                for (var next : new Generators.Neighbours8Generator(f, min, max))
                {
                    var value = board.getAtRC(next);
                    if (value > 0)
                    {
                        var newValue = value + 1;
                        board.setAtRC(next, newValue);
                        if (newValue > 9)
                        {
                            result++;
                            flashes.add(next);
                            board.setAtRC(next, 0);
                        }
                    }
                }
            }
            System.out.println("Step: "+step);
            board.printAsInts(System.out);
        }
        
        
        System.out.println(result);
        
    }
}
