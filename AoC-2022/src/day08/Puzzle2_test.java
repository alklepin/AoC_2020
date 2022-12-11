package day08;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.queries.Query;

public class Puzzle2_test extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_test().solve();
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
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLines("input1_test.txt");
        var board = Board2D.parseAsChars(lines);

        var bestScore = -1;
        for (var currentCell : board.allCellsRC())
        {
            var d1 = distance(board, currentCell, Pair.of(1,0));
            var d2 = distance(board, currentCell, Pair.of(-1,0));
            var d3 = distance(board, currentCell, Pair.of(0,1));
            var d4 = distance(board, currentCell, Pair.of(0,-1));
            var scenicScore = d1 * d2 * d3 * d4;
            if (scenicScore > bestScore)
            {
                bestScore = scenicScore;
            }
        }
        
        System.out.println(bestScore);

        var bestScoreAlt = 
            Query.wrap(board.allCellsRC())
                .select(cell ->
                    distance(board, cell, Pair.of(1,0))
                    * distance(board, cell, Pair.of(-1,0))
                    * distance(board, cell, Pair.of(0,1))
                    * distance(board, cell, Pair.of(0,-1))
                    )
                .max();
        System.out.println(bestScoreAlt);
        
    }
    
    private int distance(Board2D board, IntPair currentCell, IntPair delta)
    {
        var distance = 0;
        var height = board.getAtRC(currentCell);
        for (var cell : board.rayRC(currentCell, delta))
        {
            distance++;
            if (board.getAtRC(cell) >= height)
            {
                break;
            }
        }
        return distance;
    }
}
