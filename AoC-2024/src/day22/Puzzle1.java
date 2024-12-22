package day22;

import common.LinesGroup;
import common.PuzzleCommon;

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
        
//        LinesGroup lines = readAllLines(inputFile);

        var step = 2000;
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        long result = 0;
        for (String line : lines)
        {
            long v = parseLong(line);
            var s = new Secret(v);
            for (var idx = 0; idx < step; idx++)
            {
                s.next();
            }
            result += s.value;
        }
        System.out.println(result);
        
//        var s = new Secret(123);
//        for (var idx = 0; idx < 10; idx++)
//        {
//            s.next();
//            System.out.println(s.value);
//        }
        
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
