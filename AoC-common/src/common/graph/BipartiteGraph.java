package common.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import common.queries.Query;

public class BipartiteGraph<T1, T2>
{
    private HashSet<T1> m_nodes1 = new HashSet<>();
    private HashSet<T2> m_nodes2 = new HashSet<>();
    private HashMap<T1,HashSet<T2>> m_relations = new HashMap<>();
    private HashMap<T1,T2> m_bestMatching = new HashMap<>();
    
    public void addRelation(T1 node1, T2 node2)
    {
        m_nodes1.add(node1);
        m_nodes2.add(node2);
        HashSet<T2> related = m_relations.get(node1);
        if (related == null)
        {
            related = new HashSet<T2>();
            m_relations.put(node1, related);
        }
        related.add(node2);
    }
    
    public void findBestMatching()
    {
        m_bestMatching.clear();
        HashMap<T2,T1> matchedFrom = new HashMap<>();
        HashMap<T2,T1> prevNode = new HashMap<>();
        T2 endOfChainFound;
        boolean hasChanges = true;
        while (hasChanges)
        {
            hasChanges = false;
            endOfChainFound = null;
            prevNode.clear();
            
            HashSet<T1> front1 = Query.wrap(m_nodes1).where(node -> !m_bestMatching.containsKey(node)).toSet();
            Queue<T1> currentFront = new LinkedList<>(front1);
            loop:
            while (currentFront.size() > 0)
            {
                T1 node1 = currentFront.poll();
                System.out.println("Try from: "+node1);
                for (T2 node2 : Query.wrap(m_relations.get(node1)).where(node -> m_bestMatching.get(node1) != node))
                {
                    T1 pairedNode = matchedFrom.get(node2);
                    prevNode.put(node2, node1);
                    System.out.println("  go to: "+node2);
                    if (pairedNode == null)
                    {
                        System.out.println("    found!");
                        endOfChainFound = node2;
                        break loop;
                    }
                    else
                    {
                        System.out.println("    next node: "+pairedNode);
                        currentFront.add(pairedNode);
                    }
                }
            }
            if (endOfChainFound != null)
            {
                hasChanges = true;
                while (endOfChainFound != null)
                {
                    T1 from = prevNode.get(endOfChainFound);
                    T2 oldTo = m_bestMatching.get(from);
                    m_bestMatching.put(from, endOfChainFound);
                    matchedFrom.put(endOfChainFound, from);
                    endOfChainFound = oldTo;
                }
            }
            System.out.println("Match state:");
            for (Map.Entry<T1, T2> entry : m_bestMatching.entrySet())
            {
                System.out.printf("%s -> %s\n", entry.getKey(), entry.getValue());
            }
        }
    }
    
    public T2 getMatch(T1 value)
    {
        return m_bestMatching.get(value);
    }
    
}
