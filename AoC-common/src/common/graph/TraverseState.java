package common.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;

public class TraverseState <TNodeKey, TValue>
{
    
    private HashMap<TNodeKey, TValue> m_nodeValues = new HashMap<>();
    private HashMap<TNodeKey, TNodeKey> m_visitedFrom = new HashMap<>();
    private HashSet<TNodeKey> m_visited = new HashSet<>();
    private NodeAggregator<TNodeKey, TValue> m_aggregator;
    private HashSet<TNodeKey> m_leafs = new HashSet<TNodeKey>();
    private TNodeKey m_source;

    public TraverseState(TNodeKey source)
    {
        this(source, null);
    }
    
    public TraverseState(TNodeKey source, NodeAggregator<TNodeKey, TValue> aggregator)
    {
        m_source = source;
        m_leafs.add(source);
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

    public void updateNodeValue(TNodeKey nodeKey, BiFunction<? super TNodeKey, ? super TValue, ? extends TValue> updater)
    {
        m_nodeValues.compute(nodeKey, updater);
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
    
    public TNodeKey visitedFrom(TNodeKey key)
    {
        return m_visitedFrom.get(key);
    }
    
    public TNodeKey getSource()
    {
        return m_source;
    }
    
    public void saveTransition(TNodeKey current, TNodeKey next)
    {
        m_visitedFrom.put(next, current);
        m_leafs.remove(current);
        m_leafs.add(next);
    }

    public Iterable<TNodeKey> leafs()
    {
        return m_leafs;
    }
}
