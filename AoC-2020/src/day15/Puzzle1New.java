package day15;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;

public class Puzzle1New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1New().solve();
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
            numberToPrevTurn.put(lastNamedNumber, knownTurn);
            numberToTurn.put(lastNamedNumber, currentTurn);
        }
        
        for (int i = start.length; i < 2020; i++)
        {
            int currentTurn = i+1;
            int guess = lastNamedNumber;
            Integer guessState = getOrDefault(numberToPrevTurn, guess, i);
            lastNamedNumber = i - guessState;
            
            Integer knownTurn = numberToTurn.get(lastNamedNumber);
            numberToPrevTurn.put(lastNamedNumber, knownTurn);
            numberToTurn.put(lastNamedNumber, currentTurn);
        }
        System.out.println(lastNamedNumber);
    }
        
    
}
