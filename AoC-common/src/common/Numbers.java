package common;

public class Numbers
{
    public static long nod(long a, long b)
    {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a < b)
        {
            var tmp = a;
            a = b;
            b = tmp;
        }
        if (a == 0)
            return 1;
        
        while (b > 0)
        {
            var remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
    
    public static long nok(long a, long b)
    {
        return a * b / nod(a,b);
    }

    public static long nok(long... values)
    {
        if (values.length == 0)
            throw new IllegalArgumentException();
        if (values.length == 1)
            return values[0];
        var result = values[0];
        for (var idx = 1; idx < values.length; idx++)
        {
            result = result * values[idx] / nod(result, values[idx]);
        }
        return result;
    }

    public static long nok(Iterable<? extends Number> values)
    {
        var iter = values.iterator();
        if (!iter.hasNext())
            throw new IllegalArgumentException();
        long result = iter.next().longValue();
        while (iter.hasNext())
        {
            var v = iter.next().longValue();
            result = result * v / nod(result, v);
        }
        return result;
    }

    public static int nod(int a, int b)
    {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a < b)
        {
            var tmp = a;
            a = b;
            b = tmp;
        }
        if (a == 0)
            return 1;
        
        while (b > 0)
        {
            var remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
    
    public static long nod(long... values)
    {
        if (values.length == 0)
            throw new IllegalArgumentException();
        if (values.length == 1)
            return values[0];
        var result = values[0];
        for (var idx = 1; idx < values.length; idx++)
        {
            result = nod(result, values[idx]);
        }
        return result;
    }
    
    public static int nod(int... values)
    {
        if (values.length == 0)
            throw new IllegalArgumentException();
        if (values.length == 1)
            return values[0];
        var result = values[0];
        for (var idx = 1; idx < values.length; idx++)
        {
            result = nod(result, values[idx]);
        }
        return result;
    }

    public static int nodInt(Iterable<Integer> values)
    {
        var iter = values.iterator();
        if (!iter.hasNext())
            throw new IllegalArgumentException();
        int result = iter.next();
        while (iter.hasNext())
        {
            var v = iter.next();
            result = nod(result, v);
        }
        return result;
    }

    public static long nodLong(Iterable<Long> values)
    {
        var iter = values.iterator();
        if (!iter.hasNext())
            throw new IllegalArgumentException();
        long result = iter.next();
        while (iter.hasNext())
        {
            var v = iter.next();
            result = nod(result, v);
        }
        return result;
    }
    

    public static void main(String [] args)
    {
        test(0,0);
        test(0,1);
        test(1,1);
        test(2,1);
        test(1,2);
        test(2,2);
        test(2,3);
        test(9,12);
        test(60,33);
        test(33,121);
        
    }
    
    public static void test(int a, int b)
    {
        System.out.printf("NOD(%1$s, %2$s) = %3$s\n", a, b, nod(a,b));
    }
    
}
