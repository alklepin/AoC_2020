package day25;

import java.math.BigInteger;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }

    static String digits = "=-012";
    
    long decode(String line)
    {
        long val = 0;
        long base = 1;
        for (int idx = line.length()-1; idx>=0; idx--)
        {
            var v = digits.indexOf(line.substring(idx, idx+1));
            val += base * (v-2);
            base *= 5;
        }
        return val;
    }
    
//    String encode(long val)
//    {
//        StringBuilder result = new StringBuilder();
//        var base = 1;
//        while (val > 0)
//        {
//            int v = (int)(val % 5);
//            val -= v;
//            if (v >= 3)
//            {
//                v -= 5;
//                val += base * 5; 
//            }
//            result.append(digits.charAt(v+2));
//            val /= 5;
//        }
//        result.reverse();
//        return result.toString();
//    }
//
//    long decode(String line)
//    {
//        BigInteger val = BigInteger.valueOf(0);
//        BigInteger base = BigInteger.valueOf(1)1;
//        for (int idx = line.length()-1; idx>=0; idx--)
//        {
//            var v = digits.indexOf(line.substring(idx, idx+1));
//            val = val.add(BigInteger.valueOf(-1)).multiply(BigInteger.valueOf(base));
//            base *= 5;
//        }
//        return val;
//    }
//
    String encode(long val)
    {
        StringBuilder result = new StringBuilder();
        var base = 1;
        while (val > 0)
        {
            int v = (int)(val % 5);
            val -= v;
            if (v >= 3)
            {
                v -= 5;
                val += base * 5; 
            }
            result.append(digits.charAt(v+2));
            val /= 5;
        }
        result.reverse();
        return result.toString();
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        long result = 0;
        for (String line : lines)
        {
            result += decode(line);
        }
        System.out.println(result);
        System.out.println(encode(result));
        
        System.out.println(encode(1));
        System.out.println(encode(2));
        System.out.println(encode(3));
        System.out.println(encode(4));
        System.out.println(encode(5));
        System.out.println(encode(6));
        System.out.println(encode(7));
        System.out.println(encode(8));
        System.out.println(encode(9));
        System.out.println(encode(10));
        System.out.println(encode(15));
        System.out.println(encode(20));
        System.out.println(encode(2022));
        System.out.println(encode(12345));
        System.out.println(encode(314159265));
        System.out.println(decode("1121-1110-1=0"));
        
    }
}
