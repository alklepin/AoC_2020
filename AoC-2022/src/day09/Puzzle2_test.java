package day09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.Pair;

public class Puzzle2_test extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_test().solve();
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
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        var minPair = Pair.of(-1, -1);
        var maxPair = Pair.of(1, 1);
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input1_test.txt");
        var visited = new HashSet<IntPair>();
        int result = 0;
        IntPair rope[] = new IntPair[10];
        for (int idx = 0; idx < rope.length; idx++)
        {
            rope[idx] = Pair.of(0, 0);
        }
        visited.add(rope[9]);
        for (String line : lines)
        {
            var parts = line.split(" ");
            char dir = parts[0].charAt(0);
            int count = parseInt(parts[1]);
            for (var step = 0; step < count; step++)
            {
                var head = rope[0];
                head = switch (dir)
                {
                    case 'U' -> head.add(Pair.of(0, 1));
                    case 'D' -> head.add(Pair.of(0, -1));
                    case 'L' -> head.add(Pair.of(-1, 0));
                    case 'R' -> head.add(Pair.of(1, 0));
                    default -> throw new IllegalStateException();
                };
                rope[0] = head;
                
                for (int idx = 1; idx < rope.length; idx++)
                {
                    var prevKnot = rope[idx - 1];
                    var currentKnot = rope[idx];

                    var diff = prevKnot.minus(currentKnot);
                    
                    if (diff.lengthCheb() >= 2)
                    {
                        diff = diff.signum();
                    }
                    else
                    {
                        diff = IntPair.ZERO;
                    }
                    
                    var currentKnotNew = currentKnot.add(diff);

//                    System.out.println("Knot " + idx + " moves from "+ currentKnot + " to " + currentKnotNew + "prev: " + prevKnot);
                    
                    rope[idx] = currentKnotNew;
                }
                
                visited.add(rope[9]);
                
//                var board = new Board2D(100, 100);
//                board.modifyEachCellXY((x, y) -> '.');
//                for (int idx = rope.length-1; idx >= 0; idx--)
//                {
//                    board.setAtXY(rope[idx], '0'+idx);
//                }
//                board.printAsStringsRev(System.out);
//                System.out.println("==============");
                
            }
        }
        System.out.println(visited.size());
        
    }
}
