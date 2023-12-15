package day15;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import common.LinesGroup;
import common.PuzzleCommon;
import common.queries.Query;

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
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        String line = lines.get(0);
        var parts = line.split(",");
        long result = 0;
        ArrayList<ArrayList<Lens>> boxes = new ArrayList<>();
        for (var idx = 0; idx < 256; idx++)
            boxes.add(new ArrayList<>());
        HashMap<String, Lens> lenses = new HashMap<>();
        for (var part : parts)
        {
            var label = part.split("[-=]")[0];
            switch (part.charAt(label.length()))
            {
                case '=':
                {
                    var focusLength = parseInt(part.substring(label.length()+1)); 
                    var lens = new Lens(label, focusLength);
                    var existing = lenses.get(lens.label);
                    if ((existing == null) || (existing.deleted)) 
                    {
                        lenses.put(lens.label, lens);
                        boxes.get(lens.box).add(lens);
                    }
                    else
                    {
                        existing.focalLength = lens.focalLength;
                    }
                    break;
                }
                    
                case '-':
                {
                    var existing = lenses.get(label);
                    if (existing != null)
                        existing.deleted = true;
                    
                }
            }
        }
        for (var idx = 0; idx < boxes.size(); idx++)
        {
//            System.out.println("Box: "+idx);
            var box = boxes.get(idx);
            var packed = Query.wrap(box).where(l -> !l.deleted).toList();
            for (var s = 0; s < packed.size(); s++)
            {
                var lens = packed.get(s);
//                System.out.println("Lens: "+lens.label + " l="+lens.focalLength);
                long power = (idx+1) * (s+1) * lens.focalLength;
                result += power;
            }
        }
        System.out.println(result);
    }
    
    static class Lens
    {
        int focalLength;
        int box;
        String label;
        boolean deleted;
        public Lens(String label, int focalLength)
        {
            box = hash(label);
            this.label = label;
            deleted = false;
            this.focalLength = focalLength;
        }
    }
    
    public static int hash(String s)
    {
        int result = 0;
        for (var c : s.toCharArray())
        {
            result += c;
            result = (result * 17) % 256;
        }
        return result;
    }
}
