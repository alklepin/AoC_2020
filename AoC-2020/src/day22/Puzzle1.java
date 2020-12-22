package day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import common.PuzzleCommon;
import common.queries.Query;

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

    LinkedList<Integer> deck1 = new LinkedList<Integer>();
    LinkedList<Integer> deck2 = new LinkedList<Integer>();
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());
        
        for (String line : Query.wrap(groups.get(0)).skip(1))
        {
            deck1.add(parseInt(line));
        }
        
        for (String line : Query.wrap(groups.get(1)).skip(1))
        {
            deck2.add(parseInt(line));
        }
        
        while (deck1.size() > 0 && deck2.size() > 0)
        {
            Integer card1 = deck1.poll();
            Integer card2 = deck2.poll();
            if (card1.compareTo(card2) > 0)
            {
                deck1.add(card1);
                deck1.add(card2);
            }
            else
            {
                deck2.add(card2);
                deck2.add(card1);
            }
        }
        
        LinkedList<Integer> winner = deck1.size() > 0 ? deck1 : deck2;
        long score = 0;
        long value = winner.size();
        for (Integer card : winner)
        {
            score += value * card;
            value--;
        }
        
        
        System.out.println(score);
        
    }
}
