package day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Bounds;
import common.boards.Generators;
import common.boards.IntPair;

public class Puzzle2Test extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2Test().solve();
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
        for (var s : sensors)
        {
            for (var p : Generators.rhomb(s.location, s.distance+1))
                candidates.add(p);
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
    }
}
