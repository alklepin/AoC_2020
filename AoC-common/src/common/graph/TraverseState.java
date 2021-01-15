package common.graph;

import java.util.HashMap;
import java.util.HashSet;

public class TraverseState <TNodeKey, TValue>
{
    public HashMap<TNodeKey, TValue> m_nodeValues = new HashMap<>();
    public HashMap<TNodeKey, TNodeKey> m_visitedFrom = new HashMap<>();
    public HashSet<TNodeKey> m_visited = new HashSet<>();
    private NodeAggregator<TNodeKey, TValue> m_aggregator;

    public TraverseState(NodeAggregator<TNodeKey, TValue> aggregator)
    {
        m_aggregator = aggregator;
    }
    
    public void aggregate(TNodeKey node, Iterable<TNodeKey> childNodes)
    {
        if (m_aggregator != null)
        {
            TValue value = m_aggregator.getNodeValue(node, childNodes, this);
            m_nodeValues.put(node, value);
        }
    }
    
    public void setNodeValue(TNodeKey nodeKey, TValue value)
    {
        m_nodeValues.put(nodeKey, value);
    }
    
    public TValue getNodeValue(TNodeKey nodeKey)
    {
        return m_nodeValues.get(nodeKey);
    }
    
    public void addVisited(TNodeKey nodeKey)
    {
        m_visited.add(nodeKey);
    }
    
    public boolean isVisited(TNodeKey nodeKey)
    {
        return m_visited.contains(nodeKey);
    }
    
    public void saveTransition(TNodeKey currentRoot, TNodeKey next)
    {
        m_visitedFrom.put(next, currentRoot);
    }

}
