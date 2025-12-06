package day06;

import common.LinesGroup;
import common.PuzzleCommon;
import common.queries.Query;

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
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var rowsCount = lines.size();
        String[][] data = new String[rowsCount][]; 
        for (var idx = 0; idx < lines.size(); idx++)
        {
            var line = lines.get(idx);
            data[idx] = Query.wrap(line.split(" +")).toArray();
        }
        
        long result = 0;
        for (var idx = 0; idx < data[0].length; idx++)
        {
            var op = data[rowsCount-1][idx];
            long res = switch (op)
            {
                case "+" -> {
                    long sum = 0;
                    for (var row = 0; row < rowsCount-1; row++)
                    {
                        sum += parseLong(data[row][idx]);
                    }
                    yield sum;
                }
                case "*" -> {
                    long mul = 1;
                    for (var row = 0; row < rowsCount-1; row++)
                    {
                        mul *= parseLong(data[row][idx]);
                    }
                    yield mul;
                }
                default -> throw new IllegalStateException();
            };
            result += res;
        }
        System.out.println(result);
    }
}
