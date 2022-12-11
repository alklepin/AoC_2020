package day25;

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
        int key1 = 12232269;
        int key2 = 19452773;
//        int key1 = 5764801;
//        int key2 = 17807724;
        int loop1 = loop(key1);
        int loop2 = loop(key2);
        
        System.out.println(loop1);
        System.out.println(loop2);

        System.out.println(continueLoop(key1, loop2));
        System.out.println(continueLoop(key2, loop1));
    }
    
    public int loop(int target)
    {
        int count = 0;
        long value = 1;
        long subject = 7;
        while (value != target)
        {
            value = (subject * value) % 20201227; 
            count++;
        }
        return count;
    }
    
    public long continueLoop(long subject, int loopSize)
    {
        long value = 1;
        for (int i = 0; i < loopSize; i++)
        {
            value = (subject * value) % 20201227; 
        }
        return value;
    }
}
