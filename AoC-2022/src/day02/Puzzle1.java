package day02;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
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
    
    
    private enum GameValue
    {
        Rock(1), Paper(2), Scissors(3);
        
        private int score;
        
        private GameValue(int score)
        {
            this.score = score;
        }
        
        public int scoreAgainst(GameValue other)
        {
            int result = 0;
            if (this == Rock)
            {
                if (other == Rock)
                    result += 3;
                else if (other == Scissors)
                    result += 6;
            }
            if (this == Paper)
            {
                if (other == Paper)
                    result += 3;
                else if (other == Rock)
                    result += 6;
            }
            if (this == Scissors)
            {
                if (other == Scissors)
                    result += 3;
                else if (other == Paper)
                    result += 6;
            }
            result += this.score;
            return result;
        }
    }
    
    private class Game
    {
        GameValue elf;
        GameValue player;
        
        public Game(String line)
        {
            elf = decode(line.charAt(0));
            player = decode(line.charAt(2));
        }
        
        private GameValue decode(char c)
        {
            return switch (c)
            {
                case 'A' -> GameValue.Rock; 
                case 'B' -> GameValue.Paper; 
                case 'C' -> GameValue.Scissors;
                case 'X' -> GameValue.Rock; 
                case 'Y' -> GameValue.Paper; 
                case 'Z' -> GameValue.Scissors;
                default -> null;
            };
        }
        
        
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
        int result = 0;
        for (String line : lines)
        {
            var game = new Game(line);
            result += game.player.scoreAgainst(game.elf);
        }
        System.out.println(result);
        
    }
}
