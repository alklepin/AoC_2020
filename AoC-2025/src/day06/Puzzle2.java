package day06;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.queries.Query;

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
    
    public long solveSum(ArrayList<String> rows)
    {
        long result = 0;
        var len = rows.get(0).length();
        for (var pos = 0; pos < len; pos++)
        {
            long num = 0;
            for (var idx = 0; idx < rows.size()-1; idx++)
            {
                var c = rows.get(idx).charAt(pos);
                if ("0123456789".indexOf(c) >= 0)
                {
                    num = num*10 + (c-'0');
                }
            }
            result += num;
        }
        return result;
    }

    public long solveMul(ArrayList<String> rows)
    {
        long result = 1;
        var len = rows.get(0).length();
        for (var pos = 0; pos < len; pos++)
        {
            long num = 0;
            for (var idx = 0; idx < rows.size()-1; idx++)
            {
                var c = rows.get(idx).charAt(pos);
                if ("0123456789".indexOf(c) >= 0)
                {
                    num = num*10 + (c-'0');
                }
            }
            result *= num;
        }
        return result;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile, false);
        var rowsCount = lines.size();
        ArrayList<Integer> puzzleStarts = new ArrayList<>();
        ArrayList<String> puzzleOp = new ArrayList<>();
        var opLine = lines.get(lines.size()-1);
        for (var idx = 0; idx < opLine.length(); idx++)
        {
            if ("+*".indexOf(opLine.charAt(idx)) >= 0)
            {
                puzzleStarts.add(idx);
                puzzleOp.add(String.valueOf(opLine.charAt(idx)));
            }
        }
        ArrayList<Integer> puzzleEnd = 
            Query.wrap(puzzleStarts).skip(1).select(v -> v-1).toList();
        puzzleEnd.add(opLine.length());
        
        
        long result = 0;
        for (var num = 0; num < puzzleStarts.size(); num++)
        {
            int start = puzzleStarts.get(num); 
            int end = puzzleEnd.get(num); 
            var rows = Query.wrap(lines).select(s -> s.substring(start, end)).toList();
            
            long res = switch (rows.get(rowsCount-1).trim())
            {
                case "+" -> solveSum(rows);
                case "*" -> solveMul(rows);
                default -> throw new IllegalStateException();
            };
            result += res;
            
        }
        
        System.out.println(result);
    }
}
