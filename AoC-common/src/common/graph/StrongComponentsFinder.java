package common.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import common.graph.ImplicitGraph.MoveGenerator;

public class StrongComponentsFinder
{
//  public static <TNode> StrongComponentsSearchResult<TNode> StrongComponentsFinder(TNode start, MoveGenerator<TNode> moveGenerator)
//  {
//      return StrongComponentsFinder(Query.wrap(start), moveGenerator);
//  }

    public static <TNode> StrongComponentsSearchResult<TNode> findStrongComponents(Iterable<TNode> nodes, MoveGenerator<TNode> moveGenerator)
    {
        var state = new StrongComponentsFinderState<TNode>(moveGenerator);
        for (var start : nodes)
        {
            if (state.nodeInfos.get(start) == null)
            {
                state.visitedFrom.put(start, start);
                StrongComponentsFinderImpl(start, state);
            }
        }
        return new StrongComponentsSearchResult<TNode>(state.strongComponents, state.bridges);
    }
  
    // Finds strong adjacency components for undirected graph
    // see https://ru.wikipedia.org/wiki/Алгоритм_Тарьяна
    public static <TNode> void StrongComponentsFinderImpl(TNode current, StrongComponentsFinderState<TNode> state)
    {
        var nodeInfo = new NodeInfo<TNode>();
        state.nodeInfos.put(current, nodeInfo);
        nodeInfo.index = state.nodeInfos.size();
        nodeInfo.lowLink = nodeInfo.index;
        nodeInfo.onStack = true;
        state.stack.push(current);

        // System.out.println("Look at: " + current + " index: "+ nodeInfo.index);
        
        for (var next : state.moveGenerator.nextNodes(current))
        {
            if (next.equals(state.visitedFrom.get(current)))
                continue;
            
            var nextNodeInfo = state.nodeInfos.get(next);
            if (nextNodeInfo == null)
            {
                state.visitedFrom.put(next, current);
                StrongComponentsFinderImpl(next, state);
                nextNodeInfo = state.nodeInfos.get(next);
                nodeInfo.lowLink = Math.min(nodeInfo.lowLink, nextNodeInfo.lowLink);
            }
            else if (nextNodeInfo.onStack)
            {
                // Вершина w находится в стеке, значит, принадлежит той же компоненте сильной связности, что и v
                // Если w не в стеке, значит, ребро (v, w) ведёт в ранее обработанную компоненту сильной связности и должна быть проигнорирована
                // Замечание: в следующей строке намеренно используется w.index вместо w.lowlink - это отсылает к исходной статье Тарьяна
                // Если заменить w.index на w.lowlink, алгоритм останется корректным
                nodeInfo.lowLink = Math.min(nodeInfo.lowLink, nextNodeInfo.index);
            }
        }

        //System.out.println("Look at: " + current + " index: "+ nodeInfo.index + " low index: " + nodeInfo.lowLink);
        
        if (nodeInfo.lowLink == nodeInfo.index)
        {
            ArrayList<TNode> strongComponent = new ArrayList<>();
            TNode node;
            do
            {
              node = state.stack.pop();
              var nInfo = state.nodeInfos.get(node);
              nInfo.onStack = false;
              strongComponent.add(node);
            } while (!current.equals(node));
            state.strongComponents.add(strongComponent);

            var prev = state.visitedFrom.get(current);
            if (!current.equals(prev))
                state.bridges.add(Edge.of(current, prev));
        }
    }

    public static class Edge<TNode>
    {
        TNode n1;
        TNode n2;

        public static <T> Edge<T> of(T node1, T node2)
        {
            return new Edge<T>(node1, node2);
        }
        
        public Edge(TNode n1, TNode n2)
        {
            this.n1 = n1;
            this.n2 = n2;
        }
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
            result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
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
            @SuppressWarnings("unchecked")
            Edge<TNode> other = (Edge<TNode>)obj;
            if (n1 == null)
            {
                if (other.n1 != null)
                    return false;
            }
            else if (!n1.equals(other.n1))
                return false;
            if (n2 == null)
            {
                if (other.n2 != null)
                    return false;
            }
            else if (!n2.equals(other.n2))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Edge [n1=" + n1 + ", n2=" + n2 + "]";
        }
    }
    
    private static class NodeInfo<TNode>
    {
        int index;
        int lowLink;
        boolean onStack;
    }
    
    private static class StrongComponentsFinderState<TNode>
    {
        HashMap<TNode, NodeInfo<TNode>> nodeInfos = new HashMap<>();
        HashMap<TNode, TNode> visitedFrom = new HashMap<>();
        ArrayList<Edge<TNode>> bridges = new ArrayList<>();
        Stack<TNode> stack = new Stack<>();
        ArrayList<ArrayList<TNode>> strongComponents = new ArrayList<>();
        
        MoveGenerator<TNode> moveGenerator;
        
        public StrongComponentsFinderState(MoveGenerator<TNode> moveGenerator)
        {
            this.moveGenerator = moveGenerator;
        }
    }
    
    public static class StrongComponentsSearchResult<TNode>
    {

        private final ArrayList<ArrayList<TNode>> m_strongComponents;
        private final ArrayList<Edge<TNode>> m_bridges;

        public StrongComponentsSearchResult(
            ArrayList<ArrayList<TNode>> strongComponents,
            ArrayList<Edge<TNode>> bridges)
        {
            m_strongComponents = strongComponents;
            m_bridges = bridges;
        }

        public ArrayList<ArrayList<TNode>> getStrongComponents()
        {
            return m_strongComponents;
        }

        public ArrayList<Edge<TNode>> getBridges()
        {
            return m_bridges;
        }
    }
}
