package day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.LongPair;

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

    static HashMap<State, LongPair> wins = new HashMap<State, LongPair>();
    
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
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        long result = 0;
        int pos1 = parseInt(parse("Player \\d starting position: (\\d+)", lines.get(0))[1]) - 1;
        int pos2 = parseInt(parse("Player \\d starting position: (\\d+)", lines.get(1))[1]) - 1;

        var state = new State(false, pos1, pos2, 0, 0);
        var r = findWins(state);
        
        
        System.out.println(r);
        System.out.println(Math.max(r.getX(), r.getY()));

//        int[] counts = new int[15];
//        for (int x = 1; x <= 3; x++)
//            for (int y = 1; y <= 3; y++)
//                for (int z = 1; z <= 3; z++)
//                    counts[x+y+z]++;
//        for (var i = 0; i < counts.length; i++)
//        {
//            System.out.printf("%s -> %s\n", i, counts[i]);
//        }
        
    }
    
    static int[] outcomes = new int[] {0, 0, 0, 1, 3, 6, 7, 6, 3, 1};
    
    public static LongPair findWins(State state)
    {
        // possible outcomes:
        // (1,1,1) = 3 x 1
        // (2,1,1) = (1,2,1) = (1,1,2) = 4 x 3 
        // (3,1,1) = (1,3,1) = (1,1,3) = (2,2,1) = (2,1,2) = (1,2,2) = 5 x 6 

        // (4,1,1) = (1,4,1) = (1,1,4) = (2,2,1) = (2,1,2) = (1,2,2) = 6 x 7 
        
        // (3,3,1) = (3,1,3) = (1,3,3) = 7 x 6 
        // (3,3,1) = (3,1,3) = (1,3,3) = 8 x 3 
        // (3,3,3) = 9 x 1
        
//        3 -> 1
//        4 -> 3
//        5 -> 6
//        6 -> 7
//        7 -> 6
//        8 -> 3
//        9 -> 1
        LongPair result;
        
        result = wins.get(state);
        if (result != null)
            return result;

        if (state.score1 >= 21)
        {
            var w = new LongPair(1, 0);
            result = w;
        }
        else if (state.score2 >= 21)
        {
            var w = new LongPair(0, 1);
            result = w;
        }
        else
        {
            var winsCount = LongPair.ZERO;
            if (state.secondMoves)
            {
                for (int idx = 3; idx <= 9; idx++)
                {
                    var newPos = (state.pos2 + idx) % 10;
                    var nextState = new State(false, state.pos1, newPos, state.score1, state.score2 + newPos + 1);
                    var wins = findWins(nextState);
                    winsCount = winsCount.add(wins.mult(outcomes[idx]));
                }
            }
            else
            {
                for (int idx = 3; idx <= 9; idx++)
                {
                    var newPos = (state.pos1 + idx) % 10;
                    var nextState = new State(true, newPos, state.pos2, state.score1 + newPos + 1, state.score2);
                    var wins = findWins(nextState);
                    winsCount = winsCount.add(wins.mult(outcomes[idx]));
                }
            }
            result = winsCount;
        }
        
        wins.put(state, result);
//        System.out.println(state + " -> " + result);
        return result;
    }

    
    private static class State
    {
        public int pos1;
        public int pos2;
        public int score1;
        public int score2;
        public boolean secondMoves;

        public State(boolean secondMoves, int pos1, int pos2, int score1, int score2)
        {
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.score1 = score1;
            this.score2 = score2;
            this.secondMoves = secondMoves;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(pos1, pos2, score1, score2, secondMoves);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            State other = (State)obj;
            return pos1 == other.pos1 && pos2 == other.pos2
                && score1 == other.score1 && score2 == other.score2
                && secondMoves == other.secondMoves;
        }

        @Override
        public String toString()
        {
            return "State ((" + pos1 + ", " + pos2 + "), ("
                + score1 + ", " + score2 + "), " + secondMoves;
        }

    }
}
