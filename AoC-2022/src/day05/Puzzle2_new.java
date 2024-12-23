package day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import common.LinesGroup;
import common.PuzzleCommon;
import common.regexp.FieldsParser;

public class Puzzle2_new extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_new().solve();
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

    static class MoveInfo
    {
        int count;
        int from;
        int to;
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

        var parser = FieldsParser.getFor("move (?<count>\\d+) from (?<from>\\d+) to (?<to>\\d+)", MoveInfo.class);
        var moveInfo = groups.get(1);
        Stack<Character> buffer = new Stack<>();
        for (var line : moveInfo)
        {
            var move = parser.parse(line);
            var source = stacks.get(move.from-1);
            var target = stacks.get(move.to-1);
            for (int cnt = 0; cnt < move.count; cnt++)
            {
                buffer.push(source.pop());
            }
            while (!buffer.isEmpty())
            {
                target.push(buffer.pop());
            }
        }

        StringBuffer result = new StringBuffer();
        for (var idx = 0; idx < stacks.size(); idx++)
        {
            var stack = stacks.get(idx);
            if (!stack.isEmpty())
                result.append(stack.peek());
        }
        
        
//        LinesGroup lines = readAllLines("input1.txt");
        
//        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
        System.out.println(result);
        
    }
}
 