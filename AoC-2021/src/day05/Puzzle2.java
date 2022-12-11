package day05;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators.RayGenerator;
import common.boards.Pair;
import common.geometry.SegmentInt;

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
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        ArrayList<SegmentInt> segments = new ArrayList<>();
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        for (String line : lines)
        {
            var parsed = parse("(\\d+),(\\d+) -> (\\d+),(\\d+)", line);
            var start = Pair.of(parseInt(parsed[1]), parseInt(parsed[2]));
            var end = Pair.of(parseInt(parsed[3]), parseInt(parsed[4]));
            var segment = new SegmentInt(start, end);
            segments.add(segment);
            System.out.println(segment);
        }
        
        int maxCount = 0;
        Board2D board = new Board2D(1000,1000);
//        Board2D board = new Board2D(10,10);
        for (var segment : segments)
        {
            for (var point : new RayGenerator(segment))
            {
                var count = board.getAtXY(point);
                count++;
                if (count > maxCount)
                {
                    maxCount++;
                }
                board.setAtXY(point, count);
            }
        }
        
        result = board.countCells(v -> v > 1);
        
        System.out.println(maxCount);
        System.out.println(result);
        
    }
}
