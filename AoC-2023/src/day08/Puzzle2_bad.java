package day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2_bad extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_bad().solve();
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
//        var inputFile = "input2_test.txt";
        
        LinesGroup lines = readAllLines(inputFile);
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        
        String directions = lines.get(0);
        lines = lines.remainder(2);
        
        HashMap<String, Node> nodes = new HashMap<>();
        
        HashSet<String> startState = new HashSet<>();
        Pattern p = Pattern.compile("(.*) = \\((.*), (.*)\\)");
        for (String line : lines)
        {
            Matcher m = p.matcher(line);
            if (m.matches())
            {
                String from = m.group(1);
                String left = m.group(2);
                String right = m.group(3);
                nodes.put(from, new Node(left, right));
                if (from.endsWith("A"))
                    startState.add(from);
            }
            else
            {
                // ignore
                //throw new IllegalStateException();
            }
        }
        
        int result = 0;
        int dirPos = 0;
        var currentState = startState;
        printState(currentState);
        while (!isFinal(currentState))
        {
            var nextState = new HashSet<String>();
            for (var s : currentState)
            {
                var n = nodes.get(s);
                var d = directions.charAt(dirPos);
                String next = (d == 'L') ? n.m_left : n.m_right;
                nextState.add(next);
            }

            result++;
            currentState = nextState;
//            printState(nextState);
            dirPos = (dirPos + 1) % directions.length();
        }
        
        System.out.println(result);
        
    }
    
    public static boolean isFinal(HashSet<String> list)
    {
        for (var l : list)
        {
            if (!l.endsWith("Z"))
                return false;
        }
        return true;
    }

    public static void printState(HashSet<String> list)
    {
        StringBuilder sb = new StringBuilder();
        for (var l : list)
        {
            sb.append(l).append(',');
        }
        System.out.println(sb);
    }
    
    public static class Node
    {
        String m_left;
        String m_right;

        public Node(String left, String right)
        {
            m_left = left;
            m_right = right;
        }
    }
}
