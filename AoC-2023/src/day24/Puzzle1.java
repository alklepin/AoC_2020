package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Line;
import common.geometry.Vect2D;
import common.geometry.Vect3D;
import common.geometry.Vect3I;

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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        ArrayList<Item> items = new ArrayList<>();
        for (String line : lines)
        {
            var parts = line.split("\\@");
            var pos3D = Vect3D.from(parts[0].trim());
            var vel3D = Vect3D.from(parts[1].trim());
            var item = new Item(pos3D, vel3D);
            items.add(item);
        }
        
//        var minBound = new Vect2D(7,7);
//        var maxBound = new Vect2D(27,27);
        var minBound = new Vect2D(200000000000000d,200000000000000d);
        var maxBound = new Vect2D(400000000000000d,400000000000000d);
        
        for (var idx1 = 0; idx1 < items.size(); idx1++)
        {
            var i1 = items.get(idx1);
            for (var idx2 = idx1+1; idx2 < items.size(); idx2++)
            {
                var i2 = items.get(idx2);
                
                var line1 = i1.line2D;
                var line2 = i2.line2D;
                var intersPnt = line1.intersectWith(line2);
                
                if (intersPnt == null)
                    continue;
                if (!intersPnt.inRectangle(minBound, maxBound))
                    continue;
                if (intersPnt.minus(line1.getPoint()).scalarMult(line1.getDirection()) >= 0
                    && intersPnt.minus(line2.getPoint()).scalarMult(line2.getDirection()) >= 0
                    )
                    result++;
            }
        }
        
        System.out.println(result);
        
    }
    
    public static class Item
    {
        Vect3D pos;
        Vect3D vel;
        Line line2D;
        public Item(Vect3D pos, Vect3D vel)
        {
            super();
            this.pos = pos;
            this.vel = vel;
            
            var pnt = new Vect2D(pos.getX(), pos.getY());
            var v = new Vect2D(vel.getX(), vel.getY());
            
            line2D = new Line(pnt, v);
             
        }
        
        public Line get2DLine()
        {
            return line2D;
        }
        
    }
}
