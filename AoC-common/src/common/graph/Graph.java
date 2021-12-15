package common.graph;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.function.Supplier;

public class Graph<TNodeKey, TInfo>
{
    private HashMap<TNodeKey, Node<TNodeKey, TInfo>> m_nodes = new HashMap<>();
    private Supplier<TInfo> m_infoFactory;
    
    private static class Node<TKey, TInf>
    {
        private TKey m_nodeKey;
        private TInf m_info;
        private HashMap<TKey, Double> m_nextNodes = new HashMap<>();
        private HashMap<TKey, Double> m_prevNodes = new HashMap<>();

        public Node(TKey nodeKey, TInf info)
        {
            m_nodeKey = nodeKey;
            m_info = info;
        }

        public TKey getNodeKey()
        {
            return m_nodeKey;
        }

        public TInf getInfo()
        {
            return m_info;
        }

        public void addNext(TKey toNodeKey, double weight)
        {
            m_nextNodes.put(toNodeKey, weight);
        }

        public void addPrev(TKey fromNodeKey, double weight)
        {
            m_prevNodes.put(fromNodeKey, weight);
        }

        public Iterable<TKey> nextNodes()
        {
            return m_nextNodes.keySet();
        }
        
        public double getEdgeWeightTo(TKey next)
        {
            return m_nextNodes.get(next);
        }
    }
    
    public Graph(Supplier<TInfo> infoFactory)
    {
        m_infoFactory = infoFactory;
        
    }

    public void addNode(TNodeKey nodeKey)
    {
        getOrCreateNode(nodeKey);
    }
    
    public void addEdge(TNodeKey from, TNodeKey to)
    {
        addEdge(from, to, 0);
    }
    
    public void addEdge(TNodeKey from, TNodeKey to, double weight)
    {
        Node<TNodeKey, TInfo> fromNode = getOrCreateNode(from);
        Node<TNodeKey, TInfo> toNode = getOrCreateNode(to);
        fromNode.addNext(to, weight);
        toNode.addPrev(from, weight);
    }
    
    public TInfo getNodeInfo(TNodeKey nodeKey)
    {
        Node<TNodeKey, TInfo> node = getOrCreateNode(nodeKey);
        return node.getInfo();
    }
    
    private Node<TNodeKey, TInfo> getOrCreateNode(TNodeKey nodeKey)
    {
        Node<TNodeKey, TInfo> node = m_nodes.get(nodeKey);
        if (node == null)
        {
            node = new Node<TNodeKey, TInfo>(nodeKey, m_infoFactory.get());
            m_nodes.put(nodeKey, node);
        }
        return node;
    }
    
    public void print(PrintStream ps)
    {
        for (Node<TNodeKey, TInfo> node : m_nodes.values())
        {
            ps.printf("%s (%s) -> ", node.getNodeKey(), node.m_info);
            for (TNodeKey next : node.nextNodes())
                ps.printf("%s; ", next.toString());
            ps.println();
        }
    }

    public boolean isReachable(TNodeKey source, TNodeKey target)
    {
        return findPath(source, target) != null;
    }
    
    
    private static class NodeDistanceComparator<TNodeKey> implements Comparator<Pair<TNodeKey, Double>>
    {
        public static final NodeDistanceComparator instance = new NodeDistanceComparator();
        
        @Override
        public int compare(Pair<TNodeKey, Double> o1, Pair<TNodeKey, Double> o2)
        {
            return o1.getItem2().compareTo(o2.getItem2());
        }
        
    }
    
