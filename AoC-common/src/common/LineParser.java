package common;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

public class LineParser
{
    private final String source;
    private final int startPos;
    
    public LineParser(String s)
    {
        source = s;
        startPos = 0;
    }

    public LineParser(String s, int startPos)
    {
        if (startPos < 0 || startPos >= s.length())
            throw new IllegalArgumentException();
        
        source = s;
        this.startPos = startPos; 
    }
    
    public LineParser skip(int length)
    {
        return new LineParser(source, startPos + length);
    }

    public LineParser skipTill(String regex)
    {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(source.substring(startPos));
        var newPos = startPos;
        if (matcher.find())
        {
            newPos = matcher.end();
        }
        return new LineParser(source, newPos);
    }
    
    public ArrayList<String> listOfStrings()
    {
        return list("[\\s]+", str -> str);
    }

    public ArrayList<Long> listOfLongs()
    {
        return listOfLongs("[\\s\\,\\;]+");
    }
    
    public ArrayList<Long> listOfLongs(String regexSeparator)
    {
        return list(regexSeparator,
            (str) -> Long.parseLong(str));
    }
    
    public ArrayList<Integer> listOfInts()
    {
        return listOfInts("[\\s\\,\\;]+");
    }
    
    public ArrayList<Integer> listOfInts(String regexSeparator)
    {
        return list(regexSeparator,
            (str) -> Integer.parseInt(str));
    }

    public <T> ArrayList<T> list(String regexSeparator, Function<String, T> converter)
    {
        var pattern = Pattern.compile(regexSeparator+"|$");
        var matcher = pattern.matcher(source);
        ArrayList<T> result = new ArrayList<>();
        var idx = startPos;
        while (idx < source.length() && matcher.find(idx))
        {
            var str = source.substring(idx, matcher.start());
            if (str.length() > 0)
                result.add(converter.apply(str));
            idx = matcher.end();
        }
        return result;
    }

    public LineParser segment(String regexSeparator)
    {
        var pattern = Pattern.compile(regexSeparator+"|$");
        var matcher = pattern.matcher(source);
        if (matcher.find(startPos))
        {
            return new LineParser(source.substring(startPos, matcher.start()));
        }
        return this;
    }
    
    public String toString()
    {
        return source.substring(startPos);
    }

    public <T> T parse(Function<LineParser, T> parser)
    {
        return parser.apply(this);
    }
}
