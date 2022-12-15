package day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Bounds;
import common.boards.IntPair;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
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
        System.out.println(maxDistance);
        System.out.println(bounds.min());
        System.out.println(bounds.max());

        int count = 0;
        int lineY = 2000000;
//        int lineY = 10;
        for (var x = bounds.min().getX() - maxDistance; x < bounds.max().getX() + maxDistance; x++)
        {
            var p = IntPair.of(x, lineY);
            for (var s : sensors)
            {
                if (s.covers(p))
                {
                    count++;
                    break;
                }
            }
        }
        int beaconsAtLine = 0;
        for (var s : beacons)
        {
            if (s.getY() == lineY)
            {
                beaconsAtLine++;
            }
        }
        System.out.println(count - beaconsAtLine);
        
    }
}
