package day18;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;

public class Puzzle2 extends PuzzleCommon
{

    private Object m_object;

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
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
        var inputFile = "input1_test.txt";
//        var inputFile = "input2_test.txt";
        
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
//        var board = new Board2D(bounds.getX()+1, bounds.getY()+1);
        System.out.println(minCell);
        System.out.println(maxCell);
//        board.setAll('.');
        
//        state = start.add(Pair.of(0,0));
        state = start;
        long area = 0;
        long area1 = 0;
        long delta = 0;
        long straight = 0;
        long right = 0;
        long left = 0;
        IntPair prevDir = decodeDir(items.get(items.size()-1).dir);
        for (var item : items)
        {
            var dir = decodeDir(item.dir);
            var nextState = state.add(dir.mult(item.length));
            var v1 = state;
            var v2 = (nextState.minus(state));
            var md = prevDir.vectorMult(dir);
            delta += item.length-1;
            if (md > 0)
                right++;
            else if (md < 0)
                left++;
            else
                straight++;
            area += v1.vectorMult(v2);
            area1 += v1.asVector().vectorMult(v2.asVector());
            state = nextState;
            prevDir = dir;
        }
        System.out.println(delta/2 + (3*right + left) / 4 + straight / 2 + area/2);
        
        System.out.println("delta: "+delta);
        System.out.println("right: "+right);
        System.out.println("left: "+left);
        System.out.println("straight: "+straight);
        System.out.println("area: "+area);
//        board.printAsStrings(System.out);

//        System.out.println(1l+delta+area/2);
//        System.out.println(1l+delta+area1/2);
//        System.out.println(area1/2);
    }
    
    public static IntPair decodeDirOld(char c)
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

    public static IntPair decodeDir(char c)
    {
        return switch (c)
            {
                case '3' -> IntPair.DOWN;
                case '1' -> IntPair.UP;
                case '2' -> IntPair.LEFT;
                case '0' -> IntPair.RIGHT;
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
            var code = parts[2].substring(2, parts[2].length()-1);
            color = Integer.parseInt(code.substring(0, 5), 16);
            
            length = color;
            dir = code.charAt(5);
        }
        
        
    }
    
    
}
