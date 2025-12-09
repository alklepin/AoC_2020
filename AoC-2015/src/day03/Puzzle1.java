package day03;

import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.queries.Query;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
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
        
        IntPair pos = IntPair.of(0, 0);
        HashMap<IntPair, IntHolder> counters = new HashMap<>();
        counters.put(pos, new IntHolder(1));
        for (char c : line.toCharArray())
        {
            
            var nextPos = pos.add(IntPair.decodeDirectionV_XY(c));
            var count = counters.computeIfAbsent(nextPos, p -> new IntHolder(0));
            count.value++;
            pos = nextPos;
        }
        int result = Query.wrap(counters.values()).where(c -> c.value >= 1).count();
        System.out.println(result);
        
    }
}
