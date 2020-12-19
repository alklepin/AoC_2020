package day19;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

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
    
    public interface Match
    {
        public int consume(String s);
        public ArrayList<String> generate();
    }
    
    public class SimpleMatchRule implements Match
    {
        public char m_expected;
        
        public SimpleMatchRule(char c)
        {
            m_expected = c;
        }
        
        @Override
        public int consume(String s)
        {
            if (s.length() > 0 && s.charAt(0) == m_expected)
                return 1;
            return 0;
        }

        @Override
        public ArrayList<String> generate()
        {
            ArrayList<String> result = new ArrayList<String>();
            result.add(""+m_expected);
            return result;
        }
    }

    public class EmptyMatchRule implements Match
    {
        public EmptyMatchRule()
        {
        }
        
        @Override
        public int consume(String s)
        {
            return 0;
        }

        @Override
        public ArrayList<String> generate()
        {
            ArrayList<String> result = new ArrayList<String>();
            result.add("");
            return result;
        }
    }
    
    public class ComplexMatchRule implements Match
    {
        private Match m_rule1;
        private Match m_rule2;
        private Match m_rule3;
        
        public ComplexMatchRule(Match rule1, Match rule2, Match rule3)
        {
            m_rule1 = rule1;
            m_rule2 = rule2;
            m_rule3 = rule3;
        }
        
        @Override
        public int consume(String s)
        {
            int part1Length = m_rule1.consume(s);
            String part2 = s.substring(part1Length);
            int part2Length = m_rule2.consume(part2);
            String part3 = s.substring(part1Length + part2Length);
            int part3Length = m_rule3.consume(part3);
            
            if (part1Length + part2Length + part3Length < s.length())
                return 0;
            return s.length();
        }

        @Override
        public ArrayList<String> generate()
        {
            ArrayList<String> part1 = m_rule1.generate();
            ArrayList<String> part2 = m_rule2.generate();
            ArrayList<String> part3 = m_rule3.generate();
            ArrayList<String> result = new ArrayList<String>();
            for (String s1 : part1)
                for (String s2 : part2)
                    for (String s3 : part3)
                        result.add(s1+s2+s3);
            return result;
        }
    }

    public class AlternateMatchRule implements Match
    {
        private Match m_rule1;
        private Match m_rule2;
        
        public AlternateMatchRule(Match rule1, Match rule2)
        {
            m_rule1 = rule1;
            m_rule2 = rule2;
        }

        @Override
        public int consume(String s)
        {
            int part1Lemgth = m_rule1.consume(s);
            if (part1Lemgth == s.length())
                return s.length();

            int part2Lemgth = m_rule2.consume(s);
            if (part2Lemgth == s.length())
                return s.length();
            
            return 0;
        }
        
        @Override
        public ArrayList<String> generate()
        {
            ArrayList<String> part1 = m_rule1.generate();
            ArrayList<String> part2 = m_rule2.generate();
            HashSet<String> bothParts = new HashSet<String>();
            bothParts.addAll(part1);
            bothParts.addAll(part2);
            ArrayList<String> result = new ArrayList<String>(bothParts);
            return result;
        }
    }
    
    public class InputComparator implements Comparator<String>
    {

        @Override
        public int compare(String o1, String o2)
        {
            int idx1 = parseInt(o1.split(":")[0]);
            int idx2 = parseInt(o2.split(":")[0]);
            return idx1 - idx2;
        }
        
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("test.txt");
        // System.out.println(groups.size());
        
        int result = 0;
        
        ArrayList<Match> rules = new ArrayList<>(groups.get(0).size());
        ArrayList<String> ruleLines = new ArrayList<>(groups.get(0).size());
        
        for (String line: groups.get(0)) 
        {
            rules.add(null);
            ruleLines.add(null);
        }
        for (String line: groups.get(0)) 
        {
            String [] parts = line.split(":");
            int ruleIdx = parseInt(parts[0]);
            ruleLines.set(ruleIdx, line);
        }
        
        Match rule0 = parseRule(0, rules, ruleLines);
        HashSet<String> validLines = new HashSet<>();
        long counter = 0;
        for (String s : rule0.generate())
        {
            validLines.add(s);
//            System.out.println(s);
            counter++;
        }
//        System.out.println(counter);
        
        for (String testLine : groups.get(1))
        {
//            if (rule0.consume(testLine) == testLine.length())
//            {
//                System.out.println(testLine);
//                result++;
//            }
            if (validLines.contains(testLine))
            {
//                System.out.println(testLine);
                result++;
            }
        }
        
        System.out.println(result);
    }
    
    
    private Match parseRule(int i, ArrayList<Match> rules, ArrayList<String> ruleLines)
    {
        Match result = rules.get(i);
        if (result != null)
            return result;
        
        String line = ruleLines.get(i);
        Match rule;
        String [] parts = line.split(":");
        int ruleIdx = parseInt(parts[0]);
        String ruleString = parts[1].trim();
        int quoteIdx = ruleString.indexOf("\""); 
        if (quoteIdx >= 0)
        {
            rule = new SimpleMatchRule(ruleString.charAt(quoteIdx+1));
        }
        else
        {
            if (ruleString.contains("|"))
            {
                String[] twoRules = ruleString.split("\\|");
                Match rule1 = createRule(twoRules[0].trim(), rules, ruleLines);
                Match rule2 = createRule(twoRules[1].trim(), rules, ruleLines);
                rule = new AlternateMatchRule(rule1, rule2);
            }
            else
            {
                rule = createRule(ruleString, rules, ruleLines);
            }
        }
        rules.set(ruleIdx, rule);
        return rule;
    }

    public Match createRule(String ruleString, ArrayList<Match> rules, ArrayList<String> ruleLines)
    {
        String[] ruleParts = ruleString.split(" ");
        int rule1Idx = parseInt(ruleParts[0]);
        int rule2Idx = -1;
        if (ruleParts.length > 1)
        {
            rule2Idx = parseInt(ruleParts[1]);
        }
        int rule3Idx = -1;
        if (ruleParts.length > 2)
        {
            rule3Idx = parseInt(ruleParts[2]);
        }
        Match rule1 = parseRule(rule1Idx, rules, ruleLines);
        Match rule2 = rule2Idx >= 0 ? parseRule(rule2Idx, rules, ruleLines) : new EmptyMatchRule();
        Match rule3 = rule3Idx >= 0 ? parseRule(rule3Idx, rules, ruleLines) : new EmptyMatchRule();
        return new ComplexMatchRule(rule1, rule2, rule3);
    }
}
