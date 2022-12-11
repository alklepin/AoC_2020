package day22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.Tuple;
import common.geometry.CubeInt;

public class Puzzle4 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle4().solve();
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
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input3.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input4.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input5.txt");
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
        
        var xpoints = new ArrayList<Integer>();
        var ypoints = new ArrayList<Integer>();
        var zpoints = new ArrayList<Integer>();
        for (var d : data)
        {
            xpoints.add(d.getValue1().getPoint1().getX());
            xpoints.add(d.getValue1().getPoint2().getX());
            ypoints.add(d.getValue1().getPoint1().getY());
            ypoints.add(d.getValue1().getPoint2().getY());
            zpoints.add(d.getValue1().getPoint1().getZ());
            zpoints.add(d.getValue1().getPoint2().getZ());
        }
        
        Collections.sort(xpoints);
        Collections.sort(ypoints);
        Collections.sort(zpoints);
        xpoints = unique(xpoints);
        ypoints = unique(ypoints);
        zpoints = unique(zpoints);
        System.out.println(xpoints.size() * ypoints.size() * zpoints.size());
     
        HashMap<CubeInt, Boolean> space = new HashMap<>();
        for (int p1 = 1; p1 < xpoints.size(); p1++)
        {
            for (int p2 = 1; p2 < ypoints.size(); p2++)
            {
                for (int p3 = 1; p3 < zpoints.size(); p3++)
                {
                    space.put(CubeInt.of(
                        xpoints.get(p1-1), xpoints.get(p1), 
                        ypoints.get(p2-1), ypoints.get(p2), 
                        zpoints.get(p3-1), zpoints.get(p3) 
                        ), false);
                }
            }
        }
            
        var idx = 0;
        for (var d : data)
        {
            idx++;
            System.out.println("Processing line "+idx);
            var cube = d.getValue1();
            if (d.getValue2())
            {
                for (var entry : space.entrySet())
                {
                    if (entry.getKey().in(cube))
                    {
                        entry.setValue(true);
                    }
                }
            }
            else
            {                
                for (var entry : space.entrySet())
                {
                    if (entry.getKey().in(cube))
                    {
                        entry.setValue(false);
                    }
                }
            }

//            long count = 0;
//            for (var c : space.entrySet())
//            {
//                if (c.getValue().booleanValue())
//                {
//                    var cur = c.getKey();
//                    System.out.println("  Adding volume "+cur.volume() + " -> "+cur);
//                    count += cur.volume();
//                }
//            }
//            System.out.println(count);
//            System.out.println(count);
        }
        long count = 0;
        for (var c : space.entrySet())
        {
            if (c.getValue().booleanValue())
            {
                var cur = c.getKey();
//                System.out.println("  Adding volume "+cur.volume() + " -> "+cur);
                count += cur.volume();
            }
        }
        System.out.println(count);
        System.out.println(count);
        
    }
    
    private ArrayList<Integer> unique(ArrayList<Integer> data)
    {
        if (data.size() == 0)
            return data;
        for (var pos = 1; pos < data.size(); pos++)
        {
            if (data.get(pos).equals(data.get(pos-1)))
            {
                data.remove(pos);
            }
        }
        return data;
    }
}
