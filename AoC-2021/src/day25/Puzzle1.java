package day25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;

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
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        ArrayList<String> lines = readAllLines("input1.txt");

        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input10.txt");
        HashSet<IntPair> ewHerd = new HashSet<>();
        HashSet<IntPair> nsHerd = new HashSet<>();
        int result = 0;
        int lineIdx = 0;
        for (String line : lines)
        {
            for (var colIdx = 0; colIdx < line.length(); colIdx++)
            {
                var pair = Pair.of(lineIdx, colIdx);
                switch (line.charAt(colIdx))
                {
                    case '>':
                    {
                        ewHerd.add(pair);
                        break;
                    }
                    case 'v':
                    {
                        nsHerd.add(pair);
                        break;
                    }
                }
            }
            lineIdx++;
        }
        int heigth = lineIdx;
        int width = lines.get(0).length();
        
        int stepsCount = 0;
        boolean hasMove = true;
        while (hasMove)
        {
            stepsCount++;
            
            hasMove = false;
            HashSet<IntPair> ewHerdNext = new HashSet<>();
            HashSet<IntPair> nsHerdNext = new HashSet<>();
            for (var p : ewHerd)
            {
                var nextPair = Pair.of(p.getX(), (p.getY() + 1) % width);
                if (!ewHerd.contains(nextPair) && !nsHerd.contains(nextPair))
                {
                    ewHerdNext.add(nextPair);
                    hasMove = true;
                }
                else
                {
                    ewHerdNext.add(p);
                }
            }
            for (var p : nsHerd)
            {
                var nextPair = Pair.of((p.getX() + 1) % heigth, p.getY());
                if (!ewHerdNext.contains(nextPair) && !nsHerd.contains(nextPair))
                {
                    nsHerdNext.add(nextPair);
                    hasMove = true;
                }
                else
                {
                    nsHerdNext.add(p);
                }
            }
            
//            Board2D board = new Board2D(width, heigth);
//            board.setAll('.');
//            for (var p : ewHerd)
//            {
//                board.setAtRC(p, '>');
//            }
//            for (var p : nsHerd)
//            {
//                board.setAtRC(p, 'v');
//            }
//            board.printAsStrings(System.out);
//            System.out.println("===============");
            
            ewHerd = ewHerdNext;
            nsHerd = nsHerdNext;
            
            
        }
        
        System.out.println(stepsCount);
        
    }
}
