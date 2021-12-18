package day18;

import java.util.ArrayList;
import java.util.HashMap;

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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
        int result = 0;
        ArrayList<Node> numbers = new ArrayList<Node>();
        for (String line : lines)
        {
            var node = parse(line, 0);
            numbers.add(node);
//            System.out.println(line);
//            System.out.println(node.print());
//            System.out.println();
            
        }
        System.out.println(result);
        
    }
    
    public static Node parse(String line, int pos)
    {
        var c = line.charAt(pos);
        
        if (c >= '0' && c <= '9')
            return new Node(c-'0',1);
        if (c == '[')
        {
            var n1 = parse(line, pos+1);
            pos += n1.length + 1; // skip comma
            var n2 = parse(line, pos+1);
            pos += n1.length + 1; // skip closing bracket
            return new Node(n1, n2);
        }
        throw new IllegalStateException();
    }
    
    public static class Node
    {
        Node left;
        Node right;
        int value;
        boolean leaf;
        int length;

        public Node(int value, int length)
        {
            this.value = value;
            this.length = length;
            leaf = true;
        }
        
        public Node(Node left, Node right)
        {
            this.left = left;
            this.right = right;
            this.length = left.length + right.length + 3;
            leaf = false;
        }
        
        public int getValuer()
        {
            if (leaf)
                return value;
            throw new IllegalStateException();
        }
        
        public Node add(Node other)
        {
            Node result = new Node(this, other);
            result.explode();
            return result;
        }
        
        public void explode()
        {
            explodeImpl(4);
        }

        public void explodeImpl(int level)
        {
            if (!leaf)
            {
                left.explodeImpl(level-1);
                right.explodeImpl(level-1);
            }
            if (level <= 0)
            {
                if (!leaf)
                    throw new IllegalStateException();
                int v1 = left.getValue();
                int v2 = right.getValue();
            }
            if (!leaf)
        }
        
        public String print()
        {
            if (leaf)
                return ""+(char)('0'+value);
            return "[" + left.print() + "," + right.print() + "]";
        }
        
    }
    
    
    
}
