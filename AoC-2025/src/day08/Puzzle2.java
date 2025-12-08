package day08;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Vect3D;
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
    
    private static class PSegment
    {
        Vect3D point1;
        Vect3D point2;
        double length;
        
        public PSegment(Vect3D point1, Vect3D point2)
        {
            this.point1 = point1;
            this.point2 = point2;
            length = point1.minus(point2).length();
        }

        @Override
        public String toString()
        {
            return "PSegment [point1=" + point1 + ", point2=" + point2
                + ", length=" + length + "]";
        }
        
    }
    
    private static class SegComparator implements Comparator<PSegment>
    {
        static final SegComparator instance = new SegComparator();
        @Override
        public int compare(PSegment o1, PSegment o2)
        {
            return Double.compare(o1.length, o2.length);
        }
        
    }

    private static class HSetComparator implements Comparator<HSet>
    {
        static final HSetComparator instance = new HSetComparator();
        @Override
        public int compare(HSet o1, HSet o2)
        {
            return -Double.compare(o1.size, o2.size);
        }
        
    }
    
    private static class HSet
    {
        public HSet parent;
        public int id;
        public int size;
        
        public HSet(int id)
        {
            parent = null;
            this.id = id;
            size = 1;
        }
        
        public HSet findEffectiveSet()
        {
            HSet result = this;
            if (parent != null)
            {
                result = parent.findEffectiveSet();
                parent = result;
            }
            return result;
        }
        
        public int size()
        {
            return findEffectiveSet().size;
        }

        public int id()
        {
            return findEffectiveSet().id;
        }
        
        public void join(HSet other)
        {
            var s1 = this.findEffectiveSet();
            var s2 = other.findEffectiveSet();
            if (s1 == s2)
            {
                System.out.println("Error!");
            }
            if (s1.size >= s2.size)
            {
                s2.size = s1.size + s2.size;
                s1.id = s2.id;
                s1.parent = s2;
            }
            else
            {
                s1.size = s1.size + s2.size;
                s2.id = s1.id;
                s2.parent = s1;
            }
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
        var steps = 1000;
//        var inputFile = "input1_test.txt";
//        var steps = 10;
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<Vect3D> points = new ArrayList<>();
        HashMap<Vect3D, HSet> sets = new HashMap<>();
        for (String line : lines)
        {
            var point = Vect3D.from(line);
            points.add(point);
            sets.put(point, new HSet(points.size()));
        }
        ArrayList<PSegment> segments = new ArrayList<>();
        for (var idx1 = 0; idx1 < points.size(); idx1++)
        {
            for (var idx2 = idx1+1; idx2 < points.size(); idx2++)
            {
                segments.add(new PSegment(points.get(idx1), points.get(idx2)));
            }
        }
        Collections.sort(segments, SegComparator.instance);
        PSegment lastSegment = null; 
        for (var idx = 0; idx < segments.size(); idx++)
        {
            var s = segments.get(idx);
            var p1 = s.point1;
            var p2 = s.point2;
            var s1 = sets.get(p1);
            var s2 = sets.get(p2);
            System.out.println(MessageFormat.format("Connecting: {0} and {1} length {2}", p1, p2, s.length));
            if (s1.id() != s2.id())
            {
                System.out.println(MessageFormat.format("Merged sets {0} and {1}", s1.id, s2.id));
                s1.join(s2);
            }
            if (s1.size() == points.size())
            {
                lastSegment = s;
                break;
            }
        }
        
//        var res = Query.wrap(sets.values()).select(s -> s.findEffectiveSet()).distinct().sorted(HSetComparator.instance).toList();
//        
//        for (var s : res)
//        {
//            System.out.println(MessageFormat.format("Set {0} size {1}", s.id, s.size));
//        }
        
        long result = Math.round(lastSegment.point1.getX() * lastSegment.point2.getX());
        
        System.out.println(result);
        
    }

}
