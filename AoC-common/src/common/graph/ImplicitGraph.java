package common.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;

import common.queries.Query;

public class ImplicitGraph
{
    public static interface MoveGenerator<TNode>
    {
        public Iterable<TNode> nextNodes(TNode current);
    }

    public static class SearchResult<TNode>
    {
        private final HashSet<TNode> starts;
        private final TNode end;
        private final HashMap<TNode, TNode> visitedFrom;
        
        public SearchResult(Iterable<TNode> starts, TNode end, HashMap<TNode, TNode> visitedFrom)
        {
            this.starts = new HashSet<TNode>();
            for (var s : starts)
                this.starts.add(s);
            this.end = end;
            this.visitedFrom = visitedFrom;
        }

        public SearchResult(TNode start, TNode end, HashMap<TNode, TNode> visitedFrom)
        {
            this.starts = new HashSet<TNode>();
            this.starts.add(start);
            this.end = end;
            this.visitedFrom = visitedFrom;
        }
        
        public TNode getEnd()
        {
            return end;
        }

        public ArrayList<TNode> getPath()
        {
            return getPath(end);
        }
        
        public ArrayList<TNode> getPath(TNode target)
        {
            if (target == null)
                throw new IllegalArgumentException();
            
            ArrayList<TNode> result = new ArrayList<>();
            TNode current = target;
            while (current != null)
            {
                result.add(current);
                current = visitedFrom.get(current);
            }
            Collections.reverse(result);
            
            return starts.contains(result.get(0)) ? result : null;
        }
        
        public Set<TNode> visited()
        {
            return visitedFrom.keySet();
        }
    }

    public static class SearchResultDFS<TNode> extends SearchResult<TNode>
    {

        public SearchResultDFS(Iterable<TNode> starts, TNode end,
            HashMap<TNode, TNode> visitedFrom)
        {
            super(starts, end, visitedFrom);
        }

        public SearchResultDFS(TNode start, TNode end, HashMap<TNode, TNode> visitedFrom)
        {
            super(start, end, visitedFrom);
        }

    }
//    public static <TNode> SearchResult<TNode> BFS(TNode start, TNode end, MoveGenerator<TNode> moveGenerator)
//    {
//        LinkedList<TNode> queue = new LinkedList<>();
//        HashMap<TNode, TNode> visitedFrom = new HashMap<>();
//        visitedFrom.put(start, start);
//        queue.add(start);
//        while (queue.size() > 0)
//        {
//            TNode current = queue.poll();
//            for (var next : moveGenerator.nextNodes(current))
//            {
//                if (visitedFrom.containsKey(next))
//                    continue;
//                
//                visitedFrom.put(next, current);
//                if (end != null && next.equals(end))
//                {
//                    break;
//                }
//                queue.add(next);
//            }
//        }
//        visitedFrom.remove(start);
//        return new SearchResult<TNode>(start, end, visitedFrom);
//    }
    

    public static Runnable emptyAction = () -> {};
    
    public static <TNode> SearchResult<TNode> BFS(TNode start, TNode end, MoveGenerator<TNode> moveGenerator)
    {
        Predicate<TNode> endCondition = (end != null) ? (next) -> next.equals(end) : (n) -> Boolean.FALSE;
        var result = BFSSteppedCond(start, endCondition, moveGenerator, emptyAction);
        return new SearchResult<TNode>(start, end, result.visitedFrom);
    }
    
    public static <TNode> SearchResult<TNode> BFSCond(TNode start, Predicate<TNode> endCondition, MoveGenerator<TNode> moveGenerator)
    {
        return BFSSteppedCond(start, endCondition, moveGenerator, emptyAction);
    }
    
    public static <TNode> SearchResult<TNode> BFSCond(Iterable<TNode> starts, Predicate<TNode> endCondition, MoveGenerator<TNode> moveGenerator)
    {
        return BFSSteppedCond(starts, endCondition, moveGenerator, emptyAction);
    }

    public static <TNode> SearchResult<TNode> BFSStepped(TNode start, TNode end, MoveGenerator<TNode> moveGenerator, Runnable stepAction)
    {
        Predicate<TNode> endCondition = (end != null) ? (next) -> next.equals(end) : (n) -> Boolean.FALSE;
        return BFSSteppedCond(start, endCondition, moveGenerator, stepAction);
    }
    
    public static <TNode> SearchResult<TNode> BFSSteppedCond(
        TNode start, Predicate<TNode> endCondition, MoveGenerator<TNode> moveGenerator, Runnable stepAction)
    {
        return BFSSteppedCond(Query.wrap(start), endCondition, moveGenerator, stepAction);
    }
    
