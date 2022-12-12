package day02;

import java.util.ArrayList;
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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
        LinesGroup lines = readAllLines(inputFile);
        var memoryBase = lines.lineTokens(0, ",").select(token -> parseLong(token)).toList();
        
        for (int attempt = 0; attempt < 10000; attempt++)
        {
            var memory = new ArrayList<Long>(memoryBase);
            memory.set(1, attempt / 100l);
            memory.set(2, (long)(attempt % 100));
            boolean running = true;
            int idx = 0;
            while (running)
            {
                var op = memory.get(idx);
                if (op == 99)
                {
                    running = false;
                    break;
                }
                else if (op == 1)
                {
                    var res = memory.get(memory.get(idx + 1).intValue()) + memory.get(memory.get(idx + 2).intValue());
                    var addr = memory.get(idx + 3);
                    memory.set(addr.intValue(), res);
                }
                else if (op == 2)
                {
                    var res = memory.get(memory.get(idx + 1).intValue()) * memory.get(memory.get(idx + 2).intValue());
                    var addr = memory.get(idx + 3);
                    memory.set(addr.intValue(), res);
                }
                else
                {
                    throw new IllegalStateException();
                }
                idx += 4;
            }
            if (memory.get(0) == 19690720)
            {
                System.out.println(attempt);
                break;
            }
            
        }
        
        
    }
}
