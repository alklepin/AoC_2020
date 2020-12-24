package day24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.IntTriple;

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
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int result = 0;
        HashSet<IntPair> tiles = new HashSet<>();
        for (String line : lines)
        {
            IntPair tile = normalize(line);
            System.out.println(tile);
            if (tiles.contains(tile))
                tiles.remove(tile);
            else
                tiles.add(tile);
        }
        System.out.println(tiles.size());
    }
    
    
    public int countChar(String line, char c)
    {
        int counter = 0;
        char [] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] == c)
                counter++;
        }
        return counter;
    }
    
    public IntPair normalize(String path)
    {
        char [] chars = path.toCharArray();
        int eCount = 0;
        int wCount = 0;
        int neCount = 0;
        int nwCount = 0;
        int seCount = 0;
        int swCount = 0;
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            switch (c)
            {
                case 's':
                {
                    i++;
                    if (chars[i] == 'e')
                        seCount++;
                    else
                        swCount++;
                    break;
                }
                case 'n':
                {
                    i++;
                    if (chars[i] == 'e')
                        neCount++;
                    else
                        nwCount++;
                    break;
                }
                case 'e':
                {
                    eCount++;
                    break;
                }
                case 'w':
                {
                    wCount++;
                    break;
                }
            }
        }
//        System.out.println(eCount + " - " + wCount);
//        System.out.println(seCount + " - " + nwCount);
//        System.out.println(swCount + " - " + neCount);
        eCount -= wCount;
        seCount -= nwCount;
        swCount -= neCount;
//        IntTriple t = new IntTriple(eCount, seCount, swCount);
//        System.out.println(t);
        int x = eCount*2 + seCount - swCount;
        int y = seCount + swCount;
        return new IntPair(x, y);
    }
}
