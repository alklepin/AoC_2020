package day7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.PuzzleCommon;
import common.graph.Graph;

public class Puzzle1New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1New().solve();
    }
    
    static class Node
    {
        public String toString()
        {
            return "";
        }
    }
    
    private HashSet<String> startColors = new HashSet<>();
    private Graph<String, Node> graph = new Graph<String, Node>(Node::new);
    
    
    public void solve()
        throws Exception
    {
        ArrayList<String> lines = readAllLines("input1.txt");
        int result = 0;
        for (String line : lines)
        {
            String[] parts = parse("(.+) bags contain (.+)", line);
//          if (parts != null)
//              System.out.printf("%s -> %s \n", parts[1], parts[2]);
            String sourceColor = parts[1];
            String[] strTargetColors = parts[2].replace('.', ',').split("bags?,");
            
            if (strTargetColors.length != 1 || !strTargetColors[0].trim().equals("no other"))
            {
                for (String s : strTargetColors)
                {
                    parts = parse("([0-9]) (.*)", s.trim());
                    graph.addEdge(sourceColor, parts[2]);
                    parseInt(parts[1], -1);
                }
            }
            startColors.add(sourceColor);
        }
        
//        graph.print(System.out);
        
        String targetColor = "shiny gold";
        int count = 0;
        for (String color: startColors)
        {
            if (!color.equals(targetColor))
            {
                if (graph.isReachable(color, "shiny gold"))
                    count++;
                for (String s : graph.findPath(color, "shiny gold"))
                {
                    System.out.println(s);
                }
            }
            System.exit(1);
        }
        
        System.out.println(count);
    }
}
