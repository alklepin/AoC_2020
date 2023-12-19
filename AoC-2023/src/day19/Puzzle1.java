package day19;

import java.util.ArrayList;
import java.util.HashMap;

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
        
        LinesGroup lines = readAllLines(inputFile);
        var groups = lines.splitByEmptyLines();
        
        HashMap<String, Workflow> wfs = parseWorkflows(groups.get(0));
        ArrayList<Item> items = parseItems(groups.get(1));

        
        var result = 0;
        for (var item : items)
        {
            System.out.println(item);
            var state = "in";
            while (!state.equals("A") && !state.equals("R"))
            {
                System.out.println("State: "+state);
                var wf = wfs.get(state);
                for (var a : wf.actions)
                {
                    if (a.source == '-')
                    {
                        state = a.target;
                        break;
                    }
                    else
                    {
                        if (item.fits(a.source, a.cond, a.value))
                        {
                            state = a.target;
                            break;
                        }
                    }
                }
            }
            System.out.println("State: "+state);
            if (state.equals("A"))
            {
                result += item.rate();
            }
        }
        System.out.println(result);
        
    }
    
    private ArrayList<Item> parseItems(LinesGroup linesGroup)
    {
        var result = new ArrayList<Item>();
        for (var line : linesGroup)
        {
            result.add(new Item(line));
        }
        return result;
    }

    private HashMap<String, Workflow> parseWorkflows(LinesGroup linesGroup)
    {
        HashMap<String, Workflow> result = new HashMap<>();
        for (var line : linesGroup)
        {
            var wf = new Workflow(line);
            result.put(wf.name,  wf);
        }
        return result;
    }

    class Item
    {
        public int x;
        public int m;
        public int a;
        public int s;
        
        public Item(String line)
        {
            var parts = line.split("[\\{\\}\\,]");
            for (var p : parts)
            {
                if (p.trim().length() == 0)
                    continue;
                var ps = p.split("\\=");
                var val = Integer.parseInt(ps[1]);
                switch (ps[0].charAt(0)) 
                {
                    case 'x':
                    {
                        x = val;
                        break;
                    }
                    case 'm':
                    {
                        m = val;
                        break;
                    }
                    case 'a':
                    {
                        a = val;
                        break;
                    }
                    case 's':
                    {
                        s = val;
                        break;
                    }
                }
            }
        }
        
        public boolean fits(char s, int cond, int value)
        {
            long val = switch (s) 
            {
                case 'x' -> this.x;
                case 'm' -> this.m; 
                case 'a' -> this.a;
                case 's' -> this.s;
                default -> throw new IllegalStateException();
            };
            return ((val - value) * cond > 0);
        }
        
        public int rate()
        {
            return x+m+a+s;
        }

        @Override
        public String toString()
        {
            return "Item [x=" + x + ", m=" + m + ", a=" + a + ", s=" + s + "]";
        }
        
        
    }
    
    class WFAction
    {
        public char source;
        public int cond;
        public int value;
        public String target;

        public WFAction(String p)
        {
            var parts = p.split("\\:");
            if (parts.length > 1)
            {
                source = parts[0].charAt(0);
                var c = parts[0].charAt(1);
                cond = (c == '>') ? 1 : c == '<' ? -1 : 0;
                value = Integer.parseInt(parts[0].substring(2));
                target = parts[1].trim();
            }
            else
            {
                source = '-';
                cond = 0;
                value = 0;
                target = p.trim();
            }
        }

        @Override
        public String toString()
        {
            return "WFAction [source=" + source + ", cond=" + cond + ", value="
                + value + ", target=" + target + "]";
        }
        
    }
    
    class Workflow
    {
        public String name;
        public ArrayList<WFAction> actions = new ArrayList<>();
        
        public Workflow(String line)
        {
            var parts = line.split("[\\{\\}]");
            name = parts[0];
            var actions = parts[1];
            var actParts = actions.split(",");
            for (var p : actParts)
            {
                var act = new WFAction(p);
                this.actions.add(act);
            }
            
        }
        
        
    }
}
