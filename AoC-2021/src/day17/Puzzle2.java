package day17;

import java.util.ArrayList;
import java.util.HashMap;

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
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("input2.txt");

        
        var parts = parse("target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)", lines.get(0));
        var x1 = parseInt(parts[1]);
        var x2 = parseInt(parts[2]);
        var y1 = parseInt(parts[3]);
        var y2 = parseInt(parts[4]);
        var min = Pair.of(Math.min(x1,x2), Math.min(y1, y2));
        var max = Pair.of(Math.max(x1,x2), Math.max(y1, y2));
        var d = Math.abs(min.getY());
        var result = (d-1) * (d-2) / 2;
        System.out.println(result);
        var count = 0;
        for (var x = 0; x < 300; x++)
        {
            for (var y = -200; y <= 300; y++)
            {
                if (modelShot(Pair.of(x, y), min, max))
                    count++;
            }
        }
        System.out.println(count);
    }
    
    public boolean modelShot(IntPair velocity, IntPair targetMin, IntPair targetMax)
    {
        ArrayList<IntPair> result = new ArrayList<>();
        var current = Pair.of(0, 0);
        for (var step = 0; step < 400; step++)
        {
            var next = current.add(velocity);
            var xDelta = velocity.getX() > 0 ? -1 : velocity.getX() < 0 ? 1 : 0;
            velocity = velocity.add(Pair.of(xDelta, -1));
            result.add(next);
            if (targetMin.componentLessEq(next) && next.componentLessEq(targetMax))
            {
                return true;
            }
//            if (!next.componentLessEq(targetMax))
//            {
//                break;
//            }
            current = next;
        }
        return false;
    }
}
