package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public String get(int pos)
    {
        return lines.get(pos);
    }

    public String line(int pos)
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
    
    public void reverse()
    {
        Collections.reverse(lines);
    }
}
