package common;

public class RangeLong implements Comparable<RangeLong>
{
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
}
