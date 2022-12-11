package day08;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
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
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLines("input1_test.txt");
        
        Board2D board = new Board2D(lines.get(0).length(), lines.size());
        
        for (var row = 0; row < board.getHeigth(); row++)
        {
            var line = lines.get(row);
            for (var col = 0; col < line.length(); col++)
            {
                board.setAtRC(row,  col, parseInt(line.substring(col, col+1)));
            }
        }
        
        IntPair bestPoint = new IntPair(-1, -1);
        long bestScore = -1;
        for (int row = 0; row < board.getHeigth(); row++)
        {
            for (int col = 0; col < board.getWidth(); col++)
            {
                var currentCell = new IntPair(row, col);
                var d1 = distance(board, currentCell, Pair.of(1,0));
                var d2 = distance(board, currentCell, Pair.of(-1,0));
                var d3 = distance(board, currentCell, Pair.of(0,1));
                var d4 = distance(board, currentCell, Pair.of(0,-1));
                var scenicScore = ((long)d1) * d2 * d3 * d4;
                if (scenicScore > bestScore)
                {
                    bestScore = scenicScore;
                    bestPoint = currentCell;
                }
            }
        }
        
        System.out.println(bestScore);
        
    }
    
    private int distance(Board2D board, IntPair currentCell, IntPair delta)
    {
        var distance = 0;
        var height = board.getAtRC(currentCell);
        for (var cell : Generators.ray(currentCell, delta, 200))
        {
            if (board.containsRC(cell))
            {
                distance++;
                if (board.getAtRC(cell) >= height)
                {
                    break;
                }
            }
            else
            {
                break;
            }
        }
        return distance;
    }
}
