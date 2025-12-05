package common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Holds set of disjoint ranges as a tree
 */
public class RangeLongSet implements Iterable<RangeLong>
{
    private TreeSet<RangeLong> ranges = new TreeSet<>(RangeComparator.INSTANCE);
    
    public static class RangeComparator implements Comparator<RangeLong>
    {
        public static final RangeComparator INSTANCE = new RangeComparator();
        
        @Override
        public int compare(RangeLong o1, RangeLong o2)
        {
            return Long.compare(o1.start(), o2.start());
        }
    }
    
    public void addRange(RangeLong range)
    {
        var firstRange = ranges.floor(range);
        var lastRange = ranges.floor(new RangeLong(range.end(), range.end()));
        if (lastRange == null)
        {
            ranges.add(range);
            return;
        }

        long min = range.start();
        long max = range.end();
        var toProcess = firstRange != null 
            ? ranges.subSet(firstRange, true, lastRange, true)
            : ranges.headSet(lastRange, true);
        var iter = toProcess.iterator();
        while (iter.hasNext())
        {
            var r = iter.next();
            if (r.intersects(range))
            {
                iter.remove();
                min = Math.min(min, r.start());
                max = Math.max(max, r.end());
            }
        }
        ranges.add(new RangeLong(min, max));
    }

    @Override
    public Iterator<RangeLong> iterator()
    {
        return ranges.iterator();
    }
    
     
}
