package day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.PuzzleCommon;
import common.Tuple;
import common.boards.IntTriple;
import common.geometry.CubeInt;

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
        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input3.txt");
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
        
        var limit = CubeInt.of(-50, 51, -50, 51, -50, 51);
        var idx = 0;
        HashSet<IntTriple> activeCubes = new HashSet<>();
        for (var d : data)
        {
            idx++;
            if (!d.getValue1().in(limit))
            {
                System.out.println("Skipping... "+idx);
                continue;
            }
            if (d.getValue2())
            {
                for (var p : d.getValue1().points())
                    activeCubes.add(p);
                System.out.println("Idx: "+idx + " after On " + activeCubes.size());
            }
            else
            {                
                for (var p : d.getValue1().points())
                    activeCubes.remove(p);
                System.out.println("Idx: "+idx + " after Off " + activeCubes.size());
            }  
        }
        
        System.out.println(activeCubes.size());
        
    }
}
