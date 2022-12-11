package day16;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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

        ArrayList<String> validTickets = new ArrayList<>();
        for (String line : otherTicketsInfos)
        {
            String[] nums = line.split(",");
            boolean[] validity = new boolean[nums.length];
            boolean isValidTicket = true;
            for (int i = 0; i < nums.length; i++)
            {
                int v = parseInt(nums[i], -1);
                boolean isValid = false;
                int idx = 0;
                for (Restriction r : restrictions)
                {
                    validity[idx] = r.fitsInRange(v);
                    isValid |= validity[idx];
                    idx++;
                    
                }
                if (!isValid)
                {
                    isValidTicket = false;
                }
            }
            if (isValidTicket)
            {
                validTickets.add(line);
            }
        }

        validTickets.add(groups.get(1).get(1));
        
//        for (String row : validTickets)
//        {
//            System.out.println(row);
//        }

        ArrayList<int[]> parsedTickets = new ArrayList<>();
        for (String line : validTickets)
        {
            String[] nums = line.split(",");
            int[] numbers = new int[nums.length];
            for (int i = 0; i < nums.length; i++)
            {
                numbers[i] = parseInt(nums[i], -1);
            }
            parsedTickets.add(numbers);
        }
        
        int numOfColumns = parsedTickets.get(0).length;
        ArrayList<ArrayList<Boolean>> restrToColumn = new ArrayList<>();
        for (int i = 0; i < numOfColumns; i++)
        {
            restrToColumn.add(new ArrayList<Boolean>(Arrays.asList(new Boolean[numOfColumns])));
        }
        
        int rIdx = 0;
        for (Restriction r : restrictions)
        {
            for (int col = 0; col < numOfColumns; col++)
            {
                boolean matches = true;
                for (int[] ticket : parsedTickets)
                {
                    if (!r.fitsInRange(ticket[col]))
                    {
                        matches = false;
                        break;
                    }
                }
                restrToColumn.get(rIdx).set(col, matches);
            }
            rIdx++;
        }

        for (rIdx = 0; rIdx < numOfColumns; rIdx++)
        {
            for (int col = 0; col < numOfColumns; col++)
            {
                System.out.print((restrToColumn.get(rIdx).get(col) ? "1" : "0") + " ");
            }
            System.out.println();
        }
        
        HashMap<Integer, Integer> restrToColumns = new HashMap<>();
        
        for (int i = 0; i < numOfColumns; i++)
        {
            for (rIdx = 0; rIdx < restrToColumn.size(); rIdx++)
            {
                
            }
        }
        
//        ArrayList<boolean[]> validTickets = new ArrayList<>();
        
//        long errorRate = 0;
//        for (String line : otherTicketsInfos)
//        {
//            String[] nums = line.split(",");
//            boolean[] validity = new boolean[nums.length];
//            boolean isValidTicket = true;
//            for (int i = 0; i < nums.length; i++)
//            {
//                int v = parseInt(nums[i], -1);
//                boolean isValid = false;
//                int idx = 0;
//                for (Restriction r : restrictions)
//                {
//                    validity[idx] = r.fitsInRange(v);
//                    isValid |= validity[idx];
//                    idx++;
//                    
//                }
//                if (!isValid)
//                {
//                    isValidTicket = false;
//                }
//            }
//            if (isValidTicket)
//            {
//                validTickets.add(validity);
//            }
//        }
//
//        for (boolean[] row : validTickets)
//        {
//            for (int i = 0; i < row.length; i++)
//            {
//                System.out.print(row[i] ? "X " : "O ");
//            }
//            System.out.println();
//        }
        
//        System.out.println(errorRate);
        
    }
}