    public static <TNode> SearchResult<TNode> BFSSteppedCond(
        Iterable<TNode> starts, Predicate<TNode> endCondition, MoveGenerator<TNode> moveGenerator, Runnable stepAction)
    {
        if (endCondition == null)
        {
            endCondition = (n) -> false;
        }
        
        LinkedList<TNode> currentQueue = new LinkedList<>();
        HashMap<TNode, TNode> visitedFrom = new HashMap<>();
        for (var s : starts)
        {
            visitedFrom.put(s, s);
            currentQueue.add(s);
        }
        TNode end = null;
        main_loop:
        while (currentQueue.size() > 0)
        {
            LinkedList<TNode> nextQueue = new LinkedList<>();
            while (currentQueue.size() > 0)
            {
                TNode current = currentQueue.poll();
                for (var next : moveGenerator.nextNodes(current))
                {
                    if (visitedFrom.containsKey(next))
                        continue;
                    
                    visitedFrom.put(next, current);
                    if (endCondition.test(next))
                    {
                        end = next;
                        break main_loop;
                    }
                    nextQueue.add(next);
                }
            }
            if (nextQueue.size() > 0)
            {
                stepAction.run();
                currentQueue = nextQueue;
            }
        }
        for (var s : starts)
        {
            visitedFrom.remove(s);
        }
        return new SearchResult<TNode>(starts, end, visitedFrom);
    }

    public static <TNode> SearchResult<TNode> DFS(TNode start, TNode end, MoveGenerator<TNode> moveGenerator)
    {
        Stack<TNode> stack = new Stack<>();
        HashMap<TNode, TNode> visitedFrom = new HashMap<>();
        HashMap<TNode, TNode> reachedFrom = new HashMap<>();
        visitedFrom.put(start, start);
        stack.add(start);
        while (stack.size() > 0)
        {
            TNode current = stack.pop();
            visitedFrom.put(current, reachedFrom.get(current));
            for (var next : moveGenerator.nextNodes(current))
            {
                if (visitedFrom.containsKey(next))
                    continue;
                
                reachedFrom.put(next, current);
                if (end != null && next.equals(end))
                {
                    break;
                }
                stack.add(next);
            }
        }
        visitedFrom.remove(start);
        return new SearchResult<TNode>(start, end, visitedFrom);
    }

    private static class Edge<TNode>
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
    
//    public static <TNode> StrongComponentsSearchResult<TNode> StrongComponentsFinder(TNode start, MoveGenerator<TNode> moveGenerator)
//    {
//        return StrongComponentsFinder(Query.wrap(start), moveGenerator);
//    }

    public static <TNode> StrongComponentsSearchResult<TNode> StrongComponentsFinder(Iterable<TNode> nodes, MoveGenerator<TNode> moveGenerator)
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
    
