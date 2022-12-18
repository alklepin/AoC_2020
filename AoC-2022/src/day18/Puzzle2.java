package day18;

import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Bounds3D;
import common.boards.IntTriple;
import common.graph.ImplicitGraph;
import common.queries.Query;

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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";

        HashSet<IntTriple> cubes = new HashSet<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        var bounds = new Bounds3D();
        for (String line : lines)
        {
            var parts = line.split(",");
            var p = new IntTriple(parseInt(parts[0]), parseInt(parts[1]), parseInt(parts[2]));
            cubes.add(p);
            bounds.extendBy(p);
        }
        var min = bounds.min();
        var max = bounds.max();
        System.out.println(min);
        System.out.println(max);

        var exteriorMin = min.add(IntTriple.of(-1, -1, -1));
        var exteriorMax = max.add(IntTriple.of(1, 1, 1));
        var exteriorBounds = new Bounds3D();
        exteriorBounds.extendBy(exteriorMax);
        exteriorBounds.extendBy(exteriorMin);
        
        IntTriple [] neighbours = 
            new IntTriple[] {IntTriple.of(1, 0, 0),IntTriple.of(-1, 0, 0),IntTriple.of(0, 1, 0),IntTriple.of(0, -1, 0),IntTriple.of(0, 0, 1),IntTriple.of(0, 0, -1)};
        
        var state = ImplicitGraph.BFS(exteriorMin, null, p ->
            Query.wrap(neighbours)
                .select(n -> p.add(n))
                .where(n -> exteriorBounds.contains(n) && !cubes.contains(n))
            );
        
        var exterior = state.visited();

        var count = cubes.size() * 6;
        
        for (var p : cubes)
        {
            IntTriple n;

            n =  p.add(IntTriple.of(1, 0, 0));
            if (cubes.contains(n) || !exterior.contains(n))
                count--;

            n =  p.add(IntTriple.of(-1, 0, 0));
            if (cubes.contains(n) || !exterior.contains(n))
                count--;

            n =  p.add(IntTriple.of(0, 1, 0));
            if (cubes.contains(n) || !exterior.contains(n))
                count--;
            
            n =  p.add(IntTriple.of(0, -1, 0));
            if (cubes.contains(n) || !exterior.contains(n))
                count--;

            n =  p.add(IntTriple.of(0, 0, 1));
            if (cubes.contains(n) || !exterior.contains(n))
                count--;

            n =  p.add(IntTriple.of(0, 0, -1));
            if (cubes.contains(n) || !exterior.contains(n))
                count--;
        }
        
        
        System.out.println(count);
        
    }
}
