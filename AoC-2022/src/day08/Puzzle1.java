package day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

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
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLines("input1_test.txt");
        Board2D board = new Board2D(lines.get(0).length(), lines.size());
        for (var row = 0; row < board.getHeigth(); row++)
        {
            var line = lines.get(row);
            for (var col = 0; col < line.length(); col++)
            {
                board.setAtRC(row,  col, parseInt(line.substring(col, col+1)));
            }
        }
        HashSet<IntPair> visible = new HashSet<>();
        for (int row = 0; row < board.getHeigth(); row++)
        {
            int currentHeight = -1;
            for (int col = 0; col < board.getWidth(); col++)
            {
                var height = board.getAtRC(row, col);
                if (height > currentHeight)
                {
                    visible.add(new IntPair(row,  col));
                    currentHeight = height;
                }
            }
            currentHeight = -1;
            for (int col = board.getWidth()-1; col >= 0; col--)
            {
                var height = board.getAtRC(row, col);
                if (height > currentHeight)
                {
                    visible.add(new IntPair(row,  col));
                    currentHeight = height;
                }
            }
        }
        
        for (int col = 0; col < board.getWidth(); col++)
        {
            int currentHeight = -1;
            for (int row = 0; row < board.getHeigth(); row++)
            {
                var height = board.getAtRC(row, col);
                if (height > currentHeight)
                {
                    visible.add(new IntPair(row,  col));
                    currentHeight = height;
                }
            }
            currentHeight = -1;
            for (int row = board.getHeigth()-1; row >= 0; row--)
            {
                var height = board.getAtRC(row, col);
                if (height > currentHeight)
                {
                    visible.add(new IntPair(row,  col));
                    currentHeight = height;
                }
            }
        }
        System.out.println(visible.size());
        
    }
}
