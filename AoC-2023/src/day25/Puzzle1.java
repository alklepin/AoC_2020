package day25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.ImplicitGraph;
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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        HashSet<Edge> edgesSet = new HashSet<>();
        ArrayList<Edge> edgesList = new ArrayList<>();
        HashMap<String, ArrayList<String>> edges = new HashMap<>();
        
        for (String line : lines)
        {
            var parts = line.split(":");
            var from = parts[0].trim();
            var toParts = parts[1].trim().split("\\s+");
            for (var t : toParts)
            {
                var edge = new Edge(from, t);
                edgesSet.add(edge);
                edgesList.add(edge);
                var listFrom = edges.get(from);
                if (listFrom == null)
                {
                    listFrom = new ArrayList<String>();
                    edges.put(from, listFrom);
                }
                listFrom.add(t);
                var listTo = edges.get(t);
                if (listTo == null)
                {
                    listTo = new ArrayList<String>();
                    edges.put(t, listTo);
                }
                listTo.add(from);
            }
        }
        //var start = edgesList.get(0).node1;
        var nodes = Query.wrap(edges.keySet()).toList();
        System.out.println("Nodes count:"+nodes.size());
        
        loop:
        for (var idx1 = 0; idx1 < edgesList.size(); idx1++)
        {
            System.out.println("Loop index:"+idx1);
            var edge1 = edgesList.get(idx1);
//            var edge1 = new Edge("hfx","pzl");
            for (var idx2 = idx1 + 1; idx2 < edgesList.size(); idx2++)
            {
                var edge2 = edgesList.get(idx2);
//                var edge2 = new Edge("bvb","cmg");
                var res = ImplicitGraph.StrongComponentsFinder(nodes, node ->
                {
                    var list = Query.wrap(edges.get(node))
                    .where(next -> {
                        var e = new Edge(node, next);
                        return !edge1.equals(e) && !edge2.equals(e);
                    }).toList();
//                    for (var n1 : list)
//                    {
//                        System.out.println("from: "+node+" to: "+n1);
//                    }
                    
                    return list;
                });
                if (res.getBridges().size() == 1) //  && res.getStrongComponents().size() >= 2
                {
                    System.out.println(res.getBridges().get(0));
                    System.out.println(edge1);
                    System.out.println(edge2);
                    System.out.println(res.getStrongComponents().get(0).size());
                    System.out.println(res.getStrongComponents().get(1).size());
                    
                    break loop;
                }
            }
        }
        System.out.println(result);
    }
    
    public static class Edge
    {
        String node1;
        String node2;
        
        public Edge(String n1, String n2)
        {
            if (n1.compareTo(n2) > 0)
            {
                var tmp = n1;
                n1 = n2;
                n2 = tmp;
            }
            node1 = n1;
            node2 = n2;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
            result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Edge other = (Edge)obj;
            if (node1 == null)
            {
                if (other.node1 != null)
                    return false;
            }
            else if (!node1.equals(other.node1))
                return false;
            if (node2 == null)
            {
                if (other.node2 != null)
                    return false;
            }
            else if (!node2.equals(other.node2))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Edge [node1=" + node1 + ", node2=" + node2 + "]";
        }
        
        
    }
}
