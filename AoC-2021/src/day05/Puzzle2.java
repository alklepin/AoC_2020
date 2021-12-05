package day05;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
import common.boards.Generators.RayGenerator;
import common.geometry.Line;
import common.geometry.Segment;
import common.geometry.Vect2D;

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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<Segment> segments = new ArrayList<>();
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        for (String line : lines)
        {
            var parsed = parse("(\\d+),(\\d+) -> (\\d+),(\\d+)", line);
            var start = new Vect2D(parseInt(parsed[1]), parseInt(parsed[2]));
            var end = new Vect2D(parseInt(parsed[3]), parseInt(parsed[4]));
            var segment = new Segment(start, end);
            segments.add(segment);
            System.out.println(segment);
        }
        
        int maxCount = 0;
        Board2D board = new Board2D(1000,1000);
//        Board2D board = new Board2D(10,10);
        for (var segment : segments)
        {
            var start = segment.getPoint1();
            var end = segment.getPoint2();
            var delta = end.minus(start);
            {
                var count = board.getAt((int)start.getY(), (int)start.getX());
                count++;
                if (count > maxCount)
                {
                    maxCount++;
                }
                board.setAt((int)start.getY(), (int)start.getX(), count);
            }
            var length = (int)Math.max(Math.abs(delta.getX()), Math.abs(delta.getY()));
            for (var point : new RayGenerator(start, delta.divideBy(length), length))
            {
                var count = board.getAt(point.getY(), point.getX());
                count++;
                if (count > maxCount)
                {
                    maxCount++;
                }
                board.setAt(point.getY(), point.getX(), count);
            }
        }
      board.printAsInts(System.out);
      System.out.println();
        
        for (int x = 0; x < board.getWidth(); x++)
        {
            for (var y = 0; y < board.getHeigth(); y++)
            {
                var count = board.getAt(x, y);
//                if (count == maxCount)
                if (count > 1)
                    result++;
            }
        }
        
        System.out.println(maxCount);
        System.out.println(result);
        
    }
}
