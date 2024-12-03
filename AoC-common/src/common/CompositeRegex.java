package common;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompositeRegex
{
    private final ArrayList<String> regexps;
    private final Pattern pattern;
    
    public static CompositeRegex of(String... regexps)
    {
        return new CompositeRegex(regexps);
    }
    
    public CompositeRegex(String... regexps)
    {
        this.regexps = new ArrayList<String>();
        var composite = new StringBuilder();
        var idx = 0;
        for (var regexp : regexps) 
        {
            this.regexps.add(regexp);
            composite
                .append("(?<")
                .append("grpid")
                .append(idx)
                .append(">")
                .append(regexp)
                .append(")|");
            idx++;
        }
        if (composite.length() > 0)
            composite.deleteCharAt(composite.length()-1); // remove trailing '|'
        pattern = Pattern.compile(composite.toString());        
    }
    
    public Matcher matcher(CharSequence source)
    {
        return pattern.matcher(source);
    }

    public ArrayList<String> allMatches(String source)
    {
        return allMatches(source, str -> str);
    }

    public <T> ArrayList<T> allMatches(String source, Function<String, T> converter)
    {
        var matcher = matcher(source);
        ArrayList<T> result = new ArrayList<>();
        while (matcher.find())
        {
            var str = source.substring(matcher.start(), matcher.end());
            result.add(converter.apply(str));
        }
        return result;
    }
}
