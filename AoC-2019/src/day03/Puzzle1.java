package day03;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.Pair;
import common.geometry.Segment;
import common.geometry.Vect2D;

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
    
    private IntPair diff(char direction)
    {
        return switch (direction)
            {
                case 'U' -> Pair.of(0,  1); 
                case 'D' -> Pair.of(0,  -1); 
                case 'R' -> Pair.of(1,  0); 
                case 'L' -> Pair.of(-1,  0); 
                default -> throw new IllegalStateException();
            };
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
        LinesGroup lines = readAllLines(inputFile);
        ArrayList<Segment> segments1 = new ArrayList<>();
        var current = Pair.of(0, 0);
        for (var token : lines.lineTokens(0, ","))
        {
            var dirChar = token.charAt(0);
            var length = parseInt(token.substring(1));
            var diff = diff(dirChar);
            var next = current.add(diff.mult(length));
            var segment = new Segment(new Vect2D(current.getX(), current.getY()), 
                new Vect2D(next.getX(), next.getY()));
            segments1.add(segment);
            current = next;
        }

        ArrayList<Segment> segments2 = new ArrayList<>();
        current = Pair.of(0, 0);
        for (var token : lines.lineTokens(1, ","))
        {
            var dirChar = token.charAt(0);
            var length = parseInt(token.substring(1));
            var diff = diff(dirChar);
            var next = current.add(diff.mult(length));
            var segment = new Segment(new Vect2D(current.getX(), current.getY()), 
                new Vect2D(next.getX(), next.getY()));
            segments2.add(segment);
            current = next;
        }
        
        var points = new ArrayList<IntPair>();
        for (var s1 : segments1)
        {
            for (var s2 : segments2)
            {
                var p = s1.intersect(s2);
                if (p!= null)
                {
                    points.add(p.asIntPair());
                }
            }
        }
        
        int minDistance = Integer.MAX_VALUE;
        for (var p : points)
        {
            if (!p.equals(IntPair.ZERO))
                minDistance = Math.min(minDistance, p.lengthManh());
        }
        
        System.out.println(minDistance);
        
        
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
}
