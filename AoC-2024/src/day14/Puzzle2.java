package day14;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

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
        
        try (var fout = new FileWriter("output.txt");
            var pw = new PrintWriter(fout))
        {
            for (var step = 1; step < dims.getX() * dims.getY(); step++)
            {
                processAtStep(step, dims, robots, pw);
            }
            pw.flush();
        }
        // Then look at the output, find "##################" and write down step count
        // 7520
    }
    
    public void processAtStep(int step, IntPair dims, ArrayList<Robot> robots, PrintWriter w)
    {
        var board = new Board2D(dims.getX(), dims.getY());
        board.setAll(' ');
        for (var r : robots)
        {
            var newPos = r.position.add(r.velocity.mult(step)).componentModulo(dims);
            var x = newPos.getX();
            var y = newPos.getY();
            board.setCharAtXY(x, y, '#');
        }
        w.println("Step="+step);
        board.printAsStrings(w);
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