    public TraverseState<TNodeKey, Double> findPathDijkstra(TNodeKey source, TNodeKey target)
    {
        TraverseState<TNodeKey, Double> state = new TraverseState<TNodeKey, Double>(null);
        PriorityQueue<Pair<TNodeKey, Double>> queue = new PriorityQueue<Pair<TNodeKey, Double>>(NodeDistanceComparator.instance);
        for (TNodeKey nodeKey : m_nodes.keySet())
        {
            state.setNodeValue(nodeKey, Double.MAX_VALUE);
        }
        
        state.setNodeValue(source, 0.0);
        queue.add(new Pair<TNodeKey, Double>(source, 0.0));
        while (queue.size() > 0)
        {
            TNodeKey current = queue.poll().getItem1();
            Node<TNodeKey, TInfo> node = m_nodes.get(current);
            state.addVisited(current);

            if (current.equals(target))
            {
                break;
            }
            
            double currentDistance = state.getNodeValue(current);
            
            for (TNodeKey next : node.nextNodes())
            {
                if (state.isVisited(next))
                    continue;
                
                double newDistance = currentDistance + node.getEdgeWeightTo(next);
                if (state.getNodeValue(next).doubleValue() <= newDistance)
                    continue;
                
                state.setNodeValue(next, newDistance);
                state.saveTransition(current, next);
                queue.add(new Pair<TNodeKey, Double>(next, newDistance));
            }
        }
        
        return state;
    }
    
    public ArrayList<TNodeKey> findPath(TNodeKey source, TNodeKey target)
    {
        LinkedList<TNodeKey> queue = new LinkedList<>();
        HashSet<TNodeKey> visited = new HashSet<>();
        HashMap<TNodeKey, TNodeKey> visitedFrom = new HashMap<>();
        visited.add(source);
        queue.add(source);
        while (queue.size() > 0)
        {
            TNodeKey current = queue.poll();
            Node<TNodeKey, TInfo> node = m_nodes.get(current);
            
            for (TNodeKey next : node.nextNodes())
            {
                if (visited.contains(next))
                    continue;
                
                visited.add(next);
                visitedFrom.put(next, current);
                if (next.equals(target))
                {
                    break;
                }
                queue.add(next);
            }
        }
        
        if (!visited.contains(target))
            return null;
        
        ArrayList<TNodeKey> result = new ArrayList<>();
        TNodeKey current = target;
        while (current != null)
        {
            result.add(current);
            current = visitedFrom.get(current);
        }
        Collections.reverse(result);
        return result;
    }

    public TraverseState<TNodeKey, Integer> traverseDFS(TNodeKey root)
    {
        TraverseState<TNodeKey, Integer> state = new TraverseState<TNodeKey, Integer>(null);
        traverseSubTreeDFS(root, state);
        return state;
    }

    public <TValue> TraverseState<TNodeKey, TValue> traverseDFS(TNodeKey root, NodeAggregator<TNodeKey, TValue> aggregator)
    {
        TraverseState<TNodeKey, TValue> state = new TraverseState<TNodeKey, TValue>(aggregator);
        traverseSubTreeDFS(root, state);
        return state;
    }
    
    private <TValue> void traverseSubTreeDFS(TNodeKey currentRoot, TraverseState<TNodeKey, TValue> state)
    {
        Node<TNodeKey, TInfo> node = m_nodes.get(currentRoot);
        state.addVisited(currentRoot);
        for (TNodeKey next : node.nextNodes())
        {
            if (state.isVisited(next))
                continue;
            
            traverseSubTreeDFS(next, state);
            state.saveTransition(currentRoot, next);
        }
        state.aggregate(currentRoot, node.nextNodes());
    }
    
    public static void main(String[] args)
    {
        Graph<Integer, String> graph = new Graph<Integer, String>(()->"");
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(1, 3, 1.5);
        graph.addEdge(4, 2, 0.5);
        graph.addEdge(3, 5, 0.5);
        graph.addEdge(5, 1, 0.5);
        TraverseState<Integer, Double> state = graph.findPathDijkstra(1, null);
        for (Integer nodeKey : graph.m_nodes.keySet())
        {
            System.out.println("node: " + nodeKey + " -> " + state.getNodeValue(nodeKey));
        }
    }
}
