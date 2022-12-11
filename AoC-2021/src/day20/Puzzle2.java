package day20;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.Pair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("input2.txt");
        // System.out.println(groups.size());
        
        int result = 0;
        var alg = groups.get(0).get(0);
        var board = new HashMap<IntPair, Integer>();
        var maxX = 0;
        var maxY = 0;
        var minX = 0;
        var minY = 0;
        {
            var x = 0; 
            for (var line : groups.get(1))
            {
                maxY = Math.max(maxY, line.length());
                var y = 0;
                for (var c : line.toCharArray())
                {
                    if (c == '#')
                        board.put(Pair.of(x,  y), 1);
                    y++;
                }
                x++;
                maxX++;
            }
        }
        
//        System.out.println(alg);
        printBoard(board);
        
        
//        var v1 = getRegionNum(board, 2, 2);
//        System.out.println(v1);
        
//        System.exit(0);
        var count = 0;
        var hasChanges = true;
        var minX0 = minX;
        var minY0 = minY;
        var maxX0 = maxX;
        var maxY0 = maxY;
        
        var infColor = 0;
        while (count < 50)
        {
            if (infColor == 1)
            {
                for (var x = minX - 3; x <= maxX + 3; x++)
                {
                    board.put(Pair.of(x,  minY-3), 1);
                    board.put(Pair.of(x,  minY-2), 1);
                    board.put(Pair.of(x,  minY-1), 1);
                    board.put(Pair.of(x,  maxY+1), 1);
                    board.put(Pair.of(x,  maxY+2), 1);
                    board.put(Pair.of(x,  maxY+3), 1);
                }
                for (var y = minY - 3; y <= maxY + 3; y++)
                {
                    board.put(Pair.of(minX-3, y), 1);
                    board.put(Pair.of(minX-2, y), 1);
                    board.put(Pair.of(minX-1, y), 1);
                    board.put(Pair.of(maxX+1, y), 1);
                    board.put(Pair.of(maxX+2, y), 1);
                    board.put(Pair.of(maxX+3, y), 1);
                }
            }
            minX -= 2;
            minY -= 2;
            maxX += 2;
            maxY += 2;
            hasChanges = false;
            var nextBoard = new HashMap<IntPair, Integer>();
            for (var x = minX; x <= maxY; x++)
            {
                for (var y = minY; y <= maxY; y++)
                {
                    var idx = Pair.of(x, y);
                    var oldV = board.get(idx);
                    
                    var v = getRegionNum(board, x, y);
                    var newV = alg.charAt(v) == '#' ? Integer.valueOf(1) : null;
                    if (newV != null)
                    {
                        nextBoard.put(idx, newV);
                    }
                    if (valueOf(oldV) != valueOf(newV))
                        hasChanges = true;
                }
            }
            board = nextBoard;
//            printBoard(board);
            count++;
            infColor = (infColor + 1) % 2;
        }
         
        result = 0;
        for (var c : board.entrySet())
        {
            var v = c.getValue();
            if (valueOf(v) == 1)
                result++;
        }
        
        
        
        
        System.out.println(result);
        System.out.println(board.size());
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
    
    private static void printBoard(HashMap<IntPair, Integer> board)
    {
        var minX = Integer.MAX_VALUE;
        var minY = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var maxY = Integer.MIN_VALUE;
        for (var c : board.entrySet())
        {
            minX = Math.min(minX, c.getKey().getX());
            maxX = Math.max(maxX, c.getKey().getX());
            minY = Math.min(minY, c.getKey().getY());
            maxY = Math.max(maxY, c.getKey().getY());
        }
        
        System.out.println("----------------");
        for (var x = minX; x <= maxX; x++)
        {
            var line = new StringBuilder();
            for (var y = minY; y <= maxY; y++)
            {
                var v = valueOf(board.get(Pair.of(x,  y))); 
                line.append(v == 0 ? '.' : '#');
            }
            System.out.println(line);
        }
        System.out.println("----------------");
    }
    
    private static int valueOf(Integer v)
    {
        if (v == null)
            return 0;
        return v.intValue();
    }
    
    public int getRegionNum(HashMap<IntPair, Integer> board, int x, int y)
    {
        var result = 0;
        for (int x0 = x - 1; x0 <= x + 1; x0++)
        {
            for (int y0 = y - 1; y0 <= y + 1; y0++)
            {
                var v = board.get(Pair.of(x0,  y0));
                
                result = result * 2 + ((v != null) ? v.intValue() : 0); 
            }
        }
        return result;
    }
}
