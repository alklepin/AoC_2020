package common.boards;

public class Bounds
{
    private IntPair min;
    private IntPair max;
    
    public Bounds()
    {
        max = IntPair.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        min = IntPair.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
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
        max = max.componentMax(p);
        min = min.componentMin(p);
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
