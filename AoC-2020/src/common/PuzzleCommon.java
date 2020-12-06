package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
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
    
    public ArrayList<String> readAllLines(String localFile)
        throws IOException
    {
        ArrayList<String> lines = new ArrayList<>();
        try (InputStream fis = loadLocalFile(localFile))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
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

        public int size()
        {
            return lines.size();
        }
    }
    
    public ArrayList<LinesGroup> readAllLineGroups(String localFile)
        throws IOException
    {
        ArrayList<LinesGroup> groups = new ArrayList<>();
        try (InputStream fis = loadLocalFile(localFile))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
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
}
