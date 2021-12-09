package day09;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators.Neighbours4Generator;
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
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        
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
        
        IntPair min = new IntPair(0,0);
        IntPair max = new IntPair(height-1,width-1);
        for (int iRow = 0; iRow < height; iRow++)
        {
            for (int iCol = 0; iCol < width; iCol++)
            {
                var found = true;
                var val = board.getAtRC(iRow, iCol);
                for (var pair : new Neighbours4Generator(Pair.of(iRow, iCol), min, max))
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
                }
            }
        }
        
        System.out.println(result);
        
    }
}
