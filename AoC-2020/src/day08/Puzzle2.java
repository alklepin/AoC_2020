package day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    
    static class Command
    {
        public String m_code;
        public int m_arg;

        public Command(String coedg, int arg)
        {
            m_code = coedg;
            m_arg = arg;
        }

        @Override
        public String toString()
        {
            return "Command [m_code=" + m_code + ", m_arg=" + m_arg + "]";
        }
    }
    
    public static class LoopException extends RuntimeException
    {
        
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<Command> program = new ArrayList<>();
        ArrayList<String> lines = readAllLines("input1.txt");
        long result = 0;
        for (String line : lines)
        {
            String [] parts = line.split(" ");
            program.add(new Command(parts[0], parseInt(parts[1], Integer.MIN_VALUE)));
        }
        
//        for (Command c : program)
//        {
//            System.out.println(c);
//        }
        
        for (int i = 0; i < program.size(); i++)
        {
            try
            {
                result = execute(program, i);
            }
            catch (LoopException ex)
            {
                continue;
            }
            break;
        }
        System.out.println(result);
        
    }
    
    public long execute(ArrayList<Command> program, int cmdToChange)
    {
        HashSet<Integer> visited = new HashSet<>();
        int currentIdx = 0;
        long accumulator = 0;
        while (currentIdx < program.size())
        {
            visited.add(currentIdx);
            Command current = program.get(currentIdx);
            int idxDelta = 1;
            switch (current.m_code)
            {
                case "nop":
                    if (currentIdx == cmdToChange)
                        idxDelta = current.m_arg;
                    break;
                case "acc":
                    accumulator += current.m_arg;
                    break;
                case "jmp":
                    if (currentIdx != cmdToChange)
                        idxDelta = current.m_arg;
                    break;
            }
            currentIdx += idxDelta;
            if (visited.contains(currentIdx))
            {
                throw new LoopException();
            }
        }
        return accumulator;
    }
}
