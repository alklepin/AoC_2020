package day11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.ImplicitGraph;
import common.graph.StrongComponentsFinder.Edge;
import common.queries.Query;

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
    
    HashMap<String, HashSet<String>> graph = new HashMap<>();
    HashMap<String, HashSet<String>> graphRev = new HashMap<>();
    HashSet<Edge<String>> bridges = new HashSet<>();
    HashMap<String, Long> counters = new HashMap<>();
    
    long countPaths(String node)
    {
        if (node.equals("out"))
            return 1;

        var counter = counters.get(node);
        if (counter != null)
            return counter;
        
        var nextNodes = graph.get(node);
        if (nextNodes == null)
        {
            counter = 0L;
        }
        else
        {
            long result = 0;
            for (var n : nextNodes)
            {
                result += countPaths(n);
            }
            counter = result;
        }
        counters.put(node,  counter);
        
        return counter;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
//        var inputFile = "input1_test2.txt";
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        for (String line : lines)
        {
            var parts = line.split(" ");
            var key = parts[0];
            key = key.substring(0, key.length()-1);
            var list = graph.computeIfAbsent(key, k -> new HashSet<>());
            graphRev.computeIfAbsent(key, k -> new HashSet<>());
            for (var idx = 1; idx < parts.length; idx++)
            {
                var node = parts[idx];
                list.add(node);
                graph.computeIfAbsent(node, k -> new HashSet<>());
                var rev = graphRev.computeIfAbsent(node, k -> new HashSet<>());
                rev.add(key);
            }
        }
        long result = countPaths("you");
//        var comp = ImplicitGraph.findStrongComponents(Query.yield("you"), s -> 
//            Query.concat(graph.get(s), graphRev.get(s)));
//        var br = comp.getBridges();
//        System.out.println(br.size());
//        for (var c : comp.getStrongComponents())
//        {
//            System.out.println(" -> "+c.size());
//            for (var v : c)
//            {
//                System.out.print(" "+v);
//            }
//            System.out.println();
//            if (c.contains("out"))
//                System.out.println(" -> out found!");
//                
//        }
        
        System.out.println(result);
        
    }
}
