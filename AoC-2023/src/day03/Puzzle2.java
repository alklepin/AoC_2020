package day03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        var board = Board2D.parseAsCharsXY(lines);
        var gears = new HashMap<IntPair, ArrayList<Integer>>();
        
        for (var p : board.allCellsXY())
        {
            if (board.getCharAtXY(p) == '*')
                gears.put(p, new ArrayList<>());
        }
        
        for (var y = 0; y < board.getHeigth(); y++)
        {
            for (var x = 0; x < board.getWidth(); x++)
            {
                var cell = new IntPair(x, y);
                if ("0123456789".indexOf(board.getAtXY(cell)) >= 0)
                {
                    var curGears = new HashSet<IntPair>();
                    var b = new StringBuilder();
                    b.append(board.getCharAtXY(cell));
                    for (var p : board.neighbours8XY(cell))
                    {
                        if (board.getCharAtXY(p) == '*')
                            curGears.add(p);
                    }
                    cell = cell.nextRight();
                    while (board.containsXY(cell) && "0123456789".indexOf(board.getAtXY(cell)) >= 0)
                    {
                        b.append(board.getCharAtXY(cell));
                        for (var p : board.neighbours8XY(cell))
                        {
                            if (board.getCharAtXY(p) == '*')
                                curGears.add(p);
                        }
                        cell = cell.nextRight();
                    }
                    x = cell.getX();
                    
                    var val = parseInt(b.toString());
                    for (var gear : curGears)
                    {
                        gears.get(gear).add(val);
                    }
                }
            }
        }
        var result = 0;
        for (var list : gears.values())
        {
            if (list.size() == 2)
            {
                result += list.get(0) * list.get(1);
            }
        }
        System.out.println(result);
        
    }
}
