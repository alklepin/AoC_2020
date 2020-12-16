package day16;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;
import common.boards.IntPair;
import common.queries.Query;

public class Puzzle1New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1New().solve();
    }
    
    public class Restriction
    {
        public ArrayList<IntPair> m_ranges = new ArrayList<>();
        
        public String m_name;
        
        public Restriction(String name)
        {
            m_name = name;
        }

        public void addRange(IntPair range)
        {
            m_ranges.add(range);
        }
        
        public boolean contains(int value)
        {
            for (IntPair p : m_ranges)
            {
                if (value >= p.getX() && value <= p.getY())
                    return true;
            }
            return false;
        }

        public void print(PrintStream out)
        {
            out.printf("%s: ", m_name);
            for (IntPair p : m_ranges)
            {
                out.printf("%d-%d ", p.getX(), p.getY());
            }
            out.println();
            
        }
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("test.txt");
        // System.out.println(groups.size());
        
        ArrayList<Restriction> restrictions = new ArrayList<>();
        
        for (String line : groups.get(0))
        {
            String ranges[] = line.substring(line.indexOf(':')+1).split("or");
            String name = line.substring(0, line.indexOf(':'));
            Restriction r = new Restriction(name);
            for (int i = 0; i < ranges.length; i++)
            {
                String values[] = ranges[i].trim().split("-");
                int a = parseInt(values[0]);
                int b = parseInt(values[1]);
                r.addRange(new IntPair(a,b));
            }
            restrictions.add(r);
        }
        
//        for (Restriction r : restrictions)
//        {
//            r.print(System.out);
//        }
        
        ArrayList<String> otherTicketsInfos = Query.wrap(groups.get(2)).skip(1).toList();

        long errorRate = 0;
        for (String line : otherTicketsInfos)
        {
            String[] nums = line.split(",");
            for (int i = 0; i < nums.length; i++)
            {
                int v = parseInt(nums[i]);
                boolean isValid = Query.wrap(restrictions).any(r -> r.contains(v));
                if (!isValid)
                {
                    errorRate += v;
                }
            }
        }
        
        
        System.out.println(errorRate);
        
    }
}
