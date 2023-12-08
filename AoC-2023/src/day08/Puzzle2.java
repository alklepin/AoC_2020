package day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.LinesGroup;
import common.Numbers;
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
    
    HashMap<String, Node> nodes = new HashMap<>();
    String directions;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLines(inputFile);
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        
        directions = lines.get(0);
        lines = lines.remainder(2);
        
        ArrayList<String> startState = new ArrayList<>();
        
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
        
        ArrayList<Integer> lengths = new ArrayList<>();
        for (var s : startState)
        {
            lengths.add(getPathLength(s));
        }
        var result = Numbers.nok(lengths);
        
        System.out.println(result);

        
//        System.out.println(getPathLength("AAA"));
//        System.out.println(getPathLength("XFA"));
//        System.out.println(getPathLength("SBA"));
//        System.out.println(getPathLength("QJA"));
//        System.out.println(getPathLength("BFA"));
//        System.out.println(getPathLength("DFA"));

//        System.out.println(getPathLength("BKZ"));
        
    }
    
    public int getPathLength(String from)
    {
        int result = 0;
        int dirPos = 0;
        var currentNode = from;
        var depth = 1;
        while (depth > 0)
        {
            var n = nodes.get(currentNode);
            var d = directions.charAt(dirPos);
            String next = (d == 'L') ? n.m_left : n.m_right;  
            result++;
            
            currentNode = next;
            dirPos = (dirPos + 1) % directions.length();
            if (currentNode.endsWith("Z"))
                depth--;
        }
        System.out.println(currentNode);
        return result;
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
