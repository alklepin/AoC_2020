package day23;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        HashSet<NamesPair> network = new HashSet<>();
        HashSet<String> namesSet = new HashSet<>();
        for (String line : lines)
        {
            var parts = split(line, "-");
            network.add(new NamesPair(parts.get(0), parts.get(1)));
            network.add(new NamesPair(parts.get(1), parts.get(0)));
            namesSet.add(parts.get(0));
            namesSet.add(parts.get(1));
        }
        
        ArrayList<String> names = new ArrayList<>();
        names.addAll(namesSet);
        Collections.sort(names);
        int result = 0;
        
        for (var i1 = 0; i1 < names.size(); i1++)
        {
            for (var i2 = i1+1; i2 < names.size(); i2++)
            {
                for (var i3 = i2+1; i3 < names.size(); i3++)
                {
                    var n1 = names.get(i1);
                    var n2 = names.get(i2);
                    var n3 = names.get(i3);
                    var count = 0;
                    if (network.contains(new NamesPair(n1, n2)))
                        count++;
                    if (network.contains(new NamesPair(n2, n3)))
                        count++;
                    if (network.contains(new NamesPair(n1, n3)))
                        count++;
                    if (count >= 3 
                        && (n1.startsWith("t") || n2.startsWith("t") || n3.startsWith("t")))
                    {
                        System.out.println(MessageFormat.format("{0},{1},{2}", n1, n2, n3));
                        result++;
                    }
                }
            }
        }
        
        System.out.println(result);
    }
    
    static class NamesPair
    {
        public final String name1;
        public final String name2;
        public NamesPair(String name1, String name2)
        {
            super();
            this.name1 = name1;
            this.name2 = name2;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name1 == null) ? 0 : name1.hashCode());
            result = prime * result + ((name2 == null) ? 0 : name2.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            NamesPair other = (NamesPair)obj;
            if (name1 == null)
            {
                if (other.name1 != null)
                    return false;
            }
            else if (!name1.equals(other.name1))
                return false;
            if (name2 == null)
            {
                if (other.name2 != null)
                    return false;
            }
            else if (!name2.equals(other.name2))
                return false;
            return true;
        }
        @Override
        public String toString()
        {
            return "NamesPair [name1=" + name1 + ", name2=" + name2 + "]";
        }
        
        
        
    }
}
