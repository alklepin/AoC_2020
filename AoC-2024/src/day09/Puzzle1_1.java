package day09;

import java.util.ArrayList;
import java.util.Arrays;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1_1().solve();
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
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
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
        
        
        System.out.println(buf);
        var blockIdx = blocks.size()-1;
        var blockCopied = 0;
        var blockRemaining = blocks.get(blockIdx).length;
        var freeBlockIdx = 0;
        var freeBlockUsedSpace = 0;
        while (freeSpace > 0 && blockIdx >= 0)
        {
            var fblock = freeBlocks.get(freeBlockIdx);
            var block = blocks.get(blockIdx);
            var toMove = Math.min(blockRemaining, freeSpace);
            toMove = Math.min(toMove, fblock.length-freeBlockUsedSpace);
            
            System.arraycopy(sectors, block.initialPosition + blockCopied,
                sectors, fblock.initialPosition + freeBlockUsedSpace, toMove);
            Arrays.fill(sectors, block.initialPosition + blockCopied, block.initialPosition 
                + blockCopied+toMove, -1);
            
            blockCopied += toMove;
            blockRemaining -= toMove;
            freeSpace -= toMove;

            var newPosition = block.newPosition;
            newPosition = Math.min(newPosition, fblock.initialPosition + freeBlockUsedSpace);
            block.newPosition = newPosition;
            
            freeBlockUsedSpace += toMove;
            if (freeBlockUsedSpace >= fblock.length)
            {
                freeBlockIdx++;
                freeBlockUsedSpace = 0;
            }
            if (blockRemaining == 0)
            {
                blockIdx--;
                if (blockIdx < blocks.size())
                {
                    blockRemaining = blocks.get(blockIdx).length;
                    blockCopied = 0;
                }
            }
            printSectors(sectors);
        }
        
        long result = 0;
//        for (var b : blocks)
//        {
//            System.out.println("Block "+b.id+" starts at "+b.newPosition);
//            result += b.id * b.newPosition;
//        }
        for (var sIdx = 0; sIdx < sectors.length; sIdx++)
        {
            var s = sectors[sIdx];
            //System.out.println("Block "+b.id+" starts at "+b.newPosition);
            if (s >= 0)
            {
                result += sIdx * s;
            }
        }

        printSectors(sectors);
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
