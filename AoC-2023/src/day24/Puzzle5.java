package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Line;
import common.geometry.Line3D;
import common.geometry.Vect2D;
import common.geometry.Vect3D;
import common.geometry.Vect3I;
import day24.Puzzle6.Item;

public class Puzzle5 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle5().solve();
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
        var srcMult = 1000000d;
        for (String line : lines)
        {
            var parts = line.split("\\@");
            var pos3D = Vect3D.from(parts[0].trim());
            var vel3D = Vect3D.from(parts[1].trim());
            var item = new Item(pos3D.divideBy(srcMult), vel3D);
            items.add(item);
        }
        
        var item1 = items.get(0);
        var item2 = items.get(1);
        var item3 = items.get(2);

        long start1 = 300000000000000l;
        long start2 = 300000000000000l;
        long start3 = 300000000000000l;
//        long start1 = 316191576162l;
//        long start2 = 316191576162l;
//        long start3 = 749603755203l;
        long step = 10000l;
        long incStep = 100000000000000000l;
        
        var stop = false;
        var t1 = start1;
        var t2 = start2;
        var t3 = start3;
        while (!stop)
        {
            var a = angleAt(item1, t1, item2, t2, item3, t3);
            var diff = angleDiffAt(item1, t1, item2, t2, item3, t3);
            t1 += (long)(diff.getX() * incStep);
            t2 += (long)(diff.getY() * incStep);
            t3 += (long)(diff.getZ() * incStep);
            System.out.println("t1: "+t1+" t2:"+t2+" t3:"+t3+" angleAt: "+a);
            if (diff.length() < 1E-13)
                stop = true;
        }

//        System.out.println(d1);
//        System.out.println(d2);

        
        
        System.out.println(result);
        
    }
    
    public double angleAt(Item item1, long t1, Item item2, long t2, Item item3, long t3)
    {
        var p1 = item1.locationAt(t1);
        var p2 = item2.locationAt(t2);
        var p3 = item3.locationAt(t3);
        
        var v1 = p2.minus(p1);
        var v2 = p3.minus(p1);
        
        var v1norm = v1.divideBy(v1.length());
        var v2norm = v2.divideBy(v2.length());
        
        var scalar = v1norm.scalarMult(v2norm);
        
        return scalar;
    }
    
    static int diffStep = 100;
    
    public Vect3D angleDiffAt(Item item1, long t1, Item item2, long t2, Item item3, long t3)
    {
        var a = angleAt(item1, t1, item2, t2, item3, t3);
        var a1 = angleAt(item1, t1 - diffStep, item2, t2, item3, t3);
        var a2 = angleAt(item1, t1, item2, t2 + diffStep, item3, t3);
        var a3 = angleAt(item1, t1, item2, t2, item3, t3 + diffStep);
        return Vect3D.of(a1-a, a2-a, a3-a);
    }
    
    public static class Item
    {
        Vect3D pos;
        Vect3D vel;
        Line line2D;
        Line3D line3D;
        public Item(Vect3D pos, Vect3D vel)
        {
            super();
            this.pos = pos;
            this.vel = vel;
            
            var pnt = new Vect2D(pos.getX(), pos.getY());
            var v = new Vect2D(vel.getX(), vel.getY());
            
            line2D = new Line(pnt, v);
            line3D = new Line3D(pos, vel);
             
        }
        
        public Vect3D locationAt(long time)
        {
            return pos.add(vel.mult(time));
        }
        
        public Line get2DLine()
        {
            return line2D;
        }
        
    }
}
