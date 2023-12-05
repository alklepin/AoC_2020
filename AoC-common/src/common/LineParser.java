package common;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

public class LineParser
{
    private String source;
    private int startPos;
    private int idx;
    
    public LineParser(String s)
    {
        source = s;
        idx = 0;
    }

    public LineParser(String s, int startPos)
    {
        if (startPos < 0 || startPos >= s.length())
            throw new IllegalArgumentException();
        
        source = s;
        this.startPos = startPos; 
        idx = startPos;
    }
    
    public LineParser skip(int length)
    {
        idx += length;
        return this;
    }

    public LineParser skipTill(String regex)
    {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(source.substring(startPos));
        if (matcher.find())
        {
            idx = matcher.end();
        }
        return this;
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
        while (idx < source.length() && matcher.find(idx))
        {
            var str = source.substring(idx, matcher.start());
            if (str.length() > 0)
                result.add(converter.apply(str));
            idx = matcher.end();
        }
        return result;
    }
    
    public String toString()
    {
        return source.substring(idx);
    }

    public <T> T parse(Function<LineParser, T> parser)
    {
        return parser.apply(this);
    }
    
//    String nextSegment(String regex)
//    {
//        var pattern = Pattern.compile(regex)
//    }
    
}
