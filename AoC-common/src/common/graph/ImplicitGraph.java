package common.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;

public class ImplicitGraph
{
    public static interface MoveGenerator<TNode>
    {
        public Iterable<TNode> nextNodes(TNode current);
    }
    
    public static class SearchResult<TNode>
    {
        private final TNode start;
        private final TNode end;
        private final HashMap<TNode, TNode> visitedFrom;
        
        public SearchResult(TNode start, TNode end, HashMap<TNode, TNode> visitedFrom)
        {
            this.start = start;
            this.end = end;
            this.visitedFrom = visitedFrom;
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
            
            return result.get(0).equals(start) ? result : null;
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

    public static <TNode> SearchResult<TNode> BFSStepped(TNode start, TNode end, MoveGenerator<TNode> moveGenerator, Runnable stepAction)
    {
        Predicate<TNode> endCondition = (end != null) ? (next) -> next.equals(end) : (n) -> Boolean.FALSE;
        return BFSSteppedCond(start, endCondition, moveGenerator, stepAction);
    }
    
    public static <TNode> SearchResult<TNode> BFSSteppedCond(TNode start, Predicate<TNode> endCondition, MoveGenerator<TNode> moveGenerator, Runnable stepAction)
    {
        LinkedList<TNode> currentQueue = new LinkedList<>();
        HashMap<TNode, TNode> visitedFrom = new HashMap<>();
        visitedFrom.put(start, start);
        currentQueue.add(start);
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
        visitedFrom.remove(start);
        return new SearchResult<TNode>(start, end, visitedFrom);
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
    
    
}
