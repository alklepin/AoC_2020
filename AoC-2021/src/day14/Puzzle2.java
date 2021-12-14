package day14;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    private HashMap<String, String> substs = new HashMap<>();
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("input2.txt");
        // System.out.println(groups.size());
        
        var line = groups.get(0).get(0);
        
        for (var rule : groups.get(1))
        {
            var parts = rule.split("->");
            var from = parts[0].trim();
            var to = parts[1].trim();
            substs.put(from, to);
        }

        var current = line;
        
        HashSet<Character> alphabet = new HashSet<>();
        for (var c : current.toCharArray())
        {
            alphabet.add(c);
        }
        for (var s : substs.values())
        {
            alphabet.add(s.charAt(0));
        }
        
        HashSet<String> pairs = new HashSet<>();
        for (var c1 : alphabet)
        {
            for (var c2 : alphabet)
            {
                pairs.add(new StringBuilder().append(c1).append(c2).toString());
            }
        }
        
        HashMap<String, Vector> start = new HashMap<>();
        for (var pair : pairs)
        {
            Vector v = start.computeIfAbsent(pair, key -> new Vector());
            var c1 = pair.charAt(0);
            v.setAt(c1, v.getAt(c1) + 1);
        }
        
        for (var entry : substs.entrySet())
        {
            var v = start.computeIfAbsent(entry.getKey(), key -> new Vector());
            var c2 = entry.getValue().charAt(0);
            v.setAt(c2, v.getAt(c2) + 1);
        }

//        for (var pair : pairs)
//        {
//            Vector v = start.computeIfAbsent(pair, key -> new Vector());
//            System.out.println(pair + ": "+v);
//        }
        
        var currentCounters = start;
        for (int i = 0; i < 39; i++)
        {
            HashMap<String, Vector> next = new HashMap<>();
            
            for (var pair : pairs)
            {
                var s1 = pair.charAt(0);
                var s2 = pair.charAt(1);
                var subst = substs.get(pair);
                Vector newVector;
                if (subst != null)
                {
                    newVector = currentCounters.get(""+s1+subst).add(currentCounters.get(""+subst+s2));
                }
                else
                {
                    newVector = currentCounters.get(""+s1+s2);
                }
                next.put(pair, newVector);
            }
            currentCounters = next;
        }

//        System.out.println("Result:");
        var result = new Vector();
        for (int pos = 0; pos < current.length()-1; pos++)
        {
            var pair = current.substring(pos, pos+2);
            var v = currentCounters.get(pair);
//            System.out.println(pair + ": "+v);
            result = result.add(v);
        }
        result.incAt(line.charAt(line.length()-1));
        
        long minCount = result.min();
        long maxCount = result.max();
        
//        System.out.println(maxCount);
//        System.out.println(minCount);
        System.out.println(maxCount - minCount);
        
    }
    
    public static class Vector
    {
        private long[] counters;
        
        public Vector()
        {
            counters = new long[26];
        }
        
        public void setAt(char c, long value)
        {
            counters[c - 'A'] = value;
        }
        
        public void incAt(char c)
        {
            counters[c - 'A']++;
        }

        public void decAt(char c)
        {
            counters[c - 'A']--;
        }

        public long getAt(char c)
        {
            return counters[c - 'A'];
        }
        
        public Vector copy()
        {
            var result = new Vector();
            result.counters = Arrays.copyOf(counters, counters.length);
            return result;
        }
        
        public Vector add(Vector other)
        {
            var result = new Vector();
            for (var idx = 0; idx < counters.length; idx++)
            {
                result.counters[idx] = counters[idx] + other.counters[idx];
            }
            return result;
        }
        
        public long min()
        {
            long result = Long.MAX_VALUE;
            for (long v : counters)
            {
                if (v < result && v > 0)
                    result = v;
            }
            return result;
        }

        public long max()
        {
            long result = Long.MIN_VALUE;
            for (long v : counters)
            {
                if (v > result)
                    result = v;
            }
            return result;
        }

        @Override
        public String toString()
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < counters.length; i++)
            {
                result.append((char)(i + 'A')).append(": ").append(counters[i]).append(" ");
            }
            return result.toString();
        }
        
        
    }
}
