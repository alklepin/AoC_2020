package common;

public class RangeLong implements Comparable<RangeLong>
{
    public static RangeLong EMPTY = new RangeLong(0,0);
    
    private long start; // inclusive
    private long end; // exclusive
    
    public RangeLong(long start, long end)
    {
        this.start = start;
        this.end = end;
    }
    
    public long start()
    {
        return start;
    }
    
    public long end()
    {
        return end;
    }

    @Override
    public int compareTo(RangeLong r)
    {
        return start < r.start ? -1 : start > r.start ? 1 : 
            end < r.end ? -1 : end > r.end ? 1 : 0;
    }

    public RangeLong intersect(RangeLong other)
    {
        var s = Math.max(start,  other.start);
        var e = Math.min(end,  other.end);
        if (e <= s)
            return EMPTY;
        return new RangeLong(s, e);
    }

    public boolean intersects(RangeLong other)
    {
        var s = Math.max(start,  other.start);
        var e = Math.min(end,  other.end);
        return e > s;
    }

    @Override
    public String toString()
    {
        return "RangeLong: [" + start + ", " + end + ")";
    }

    public boolean isEmpty()
    {
        return end <= start;
    }

    public long length()
    {
        return Math.max(0, end - start);
    }

    public boolean contains(long value)
    {
        return start <= value && value < end;
    }
}
