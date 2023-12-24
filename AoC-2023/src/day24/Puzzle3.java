package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Line;
import common.geometry.Vect2D;
import common.geometry.Vect3D;
import common.geometry.Vect3I;

public class Puzzle3 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle3().solve();
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
        var item2 = items.get(3);
        var item3 = items.get(6);
        long start; 
//        var start = 1800000000l;
//        var start = 5000000000000l;
        long left = 0;
        long right = 100000000000l;
        
        double leftVal = -10;
        double rightVal = 10;
        int sign = -1;
        while (left < right)
        {
            System.out.println("Left time: " + left + " right: "+right);
            System.out.println("Left : " + leftVal + " right: "+rightVal);
            long next = (left + right) / 2;
            
            var pos1 = item1.pos.add(item1.vel.mult(next)); 
            var pos2 = item2.pos.add(item2.vel.mult(next)); 
            var pos3 = item3.pos.add(item3.vel.mult(next));
            
            var v1 = pos3.minus(pos2);
            var v2 = pos2.minus(pos1);
            
            var scalar = v1.scalarMult(v2);
            var snorm = scalar/v1.length()/v2.length();
            
            System.out.println("Scalar: " + snorm);

            var diffStep = 10000;
            var pos11 = item1.pos.add(item1.vel.mult(next+diffStep)); 
            var pos21 = item2.pos.add(item2.vel.mult(next+diffStep)); 
            var pos31 = item3.pos.add(item3.vel.mult(next+diffStep));
            
            var v11 = pos31.minus(pos21);
            var v21 = pos21.minus(pos11);
            
            var scalar1 = v11.scalarMult(v21);
            var snorm1 = scalar/v11.length()/v21.length();
            
            var diff = snorm1 - snorm;
            System.out.println("Diff: " + diff);
            if (sign*diff < 0)
            {
                left = next;
                leftVal = diff;
            }
            else if (sign*diff > 0)
            {
                right = next;
                rightVal = diff;
            }
            else
            {
                System.out.println("Found: " + next);
                System.out.println("Diff: " + diff);
                break;
            }
            
            if (Math.abs(v1.scalarMult(v2)) < 0.0001)
                System.out.println("Time: " + left);
            break;
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
