package common.boards;

public class Bounds
{
    private IntPair min;
    private IntPair max;
    
    public static Bounds of(Iterable<IntPair> points)
    {
        return new Bounds(points);
    }

    public static Bounds of(IntPair... points)
    {
        return new Bounds(points);
    }
    
    
    public Bounds()
    {
        max = IntPair.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        min = IntPair.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public Bounds(Iterable<IntPair> points)
    {
        extendBy(points);
    }
    
    public Bounds(IntPair... points)
    {
        extendBy(points);
    }
    
    public IntPair min()
    {
        return min;
    }

    public IntPair max()
    {
        return max;
    }
    
    public void extendBy(IntPair p)
    {
        max = max != null ? max.componentMax(p) : p;
        min = min != null ? min.componentMin(p) : p;
    }

    public void extendBy(Iterable<IntPair> points)
    {
        for (var p : points)
            extendBy(p);
    }

    public void extendBy(IntPair... points)
    {
        for (var p : points)
            extendBy(p);
    }
    
    public int height()
    {
        return Math.max(0, max.getY() - min.getY());
    }

    public int width()
    {
        return Math.max(0, max.getX() - min.getX());
    }
    
    public String toString()
    {
        return "min: " + min + " max: " + max;
    }
}
