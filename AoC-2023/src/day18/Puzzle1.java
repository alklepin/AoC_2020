package day18;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;

public class Puzzle1 extends PuzzleCommon
{

    private Object m_object;

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        var inputFile = "input2_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<DataItem> items = new ArrayList<>();
        int result = 0;
        for (String line : lines)
        {
            items.add(new DataItem(line));
        }
        HashMap<IntPair, Integer> visited = new HashMap<>();
        var state = Pair.of(0, 0);
        var maxCell = state;
        var minCell = state;
        
        for (var item : items)
        {
            var dir = decodeDir(item.dir);
            var nextState = state.add(dir.mult(item.length));
            maxCell = maxCell.componentMax(nextState);
            minCell = minCell.componentMin(nextState);
            state = nextState;
        }
        var shift = minCell.reverse();
        var start = shift;
        var bounds = maxCell.add(shift);
        var board = new Board2D(bounds.getX()+1, bounds.getY()+1);
        System.out.println(minCell);
        System.out.println(maxCell);
        board.setAll('.');
        
        state = start.add(Pair.of(0,0));
        for (var item : items)
        {
            var dir = decodeDir(item.dir);
            for (var cnt = 0; cnt < item.length; cnt++)
            {
                var next = state.add(dir.mult(cnt));
                visited.put(next, item.color);
                board.setAtXY(next, '#');
            }
            var nextState = state.add(dir.mult(item.length));
            state = nextState;
        }
        
//        var res = ImplicitGraph.BFS(Pair.of(30, 10), null, 
        var res = ImplicitGraph.BFS(Pair.of(1, 1), null, 
            n -> board.neighbours4XY(n)
            .where(node -> (board.getCharAtXY(node) =='.')));
        result = res.visited().size()+1+visited.size();
        
        board.printAsStrings(System.out);
        System.out.println(result);
    }
    
    public IntPair decodeDir(char c)
    {
        return switch (c)
            {
                case 'U' -> IntPair.DOWN;
                case 'D' -> IntPair.UP;
                case 'L' -> IntPair.LEFT;
                case 'R' -> IntPair.RIGHT;
                default -> throw new IllegalStateException();
            };
    }
    
    static class DataItem
    {
        public char dir;
        public int length;
        public int color;
        
        public DataItem(String line)
        {
            var parts = line.split("\\s+");
            dir = parts[0].charAt(0);
            length = parseInt(parts[1]);
            color = parseInt(parts[2].substring(1), 16);
        }
    }
    
    
}
