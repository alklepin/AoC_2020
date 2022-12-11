package day02;

import java.util.ArrayList;
import java.util.HashMap;

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
            if (this == other)
            {
                result += 3;
            }
            else if (other == this.getWeaker())
            {
                result += 6;
            }
            result += this.score;
            return result;
        }

        public GameValue getStronger()
        {
            return switch (this)
                {
                    case Paper -> Scissors;
                    case Rock -> Paper;
                    case Scissors -> Rock;
                };
        }

        public GameValue getWeaker()
        {
            return switch (this)
                {
                    case Paper -> Rock;
                    case Rock -> Scissors;
                    case Scissors -> Paper;
                };
        }
    }
    
    private class Game
    {
        GameValue elf;
        GameValue player;
        
        public Game(String line)
        {
            elf = decode(line.charAt(0));
            player = decode(line.charAt(2), elf);
        }
        
        private GameValue decode(char c)
        {
            return switch (c)
                {
                    case 'A' -> GameValue.Rock; 
                    case 'B' -> GameValue.Paper; 
                    case 'C' -> GameValue.Scissors;
                    default -> null;
                };
        }

        private GameValue decode(char c, GameValue elf)
        {
            return switch (c)
            {
                case 'X' -> elf.getWeaker(); 
                case 'Y' -> elf; 
                case 'Z' -> elf.getStronger();
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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        for (String line : lines)
        {
            var game = new Game(line);
            result += game.player.scoreAgainst(game.elf);
        }
        System.out.println(result);
        
    }
}
