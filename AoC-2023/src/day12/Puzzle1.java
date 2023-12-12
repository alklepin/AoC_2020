package day12;

import java.util.ArrayList;
import java.util.regex.Pattern;

import common.LineParser;
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
    
    class Data
    {
        String text;
        ArrayList<Integer> values;

        public Data(String text, ArrayList<Integer> values)
        {
            this.text = text;
            this.values = values;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<Data> data = new ArrayList<>();
        for (String line : lines)
        {
            var parts = line.split(" +");
            var parser = new LineParser(parts[1]);
            var text = parts[0];
            var values = parser.listOfInts();
            data.add(new Data(text, values));
        }
        
        int result = 0;
        for (var d : data)
        {
            result += numOfArrangements(d);
        }
        System.out.println(result);
        
    }
    
    public int numOfArrangements(Data d)
    {
        var p = generateChecker(d.values);
//        System.out.println(d.text);
//        System.out.println(p);
        var arrText = d.text.toCharArray();
        int[] map = new int[20];
        
        var arrangements = 0;
        var mapSize = 0;
        for (var idx = 0; idx < arrText.length; idx++)
        {
            if (arrText[idx] == '?')
            {
                map[mapSize] = idx;
                mapSize++;
            }
        }
        
        var limit = 1 << (mapSize);
        for (var n = 0; n <limit; n++)
        {
            var bits = n;
            for (var bitIdx = 0; bitIdx < mapSize; bitIdx++)
            {
                arrText[map[bitIdx]] = (bits % 2) == 1 ? '#' : '.';
                bits /= 2;
            }
            var m = p.matcher(new String(arrText));
            if (m.matches())
                arrangements++;
        }
        return arrangements;
    }
    
    public Pattern generateChecker(ArrayList<Integer> values)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\\.*");
        for (var v : values)
        {
            sb.append("#{").append(v).append("}\\.+");
        }
        sb.setLength(sb.length()-1);
        sb.append("*");
        return Pattern.compile(sb.toString()); 
    }
    
}
