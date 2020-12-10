package day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.PuzzleCommon;
import common.graph.Graph;
import common.graph.TraverseState;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    private int max;
    
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
        ArrayList<String> lines = readAllLines("input1.txt");
        int result = 0;
        ArrayList<Integer> values = new ArrayList<>();
        for (String line : lines)
        {
            values.add(Integer.parseInt(line));
        }
        Collections.sort(values);

        Graph<Integer, String> graph = new Graph<Integer, String>(() -> "");
        max = Integer.MIN_VALUE;
        for (Integer value : values)
        {
            int v = value;
            if (v >= 3)
                graph.addEdge(v-3, v);
            if (v >= 2)
                graph.addEdge(v-2, v);
            if (v >= 1)
                graph.addEdge(v-1, v);
            if (v > max)
                max = v;
        }
        TraverseState<Integer, Long> state = graph.traverseDFS(0, this::countVariants);
        Long v = state.getNodeValue(0);
        System.out.println(v);
    }
    
    private Long countVariants(Integer node, Iterable<Integer> children, TraverseState<Integer, Long> state)
    {
        long count = (node.intValue() == max) ? 1 : 0;
        for (Integer childNode : children)
        {
            Long nodeValue = state.getNodeValue(childNode);
            if (nodeValue != null)
            {
                count += nodeValue;
            }
        }
        return count;
    }
    
}
