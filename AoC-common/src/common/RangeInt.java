package common;

public class RangeInt implements Comparable<RangeInt>
{
    public static RangeInt EMPTY = new RangeInt(0,0);
    
    private int start; // inclusive
    private int end; // exclusive
    
    public static RangeInt ofInclusive(int a, int b)
    {
        if (a <= b)
            return new RangeInt(a, b+1);
        else
            return new RangeInt(b, a+1);
    }

    public RangeInt(int start, int end)
    {
        this.start = start;
        this.end = end;
    }
    
    public int start()
    {
        return start;
    }
    
    public int end()
    {
        return end;
    }

    @Override
    public int compareTo(RangeInt r)
    {
        return start < r.start ? -1 : start > r.start ? 1 : 
            end < r.end ? -1 : end > r.end ? 1 : 0;
    }

    public RangeInt intersect(RangeInt other)
    {
        var s = Math.max(start,  other.start);
        var e = Math.min(end,  other.end);
        if (e <= s)
            return EMPTY;
        return new RangeInt(s, e);
    }

    @Override
    public String toString()
    {
        return "RangeInt: [" + start + ", " + end + ")";
    }

    public boolean isEmpty()
    {
        return end <= start;
    }

    public long length()
    {
        return Math.max(0, end - start);
    }
}
