package day08;

import common.PuzzleCommon;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.LinesGroup;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
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
        
        LinesGroup lines = readAllLines(inputFile);
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        
        String directions = lines.get(0);
        lines = lines.remainder(2);
        
        HashMap<String, Node> nodes = new HashMap<>();
        
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
            }
            else
            {
                // ignore
                //throw new IllegalStateException();
            }
        }
        
        int result = 0;
        int dirPos = 0;
        var currentNode = "AAA";
        while (!currentNode.equals("ZZZ"))
        {
            var n = nodes.get(currentNode);
            var d = directions.charAt(dirPos);
            String next = (d == 'L') ? n.m_left : n.m_right;  
            result++;
            
            currentNode = next;
            dirPos = (dirPos + 1) % directions.length();
        }
        
        System.out.println(result);
        
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
