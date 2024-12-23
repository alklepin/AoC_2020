package day14;

import java.util.ArrayList;

import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle1 extends PuzzleCommon
{

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
        var inputFile = "input1.txt"; var dims = IntPair.of(101, 103);
        
//        var inputFile = "input1_test.txt"; var dims = IntPair.of(11, 7);
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var robots = new ArrayList<Robot>();
        for (String line : lines)
        {
            var lineCleaned = line.replace("p=", "").replace(" v=", " ");
            var p = new LineParser(lineCleaned);
            var values = p.listOfInts();
            var pos = IntPair.of(values.get(0), values.get(1));
            var vel = IntPair.of(values.get(2), values.get(3));
            robots.add(new Robot(pos, vel));
        }
        
        var steps = 100;
        var counts = new int[4];
        for (var r : robots)
        {
            var newPos = r.position.add(r.velocity.mult(steps)).componentModulo(dims);
            var x = newPos.getX();
            var y = newPos.getY();
            if (x == dims.getX() / 2 || y == dims.getY() / 2)
                continue;
            var idx = (x < dims.getX() / 2 ? 0 : 1) +  (y < dims.getY() / 2 ? 0 : 1) * 2;
            counts[idx]++;
        }
        
        var result = ((long)counts[0]) * counts[1] * counts[2] * counts[3];
        
        
        System.out.println(result);
        
    }
    
    static class Robot
    {
        IntPair position;
        IntPair velocity;
        public Robot(IntPair position, IntPair velocity)
        {
            super();
            this.position = position;
            this.velocity = velocity;
        }
        
        
    }
}
