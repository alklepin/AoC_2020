package day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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
        
//        var l = "[[[[[9,8],1],2],3],4]";
//        var l = "[7,[6,[5,[4,[3,2]]]]]";
//        var l = "[[6,[5,[4,[3,2]]]],1]";
//        var l = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]";
//        var l = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]";
//        var l = "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]";
//        var n = parse(l, 0);
//        n.explode();
//        System.out.println(n.print());
//        System.out.println();
//        
//        var l1 = "[[[[4,3],4],4],[7,[[8,4],9]]]";
//        var l2 = "[1,1]";
//        var n1 = parse(l1, 0);
//        var n2 = parse(l2, 0);
//        var n3 = n1.add(n2);
//        System.out.println(n3.print());
//        
//        System.exit(0);
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input2.txt");
        int result = 0;
        ArrayList<Node> numbers = new ArrayList<Node>();
        HashSet<String> unique = new HashSet<>();
        for (String line : lines)
        {
            var node = parse(line, 0);
            numbers.add(node);
//            System.out.println(line);
//            System.out.println(node.print());
//            System.out.println();
            
        }
        long maxMagnitude = Integer.MIN_VALUE;
        for (int i1 = 0; i1 < numbers.size(); i1++)
        {
            for (int i2 = 0; i2 < numbers.size(); i2++)
            {
//                if (i1 != i2)
                {
                    var n1 = numbers.get(i1).duplicate();
                    var n2 = numbers.get(i2).duplicate();
                    var res = n1.add(n2);
                    var m = res.magnitude();
                    maxMagnitude = Math.max(maxMagnitude, m);
                }
            }
        }

        System.out.println(maxMagnitude);
        
    }
    
    public static Node parse(String line, int pos)
    {
        var c = line.charAt(pos);
        
        if (c >= '0' && c <= '9')
        {
            return new Node(c-'0',1);
        }
        if (c == '[')
        {
            var n1 = parse(line, pos+1);
            pos += n1.length + 1; // skip comma
            var n2 = parse(line, pos+1);
            pos += n1.length + 1; // skip closing bracket
            var result = new Node(n1, n2);
            result.left.setParent(result);
            result.right.setParent(result);
            return result;
        }
        throw new IllegalStateException();
    }
    
    public static class Node
        implements Cloneable
    {
        Node parent;
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
        
        public Node duplicate()
        {
            try
            {
                var result = (Node)super.clone();
                if (result.left != null)
                {
                    result.left = result.left.duplicate();
                    result.left.parent = result;
                }
                if (result.right != null)
                {
                    result.right = result.right.duplicate();
                    result.right.parent = result;
                }
                return result;
            }
            catch (CloneNotSupportedException ex)
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
                throw new IllegalStateException();
            }
        }
        
        public void setParent(Node parent)
        {
            this.parent = parent;
        }
        
        public int getValue()
        {
            if (leaf)
                return value;
            throw new IllegalStateException();
        }
        
        public Node add(Node other)
        {
            Node result = new Node(this, other);
            this.parent = result;
            other.parent = result;
            
            var hasAction = true;
            System.out.println(result.print());
            while (hasAction)
            {
                hasAction = result.explode();
                if (!hasAction)
                    hasAction = result.split();
                System.out.println(result.print());
            }
            return result;
        }
        
        public boolean explode()
        {
            return explodeImpl(4);
        }

        public boolean split()
        {
            if (!leaf)
            {
                return left.split() || right.split();
            }
            if (value >= 10)
            {
                var v1 = value / 2;
                var v2 = (value + 1) / 2;
                leaf = false;
                value = 0;

                left = new Node(v1, 1);
                left.parent = this;
                
                right = new Node(v2, 1);
                right.parent = this;
                return true;
            }
            return false;
        }

        public boolean explodeImpl(int level)
        {
            if (!leaf)
            {
                if (left.explodeImpl(level-1))
                {
                    return true;
                }
                if (right.explodeImpl(level-1))
                {
                    return true;
                }
            }
            if (level <= 0 && !leaf)
            {
                int v1 = left.getValue();
                int v2 = right.getValue();
                var prev = prevLeaf();
                var next = nextLeaf();
                if (prev != null)
                {
                    prev.value += v1;
                }
                if (next != null)
                {
                    next.value += v2;
                }
                this.leaf = true;
                this.left = null;
                this.right = null;
                this.value = 0;
                return true;
            }
            return false;
        }
        
        public Node prevLeaf()
        {
            var current = this;
            var parent = current.parent;
            while (parent != null && parent.left == current)
            {
                current = parent;
                parent = current.parent;
            }
            if (parent != null)
            {
                current = parent.left;
                while (!current.leaf)
                {
                    current = current.right;
                }
                return current;
            }
            return null;
        }

        public Node nextLeaf()
        {
            var current = this;
            var parent = current.parent;
            while (parent != null && parent.right == current)
            {
                current = parent;
                parent = current.parent;
            }
            if (parent != null)
            {
                current = parent.right;
                while (!current.leaf)
                {
                    current = current.left;
                }
                return current;
            }
            return null;
        }
        
        public long magnitude()
        {
            if (leaf)
                return value;
            return left.magnitude() * 3 + right.magnitude() * 2;
        }
        
        public String print()
        {
            if (leaf)
                return ""+(char)('0'+value);
            return "[" + left.print() + "," + right.print() + "]";
        }
        
    }
    
    
    
}
