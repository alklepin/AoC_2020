package day03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;

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
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input1_sample.txt");
        int result = 0;
        for (int idx = 0; idx < lines.size(); idx += 3)
        {
            var first = lines.get(idx);
            var second = lines.get(idx+1);
            var third = lines.get(idx+2);
            var hsFirst = new HashSet<Character>();
            for (var c : first.toCharArray())
            {
                hsFirst.add(c);
            }
            var hsSecond = new HashSet<Character>();
            for (var c : second.toCharArray())
            {
                hsSecond.add(c);
            }
            var hsThird = new HashSet<Character>();
            for (var c : third.toCharArray())
            {
                hsThird.add(c);
            }
            hsFirst.retainAll(hsSecond);
            hsFirst.retainAll(hsThird);
            if (hsFirst.size() != 1)
            {
                System.out.println("Something wrong "+hsFirst.size());
            }
            char item = '!';
            for (var e : hsFirst)
            {
                item = e;
            }
            System.out.println(item);
            var score = item >= 'a' ? item - 'a' + 1 : item - 'A' + 27;
            result += score;
                
        }
        System.out.println(result);
        
    }
}
