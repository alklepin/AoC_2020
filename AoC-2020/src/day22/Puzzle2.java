package day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.PuzzleCommon;
import common.graph.Pair;
import common.queries.Query;

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
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("test.txt");
        // System.out.println(groups.size());
        
        LinkedList<Integer> deck1 = new LinkedList<Integer>();
        LinkedList<Integer> deck2 = new LinkedList<Integer>();
        
        for (String line : Query.wrap(groups.get(0)).skip(1))
        {
            deck1.add(parseInt(line));
        }
        
        for (String line : Query.wrap(groups.get(1)).skip(1))
        {
            deck2.add(parseInt(line));
        }

        int winner = playSubGame(1, deck1, deck2);
        LinkedList<Integer> winnerDeck;
        if (winner == 2)
            winnerDeck = deck2;
        else
            winnerDeck = deck1;
        
        long score = 0;
        long value = winnerDeck.size();
        for (Integer card : winnerDeck)
        {
            score += value * card;
            value--;
        }

        System.out.println(score);
    }

    private int playSubGame(int gameId, LinkedList<Integer> deck1, LinkedList<Integer> deck2)
    {
        HashSet<Pair<String, String>> played = new HashSet<>();
//        System.out.printf("=== Game %d ===\n", gameId);
        int round = 0;
        while (deck1.size() > 0 && deck2.size() > 0)
        {
            round++;
//            System.out.printf("-- Round %d (Game %d) --\n", round, gameId);
            
            String state1 = getString(deck1);
            String state2 = getString(deck2);
//            System.out.println("Player1 deck: "+state1);
//            System.out.println("Player2 deck: "+state2);
            
            Pair<String, String> pair = new Pair<String, String>(state1, state2);
            if (played.contains(pair))
            {
                return 1;
            }
                
            played.add(pair);
            
            int winPlayer = 0;
            
            Integer card1 = deck1.poll();
            Integer card2 = deck2.poll();
            
            if (deck1.size() >= card1 && deck2.size() >= card2)
            {
                LinkedList<Integer> subDeck1 = new LinkedList<Integer>();
                LinkedList<Integer> subDeck2 = new LinkedList<Integer>();
                for (Integer v : Query.wrap(deck1).take(card1))
                    subDeck1.add(v);
                for (Integer v : Query.wrap(deck2).take(card2))
                    subDeck2.add(v);
                winPlayer = playSubGame(gameId+1, subDeck1, subDeck2);    
            }
            else if (card1.compareTo(card2) > 0)
            {
                winPlayer = 1;
            }
            else
            {
                winPlayer = 2;
            }
            if (winPlayer == 1)
            {
                deck1.add(card1);
                deck1.add(card2);
            }
            else if (winPlayer == 2)
            {
                deck2.add(card2);
                deck2.add(card1);
            }
            else
            {
                throw new IllegalStateException();
            }
        }
        if (deck1.size() > 0)
            return 1;
        else
            return 2;
    }

    private String getString(LinkedList<Integer> deck)
    {
        StringBuilder result = new StringBuilder();
        for (Integer v : deck)
        {
            result.append(v).append(',');
        }
        result.setLength(result.length()-1);
        return result.toString();
    }
    
    public int playRound(LinkedList<Integer> deck1, LinkedList<Integer> deck2)
    {
        Integer card1 = deck1.poll();
        Integer card2 = deck2.poll();
        
        if (deck1.size() >= card1 && deck2.size() >= card2)
        {
        }
        
//        if (card1.compareTo(card2) > 0)
//        {
//            winner = deck1;
//            break;
//        }
//        else
//        {
//            winner = deck2;
//            break;
//        }
        return 1;
    }
}
