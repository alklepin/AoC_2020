package common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Supplier;

public class Graph<TNodeKey, TInfo>
{
    private HashMap<TNodeKey, Node<TNodeKey, TInfo>> m_nodes = new HashMap<>();
    private Supplier<TInfo> m_infoFactory;
    
    private static class Node<TKey, TInf>
    {
        private TKey m_nodeKey;
        private TInf m_info;
        private HashSet<TKey> m_nextNodes = new HashSet<>();
        private HashSet<TKey> m_prevNodes = new HashSet<>();

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

        public void addNext(TKey toNodeKey)
        {
            m_nextNodes.add(toNodeKey);
        }

        public void addPrev(TKey fromNodeKey)
        {
            m_prevNodes.add(fromNodeKey);
        }

        public Iterable<TKey> nextNodes()
        {
            return m_nextNodes;
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
        Node<TNodeKey, TInfo> fromNode = getOrCreateNode(from);
        Node<TNodeKey, TInfo> toNode = getOrCreateNode(to);
        fromNode.addNext(to);
        toNode.addPrev(from);
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
}
