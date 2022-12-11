package day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt", true);
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1_test.txt", true);
        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
        
        var stackInfo = groups.get(0);

        var stacksCount = (stackInfo.get(0).length()+1) / 4;
        
        ArrayList<Stack<Character>> stacks = new ArrayList<>();
        for (var idx = 0; idx < stacksCount; idx++)
        {
            stacks.add(new Stack<>());
        }
        
        for (var lineIdx = stackInfo.size() - 2; lineIdx >= 0; lineIdx--)
        {
            var line = stackInfo.get(lineIdx);
            int stackIdx = 0;
            for (var idx = 1; idx < line.length(); idx+= 4, stackIdx++)
            {
               var c = line.charAt(idx);
               if (c != ' ')
               {
                   stacks.get(stackIdx).add(c);
               }
            }
        }

        var moveInfo = groups.get(1);
        for (var line : moveInfo)
        {
            var parsed = parse("move (\\d+) from (\\d+) to (\\d+)", line);
            var count = parseInt(parsed[1]);
            var from = parseInt(parsed[2]);
            var to = parseInt(parsed[3]);
            var source = stacks.get(from-1);
            var target = stacks.get(to-1);
            for (int cnt = 0; cnt < count; cnt++)
            {
                target.push(source.pop());
            }
        }

        StringBuffer result = new StringBuffer();
        for (var idx = 0; idx < stacks.size(); idx++)
        {
            var stack = stacks.get(idx);
            if (!stack.isEmpty())
                result.append(stack.peek());
        }
        
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
        System.out.println(result);
        
    }
}
 