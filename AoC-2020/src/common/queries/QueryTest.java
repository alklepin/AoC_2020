package common.queries;

import java.util.Arrays;

public class QueryTest
{
    public static void main(String[] args)
    {
        for (String s : Query.wrap(new String[] {"One", "Two"}).selectMany((s) -> Arrays.asList(s, s)))
        {
            System.out.println(s);
        }
    }
}
