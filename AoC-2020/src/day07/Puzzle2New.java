package day07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.Graph;
import common.graph.TraverseState;
import day07.Puzzle1New.Node;

public class Puzzle2New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2New().solve();
    }
    
    private HashSet<String> startColors = new HashSet<>();
    private Graph<String, Node> graph = new Graph<String, Node>(Node::new);
    private HashMap<String, Integer> bagCounts = new HashMap<>();
    
    public void solve()
        throws Exception
    {
        LinesGroup lines = readAllLines("input1.txt");
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
                    int bagsCount = parseInt(parts[1], -1);
                    bagCounts.put(sourceColor+":"+parts[2], bagsCount);
                }
            }
            startColors.add(sourceColor);
        }
        TraverseState<String, Long> state = graph.traverseDFS("shiny gold", this::countBags);
        System.out.println(state.getNodeValue("shiny gold")-1);
    }
    
    private Long countBags(String node, Iterable<String> children, TraverseState<String, Long> state)
    {
        long count = 1;
        for (String childNode : children)
        {
            Long nodeValue = state.getNodeValue(childNode);
            if (nodeValue != null)
            {
                int bagsCount = bagCounts.get(node+":"+childNode);
                count += nodeValue * bagsCount;
            }
        }
        return count;
    }
    
    private static int countChildrenOf(String from, HashMap<String, Rule> rules)
    {
        int result = 1;
        Rule rule = rules.get(from);
        for (int i = 0; i < rule.targetColors.size(); i++)
        {
            String next = rule.targetColors.get(i);
            int num = rule.targetAmounts.get(i);
            result += num * countChildrenOf(next, rules);
        }
        return result;
    }
    
    
    
    
    public static class Rule
    {
        public ArrayList<String> targetColors;
        public ArrayList<Integer> targetAmounts;
        public String sourceColor;
        
        public Rule(String line)
        {
            String[] parts = parse("(.+) bags contain (.+)", line);
//          if (parts != null)
//              System.out.printf("%s -> %s \n", parts[1], parts[2]);
          sourceColor = parts[1];
          String[] strTargetColors = parts[2].replace('.', ',').split("bags?,");
          
          targetColors = new ArrayList<String>();
          targetAmounts = new ArrayList<Integer>();
          if (strTargetColors.length != 1 || !strTargetColors[0].trim().equals("no other"))
          {
              for (String s : strTargetColors)
              {
                  parts = parse("([0-9]) (.*)", s.trim());
                  targetColors.add(parts[2]);
                  targetAmounts.add(parseInt(parts[1], -1));
              }
          }
        }
        
        public void print() 
        {
            System.out.printf("%s\n", sourceColor);
            for (int i = 0; i < targetColors.size(); i++)
            {
                System.out.printf("%s -> %d %s \n", sourceColor, targetAmounts.get(i), targetColors.get(i));
            }
        }
    }
}
