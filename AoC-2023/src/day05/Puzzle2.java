package day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;

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
        
        LinesGroup lines = readAllLines(inputFile);
        var groups = lines.split("\\s*");

        var test = groups.get(0).get(0).split(":")[1].trim().split("\\s+");
        var seedsStr = groups.get(0).get(0).split(":")[1].trim().split("\\s+");
        var seeds = new ArrayList<Long>();
        for (var s : seedsStr)
        {
            seeds.add(parseLong(s));
        }
        
        var seedToSoidMap = parseMap(groups.get(1));
        var soilToFertilizerMap = parseMap(groups.get(2));
        var fertilizerToWaterMap = parseMap(groups.get(3));
        var waterToLightMap = parseMap(groups.get(4));
        var lightToTempMap = parseMap(groups.get(5));
        var tempToHumidityMap = parseMap(groups.get(6));
        var humidityToLocationMap = parseMap(groups.get(7));
        long result = Long.MAX_VALUE;
        for (var idx = 0; idx < seeds.size(); idx += 2)
        {
            var rStart = seeds.get(idx);
            var rLength = seeds.get(idx+1);
            for (var seed = rStart; seed < rStart + rLength; seed++)
            {
                try
                {
                    var soil = seedToSoidMap.translate(seed);
                    var fert = soilToFertilizerMap.translate(soil);
                    var water = fertilizerToWaterMap.translate(fert);
                    var light = waterToLightMap.translate(water);
                    var temp = lightToTempMap.translate(light);
                    var humidity = tempToHumidityMap.translate(temp);
                    var location = humidityToLocationMap.translate(humidity);
                    if (location < result)
                        result = location;
                }
                catch (Exception e)
                {
                    // Ignore
                }
            }
        }
        
        System.out.println(result);
    }
    
    RangeMap parseMap(LinesGroup group)
    {
        var result = new RangeMap();
        for (var idx = 1; idx < group.size(); idx++)
        {
            var line = group.get(idx);
            var parts = line.split("\\ +");
            var dStart = parseLong(parts[0]);
            var sStart = parseLong(parts[1]);
            var l = parseLong(parts[2]);
            result.addRange(sStart, dStart, l);
        }
        result.sort();
        return result;
    }
    
    static class RangeMap 
    {
        static class Range
        {
            long sStart;
            long dStart;
            long length;

            public Range(long sStart, long dStart, long length)
            {
                this.sStart = sStart;
                this.dStart = dStart;
                this.length = length;
            }
            
            public long translate(long value)
            {
                if (value < sStart || value >= sStart+length)
                    return value;
                return (value-sStart) + dStart;
            }
        }
        
        ArrayList<Range> ranges = new ArrayList<>();
        
        public void addRange(long sStart, long dStart, long length)
        {
            ranges.add(new Range(sStart, dStart, length));
        }

        public void sort()
        {
            Collections.sort(ranges, this::rangeComparator);
        }
        
        public int rangeComparator(Range r1, Range r2)
        {
            return r1.sStart < r2.sStart ? -1 : r1.sStart > r2.sStart ? 1 : 0;
        }
        
        public long translate(long value)
        {
            var r = new Range(value, value, 1);
            var pos = Collections.binarySearch(ranges, r, this::rangeComparator);
            if (pos >= 0)
                return ranges.get(pos).translate(value);
            var insertPos = -(pos+1);
            if (insertPos > 0)
                return ranges.get(insertPos-1).translate(value);
            return value;
        }
        
        
    }
}
