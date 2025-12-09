package day03;

import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.queries.Query;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    private static class IntHolder
    {
        int value;
        
        public IntHolder(int v)
        {
            value = v;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var line = lines.get(0);
        
        IntPair pos1 = IntPair.of(0, 0);
        IntPair pos2 = IntPair.of(0, 0);
        HashMap<IntPair, IntHolder> counters = new HashMap<>();
        counters.put(pos1, new IntHolder(1));
        int move = 0;
        for (char c : line.toCharArray())
        {
            if (move % 2 == 0)
            {
                var nextPos = pos1.add(IntPair.decodeDirectionV_XY(c));
                var count = counters.computeIfAbsent(nextPos, p -> new IntHolder(0));
                count.value++;
                pos1 = nextPos;
            }
            else
            {
                var nextPos = pos2.add(IntPair.decodeDirectionV_XY(c));
                var count = counters.computeIfAbsent(nextPos, p -> new IntHolder(0));
                count.value++;
                pos2 = nextPos;
            }
            move++;
        }
        int result = Query.wrap(counters.values()).where(c -> c.value >= 1).count();
        System.out.println(result);
        
    }
}
