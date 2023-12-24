package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Line;
import common.geometry.Line3D;
import common.geometry.Plane3D;
import common.geometry.Vect2D;
import common.geometry.Vect3D;

public class Puzzle6 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle6().solve();
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
//        var srcMult = 1000000000d;
//        var srcMult = 10000000000d;
        var srcMult = 1d;
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
        
        // Go to the coordinate system related to the first hailstone
        var i2 = new Item(item2.pos.minus(item1.pos), item2.vel.minus(item1.vel));
        var i3 = new Item(item3.pos.minus(item1.pos), item3.vel.minus(item1.vel));
        
        // In this space we have the following:
        // hailstone #1 stands still at (0,0,0)
        // hailstone #2 moves (so we have a plane defined by hailstones #1 and #2
        // so we have to find a moment of time when hailstone #3 intersects this plane
        // (it means that all the three hailstones intersect the same line in a space)
        
        var l2 = new Line3D(i2.pos, i2.vel);
        var l3 = new Line3D(i3.pos, i3.vel);


        // Find intersection time for the hailstone #2
        var p3 = new Plane3D(Vect3D.ZERO, l3);
        var r3 = p3.intersectWith(l2);
        var t2 = r3.minus(l2.getPoint()).length() / i2.vel.length();
        
        // Find intersection time for the hailstone #3
        var p2 = new Plane3D(Vect3D.ZERO, l2);
        var r2 = p2.intersectWith(l3);
        var t3 = r2.minus(l3.getPoint()).length() / i3.vel.length();
        
        System.out.println("t2: "+(long)(t2*srcMult)+ " t3: "+(long)(t3*srcMult));
        
        // Knowing intersection times we may find points through which a stone should move
        var pnt2 = item2.pos.add(item2.vel.mult(t2));
        var pnt3 = item3.pos.add(item3.vel.mult(t3));
        
        // Find velocity direction for the stone
        var dir = pnt3.minus(pnt2);
        // and it's source position
        var s = pnt3.minus(dir.divideBy(t3-t2).mult(t3));
//        System.out.println(s);
//        System.out.println(s.scalarMult(Vect3D.of(1, 1, 1)));
//        System.out.println(s.scalarMult(Vect3D.of(1, 1, 1))*srcMult);
        System.out.println("Result: "+(long)(s.scalarMult(Vect3D.of(1, 1, 1))*srcMult));
        var a =1 ;
        
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
    
    static int diffStep = 1000;
    
    public Vect3D angleDiffAt(Item item1, long t1, Item item2, long t2, Item item3, long t3)
    {
        var a = angleAt(item1, t1, item2, t2, item3, t3);
        var a1 = angleAt(item1, t1 + diffStep, item2, t2, item3, t3);
        var a2 = angleAt(item1, t1, item2, t2 + diffStep, item3, t3);
        var a3 = angleAt(item1, t1, item2, t2, item3, t3 + diffStep);
        return Vect3D.of(a1 - a, a2 - a, a3 - a);
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
