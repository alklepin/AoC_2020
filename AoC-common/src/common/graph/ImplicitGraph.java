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
        visitedFrom.put(start, start);
        stack.add(start);
        while (stack.size() > 0)
        {
            TNode current = stack.pop();
            for (var next : moveGenerator.nextNodes(current))
            {
                if (visitedFrom.containsKey(next))
                    continue;

                visitedFrom.put(next, current);
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
                current = visitedFrom.get(current).getNode();
            }
            Collections.reverse(result);
            
            return result.get(0).equals(start) ? result : null;
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
        visitedFrom.remove(start);
        return new SearchResultDijkstra<TNode, TDistance>(start, end, visitedFrom, nodeStates);
    }

    
}
