package day13;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;

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
    
    public void solve()
        throws Exception
    {
        LinesGroup lines = readAllLines("input1.txt");
//        LinesGroup lines = readAllLines("test.txt");
        int result = 0;
        int time = parseInt(lines.get(0), 0);
        ArrayList<Integer> busesIdx = new ArrayList<>();
        String[] infos = lines.get(1).split(",");
        for (int i = 0; i < infos.length; i++)
        {
            if (!infos[i].equals("x"))
            {
                busesIdx.add(parseInt(infos[i], -1));
            }
        }
        int minTime = Integer.MAX_VALUE;
        int busIdx = 0;
        for (int i = 0; i < busesIdx.size(); i++)
        {
            int period = busesIdx.get(i);
            int lowBound = (time / period) * period;
            if (time > lowBound)
            {
                lowBound += period;
            }
            if (lowBound - time < minTime)
            {
                minTime = lowBound - time;
                busIdx = period;
            }
        }
        
        System.out.println((long)busIdx * minTime);
        
    }
}
