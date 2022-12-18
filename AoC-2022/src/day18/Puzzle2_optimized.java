package day18;

import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Bounds3D;
import common.boards.Generators;
import common.boards.IntTriple;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2_optimized extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_optimized().solve();
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
        var bounds = new Bounds3D();
        for (String line : lines)
        {
            var p = IntTriple.from(line);
            cubes.add(p);
            bounds.extendBy(p);
        }
        
        var min = bounds.min();
        var max = bounds.max();
        System.out.println(min);
        System.out.println(max);

        var exteriorMin = min.add(IntTriple.of(-1, -1, -1));
        var exteriorMax = max.add(IntTriple.of(1, 1, 1));
        
        var state = ImplicitGraph.BFS(exteriorMin, null, p ->
            Generators.neighbours6_3D(p, exteriorMin, exteriorMax)
                .where(n -> !cubes.contains(n))
            );
        
        var exterior = state.visited();

        var count = cubes.size() * 6;
        
        for (var p : cubes)
        {
            for (var n : Generators.neighbours6_3D(p, null, null))
            {
                if (cubes.contains(n) || !exterior.contains(n))
                    count--;
            }
        }
        
        
        System.out.println(count);
        
    }
}
