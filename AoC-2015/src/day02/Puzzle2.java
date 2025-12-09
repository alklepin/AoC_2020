package day02;

import java.util.Arrays;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntTriple;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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
            var box = IntTriple.from(line,"x");
            var length = (long)box.getX()*box.getY()*box.getZ();
            var sizes = new int[] {box.getX(), box.getY(), box.getZ()};
            Arrays.sort(sizes);
            length += 2*(sizes[0]+sizes[1]);
            result += length;
        }
        System.out.println(result);
        
    }
}
