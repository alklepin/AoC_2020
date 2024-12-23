package day17;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
import common.boards.Pair;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
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
        
        LinesGroup lines = readAllLines("input1.txt");

        
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
        var t = modelShot(Pair.of(18, d-1), min, max);
        for (var p : t)
        {
            System.out.println(p);
        }
    }
    
    public ArrayList<IntPair> modelShot(IntPair velocity, IntPair targetMin, IntPair targetMax)
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
                System.out.println("Hit! "+ next);
                break;
            }
//            if (!next.componentLessEq(targetMax))
//            {
//                break;
//            }
            current = next;
        }
        return result;
        
    }
}
