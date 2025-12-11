package day11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.ImplicitGraph;
import common.graph.StrongComponentsFinder.Edge;
import common.queries.Query;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }

    static class PathInfo
    {
        long pathCount = 0;
        long pathWithDacCount = 0;
        long pathWithFftCount = 0;
        long pathWithBothCount = 0;
        
        public static PathInfo empty()
        {
            return new PathInfo();
        }

        public static PathInfo outNode()
        {
            var result = new PathInfo();
            result.pathCount = 1;
            return result;
        }

        public void combineWith(String node, PathInfo nextCount)
        {
            if (node.equals("dac"))
            {
                pathWithDacCount += nextCount.pathCount;
                pathWithBothCount += nextCount.pathWithFftCount;
                if (nextCount.pathWithBothCount != 0 || nextCount.pathWithDacCount != 0)
                    throw new IllegalStateException();
            }
            else if (node.equals("fft"))
            {
                pathWithFftCount += nextCount.pathCount;
                pathWithBothCount += nextCount.pathWithDacCount;
                if (nextCount.pathWithBothCount != 0 || nextCount.pathWithFftCount != 0)
                    throw new IllegalStateException();
            }
            else
            {
                pathCount += nextCount.pathCount;
                pathWithDacCount += nextCount.pathWithDacCount;
                pathWithFftCount += nextCount.pathWithFftCount;
                pathWithBothCount += nextCount.pathWithBothCount;
            }
        }
    }
    
    HashMap<String, HashSet<String>> graph = new HashMap<>();
    HashMap<String, HashSet<String>> graphRev = new HashMap<>();
    HashSet<Edge<String>> bridges = new HashSet<>();
    HashMap<String, PathInfo> counters = new HashMap<>();
    
    PathInfo countPaths(String node)
    {
        if (node.equals("out"))
            return PathInfo.outNode();

        var counter = counters.get(node);
        if (counter != null)
            return counter;
        
        var nextNodes = graph.get(node);
        if (nextNodes == null)
        {
            counter = PathInfo.empty();
        }
        else
        {
            PathInfo result = PathInfo.empty();
            for (var n : nextNodes)
            {
                var nextCount = countPaths(n); 
                result.combineWith(node, nextCount); 
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
//        var inputFile = "input2_test.txt";
        
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
        PathInfo result = countPaths("svr");
        
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
        
        System.out.println(result.pathWithBothCount);
        
    }
}
