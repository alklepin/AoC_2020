package common.boards;

public class Pair
{
    public static IntPair of(int a, int b)
    {
        return new IntPair(a,b);
    }

    public static LongPair of(long a, long b)
    {
        return new LongPair(a,b);
    }
}