    public static <TNode> void StrongComponentsFinderImpl(TNode current, StrongComponentsFinderState<TNode> state)
    {
        var nodeInfo = new NodeInfo<TNode>();
        state.nodeInfos.put(current, nodeInfo);
        nodeInfo.index = state.nodeInfos.size();
        nodeInfo.lowLink = nodeInfo.index;
        nodeInfo.onStack = true;
        state.stack.push(current);

        // System.out.println("Look at: " + current + " index: "+ nodeInfo.index);
        var a = 1;
        
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

//         System.out.println("Look at: " + current + " index: "+ nodeInfo.index + " low index: " + nodeInfo.lowLink);
        
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

    //===========================================================================
    // Dijkstra algorithm support
    //===========================================================================

    public static interface MoveGeneratorDijkstra<TNode, TDistance extends Comparable<? super TDistance>>
    {
        public Iterable<SearchState<TNode, TDistance>> nextNodes(SearchState<TNode, TDistance> current);
    }

    public static class SearchState<TNode, TDistance extends Comparable<? super TDistance>> 
        implements Comparable<SearchState<TNode, TDistance>>
    {
        private final TNode node;
        private final TDistance distance;

        public static <TN, TD extends Comparable<? super TD>> SearchState<TN, TD> of(TN node, TD distance)
        {
            return new SearchState<TN, TD>(node, distance);
        }
        
        public SearchState(TNode nextNode, TDistance distance)
        {
            this.node = nextNode;
            this.distance = distance;
        }
        
        public TNode getNode()
        {
            return node;
        }

        public TDistance getDistance()
        {
            return distance;
        }

        @Override
        public int compareTo(SearchState<TNode, TDistance> other)
        {
            return distance.compareTo(other.distance);
        }

        public static <TNode, TDistance extends Comparable<? super TDistance>> SearchState<TNode, TDistance> from(TNode node, TDistance distance)
        {
            return new SearchState<TNode, TDistance>(node, distance);
        }

        @Override
        public String toString()
        {
            return "SearchState [node=" + node + ", distance=" + distance + "]";
        }
    }

    public static class SearchResultDijkstra<TNode, TDistance extends Comparable<? super TDistance>>
    {
        private final SearchState<TNode, TDistance> start;
        private final SearchState<TNode, TDistance> end;
        private final HashMap<TNode, SearchState<TNode, TDistance>> visitedFrom;
        private final HashMap<TNode, SearchState<TNode, TDistance>> nodeStates;
        
        public SearchResultDijkstra(SearchState<TNode, TDistance> start, 
            SearchState<TNode, TDistance> end, 
            HashMap<TNode, SearchState<TNode, TDistance>> visitedFrom,
            HashMap<TNode, SearchState<TNode, TDistance>> nodeStates)
        {
            this.start = start;
            this.end = end;
            this.visitedFrom = visitedFrom;
            this.nodeStates = nodeStates;
        }
        
        public ArrayList<TNode> getPath()
        {
            return getPath(end.node);
        }
        
        public ArrayList<TNode> getPath(TNode target)
        {
            if (target == null)
                throw new IllegalArgumentException();
            
            ArrayList<TNode> result = new ArrayList<>();
            TNode current = target;
            while (current != null)
            {
                result.add(current);
                var state = visitedFrom.get(current); 
                current = state != null ? state.getNode() : null;
            }
            Collections.reverse(result);
            
            return result.get(0).equals(start.node) ? result : null;
        }
        
        public Set<TNode> visited()
        {
            return visitedFrom.keySet();
        }
        
        public TDistance distanceTo(TNode node)
        {
            return nodeStates.get(node).distance;
        }
    }
    
    public static <TNode> 
    SearchResultDijkstra<TNode, Long> DijkstraLong(
        TNode start, 
        Predicate<TNode> endCondition, 
        MoveGeneratorDijkstra<TNode, Long> moveGenerator)
    {
        return Dijkstra(SearchState.of(start, 0l), endCondition, moveGenerator);
    }

    public static <TNode> 
    SearchResultDijkstra<TNode, Double> DijkstraDouble(
        TNode start, 
        Predicate<TNode> endCondition, 
        MoveGeneratorDijkstra<TNode, Double> moveGenerator)
    {
        return Dijkstra(SearchState.of(start, 0d), endCondition, moveGenerator);
    }
    
    
    public static <TNode, TDistance extends Comparable<? super TDistance>> 
        SearchResultDijkstra<TNode, TDistance> Dijkstra(
            SearchState<TNode, TDistance> start, 
            Predicate<TNode> endCondition, 
            MoveGeneratorDijkstra<TNode, TDistance> moveGenerator)
    {
        if (endCondition == null)
        {
            endCondition = (n) -> false;
        }
        
        PriorityQueue<SearchState<TNode, TDistance>> queue = new PriorityQueue<>();
        HashMap<TNode, SearchState<TNode, TDistance>> visitedFrom = new HashMap<>();
        HashMap<TNode, SearchState<TNode, TDistance>> nodeStates = new HashMap<>();
        visitedFrom.put(start.node, start);
        nodeStates.put(start.node, start);
        queue.add(start);
        SearchState<TNode, TDistance> end = null;
        while (queue.size() > 0)
        {
            SearchState<TNode, TDistance> current = queue.poll();
            if (endCondition.test(current.node))
            {
                end = current;
                break;
            }
            for (var next : moveGenerator.nextNodes(current))
            {
                if (next.compareTo(current) < 0)
                    throw new IllegalStateException("Negative increase has been detected");

//                System.out.printf("From %s to %s new cost %s\n", current.node, next.node, next.distance);
                
                var knownState = nodeStates.get(next.node);
                if (knownState != null && next.compareTo(knownState) >= 0)
                {
//                    System.out.println("   skipped.");
                    continue;
                }

//                System.out.println("   processed.");
                
                visitedFrom.put(next.node, current);
                nodeStates.put(next.node, next);
                
                queue.add(next);
            }
//            System.out.println("=====");
        }
        visitedFrom.remove(start.node);
        return new SearchResultDijkstra<TNode, TDistance>(start, end, visitedFrom, nodeStates);
    }

    
}
