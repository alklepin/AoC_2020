package day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

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
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        var antennas = new HashMap<Character, ArrayList<IntPair>>();
        for (var cell : board.allCellsRC())
        {
            var c = board.getCharAt(cell);
            if ((c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z'))
            {
                var fr = board.getCharAt(cell);
                var list = antennas.get(fr);
                if (list == null)
                {
                    list = new ArrayList<IntPair>();
                    antennas.put(fr, list);
                }
                list.add(cell);
            }
        }
        
        var antinodes = new HashSet<IntPair>();
        for (var ant : antennas.entrySet())
        {
            var list = ant.getValue();
            for (var i = 0; i < list.size(); i++)
            {
                for (var j = i+1; j < list.size(); j++)
                {
                    var c1 = list.get(i);
                    var c2 = list.get(j);
                    var diff = c2.minus(c1);
                    
                    antinodes.add(c1);
                    antinodes.add(c2);
                    for (var an : board.rayRC(c2, diff))
                    {
                        antinodes.add(an);
                    }
                    for (var an : board.rayRC(c2, diff.negate()))
                    {
                        antinodes.add(an);
                    }
                }
            }
        }

//        for (var an : antinodes)
//        {
//            System.out.println(an);
//        }        
        System.out.println(antinodes.size());
        
    }
}
