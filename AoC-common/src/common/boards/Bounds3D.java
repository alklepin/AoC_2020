package common.boards;

public class Bounds3D
{
    private IntTriple min;
    private IntTriple max;
    
    public static Bounds3D of(Iterable<IntTriple> points)
    {
        return new Bounds3D(points);
    }

    public static Bounds3D of(IntTriple... points)
    {
        return new Bounds3D(points);
    }
    
    public Bounds3D()
    {
        max = IntTriple.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        min = IntTriple.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public Bounds3D(Iterable<IntTriple> points)
    {
        this();
        extendBy(points);
    }

    public Bounds3D(IntTriple... points)
    {
        this();
        extendBy(points);
    }
    
    public IntTriple min()
    {
        return min;
    }

    public IntTriple max()
    {
        return max;
    }
    
    public void extendBy(IntTriple p)
    {
        max = max.componentMax(p);
        min = min.componentMin(p);
    }
    
    public void extendBy(Iterable<IntTriple> points)
    {
        for (var p : points)
            extendBy(p);
    }

    public void extendBy(IntTriple... points)
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

    public int depth()
    {
        return Math.max(0, max.getZ() - min.getZ());
    }
    
    public String toString()
    {
        return "min: " + min + " max: " + max;
    }

    public boolean contains(IntTriple p)
    {
        return min.componentLessEq(p) && max.componentGreaterEq(p);
    }
}
