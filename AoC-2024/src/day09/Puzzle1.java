package day09;

import java.util.ArrayList;
import java.util.Arrays;

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
    
    
    static class Block
    {
        int initialPosition;
        int length;
        int id;
        int newPosition;
        
        public Block(int id, int length, int idx)
        {
            this.id = id;
            this.length = length;
            this.initialPosition = idx;
            this.newPosition = idx;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLines(inputFile);
        var initialLayout = lines.get(0);
        var blocks = new ArrayList<Block>();
        var freeBlocks = new ArrayList<Block>();
        var idx = 0;
        var id = 0;
        var initialPosition = 0;
        var freeSpace = 0;
        var totalLength = 0;
        while (idx < initialLayout.length())
        {
            var c = initialLayout.charAt(idx);
            var length = c - '0';
            blocks.add(new Block(id, length, initialPosition));
            totalLength += length;
            initialPosition+= length;
            idx++;
            
            if (idx < initialLayout.length())
            {
                var cf = initialLayout.charAt(idx);
                var lengthf = cf - '0';
                freeSpace += lengthf;
                freeBlocks.add(new Block(-1, lengthf, initialPosition));
                totalLength += lengthf;
                initialPosition+= lengthf;
                
                idx++;
                id++;
            }
        }

        
        var buf = new StringBuilder();
        for (var bidx = 0; bidx < blocks.size(); bidx++)
        {
            var block = blocks.get(bidx);
            for (var cidx = 0 ; cidx < block.length; cidx++)
            {
                buf.append(block.id);
            }
            if (bidx < freeBlocks.size())
            {
                var fblock = freeBlocks.get(bidx);
                for (var cidx = 0 ; cidx < fblock.length; cidx++)
                {
                    buf.append('.');
                }

            }
        }
//        System.out.println(buf);
        
        var sectors = new int[totalLength];
        for (var b : blocks)
        {
            for (var i = 0; i < b.length; i++)
            {
                sectors[b.initialPosition + i] = b.id;
            }
        }
        for (var b : freeBlocks)
        {
            for (var i = 0; i < b.length; i++)
            {
                sectors[b.initialPosition + i] = -1;
            }
        }
        
        var idx1 = 0;
        var idx2 = sectors.length-1;
        while (idx1 <= idx2)
        {
            while (idx1 < sectors.length && sectors[idx1] >= 0)
                idx1++;
            while (idx2 >= 0 && sectors[idx2] < 0)
                idx2--;
            if (idx1 < sectors.length && idx2 >= 0 && idx1 < idx2)
            {
                sectors[idx1] = sectors[idx2];
                sectors[idx2] = -1;
            }
//            printSectors(sectors);
        }
        
        long result = 0;
        for (var sIdx = 0; sIdx < sectors.length; sIdx++)
        {
            var s = sectors[sIdx];
            //System.out.println("Block "+b.id+" starts at "+b.newPosition);
            if (s >= 0)
            {
                result += sIdx * s;
            }
        }

//        printSectors(sectors);
        System.out.println(result);
        
        
//        LinesGroup lines = readAllLinesNonEmpty(inputFile);
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
    
    public static void printSectors(int[] sectors)
    {
        var buf1 = new StringBuilder();
        for (var sIdx = 0; sIdx < sectors.length; sIdx++)
        {
            var s = sectors[sIdx];
            buf1.append((char)(s >= 0 ? '0'+s: '.'));
        }
        System.out.println(buf1);
    }
}
