package day04;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.queries.Query;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    public IntPair processGroup(LinesGroup group)
    {
        HashMap<Integer, IntPair> cells = new HashMap<>();
        Board2D board = new Board2D(5, 5);
        int i = 0;
        for (String line : group)
        {
            var numbers = line.split("\s+");
            for (var j = 0; j < numbers.length; j++)
            {
                var num = parseInt(numbers[j]);
                board.setAtRC(i, j, num);
                cells.put(num, Pair.of(i, j));
            }
            i++;
        }
        
        int [] rowUsed = new int[5];
        int [] colUsed = new int[5];
        Board2D boardUsed = new Board2D(5, 5);
        
        int step = 0;
        for (var num : steps)
        {
            step++;
            var cell  = cells.get(num);
            if (cell != null)
            {
                boardUsed.setAtRC(cell.getX(), cell.getY(), 1);
                
                rowUsed[cell.getX()]++;
                colUsed[cell.getY()]++;
                
                if (rowUsed[cell.getX()] == 5 || colUsed[cell.getY()] == 5)
                {
                    int score = 0;
                    for (int row = 0; row < 5; row++)
                    {
                        for (int col = 0; col < 5; col++)
                        {
                            if (boardUsed.getAtRC(row, col) == 0)
                            {
                                score += board.getAtRC(row, col);
                            }
                        }
                    }
                    return new IntPair(step, score * num);
                }
            }
        }
        
        return new IntPair(step, -1);
    }
    
    ArrayList<Integer> steps;
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());
        
        var selectedNumbers = groups.get(0).get(0).split(",");
        steps = new ArrayList<Integer>();
        for (var s : selectedNumbers)
        {
            steps.add(parseInt(s));
        }
        
        IntPair best = new IntPair(-1, 0);
        int board = 0;
        for (LinesGroup group : Query.wrap(groups).skip(1))
        {
            board++;
            var score = group.processGroup(this::processGroup);
            if (score.getX() > best.getX())
            {
                best = score;
            }
            System.out.println("Board:"+board+" steps:"+score.getX()+" score:"+score.getY());
        }
        System.out.println(best.getY());
        
//        LinesGroup lines = readAllLines("input1.txt");
        
//        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
}
