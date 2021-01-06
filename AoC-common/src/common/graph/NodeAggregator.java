package common.graph;

public interface NodeAggregator<TNodeKey, TValue>
{
    TValue getNodeValue(TNodeKey node, Iterable<TNodeKey> childNodes, TraverseState<TNodeKey, TValue> state);
}
