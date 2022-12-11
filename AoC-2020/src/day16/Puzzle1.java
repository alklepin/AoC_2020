package day16;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
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
        
        public boolean fitsInRange(int value)
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
        
        int result = 0;
        for (LinesGroup group : groups)
        {
            result += group.processGroup(this::processGroup);
        }
        
        ArrayList<Restriction> restrictions = new ArrayList<>();
        
        for (String line : groups.get(0))
        {
            String ranges[] = line.substring(line.indexOf(':')+1).split("or");
            String name = line.substring(0, line.indexOf(':'));
            Restriction r = new Restriction(name);
            for (int i = 0; i < ranges.length; i++)
            {
                String values[] = ranges[i].trim().split("-");
                int a = parseInt(values[0], -1);
                int b = parseInt(values[1], -1);
                r.addRange(new IntPair(a,b));
            }
            restrictions.add(r);
        }
        
//        for (Restriction r : restrictions)
//        {
//            r.print(System.out);
//        }
        
        ArrayList<String> otherTicketsInfos = new ArrayList<>();
        for (String line : groups.get(2))
        {
            otherTicketsInfos.add(line);
        }
        otherTicketsInfos.remove(0);

        long errorRate = 0;
        for (String line : otherTicketsInfos)
        {
            String[] nums = line.split(",");
            for (int i = 0; i < nums.length; i++)
            {
                int v = parseInt(nums[i], -1);
                boolean isValid = false;
                for (Restriction r : restrictions)
                {
                    isValid |= r.fitsInRange(v); 
                }
                if (!isValid)
                {
                    errorRate += v;
                }
            }
        }
        
        
        System.out.println(errorRate);
        
    }
}
