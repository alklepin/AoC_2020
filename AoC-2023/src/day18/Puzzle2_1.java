package day18;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Boards;
import common.boards.IntPair;
import common.boards.Pair;
import common.geometry.Vect2D;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2_1 extends PuzzleCommon
{

    private Object m_object;

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_1().solve();
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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
//        var inputFile = "input2_test.txt";
//        var inputFile = "input2_1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<DataItem> items = new ArrayList<>();
        for (String line : lines)
        {
            items.add(new DataItem(line));
        }
        ArrayList<IntPair> points = new ArrayList<>();
        var start = Pair.of(0, 0);
        var prev = start;
        for (var item : items)
        {
            var dir = decodeDir(item.dir);
            var pnt = prev.add(dir.mult(item.length));
            points.add(pnt);
            prev = pnt;
        }
        System.out.println(Boards.areaOfGrid(points));
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
