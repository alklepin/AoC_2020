package day03;

import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;

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
        var result = 0;
        for (var y = 0; y < board.getHeigth(); y++)
        {
            for (var x = 0; x < board.getWidth(); x++)
            {
                if ("0123456789".indexOf(board.getAtXY(x, y)) >= 0)
                {
                    var good = false; 
                    var b = new StringBuilder();
                    b.append(board.getCharAtXY(x, y));
                    for (var p : Generators.neighbours8(x, y, 0, 0, board.getWidth() - 1, board.getHeigth()-1))
                    {
                        good |= "0123456789.".indexOf(board.getAtXY(p)) < 0;
                    }
                    x++;
                    while (x < board.getWidth() && "0123456789".indexOf(board.getAtXY(x, y)) >= 0)
                    {
                        b.append(board.getCharAtXY(x, y));
                        for (var p : Generators.neighbours8(x, y, 0, 0, board.getWidth()-1, board.getHeigth()-1))
                        {
                            good |= "0123456789.".indexOf(board.getAtXY(p)) < 0;
                        }
                        x++;
                    }
                    if (good)
                    {
                        var val = parseInt(b.toString());
                        result += val;
                    }
                }
            }
        }
        System.out.println(result);
        
    }
}
