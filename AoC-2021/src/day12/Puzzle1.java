package day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.IntValue;
import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        HashMap<Node, ArrayList<Node>> forward = new HashMap<>();
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input2.txt");
        int result = 0;
        for (String line : lines)
        {
            var parts = line.split("-");
            var fromNode = new Node(parts[0].trim());
            var toNode = new Node(parts[1].trim());
            
            var nodes = forward.computeIfAbsent(fromNode, (key) -> new ArrayList<Node>());
            if (!nodes.contains(toNode))
            {
                nodes.add(toNode);
            }
            
            nodes = forward.computeIfAbsent(toNode, (key) -> new ArrayList<Node>());
            if (!nodes.contains(fromNode))
            {
                nodes.add(fromNode);
            }
        }
//        for (var e : forward.entrySet())
//        {
//            System.out.println(e.getKey().getName() + "->");
//            for (var t : e.getValue())
//            {
//                System.out.println("  "+t.getName());
//            }
//        }
        
        HashSet<Node> visited = new HashSet<>();
        IntValue pathCount = new IntValue(0);
        Node start = new Node("start");
        Node end = new Node("end");
        var pe = new PathEnumerator(forward);
        pe.enumerate(start, end);
        
        System.out.println(pe.getCount());
        
    }
    
    private class PathEnumerator
    {
        public Node start;
        public Node ends;
        public HashSet<Node> visited;
        public HashMap<Node, ArrayList<Node>> forward;
        public int count;
        
        public String prefix;
        
        public PathEnumerator(HashMap<Node, ArrayList<Node>> forward)
        {
            this.forward = forward;
            this.visited = new HashSet<>();
            count = 0;
            prefix = "";
        }
        
        public int getCount()
        {
            return count;
        }
        
        public void enumerate(Node start, Node end)
        {
            this.start = start;
            this.ends = end;
            enumerateImpl(start);
        }
        
        public void enumerateImpl(Node node)
        {
            System.out.println(prefix + node.name);
            if (node.equals(ends))
            {
                System.out.println(prefix + " found!!!");
                count++;
                return;
            }
            if (visited.contains(node))
            {
                return;
            }
            if (!node.bigCave)
            {
                visited.add(node);
            }
            prefix += "  ";
            var nextNodes = forward.get(node);
            for (var n : nextNodes)
            {
                enumerateImpl(n);
            }
            prefix = prefix.substring(0, prefix.length() - 2);
            visited.remove(node);
        }
    }
    
    private class Node
    {
        private final String name;
        private final boolean bigCave;
        
        public Node(String name)
        {
            super();
            this.name = name;
            this.bigCave = allCapitals(name);
        }
        
        private boolean allCapitals(String s)
        {
            for (var c : s.toCharArray())
            {
                if (Character.toUpperCase(c) != c)
                    return false;
            }
            return true;
        }
        
        public String getName()
        {
            return name;
        }


        public boolean isBigCave()
        {
            return bigCave;
        }


        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + (bigCave ? 1231 : 1237);
            result = prime * result + ((name == null) ? 0 : name.hashCode());
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
            Node other = (Node)obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (bigCave != other.bigCave)
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            return true;
        }
        private Puzzle1 getEnclosingInstance()
        {
            return Puzzle1.this;
        }
        
        
    }
    
}
