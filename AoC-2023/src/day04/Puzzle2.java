package day04;

import java.util.ArrayList;
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
        var result = 0;
        for (String line : group)
        {
        }
        return result;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        ArrayList<Card> cards = new ArrayList<>();
        for (String line : lines)
        {
            cards.add(new Card(line));
        }
        
        for (var idx = cards.size()-1; idx >= 0; idx--)
        {
            var card = cards.get(idx);
            card.copies = 0;
            for (var next = idx+1; next <= idx+card.cost; next++)
            {
                card.copies++;
                card.copies += cards.get(next).copies;
            }
        }
        
        for (var card : cards)
        {
            result += 1;
            result += card.copies;
        }
        
        System.out.println(result);
        
    }
    
    public static class Card
    {
        public int cost;
        public int copies;
        
        public Card(String line)
        {
            var parts = line.split(":")[1].trim().split("\\|");
            var winning = parts[0].trim().split("\\s+");
            var numbers = parts[1].trim().split("\\s+");
            var hsWinn = new HashSet<Integer>();
            for (var s : winning)
            {
                var n = parseInt(s);
                hsWinn.add(n);
            }
            cost = 0;
            for (var s : numbers)
            {
                var n = parseInt(s);
                if (hsWinn.contains(n))
                {
                    cost++;
                }
            }
        }
    }
}
