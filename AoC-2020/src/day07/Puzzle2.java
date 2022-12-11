package day07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    
    private HashMap<String, Rule> rules = new HashMap<>();
    private HashSet<String> startColors = new HashSet<>();
    
    public void solve()
        throws Exception
    {
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int result = 0;
        for (String line : lines)
        {
            Rule rule = new Rule(line);
            rule.print();
            System.out.printf("====\n");
            rules.put(rule.sourceColor, rule);
            startColors.add(rule.sourceColor);
        }
        
        result = countChildrenOf("shiny gold", rules);
        System.out.println(result-1);
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
