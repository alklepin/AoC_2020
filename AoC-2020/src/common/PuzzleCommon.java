package common;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PuzzleCommon
{
    public InputStream loadLocalFile(String fileName)
    {
        ClassLoader cl = getClass().getClassLoader();
        if (cl == null)
            cl = ClassLoader.getSystemClassLoader();
        String localPath = getClass().getPackageName().replace('.', '/');
        return cl.getResourceAsStream(localPath + '/' + fileName);        
    }
    
    public static int parseInt(String value, int defValue)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ex)
        {
            return defValue;
        }
    }
    
    public static boolean fitsRange(String value, int rangeStart, int rangeEnd)
    {
        int v = parseInt(value, rangeStart-1);
        return rangeStart <= v && v <= rangeEnd;
    }
    
    public static String join(Iterable<String> values, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (String segment : values)
        {
            if (segment != null)
                sb.append(segment).append(separator);
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - separator.length());
        return sb.toString();
    }
    
    public static String join(Iterable<String> values, char separator)
    {
        StringBuilder sb = new StringBuilder();
        for (String segment : values)
        {
            if (segment != null)
                sb.append(segment).append(separator);
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    
    public static String[] parse(String regExp, String value)
    {
        return parse(Pattern.compile(regExp), value);
    }
    
    public static String[] parse(String regExp, String value, String [] defaultValue)
    {
        return parse(Pattern.compile(regExp), value, defaultValue);
    }
    
    public static String[] parse(Pattern pattern, String value)
    {
        return parse(pattern, value, null);
    }
    
    public static String[] parse(Pattern pattern, String value, String [] defaultValue)
    {
        Matcher m = pattern.matcher(value);
        if (m.matches())
        {
            String [] result = new String[m.groupCount()+1];
            result[0] = m.group(0);
            for (int i = 1; i < result.length; i++)
            {
                result[i] = m.group(i);
            }
            return result;
        }
        return defaultValue;
    }
}
