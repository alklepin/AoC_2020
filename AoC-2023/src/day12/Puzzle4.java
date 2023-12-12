package day12;

import java.util.ArrayList;
import java.util.HashMap;

import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;

public class Puzzle4 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle4().solve();
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
            var arr = new int[values.size()];
            for (var idx = 0; idx < arr.length; idx++)
            {
                arr[idx] = values.get(idx);
            }
                

            data.add(new Data(text, arr));
        }
        
        long result = 0;
        for (var d : data)
        {
            var n = d.numOfArrangements();
            System.out.println(d.text + "->" + n);
            result += n;
        }
        System.out.println(result);
        
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
        int[] values;

        public Data(String text, int[] values)
        {
            this.text = text;
            this.values = values;
        }

        public long numOfArrangements()
        {
            return numOfArrangements(0, 0);
        }
        
        void debugPrint(String msg)
        {
            System.out.println(msg);
        }

        HashMap<IntPair, Long> cache = new HashMap<>();
        
        public long numOfArrangements(int textIdx, int valuesIdx)
        {
            var result = cache.get(IntPair.of(textIdx, valuesIdx));
            if (result != null)
                return result;
            
            var v = numOfArrangementsImpl(textIdx, valuesIdx);
            cache.put(IntPair.of(textIdx, valuesIdx), v);
            //System.out.println(text + " " + Arrays.toString(values) + " -> " + v);
            return v;
        }
        
        
        public long numOfArrangementsImpl(int textIdx, int valuesIdx)
        {
            var s = 0;
            for (var idx = valuesIdx; idx < values.length; idx++)
                s += values[idx];
            if (text.length() - textIdx < s + values.length - valuesIdx - 1)
                return 0;
            
            var cc1 = countChar(text, textIdx, '#');
            var cc2 = countChar(text, textIdx, '?');
            if (cc1 + cc2 < s)
                return 0;
            
            if (valuesIdx == values.length)
            {
                if (text.indexOf('#', textIdx) >= 0)
                    return 0;
                return 1;
            }
            
            while (textIdx < text.length() && text.charAt(textIdx) == '.')
            {
                textIdx++;
            }
            
            if (textIdx == text.length())
                return 0;
            
            long sum = 0;
            if (text.charAt(textIdx) == '?')
            {
                sum += numOfArrangements(textIdx+1, valuesIdx);
            }
            if (text.charAt(textIdx) == '?' || text.charAt(textIdx) == '#')
            {
                var group = 0;
                while (group < values[valuesIdx] && group < text.length() - textIdx &&
                    (text.charAt(group + textIdx) == '?' || text.charAt(group + textIdx) == '#'))
                    group++;
                if (group == values[valuesIdx])
                {
                    textIdx += group;
                    if (textIdx == text.length())
                        sum += values.length > valuesIdx + 1 ? 0 : 1;
                    else if (text.charAt(textIdx) == '.' || text.charAt(textIdx) == '?')
                    {
                        sum += numOfArrangements(textIdx + 1, valuesIdx + 1);
                    }
                }
            }
            return sum;
        }
        
        public static int countChar(String line, int startIdx, char c)
        {
            var result = 0;
            for (var idx = startIdx; idx < line.length(); idx++)
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
