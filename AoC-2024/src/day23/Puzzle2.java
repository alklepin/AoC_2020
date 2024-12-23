package day23;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.ImplicitGraph;
import common.queries.Query;
import common.queries.SubsetGenerator;

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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
//        var testList = new ArrayList<String>();
//        for (var s : new String[] {"1", "2", "3", "4", "5"})
//            testList.add(s);
//        for (var s : SubsetGenerator.subsets(testList, 1))
//        {
//            System.out.println(Query.wrap(s).join());
//        }
//        
//        System.exit(0);

        //        var testList = new ArrayList<String>();
//        for (var s : new String[] {"1", "2", "3", "4", "5"})
//            testList.add(s);
//        for (var s : subsets(testList, 3))
//        {
//            System.out.println(Query.wrap(s).join());
//        }
//        
//        System.exit(0);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        HashMap<String, ArrayList<String>> connections = new HashMap<>();
        HashSet<NamesPair> network = new HashSet<>();
        HashSet<String> namesSet = new HashSet<>();
        var maxSize = 0;
        for (String line : lines)
        {
            var parts = split(line, "-");
            var n1 = parts.get(0);
            var n2 = parts.get(1);
            
            network.add(new NamesPair(n1, n2));
            network.add(new NamesPair(n2, n1));
            
            namesSet.add(n1);
            namesSet.add(n2);
            
            var con1 = connections.get(n1);
            if (con1 == null)
            {
                con1 = new ArrayList<String>();
                connections.put(n1, con1);
            }
            con1.add(n2);
            maxSize = Math.max(maxSize, con1.size());
            var con2 = connections.get(n2);
            if (con2 == null)
            {
                con2 = new ArrayList<String>();
                connections.put(n2, con2);
            }
            con2.add(n1);
            maxSize = Math.max(maxSize, con2.size());
        }

//        System.out.println(maxSize);
        
        var sizesToCheck = new ArrayList<Integer>();
        for (var i = 1; i <= maxSize; i++)
        {
            var count = 0;
            for (var c : connections.values())
            {
                if (c.size() == i)
                    count++;
            }
            if (count >= i)
            {
                sizesToCheck.add(i);
//                System.out.println(""+i+" -> "+count);
            }
        }
        Collections.reverse(sizesToCheck);
        
        external_loop:
        for (var compSize : sizesToCheck)
        {
            System.out.println("Checking size: "+compSize);
            
            var subgraph = new HashSet<String>();
            for (var e : connections.entrySet())
            {
                if (e.getValue().size() >= compSize)
                {
                    subgraph.add(e.getKey());
                }
            }
            
            var searchResult = ImplicitGraph.findStrongComponents(subgraph, n -> Query.wrap(connections.get(n)).where(nm -> subgraph.contains(nm)));
            var components = searchResult.getStrongComponents();
            for (var c : components)
            {
                if (c.size() >= compSize)
                {
//                    System.out.println("Component: "+Query.wrap(c).join());

                    var toRemove = new ArrayList<String>();
//                    var compNodes = new ArrayList<String>(c);
                    var compNodes = new ArrayList<String>(c);
                    Collections.sort(compNodes);
                    for (var from : compNodes)
                    {
                        var map = new StringBuilder();
                        var count = 0;
                        for (var to : compNodes)
                        {
                            if (network.contains(new NamesPair(from, to)))
                            {
                                map.append('#');
                                count++;
                            }
                            else
                                map.append('.');
                        }
                        if (count < compSize)
                            toRemove.add(from);
                    }
                    if (compNodes.size() - toRemove.size() < compSize)
                    {
//                        System.out.println("Skipped...");
                        continue;
                    }
                    
                    compNodes.removeAll(toRemove);
                    
                    var conMaps = new ArrayList<String>();
                    var conMapsHs = new HashMap<String, String>();
                    for (var from : compNodes)
                    {
                        var map = new StringBuilder();
                        var count = 0;
                        for (var to : compNodes)
                        {
                            if (network.contains(new NamesPair(from, to)))
                            {
                                map.append('#');
                                count++;
                            }
                            else
                                map.append('.');
                        }
                        if (count >= compSize)
                        {
                            var m  = map.toString();
                            conMaps.add(m);
                            conMapsHs.put(from,  m);
//                            System.out.println(from + ": "+count);
//                            System.out.println(from + ": "+map);
                        }
                    }

//                    Collections.sort(conMaps);
//                    for (var m : conMaps)
//                        System.out.println(m);
                    Collections.sort(compNodes);
                    var hsCompNodes = new HashSet<String>(compNodes);
                    var processed = new HashSet<String>();
                    for (var first : compNodes)
                    {
                        var connected = Query.yield(first)
                            .concat(
                                Query.wrap(connections.get(first))
                                    .where(s -> hsCompNodes.contains(s)
                                        && !processed.contains(s)
                                        )).toList();

                        processed.add(first);
                        if (connected.size() < compSize)
                            continue;
                        
//                        var compNodesReversed = new ArrayList<String>();
//                        compNodesReversed.addAll(connected);
//                        Collections.reverse(compNodesReversed);
//                        for (var set : subsets(compNodesReversed, compSize))
//                        {
////                            System.out.println(Query.wrap(set).join());
//                            if (checkFullGraph(set, network))
//                            {
//                                var res = new ArrayList<String>();
//                                res.addAll(set);
//                                Collections.sort(res);
//                                System.out.println("Result found:");
//                                System.out.println(Query.wrap(res).join(","));
//                                break external_loop;
//                            }
//                        }
                        for (var set : SubsetGenerator.subsets(connected, compSize))
                        {
//                            System.out.println(Query.wrap(set).join());
                            if (checkFullGraph(set, network))
                            {
                                var res = new ArrayList<String>();
                                res.addAll(set);
                                Collections.sort(res);
                                System.out.println("Result found:");
                                System.out.println(Query.wrap(res).join(","));
                                break external_loop;
                            }
                        }
                    }
                    
//                    System.out.println("Found!!! "+compSize + " " + c.size());
//                    System.out.println("Found!!! "+compSize + " " + compNodes.size());
//                    System.out.println(Query.wrap(c).join());
                }
            }
        }
    }
    
    public boolean checkFullGraph(ArrayList<String> nodes, HashSet<NamesPair> connections)
    {
        var connected = true;
        external:
        for (var idx1 = 0; idx1 < nodes.size(); idx1++)
        {
            var n1 = nodes.get(idx1);
            for (var idx2 = idx1+1; idx2 < nodes.size(); idx2++)
            {
                var n2 = nodes.get(idx2);
                connected &= connections.contains(new NamesPair(n1, n2));
                if (!connected)
                    break external;
            }
        }
        return connected;
    }
    
    static class NamesPair
    {
        public final String name1;
        public final String name2;
        public NamesPair(String name1, String name2)
        {
            super();
            this.name1 = name1;
            this.name2 = name2;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name1 == null) ? 0 : name1.hashCode());
            result = prime * result + ((name2 == null) ? 0 : name2.hashCode());
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
            NamesPair other = (NamesPair)obj;
            if (name1 == null)
            {
                if (other.name1 != null)
                    return false;
            }
            else if (!name1.equals(other.name1))
                return false;
            if (name2 == null)
            {
                if (other.name2 != null)
                    return false;
            }
            else if (!name2.equals(other.name2))
                return false;
            return true;
        }
        @Override
        public String toString()
        {
            return "NamesPair [name1=" + name1 + ", name2=" + name2 + "]";
        }
        
        
        
    }
}
