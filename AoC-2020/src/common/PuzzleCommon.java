package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PuzzleCommon
{
    public static final String EnglishCharsLow = "abcdefghijklmnopqrstuvwxyz";
    public static final String EnglishCharsUp = EnglishCharsLow.toUpperCase();
    
    public InputStream loadLocalFile(String fileName)
    {
        ClassLoader cl = getClass().getClassLoader();
        if (cl == null)
            cl = ClassLoader.getSystemClassLoader();
        String localPath = getClass().getPackageName().replace('.', '/');
        return cl.getResourceAsStream(localPath + '/' + fileName);        
    }
    
    public static Reader toUTF8Reader(InputStream is)
        throws IOException
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            br.mark(4096);
            int firstChar = br.read();
            if (firstChar != 0xFEFF)
                br.reset();
            return br;
        }
        catch (UnsupportedEncodingException ignored)
        {
            throw new IllegalStateException(ignored);
        }
        
    }
    
    public ArrayList<String> readAllLines(String localFile)
        throws IOException
    {
        ArrayList<String> lines = new ArrayList<>();
        try (InputStream fis = loadLocalFile(localFile))
        {
            try (Scanner scanner = new Scanner(toUTF8Reader(fis)))
            {
                while (scanner.hasNextLine())
                {
                    lines.add(scanner.nextLine().trim());
                }
            }
        }
        return lines;
    }
    
    public static interface GroupProcessorAction
    {
        void process(LinesGroup lines);
    }
    
    public static interface GroupProcessorFunc<T>
    {
        T process(LinesGroup lines);
    }
    
    public static class LinesGroup implements Iterable<String>
    {
        private ArrayList<String> lines = new ArrayList<>();
        
        public void addLine(String line)
        {
            lines.add(line);
        }
        
        public void processGroup(GroupProcessorAction processor)
        {
            processor.process(this);
        }
        
        public <T> T processGroup(GroupProcessorFunc<T> processor)
        {
            return processor.process(this);
        }
        
        @Override
        public Iterator<String> iterator()
        {
            return lines.iterator();
        }

        public String get(int pos)
        {
            return lines.get(pos);
        }
        
        public int size()
        {
            return lines.size();
        }

        public Stream<String> stream() 
        {
            return StreamSupport.stream(lines.spliterator(), false);
        }
        
    }
    
    public ArrayList<LinesGroup> readAllLineGroups(String localFile)
        throws IOException
    {
        ArrayList<LinesGroup> groups = new ArrayList<>();
        try (InputStream fis = loadLocalFile(localFile))
        {
            try (Scanner scanner = new Scanner(toUTF8Reader(fis)))
            {
                LinesGroup current = null;
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine().trim();
                    if (line.length() == 0)
                    {
                        if (current != null)
                        {
                            groups.add(current);
                            current = null;
                        }
                    }
                    else
                    {
                        if (current == null)
                        {
                            current = new LinesGroup();
                        }
                        current.addLine(line);
                    }
                }
                if (current != null)
                {
                    groups.add(current);
                }
            }
        }
        return groups;
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
    
    public static ArrayList<Character> toListOfChars(String line)
    {
        return Convert.toListOfChars(line);
    }
    
    public static <K,V> V getOrDefault(Map<K,V> map, K key, V defaultValue)
    {
        V result = map.get(key);
        if (result != null)
            return result;
        else
            return defaultValue;
    }
}
