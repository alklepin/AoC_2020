package common;

import common.queries.Query;

public class Strings
{
    public static Query<String> tokenize(String line, String regexp)
    {
        return Query.wrap(line.split(regexp));
    }
}
