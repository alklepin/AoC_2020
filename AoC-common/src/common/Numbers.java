package common;

import common.boards.LongPair;

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
        return a * (b / nod(a,b));
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
            result = result * (values[idx] / nod(result, values[idx]));
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
            result = result * (v / nod(result, v));
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

    /**
     * Finds a solution of an equation ax + by = nod(a,b)
     * @param a
     * @param b
     * @return
     */
    public static BesuSolution solveBesu(long a, long b)
    {
        if (a <= 0)
            throw new IllegalArgumentException("a = "+a);
        if (b <= 0)
            throw new IllegalArgumentException("b = "+b);

        var swapped = a < b;
        if (swapped)
        {
            var tmp = a;
            a = b;
            b = tmp;
        }

        var r0 = LongPair.of(1, 0);
        var r1 = LongPair.of(0, 1);
        LongPair r2 = null;
        var v0 = a;
        var v1 = b;
        while (v1 > 0)
        {
            var q = v0 / v1;
            r2 = r0.minus(r1.mult(q));
            var remainder = v0 % v1;
            v0 = v1;
            v1 = remainder;
            r0 = r1;
            r1 = r2;
        }
        if (swapped)
            return new BesuSolution(b, a, r0.getY(), r0.getX(), v0);
        else
            return new BesuSolution(a, b, r0.getX(), r0.getY(), v0);
    }

    /**
     * Finds a solution of an equation ax + by = nod(a,b) in a ring by modulo a * b / nod(a,b)
     * Ensures that x and y are positive 
     * @param a
     * @param b
     * @return
     */
    public static BesuSolution solveBesuModulo(long a, long b)
    {
        var sol = solveBesu(a,b);
        var mod = sol.a * sol.b / sol.nod;
        var x = sol.x;
        var y = sol.y;
        if (x < 0)
        {
            var mul = -x / mod;
            x += (mul+1) * sol.b;
        }
        if (y < 0)
        {
            var mul = -y / mod;
            y += (mul+1) * sol.a;
        }
        if (x >= sol.b)
        {
            x -= x * (x / sol.b);
        }
        if (y >= sol.a)
        {
            y -= y * (y / sol.a);
        }
        return new BesuSolution(sol.a, sol.b, x, y, sol.nod);
    }
    
    public static class BesuSolution
    {
        public final long x;
        public final long y;
        public final long a;
        public final long b;
        public final long nod;

        public BesuSolution(long a, long b, long x, long y, long nod)
        {
            super();
            this.a = a;
            this.b = b;
            this.x = x;
            this.y = y;
            this.nod = nod;
        }
    }
    
    
    public static void main(String [] args)
    {
//        test(0,0);
//        test(0,1);
//        test(1,1);
//        test(2,1);
//        test(1,2);
//        test(2,2);
//        test(2,3);
//        test(9,12);
//        test(60,33);
//        test(33,121);

//        testBesu(1, 1);
//        testBesu(2, 3);
//        testBesu(3, 2);
        testBesu(991, 981);
//        testBesu(981, 991);
//        testBesu(2, 4);
        testBesuModulo(991, 981);
    }
    
    public static void test(int a, int b)
    {
        System.out.printf("NOD(%1$s, %2$s) = %3$s\n", a, b, nod(a,b));
    }

    public static void testBesu(int a, int b)
    {
        var res = solveBesu(a, b);
        System.out.printf("Besu: %1$s * x + %2$s * y = %3$s\n", res.a, res.b, res.nod);
        System.out.printf("X = %1$s Y = %2$s\n", res.x, res.y);
    }
    public static void testBesuModulo(int a, int b)
    {
        var res = solveBesuModulo(a, b);
        System.out.printf("Besu: %1$s * x + %2$s * y = %3$s\n", res.a, res.b, res.nod);
        System.out.printf("X = %1$s Y = %2$s\n", res.x, res.y);
    }
    
}
