package day01;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    public long processGroup(LinesGroup group)
    {
        long sum = 0;
        for (var line : group)
        {
            var l = Long.parseLong(line);
            sum += l;
        }
        return sum;
    }

    private static class LongComparator implements Comparator<Long>
    {
        @Override
        public int compare(Long a, Long b)
        {
            return - a.compareTo(b);
        }
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());
        
        PriorityQueue<Long> queue = new PriorityQueue<Long>(new LongComparator());
        for (LinesGroup group : groups)
        {
            var l = group.processGroup(this::processGroup);
            queue.add(l);
        }
        long result = 0;
        result += queue.poll();
        result += queue.poll();
        result += queue.poll();
        
        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
//        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
}
