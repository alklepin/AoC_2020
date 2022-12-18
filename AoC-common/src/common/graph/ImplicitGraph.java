package common.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

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
    
    public static <TNode> SearchResult<TNode> BFS(TNode start, TNode end, MoveGenerator<TNode> moveGenerator)
    {
        LinkedList<TNode> queue = new LinkedList<>();
        HashMap<TNode, TNode> visitedFrom = new HashMap<>();
        visitedFrom.put(start, start);
        queue.add(start);
        while (queue.size() > 0)
        {
            TNode current = queue.poll();
            for (var next : moveGenerator.nextNodes(current))
            {
                if (visitedFrom.containsKey(next))
                    continue;

                visitedFrom.put(next, current);
                if (end != null && next.equals(end))
                {
                    break;
                }
                queue.add(next);
            }
        }
        visitedFrom.remove(start);
        return new SearchResult<TNode>(start, end, visitedFrom);
    }
}
