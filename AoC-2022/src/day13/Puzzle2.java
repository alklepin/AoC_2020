package day13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
    
    public static class ListItem implements Comparable<ListItem>
    {
        int value;
        ListNode list;
        
        @Override
        public int compareTo(ListItem other)
        {
            if (list != null)
            {
                if (other.list != null)
                {
                    return list.compareTo(other.list);
                }
                else
                    return -other.compareTo(this);
            }
            else
            {
                if (other.list != null)
                {
                    var nl = new ListNode();
                    nl.items = new ArrayList<>();
                    nl.items.add(this);
                    return nl.compareTo(other.list);
                }
                else
                {
                    return Integer.compare(value, other.value);
                }
            }
        }
    }
    
    public static class ListNode implements Comparable<ListNode>
    {
        ArrayList<ListItem> items = new ArrayList<>();
        
        public int parse(String line, int startIdx)
        {
            if (line.charAt(startIdx) != '[')
                throw new IllegalStateException();
            
            ArrayList<ListItem> sublist = new ArrayList<>();
            
            for (var idx = startIdx + 1; idx < line.length(); idx++)
            {
                switch (line.charAt(idx))
                {
                    case ']':
                    {
                        items = sublist;
                        return idx + 1 - startIdx;
                    }
                    case '[':
                    {
                        ListNode s = new ListNode();
                        var shift = s.parse(line, idx);
                        ListItem li = new ListItem();
                        li.list = s;
                        sublist.add(li);
                        idx += shift;
                        idx--;
                        break;
                    }
                    case ',':
                    {
                        // do nothing
                        break;
                    }
                    default:
                    {
                        var numEnd = idx ;
                        var token = new StringBuilder();
                        while (line.charAt(numEnd) >= '0' && line.charAt(numEnd) <= '9')
                        {
                            token.append(line.charAt(numEnd));
                            numEnd++;
                        }
                        ListItem li = new ListItem();
                        li.value = parseInt(token.toString());
                        sublist.add(li);
                        idx = numEnd;
                        idx--;
                        break;
                    }
                }
            }
            return line.length() - startIdx;
        }
        
        public String print()
        {
            var result = new StringBuilder();
            result.append('[');
            for (var item : items)
            {
                if (item.list != null)
                    result.append(item.list.print());
                else
                    result.append(item.value);
                result.append(',');
            }
            if (result.charAt(result.length()-1) == ',')
                result.setLength(result.length()-1);
            result.append(']');
            return result.toString();
        }
        
        public String toString()
        {
            return print();
        }

        @Override
        public int compareTo(ListNode other)
        {
           int length = Math.min(items.size(), other.items.size());
           for (int idx = 0; idx < length; idx++)
           {
               var item1 = items.get(idx);
               var item2 = other.items.get(idx);
               var itemComparison = item1.compareTo(item2);
               if (itemComparison != 0)
                   return itemComparison;
           }
           return Integer.compare(items.size(), other.items.size());
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
        ArrayList<ListNode> allPackets = new ArrayList<>();
        
        int result = 0;
        int index = 0;
        for (LinesGroup group : groups)
        {
            index++;
            var lineLeft = group.line(0);
            var lineRight = group.line(1);
            var nodeLeft = new ListNode();
            nodeLeft.parse(lineLeft, 0);
            var nodeRight = new ListNode();
            nodeRight.parse(lineRight, 0);
  
//            System.out.println(nodeLeft.print());
//            System.out.println(nodeRight.print());
//
//            System.out.println(nodeLeft.compareTo(nodeRight));
//            
//            System.out.println();
            
            allPackets.add(nodeLeft);
            allPackets.add(nodeRight);
        }
        var divider1 = new ListNode();
        divider1.parse("[[2]]", 0);
        allPackets.add(divider1);

        var divider2 = new ListNode();
        divider2.parse("[[6]]", 0);
        allPackets.add(divider2);
        
        Collections.sort(allPackets);
        
        var idx1 = allPackets.indexOf(divider1);
        var idx2 = allPackets.indexOf(divider2);
        
        for (var p : allPackets)
        {
            System.out.println(p);
        }
        
        System.out.println(idx1);
        System.out.println(idx2);
        System.out.println((idx1+1) * (idx2+1));
        
    }
}
