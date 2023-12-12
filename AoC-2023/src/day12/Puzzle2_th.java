package day12;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2_th extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_th().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    static AtomicInteger tasksCount = new AtomicInteger(0);
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
//        var inputFile = "input2_2_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<Data> data = new ArrayList<>();
        for (String line : lines)
        {
            var parts = line.split(" +");
            var parser = new LineParser(parts[1]);
            var text = parts[0];
            text = text + "?" + text + "?" + text + "?" + text + "?" + text; 
            var v = parser.listOfInts();
            var values = new ArrayList<Integer>();
            values.addAll(v);
            values.addAll(v);
            values.addAll(v);
            values.addAll(v);
            values.addAll(v);
            data.add(new Data(text, values));
        }
        
        int result = 0;
        ExecutorService es = Executors.newFixedThreadPool(11);
        
        for (var d : data) 
        {
            tasksCount.incrementAndGet();
            es.execute(() -> 
            {
                d.numOfArrangements();
                tasksCount.decrementAndGet();
                synchronized (Puzzle2_th.class)
                {
                    System.out.println(d.text + "->" + d.arrangements);
                }
            });
        }
        
        while (tasksCount.get() > 0)
        {
            Thread.sleep(500);
        }
        
        for (var d : data)
        {
            var n = d.arrangements;
//            System.out.println(d.text + "->" + n);
            result += n;
        }
        System.out.println(result);
        es.close();
    }
    
    static class State
    {
        int segmentStart;
        int valStart;

        public State(int segmentStart, int valStart)
        {
            this.segmentStart = segmentStart;
            this.valStart = valStart;
        }
    }
    
    static class Data
    {
        String text;
        ArrayList<Integer> values;
        long arrangements;

        public Data(String text, ArrayList<Integer> values)
        {
            this.text = text;
            this.values = values;
        }

        public void numOfArrangements()
        {
            var arr = new int[values.size()];
            for (var idx = 0; idx < arr.length; idx++)
            {
                arr[idx] = values.get(idx);
            }
                
            arrangements = numOfArrangements(text, arr);
        }
        
        void debugPrint(String msg)
        {
            System.out.println(msg);
        }
        
        public long numOfArrangements(String text, int[] values)
        {
            var v = numOfArrangementsImpl(text, values);
            //System.out.println(text + " " + Arrays.toString(values) + " -> " + v);
            return v;
        }
        
        public long numOfArrangementsImpl(String text, int[] values)
        {
            var s = 0;
            for (var i : values)
                s += i;
            if (text.length() < s + values.length - 1)
                return 0;
            
            var cc1 = countChar(text, '#');
            var cc2 = countChar(text, '?');
            if (cc1 + cc2 < s)
                return 0;
            
            if (values.length == 0)
            {
                if (text.indexOf('#') >= 0)
                    return 0;
                int cnt = countChar(text, '?');
                //return 1l << cnt;
                return 1;
            }
            
            var v0 = values[0];
            if (v0 == 0)
            {
                if (text.length() == 0)
                    return values.length > 1 ? 0 : 1;
                if (text.charAt(0) == '.' || text.charAt(0) == '?')
                {
                    var nextV = new int[values.length - 1];
                    System.arraycopy(values, 1, nextV, 0, nextV.length);
                    return numOfArrangements(text.substring(1), nextV);
                }
                return 0;
            }
            
            var skip = 0;
            while (skip < text.length() && text.charAt(skip) == '.')
            {
                skip++;
            }
            if (skip > 0)
                text = text.substring(skip);
            
            if (text.length() == 0)
                return 0;
            
            long sum = 0;
            if (text.charAt(0) == '?')
            {
                sum += numOfArrangements(text.substring(1), values);
            }
            if (text.charAt(0) == '?' || text.charAt(0) == '#')
            {
                var group = 0;
                while (group < values[0] && group < text.length() &&
                    (text.charAt(group) == '?' || text.charAt(group) == '#'))
                    group++;
                if (group == values[0])
                {
                    var nextV = new int[values.length];
                    System.arraycopy(values, 0, nextV, 0, nextV.length);
                    nextV[0] -= group;
                    sum += numOfArrangements(text.substring(group), nextV);
                }
            }
            return sum;
        }
        
        public static int countChar(String line, char c)
        {
            var result = 0;
            for (var idx = 0; idx < line.length(); idx++)
            {
                if (line.charAt(idx) == c)
                {
                    result++;
                }
            }
            return result;
        }
    }
}
