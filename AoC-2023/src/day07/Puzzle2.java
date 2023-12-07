package day07;

import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.plugins.tiff.ExifParentTIFFTagSet;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<Hand> hands = new ArrayList<>();
        for (String line : lines)
        {
            var parts = line.split("\\s+");
            var hand = new Hand(parts[0], parseInt(parts[1]));
            hands.add(hand);
        }
        Collections.sort(hands);
        long rank = 0;
        long result = 0;
        for (var hand : hands)
        {
            rank++;
            result += hand.getCost() * rank;
            System.out.println("Rank: "+rank+" Card: " + hand.cardsStr + " Type: "+hand.type + " Cost: "+hand.cost);
        }
        System.out.println(result);
        
    }
    
    public enum Type
    {
        FiveOfAKind,
        FourOfAKind,
        FullHouse,
        ThreeOfAKind,
        TwoPair,
        OnePair,
        HighCard
    }
    
    public static class Hand implements Comparable<Hand>
    {
        String cardsStr;
        char[] cards;
        Type type;
        
        private final String cardCharsStr = "J23456789TQKA";
        private final char[] cardChars = cardCharsStr.toCharArray();
        private int cost;
        
        public Hand(String cards, int cost)
        {
            cardsStr = cards;
            this.cards = cards.toCharArray();
            this.type = findType();
            this.cost = cost;
        }
        
        public int getCost()
        {
            return cost;
        }
        
        
        public Type findType()
        {
            var counters = new int[128];
            var jokers = 0;
            for (var c : cards)
            {
                if (c == 'J')
                    jokers++;
                else
                    counters[c]++;
            }
            ArrayList<Integer> fingerprint = new ArrayList<>();
            for (var c : cardChars)
            {
                var count = counters[c];
                fingerprint.add(count);
            }
            Collections.sort(fingerprint, Hand::comparatorIntInv);
            switch (fingerprint.get(0)+jokers)
            {
                case 5:
                    return Type.FiveOfAKind;
                case 4: 
                    return Type.FourOfAKind;
                case 3: 
                {
                    return fingerprint.get(1) == 2 ? Type.FullHouse : Type.ThreeOfAKind;
                }
                case 2:
                {
                    return fingerprint.get(1) == 2 ? Type.TwoPair : Type.OnePair;
                }
                case 1: 
                {
                    return Type.HighCard;
                }
                    
                default:
                {
                    throw new IllegalStateException();
                }
            }
        }
        
        public static int comparatorIntInv(Integer i1, Integer i2)
        {
            return -(i1.compareTo(i2));
        }

        @Override
        public int compareTo(Hand o)
        {
            var result = -type.compareTo(o.type);
            if (result == 0)
            {
                for (var idx = 0; idx < cards.length; idx++)
                {
                    var p1 = cardCharsStr.indexOf(cards[idx]);
                    var p2 = cardCharsStr.indexOf(o.cards[idx]);
                    if (p1 != p2)
                    {
                        result = p1 - p2;
                        break;
                    }
                }
            }
            return result;
        }
    }
}
