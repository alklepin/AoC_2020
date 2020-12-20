package common.queries;

import common.queries.YieldIterable.Code;

public class QueryTest
{
    public static void main(String[] args)
    {
//        for (String s : Query.wrap(new String[] {"One", "Two"}).selectMany((s) -> Arrays.asList(s, s)).skip(1))
//        {
//            System.out.println(s);
//        }
//        for (Integer s : Query.range(10, 50).take(5))
//        {
//            System.out.println(s);
//        }
//        for (Integer s : new IntGenerator())
//        {
//            System.out.println(s);
//        }
        for (Integer s : generateDivisors(120))
        {
            System.out.println(s);
        }
    }
    
    public static Iterable<Integer> generateDivisors(int value)
    {
        return YieldIterable.withYield((ctx) ->
        {
            int upBound = value / 2;
            for (int i = 2; i <= upBound; i++)
            {
                if (value % i == 0)
                    ctx.yield(i);
            }
        });
    }

    public static Iterable<Integer> generateDivisors1(int value)
    {
        return new YieldIterable<Integer>(){
            protected void generate()
            {
                int upBound = value / 2;
                for (int i = 2; i <= upBound; i++)
                {
                    if (value % i == 0)
                        yield(i);
                }
            }
        };
    }
    
    public static class IntGenerator extends YieldIterable<Integer>
    {
        protected void generate()
        {
            for (int i = 0; i < 20; i+=5)
            {
                yield(i);
            }
        }
    }
}
