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
