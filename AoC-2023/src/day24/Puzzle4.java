package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Line;
import common.geometry.Vect2D;
import common.geometry.Vect3D;
import common.geometry.Vect3I;

public class Puzzle4 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle4().solve();
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
        var item2 = items.get(1);
        var item3 = items.get(2);

        var dp = item1.pos.minus(item2.pos); 
        var dv = item1.vel.minus(item2.vel);
        
        var d1 = (dv.getX() * dp.getY() - dv.getY() * dp.getX()) / (dv.getY() * item2.vel.getX() - dv.getX() * item2.vel.getY()); 
        var d2 = (dv.getX() * dp.getZ() - dv.getZ() * dp.getX()) / (dv.getZ() * item2.vel.getX() - dv.getX() * item2.vel.getZ()); 

        System.out.println(d1);
        System.out.println(d2);

        
        
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
