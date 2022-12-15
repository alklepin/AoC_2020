package day15;

import java.util.ArrayList;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Bounds;
import common.boards.IntPair;
import common.geometry.Segment;
import common.geometry.Vect2D;

public class Puzzle2Test2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2Test2().solve();
    }
    
    class Sensor
    {
        public IntPair location;
        public IntPair beacon;
        public int distance;
        
        public Sensor(IntPair location, IntPair beacon)
        {
            this.location = location;
            this.beacon = beacon;
            this.distance = beacon.minus(location).lengthManh();
        }

        public boolean covers(IntPair p)
        {
            return p.minus(location).lengthManh() <= distance;
        }
    }
    
    public void solve()
        throws Exception
    {
        long time = System.currentTimeMillis();
        
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        

        ArrayList<Sensor> sensors = new ArrayList<>();
        HashSet<IntPair> beacons = new HashSet<>(); 
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        int maxDistance = 0;
        Bounds bounds = new Bounds();
        for (String line : lines)
        {
            var data = parse("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)", line);
            var location = IntPair.of(data[1], data[2]); 
            var beacon = IntPair.of(data[3], data[4]);
            var sensor = new Sensor(location, beacon);
            sensors.add(sensor);
            beacons.add(beacon);
            bounds.extendBy(sensor.location);
            maxDistance = Math.max(maxDistance, sensor.distance);
        }
//        System.out.println(maxDistance);
//        System.out.println(bounds.min());
//        System.out.println(bounds.max());

//        var board = new Board2D(20, 20);
//        board.setAll('.');
//        for (var s : sensors)
//        {
//            for (var c : board.allCellsXY())
//            {
//                if (!board.containsXY(c))
//                    continue;
//                if (s.covers(c) && board.getAtXY(c) == '.')
//                    board.setAtXY(c, '#');
//            }
//            if (board.containsXY(s.location))
//                board.setAtXY(s.location, 'S');
//            if (board.containsXY(s.beacon))
//                board.setAtXY(s.beacon, 'B');
//        }
//        board.printAsStrings(System.out);
//        
//        var testP = IntPair.of(14, 11);
//        for (var s : sensors)
//        {
//            System.out.println("Sensor: ");
//            System.out.println("MaxDistance: " + s.distance);
//            System.out.println("Distance: " + testP.minus(s.location).lengthManh());
//        }
        
        
        
        HashSet<IntPair> candidates = new HashSet<>();
        for (var s1 : sensors)
        {
            var d1 = s1.distance;
            var p1l = s1.location.add(IntPair.LEFT.mult(d1+1));
            var p1u = s1.location.add(IntPair.UP.mult(d1+1));
            var p1r = s1.location.add(IntPair.RIGHT.mult(d1+1));
            var p1d = s1.location.add(IntPair.DOWN.mult(d1+1));
            for (var s2 : sensors)
            {
                if (s2 == s1)
                    continue;

                var d2 = s2.distance;
                var p2l = s2.location.add(IntPair.LEFT.mult(d2+1));
                var p2u = s2.location.add(IntPair.UP.mult(d2+1));
                var p2r = s2.location.add(IntPair.RIGHT.mult(d2+1));
                var p2d = s2.location.add(IntPair.DOWN.mult(d2+1));
                
                var s1lu = new Segment(p1l, p1u);
                var s1ur = new Segment(p1u, p1r);
                var s1rd = new Segment(p1r, p1d);
                var s1dl = new Segment(p1d, p1l);

                var s2lu = new Segment(p2l, p2u);
                var s2ur = new Segment(p2u, p2r);
                var s2rd = new Segment(p2r, p2d);
                var s2dl = new Segment(p2d, p2l);
                
                Vect2D p;

                p = s1lu.intersect(s2ur);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());
                
                p = s1lu.intersect(s2dl);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());
                
                p = s1rd.intersect(s2ur);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());
                
                p = s1rd.intersect(s2dl);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());

                p = s2lu.intersect(s1ur);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());

                p = s2lu.intersect(s1dl);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());

                p = s2rd.intersect(s1ur);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());

                p = s2rd.intersect(s1dl);
                if (p != null && p.minus(p.asIntPair().asVector()).length() <= 0.1)
                    candidates.add(p.asIntPair());
            }
        }

//        for (var p : candidates)
//        {
//            if (board.containsXY(p))
//                board.setAtXY(p, '*');
//        }
//        board.printAsStrings(System.out);
        
        
        var limit = 4000000;
//        var limit = 20;
        
        var minLimit = IntPair.ZERO;
        var maxLimit = IntPair.of(limit, limit);
        
        for (var p : candidates)
        {
            if (!p.inRectangle(minLimit, maxLimit))
                continue;

            var covered = false;
            for (var s1 : sensors)
            {                
                if (s1.covers(p))
                {
                    covered = true;
                    break;
                }
            }
            if (!covered)
            {
                System.out.println(p);
                System.out.println(p.getX() * 4000000l + p.getY());
            }
        }
        
        long time1 = System.currentTimeMillis();
        System.out.println(time1-time);
        
    }
}
