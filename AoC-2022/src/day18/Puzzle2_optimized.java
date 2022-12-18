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
        for (String line : lines)
        {
            cubes.add(IntTriple.from(line));
        }

        var bounds = Bounds3D.of(cubes);
        
        var exteriorMin = bounds.min().add(IntTriple.of(-1, -1, -1));
        var exteriorMax = bounds.max().add(IntTriple.of(1, 1, 1));
        
        var state = ImplicitGraph.BFS(exteriorMin, null, p ->
            Generators.neighbours6_3D(p, exteriorMin, exteriorMax)
                .where(n -> !cubes.contains(n))
            );
        
        var exterior = state.visited();

        var result = cubes.size() * 6 -
            Query.wrap(cubes)
            .selectMany(p -> Generators.neighbours6_3D(p, null, null))
            .where(n -> cubes.contains(n) || !exterior.contains(n))
            .count();
        System.out.println(result);
        
    }
}
