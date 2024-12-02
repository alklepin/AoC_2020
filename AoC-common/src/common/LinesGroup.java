package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import common.queries.Query;

public class LinesGroup implements Iterable<String>
{
    public static interface GroupProcessorAction
    {
        void process(LinesGroup lines);
    }

    public static interface GroupProcessorFunc<T>
    {
        T process(LinesGroup lines);
    }
    
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
    
    public Iterable<LineParser> parsers()
    {
        return Query.wrap(lines).select(line -> new LineParser(line));
    }

    public String get(int pos)
    {
        return lines.get(pos);
    }

    public LineParser lineParser(int pos)
    {
        return new LineParser(lines.get(pos));
    }

    public String line(int pos)
    {
        return lines.get(pos);
    }

    public Query<String> lineTokens(int pos)
    {
        return Strings.tokenize(lines.get(pos));
    }

    public Query<String> lineTokens(int pos, String regexp)
    {
        return Strings.tokenize(lines.get(pos), regexp);
    }
    
    public int size()
    {
        return lines.size();
    }

    public Stream<String> stream() 
    {
        return StreamSupport.stream(lines.spliterator(), false);
    }
    
    public void reverse()
    {
        Collections.reverse(lines);
    }
    
    public Collection<String> asCollection()
    {
        return Collections.unmodifiableCollection(lines);
    }
    
    public void trimAll()
    {
        for (var idx = 0; idx < lines.size(); idx++)
        {
            lines.set(idx, lines.get(idx).trim());
        }
    }
    
    public void trimRightAll()
    {
        for (var idx = 0; idx < lines.size(); idx++)
        {
            var line = new StringBuilder(lines.get(idx));
            while (line.charAt(line.length()-1) == ' ')
                line.setLength(line.length()-1);
            
            lines.set(idx, line.toString());
        }
    }

    public LinesGroup remainder(int fromPos)
    {
        var result = new LinesGroup();
        for (var idx = fromPos; idx < lines.size(); idx++)
        {
            result.addLine(lines.get(idx));
        }
        return result;
    }
    
    public ArrayList<LinesGroup> split(String regex)
    {
        var pattern = Pattern.compile(regex);
        
        ArrayList<LinesGroup> groups = new ArrayList<>();
        LinesGroup current = null;
        for (var line : lines)
        {
            if (pattern.matcher(line).matches())
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
        return groups;
    }

    public ArrayList<LinesGroup> splitByEmptyLines()
    {
        return split("\\s*");
    }
    
}
