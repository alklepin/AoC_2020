package day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.PuzzleCommon;
import common.Tuple;
import common.boards.IntTriple;
import common.geometry.CubeInt;

public class Puzzle5 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle5().solve();
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
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input3.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input4.txt");
        ArrayList<String> lines = readAllLinesNonEmpty("input5.txt");
        ArrayList<Tuple<CubeInt, Boolean>> data = new ArrayList<>();
        int result = 0;
        for (String line : lines)
        {
            var parts = parse("(on|off) x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)", line);
            var state = parts[1].equals("on");
            var x1 = parseInt(parts[2]);
            var x2 = parseInt(parts[3])+1;
            var y1 = parseInt(parts[4]);
            var y2 = parseInt(parts[5])+1;
            var z1 = parseInt(parts[6]);
            var z2 = parseInt(parts[7])+1;
            var cube = CubeInt.of(x1, x2, y1, y2, z1, z2);
            data.add(Tuple.of(cube, state));
        }
        
        HashSet<CubeInt> activeCubes = new HashSet<>();
        var idx = 0;
        for (var d : data)
        {
            idx++;
            System.out.println("Processing line "+idx);
            HashSet<CubeInt> activeCubesNext = new HashSet<>();
            if (d.getValue2())
            {
                if (activeCubes.size() > 0)
                {
                    activeCubesNext.addAll(activeCubes);
                    activeCubesNext.add(d.getValue1());
                }
                else
                {
                    activeCubesNext.add(d.getValue1());
                }
            }
            else
            {                
                for (var c : activeCubes)
                {
                    var parts = c.sub(d.getValue1());
                    for (var p : parts)
                    {
                        activeCubesNext.add(p);
                    }
                }
            }
            activeCubes = activeCubesNext;

//            long count = 0;
//            for (var c : activeCubes)
//            {
//                count += c.volume();
//            }
//            System.out.println(count);
        }
        
        long count = 0;
        for (var c : CubeInt.unionAll(activeCubes))
        {
            count += c.volume();
        }
        System.out.println(count);
        
    }
}
