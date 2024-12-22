package day22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import common.LinesGroup;
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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
//        var inputFile = "input1_test1.txt";

//        var inputFile = "input2_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);

        var step = 2000;
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<long[]> sequences = new ArrayList<>();
        ArrayList<long[]> differences = new ArrayList<>();
        
        long result = 0;
        for (String line : lines)
        {
            long v = parseLong(line);
            var seq = new ArrayList<Long>();
            var diffs = new ArrayList<Long>();
            seq.add(v % 10);
            var s = new Secret(v);
            long prev = v % 10;
            for (var idx = 0; idx < step; idx++)
            {
                s.next();
                long d = s.value % 10;
                seq.add(d);
                diffs.add(d - prev);
                prev = d;
            }
            sequences.add(toArray(seq));
            differences.add(toArray(diffs));
        }
        
        var seqs = new ArrayList<long[]>();
        var known = new HashSet<Long>();
        var fd = differences.get(0);
        for (var idx = 0; idx < fd.length-4; idx++)
        {
            var s = new long[] {fd[idx], fd[idx+1], fd[idx+2], fd[idx+3]};
            var h = (((s[0] + 10) * 20 + (s[1] + 10)) * 20 + (s[2] + 10)) * 20 + (s[3] + 10);
            if (known.add(h))
                seqs.add(s);
        }
        
        long best = 0;
        for (var seq : seqs)
        {
            long bananas = 0;
            for (var idx = 0; idx < sequences.size(); idx++)
            {
                var s = sequences.get(idx);
                var t = differences.get(idx);
                var loc = firstINdexOf(t, seq);
                if (loc >= 0)
                    bananas += s[loc+4]; 
            }
            if (best < bananas)
            {
                best = bananas;
                System.out.println(">"+best);
                System.out.println(Arrays.toString(seq));
            }
        }
        System.out.println(best);
        
//        var seq = new long[] {-1, -1, 0, 2};
//        var s = sequences.get(0);
//        var t = differences.get(0);
//        
//        var loc = firstINdexOf(t, seq);
//        System.out.println(loc);
//        System.out.println(s[loc+4]);
        
        
//        System.out.println(result);
        
//        var s = new Secret(123);
//        for (var idx = 0; idx < 10; idx++)
//        {
//            s.next();
//            System.out.println(s.value);
//        }
        
    }
    
    public long[] toArray(ArrayList<Long> array)
    {
        var res = new long[array.size()];
        for (var idx = 0; idx < array.size(); idx++)
        {
            res[idx] = array.get(idx);
        }
        return res;
    }
    
    public int firstINdexOf(long[] data, long[] fragment)
    {
        for (var idx = 0; idx < data.length; idx++)
        {
            if (isPrefixOf(fragment, data, idx, fragment.length))
                return idx;
        }
        return -1;
    }
    
    /**
     * Checks whether given prefix is at beginning of the portion of given buffer starting at given offset and having specified number of bytes
     * @param prefix The prefix to look for
     * @param buffer The buffer where to look for the prefix
     * @param offset The offset of portion of buffer to scan
     * @param count The number of bytes to scan
     * @return true if there is a given prefix starting from specified offset
     */
    public static boolean isPrefixOf(long [] prefix,  long [] buffer, int offset, int count)
    {
        if (offset > buffer.length)
            offset = buffer.length;
        
        var maxLength = buffer.length - offset;
        if (count > maxLength)
            count = maxLength;
        
        if (count < prefix.length)
            return false;
        
        var lengthToScan = prefix.length;
        for (var idx = 0; idx < lengthToScan; idx++)
        {
            if (buffer[offset + idx] != prefix[idx])
                return false;
        }
        
        return true;
    }
    
    
    public class Secret
    {
        public long value;
        
        public Secret(long v)
        {
            value = v;
        }
        
        public void next()
        {
            mix(value * 64);
            prune();
            mix(value / 32);
            prune();
            mix(value * 2048);
            prune();
        }
        
        private void mix(long v)
        {
            value = value ^ v;
        }

        private void prune()
        {
            value = value % 16777216;
        }
    }
}
