package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Line;
import common.geometry.Vect2D;
import common.geometry.Vect3D;
import common.geometry.Vect3I;

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
        
        var item1 = items.get(0);
        var item2 = items.get(10);
        var item3 = items.get(30);
        long start; 
//        var start = 1800000000l;
//        var start = 5000000000000l;
        start = 54760742187l-10;;
//        54728889464
        
        var step = 1;
        System.out.println("Start time: " + start + " step: "+step);
//        for (long time = start; time < start + 1000*step; time+= step)
        for (long time = 54729962348l; time < 54729965328l; time+= 1)
        {
            var pos1 = item1.pos.add(item1.vel.mult(time)); 
            var pos2 = item2.pos.add(item2.vel.mult(time)); 
            var pos3 = item3.pos.add(item3.vel.mult(time));
            
            var v1 = pos3.minus(pos2);
            var v2 = pos2.minus(pos1);
            
            var scalar = v1.scalarMult(v2);
            var snorm = scalar/v1.length()/v2.length();
            
//            System.out.println("Scalar: " + snorm);

            var pos11 = item1.pos.add(item1.vel.mult(time+1)); 
            var pos21 = item2.pos.add(item2.vel.mult(time+1)); 
            var pos31 = item3.pos.add(item3.vel.mult(time+1));
            
            var v11 = pos31.minus(pos21);
            var v21 = pos21.minus(pos11);
            
            var scalar1 = v11.scalarMult(v21);
            var snorm1 = scalar/v11.length()/v21.length();
            
            var diff = snorm1 - snorm;
//            System.out.println("Diff: " + diff);
            
            if (Math.abs(snorm) >= 0.9999999999)
                System.out.println("Time: " + time);
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
