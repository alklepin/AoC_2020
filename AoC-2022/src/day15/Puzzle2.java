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

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
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
    
    public IntPair LEFT = IntPair.of(-1, 0);
    public IntPair RIGHT = IntPair.of(1, 0);
    public IntPair UP = IntPair.of(0, 1);
    public IntPair DOWN = IntPair.of(0, -1);
    
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
            var location = IntPair.of(parseInt(data[1]), parseInt(data[2])); 
            var beacon = IntPair.of(parseInt(data[3]), parseInt(data[4]));
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
            for (var p : Generators.ray(s.location.add(LEFT.mult(s.distance+1)), UP.add(RIGHT), s.distance, true))
                candidates.add(p);
            for (var p : Generators.ray(s.location.add(UP.mult(s.distance+1)), DOWN.add(RIGHT), s.distance, true))
                candidates.add(p);
            for (var p : Generators.ray(s.location.add(RIGHT.mult(s.distance+1)), DOWN.add(LEFT), s.distance, true))
                candidates.add(p);
            for (var p : Generators.ray(s.location.add(DOWN.mult(s.distance+1)), UP.add(LEFT), s.distance, true))
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
        for (var p : candidates)
        {
            if (p.getX() < 0 || p.getX() > limit)
                continue;
            if (p.getY() < 0 || p.getY() > limit)
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
