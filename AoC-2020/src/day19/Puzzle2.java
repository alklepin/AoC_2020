package day19;

import java.util.ArrayList;
import java.util.Comparator;
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
    
    public interface Match
    {
        public boolean matches(String segment, ArrayList<Match> rules);
        public ArrayList<String> generate(ArrayList<Match> rules);
    }
    
    public class SimpleMatchRule implements Match
    {
        public char m_expected;
        
        public SimpleMatchRule(char c)
        {
            m_expected = c;
        }
        
        @Override
        public ArrayList<String> generate(ArrayList<Match> rules)
        {
            ArrayList<String> result = new ArrayList<String>();
            result.add(""+m_expected);
            return result;
        }

        @Override
        public boolean matches(String segment, ArrayList<Match> rules)
        {
            if (segment.length() == 1 && segment.charAt(0) == m_expected)
                return true;
            return false;
        }
    }

    public class ComplexMatchRule implements Match
    {
        ArrayList<int[]> m_alternatives = new ArrayList<int[]>();
        String m_info;
        HashMap<String, Boolean> cache = new HashMap<>();
        
        public ComplexMatchRule(String ruleLine)
        {
            m_info = ruleLine;
            parseRule(ruleLine);
        }

        private void parseRule(String ruleString)
        {
            String[] alternatives = ruleString.split("\\|");
            for (String rule : alternatives)
            {
                rule = rule.trim();
                
                String[] parts = rule.split(" ");
                int[] sequence = new int[parts.length];
                m_alternatives.add(sequence);
                for (int iIdx = 0; iIdx < sequence.length; iIdx++)
                {
                    sequence[iIdx] = parseInt(parts[iIdx].trim());
                }
            }
        }
        
        @Override
        public boolean matches(String segment, ArrayList<Match> rules)
        {
            Boolean knownResult = cache.get(segment);
            if (knownResult != null)
                return knownResult;
            
            boolean result = false;
            for (int[] sequence : m_alternatives)
            {
                if (matchesImpl(segment, sequence, 0, rules))
                {
                    result = true;
                    break;
                }
            }
//            System.out.printf("%s: %s -> %s\n", m_info, segment, ""+result);
            cache.put(segment, result);
            return result;
        }
        
        private boolean matchesImpl(String segment, int [] sequence, int seqIdx, ArrayList<Match> rules)
        {
            if (seqIdx < sequence.length)
            {
                Match rule = rules.get(sequence[seqIdx]);
                for (int iSplitIdx = 1; iSplitIdx <= segment.length(); iSplitIdx++)
                {
                    if (rule.matches(segment.substring(0, iSplitIdx), rules))
                    {
                        String nextSegment = "";
                        if (iSplitIdx < segment.length())
                        {
                            nextSegment = segment.substring(iSplitIdx);
                        }
                        if (matchesImpl(nextSegment, sequence, seqIdx+1, rules))
                            return true;
                    }
                }
                return false;
            }
            return segment.length() == 0;
        }

        
        private ArrayList<String> generateImpl(int [] sequence, int iStartIdx, ArrayList<Match> rules)
        {
            ArrayList<String> result = new ArrayList<>();
            if (iStartIdx < sequence.length)
            {
                ArrayList<String> tmp = generateImpl(sequence, iStartIdx+1, rules);
                Match rule = rules.get(sequence[iStartIdx]);
                ArrayList<String> tmp1 = rule.generate(rules);
                for (String s1 : tmp1)
                    for (String s2 : tmp)
                    {
                        String s = s1+s2;
                        if (s.length() <= 96)
                            result.add(s);
                    }
            }
            else
            {
                result.add("");
            }
            return result;
        }

        @Override
        public ArrayList<String> generate(ArrayList<Match> rules)
        {
            HashSet<String> result = new HashSet<>();
            for (int[] sequence : m_alternatives)
            {
                result.addAll(generateImpl(sequence, 0, rules));
            }
            ArrayList<String> r = new ArrayList<>(result);
            return r;
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
        ArrayList<LinesGroup> groups = readAllLineGroups("input2.txt");
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
            parseRule(line, rules);
        }
        
        Match rule0 = rules.get(0);
//        HashSet<String> validLines = new HashSet<>();
//        long counter = 0;
//        for (String s : rule0.generate(rules))
//        {
//            validLines.add(s);
////            System.out.println(s);
//            counter++;
//        }
//        System.out.println(counter);
        
        for (String testLine : groups.get(1))
        {
            if (rule0.matches(testLine, rules))
            {
//                System.out.println(testLine);
                result++;
            }
//            if (validLines.contains(testLine))
//            {
////                System.out.println(testLine);
//                result++;
//            }
        }
        
        System.out.println(result);
    }
    
    
    private Match parseRule(String line, ArrayList<Match> rules)
    {
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
            rule = new ComplexMatchRule(ruleString);
        }
        rules.set(ruleIdx, rule);
        return rule;
    }
}
