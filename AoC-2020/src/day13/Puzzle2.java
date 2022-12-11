package day13;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.Pair;

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
    
    public void solve()
        throws Exception
    {
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int result = 0;
        int time = parseInt(lines.get(0), 0);
        ArrayList<IntPair> busesIdx = new ArrayList<>();
        String[] infos = lines.get(1).split(",");
        for (int i = 0; i < infos.length; i++)
        {
            if (!infos[i].equals("x"))
            {
                busesIdx.add(Pair.of(i, parseInt(infos[i], -1)));
            }
        }

        long mul = busesIdx.get(0).getY();
        long nod = mul;
        for (int i = 1; i < busesIdx.size(); i++)
        {
            IntPair pair = busesIdx.get(i);
            nod = NOD(nod, pair.getY());
            mul = mul * pair.getY();
        }
        
        long period = busesIdx.get(0).getY();
        long position = 0;
        for (int i = 1; i < busesIdx.size(); i++)
        {
            IntPair pair = busesIdx.get(i);
            while (true)
            {
                position += period;
                if (((position + pair.getX()) % pair.getY()) == 0)
                {
                    period *= pair.getY();
                    break;
                }
            }
        }
        
        
        System.out.println(position);
//        System.out.println(mul / nod);

//      System.exit(0);
        
//        HashMap<Integer, Integer> map = new HashMap<>();
//        outer:        
//            for (int i = 0; i < busesIdx.size(); i++)
//            {
//                for (int j = 1; j < 10000000; j++)
//                {
//                    IntPair pair = busesIdx.get(i);
//                    int busTime = -pair.getX() + pair.getY() * j;
//                    Integer counter = map.get(busTime);
//                    if (counter != null)
//                    {
//                        counter += 1;
//                    }
//                    else
//                    {
//                        counter = 1;
//                    }
//                    map.put(busTime, counter);
//                    if (counter == busesIdx.size())
//                    {
//                        System.out.println(counter+1);
//                        break outer;
//                    }
//                }
//            }
        
        
//        HashMap<Integer, Integer> map = new HashMap<>();
//        for (long j = 1; j < 10000000000L; j++)
//        {
//            boolean found = true;
//            for (int i = 0; i < busesIdx.size(); i++)
//            {
//                IntPair pair = busesIdx.get(i);
//                long remainder = (j + pair.getX()) % pair.getY();
//                if (remainder != 0)
//                {
//                    found = false;
//                    break;
//                }
//            }
//            if (found)
//            {
//                System.out.println(j);
//                break;
//            }
//        }
    }
    
    public long NOD(long a, long b)
    {
        if (a < b)
        {
            long temp = a;
            a = b;
            b = temp;
        }
        while (true)
        {
            long temp = b;
            b = a % b;
            a = temp;
            if (b == 0)
                return a;
        }
    }
}
