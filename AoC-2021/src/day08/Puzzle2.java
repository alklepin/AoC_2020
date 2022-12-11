package day08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        String digits[] = new String[10];
        for (String line : lines)
        {
            var parts = line.split("\\|");
            var source = parts[0].trim().split("\\s+");
            Arrays.sort(source, (a,b) -> Integer.valueOf(a.length()).compareTo(b.length()));
            for (var i = 0; i < source.length; i++)
            {
                source[i] = norm(source[i]);
            }
            digits[1] = source[0];
            digits[7] = source[1];
            digits[4] = source[2];
            digits[8] = source[9];
            var segmentA = diff(digits[7], digits[1]);
            var tmpCDE = xor(xor(source[6], source[7]),xor(source[8], source[9]));
            var segmentC = common(tmpCDE, digits[1]);
            var segmentF = diff(digits[1], ""+segmentC);
            digits[6] = norm(xor(digits[8], ""+segmentC));
            if (contains(source[3], tmpCDE))
                digits[2] = source[3];
            if (contains(source[4], tmpCDE))
                digits[2] = source[4];
            if (contains(source[5], tmpCDE))
                digits[2] = source[5];
            var tmpBF = xor(digits[2], digits[8]);
            var segmentB = diff(tmpBF, ""+segmentF);
            var segmentD = diff(digits[4], ""+segmentB + segmentC + segmentF);
            var tmpABCDF= norm(digits[4]+segmentA);
            if (contains(source[6], tmpABCDF))
                digits[9] = source[6];
            if (contains(source[7], tmpABCDF))
                digits[9] = source[7];
            if (contains(source[8], tmpABCDF))
                digits[9] = source[8];
            var segmentG = diff(digits[9], tmpABCDF);
            digits[3] = norm(""+segmentA + segmentC + segmentD + segmentF + segmentG);
            digits[5] = norm(xor(xor(xor(source[3], source[4]), xor(source[5], digits[3])), digits[2]));
            digits[0] = norm(xor(digits[8], ""+segmentD));

            HashMap<String, Integer> digs = new HashMap<>();
            for (int i = 0; i < 10; i++)
                digs.put(digits[i], i);
            
            var target = parts[1].trim().split("\\s+");
            var value = 0;
            for (var s : target)
            {
                value *= 10;
                var key = norm(s);
                if (digs.containsKey(key))
                    value += digs.get(key);
                else
                    throw new IllegalStateException();
                
            }
            result += value;
        }
        System.out.println(result);
        
    }
    
    public String norm(String s)
    {
        var chars = s.split("|");
        Arrays.sort(chars);
        StringBuilder res = new StringBuilder();
        for (var s1 : chars)
        {
            res.append(s1);
        }
        return res.toString();
    }
    
    public boolean contains(String a, String b)
    {
        if (a.length() < b.length())
        {
            var c = a;
            a = b;
            b = c;
        }
        for (var c : b.split(""))
        {
            if (!a.contains(""+c))
            {
                return false;
            }
        }
        return true;
    }

    public char diff(String a, String b)
    {
        if (a.length() < b.length())
        {
            var c = a;
            a = b;
            b = c;
        }
        for (var c : a.split(""))
        {
            if (!b.contains(""+c))
            {
                return c.charAt(0);
            }
        }
        throw new IllegalStateException();
    }

    public char common(String a, String b)
    {
        if (a.length() < b.length())
        {
            var c = a;
            a = b;
            b = c;
        }
        for (var c : a.split(""))
        {
            if (b.contains(""+c))
            {
                return c.charAt(0);
            }
        }
        throw new IllegalStateException();
    }
    
    public String xor(String a, String b)
    {
        if (a.length() < b.length())
        {
            var c = a;
            a = b;
            b = c;
        }
        StringBuilder result = new StringBuilder();
        for (var c : a.split(""))
        {
            if (!b.contains(""+c))
            {
                result.append(c);
            }
        }
        for (var c : b.split(""))
        {
            if (!a.contains(""+c))
            {
                result.append(c);
            }
        }
        return result.toString();
    }
}
