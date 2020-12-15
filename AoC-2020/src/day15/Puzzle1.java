package day15;

import java.util.ArrayList;
import java.util.HashMap;

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
        int[] start = new int[] {1,0,15,2,10,13};
//        int[] start = new int[] {0,3,6};
        HashMap<Integer, Integer> numberToTurn = new HashMap<>();
        HashMap<Integer, Integer> numberToPrevTurn = new HashMap<>();
        
        int lastNamedNumber = 0;
        for (int i = 0; i < start.length; i++)
        {
            lastNamedNumber = start[i];
            int currentTurn = i+1;
            Integer knownTurn = numberToTurn.get(lastNamedNumber);
            if (knownTurn == null)
            {
                numberToTurn.put(lastNamedNumber, currentTurn);
            }
            else
            {
                numberToPrevTurn.put(lastNamedNumber, knownTurn);
                numberToTurn.put(lastNamedNumber, currentTurn);
            }
        }
        
        for (int i = start.length; i < 2020; i++)
        {
            int currentTurn = i+1;
            int guess = lastNamedNumber;
            Integer guessState = numberToPrevTurn.get(guess);
            if (guessState == null)
            {
                lastNamedNumber = 0;
            }
            else
            {
                lastNamedNumber = i - guessState;
            }
            
            Integer knownTurn = numberToTurn.get(lastNamedNumber);
            if (knownTurn == null)
            {
                numberToTurn.put(lastNamedNumber, currentTurn);
            }
            else
            {
                numberToPrevTurn.put(lastNamedNumber, knownTurn);
                numberToTurn.put(lastNamedNumber, currentTurn);
            }
            
        }
        System.out.println(lastNamedNumber);
    }
        
    
}
