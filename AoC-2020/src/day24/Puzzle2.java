package day24;

import java.util.HashSet;

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
    
    public IntPair[] neighbours = new IntPair[] {
        new IntPair(2, 0),
        new IntPair(-2, 0),
        new IntPair(1, 1),
        new IntPair(1, -1),
        new IntPair(-1, 1),
        new IntPair(-1, -1)
    };
    public IntPair[] neighboursToProcess = new IntPair[] {
        new IntPair(2, 0),
        new IntPair(-2, 0),
        new IntPair(1, 1),
        new IntPair(1, -1),
        new IntPair(-1, 1),
        new IntPair(-1, -1),
        new IntPair(0,0)
    };
    
    public void solve()
        throws Exception
    {
        LinesGroup lines = readAllLines("input1.txt");
//        LinesGroup lines = readAllLines("test.txt");
        int result = 0;
        HashSet<IntPair> tiles = new HashSet<>();
        for (String line : lines)
        {
            IntPair tile = normalize(line);
//            System.out.println(tile);
            if (tiles.contains(tile))
                tiles.remove(tile);
            else
                tiles.add(tile);
        }
        HashSet<IntPair> tilesNext = new HashSet<>();
        HashSet<IntPair> processed = new HashSet<>();
        for (int i = 0; i < 100; i++)
        {
            tilesNext.clear();
            tilesNext.addAll(tiles);
            processed.clear();
            for (IntPair pair : tiles)
            {
                for (IntPair delta : neighboursToProcess)
                {
                    IntPair next = pair.add(delta);
                    if (!processed.contains(next))
                    {
                        processed.add(next);
                        int count = 0;
                        for (IntPair n : neighbours)
                        {
                            IntPair nt = next.add(n);
                            if (tiles.contains(nt))
                            {
                                count++;
                            }
                        }
                        if (tiles.contains(next)) 
                        {
                            // black
                            if (count == 0 || count > 2)
                            {
                                tilesNext.remove(next);
                            }
                        }
                        else
                        {
                            // white
                            if (count == 2)
                            {
                                tilesNext.add(next);
                            }
                        }
                    }
                }
            }
            tiles.clear();
            tiles.addAll(tilesNext);
            System.out.println("Day" + i + ": " + tiles.size());
        }
        
        System.out.println(tiles.size());
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
