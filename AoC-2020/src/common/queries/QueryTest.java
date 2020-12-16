package common.queries;

import java.util.Arrays;

public class QueryTest
{
    public static void main(String[] args)
    {
//        for (String s : Query.wrap(new String[] {"One", "Two"}).selectMany((s) -> Arrays.asList(s, s)).skip(1))
//        {
//            System.out.println(s);
//        }
        for (Integer s : Query.range(10, 50).take(5))
        {
            System.out.println(s);
        }
    }
}
