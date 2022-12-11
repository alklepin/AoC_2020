package day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

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
        ArrayList<Long> scores = new ArrayList<>();
        for (String line : lines)
        {
            Stack<Character> stack = new Stack<>();
            char[] chars = line.toCharArray();
            boolean stop = false;
            
            for (var idx = 0; idx < chars.length && !stop; idx++)
            {
                char c = chars[idx];
                switch (c)
                {
                    case '{':
                    case '[':
                    case '(':
                    case '<':
                    {
                        stack.add(c);
                        break;
                    }
                    case '}':
                    {
                        if (stack.pop() != '{')
                        {
                            result += 1197;
                            stop = true;
                        }
                        break;
                    }
                    case ']':
                    {
                        if (stack.pop() != '[')
                        {
                            result += 57;
                            stop = true;
                        }
                        break;
                    }
                    case ')':
                    {
                        if (stack.pop() != '(')
                        {
                            result += 3;
                            stop = true;
                        }
                        break;
                    }

                    case '>':
                    {
                        if (stack.pop() != '<')
                        {
                            result += 25137;
                            stop = true;
                        }
                        break;
                    }
                }
            }
            long score = 0;
            if (!stop)
            {
                while (stack.size() > 0)
                {
                    char c = stack.pop();
                    switch (c)
                    {
                        case '(':
                            score = score * 5 + 1;
                            break;
                        case '[':
                            score = score * 5 + 2;
                            break;
                        case '{':
                            score = score * 5 + 3;
                            break;
                        case '<':
                            score = score * 5 + 4;
                            break;
                    }
                }
                scores.add(score);
            }
        }
        Collections.sort(scores);
        result = scores.get(scores.size() / 2 );
        System.out.println(result);
        
    }
}
