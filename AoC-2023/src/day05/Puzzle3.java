package day05;

import java.util.ArrayList;
import java.util.Collections;

import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle3 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle3().solve();
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLines(inputFile);
        var groups = lines.split("\\s*");

        var seeds = groups.get(0).lineParser(0).skipTill(": ").listOfLongs();
        
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
            var range = group.lineParser(idx).parse(RangeMap.Range::parse);
            result.addRange(range);
        }
        result.sort();
        return result;
    }
    
    static class RangeMap 
    {
        static class Range implements Comparable<Range>
        {
            long sStart;
            long dStart;
            long length;

            public static Range parse(LineParser parser)
            {
                var list = parser.listOfLongs();
                return new Range(list.get(1), list.get(0), list.get(2));
            }
            
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

            @Override
            public int compareTo(Range r)
            {
                return sStart < r.sStart ? -1 : sStart > r.sStart ? 1 : 0;
            }
        }
        
        ArrayList<Range> ranges = new ArrayList<>();
        
        public void addRange(Range range)
        {
            ranges.add(range);
        }

        public void sort()
        {
            Collections.sort(ranges);
        }
        
        public int rangeComparator(Range r1, Range r2)
        {
            return r1.sStart < r2.sStart ? -1 : r1.sStart > r2.sStart ? 1 : 0;
        }
        
        public long translate(long value)
        {
            var r = new Range(value, value, 1);
            var pos = Collections.binarySearch(ranges, r);
            if (pos >= 0)
                return ranges.get(pos).translate(value);
            var insertPos = -(pos+1);
            if (insertPos > 0)
                return ranges.get(insertPos-1).translate(value);
            return value;
        }
        
        
    }
}
