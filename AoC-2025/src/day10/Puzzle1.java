package day10;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.Graph;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    static class Machine
    {
        String sTargetState;
        int targetState;
        ArrayList<Integer> buttons;
        ArrayList<Integer> joltages;

        public Machine(String lights, ArrayList<Integer> buttons, ArrayList<Integer> joltages)
        {
            sTargetState = lights;
            targetState = lightsAsANumber(lights);
            this.buttons = buttons;
            this.joltages = joltages;
        }
        
        long stepsToTarget()
        {
            var result = ImplicitGraph.BFS(0, targetState, node -> Query.wrap(buttons).select(b -> node ^ b));
            return result.getPath().size();
        }
    }
    
    static int lightsAsANumber(String lights)
    {
        var result = 0;
        for (var idx = 0; idx < lights.length(); idx++)
        {
            result = result << 1;
            if (lights.charAt(idx) == '#')
                result += 1;
        }
        return result;
    }
    
    static int numsAsNumber(int[] nums)
    {
        var result = 0;
        for (var n : nums)
        {
            result |= 1 << (n - 1);
        }
        return result;
    }
    
    static int[] parseIntArray(String line)
    {
        var parts = split(line, ",");
        var result = new int[parts.size()];
        for (var idx = 0; idx < parts.size(); idx++)
        {
            result[idx] = parseInt(parts.get(idx).trim());
        }
        return result;
    }

    static ArrayList<Integer> parseIntArrayList(String line)
    {
        var parts = split(line, ",");
        var result = new ArrayList<Integer>(parts.size());
        for (var p : parts)
        {
            result.add(parseInt(p.trim()));
        }
        return result;
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
       
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            var idx0 = line.indexOf('[');
            var idx1 = line.indexOf(']');
            var idx2 = line.indexOf('{');
            var idx3 = line.indexOf('}');
            var lights = line.substring(idx0+1, idx1);
            var buttonStrings = split(line.substring(idx1+1, idx2-1),"(");
            var joltages = line.substring(idx2+1, idx3);
            var buttons = new ArrayList<Integer>();
            for (var button : Query.wrap(buttonStrings)
                .select(b -> b.trim())
                .select(b -> b.substring(0, b.length()-1)).where(s -> s.length() > 0))
            {
                buttons.add(numsAsNumber(parseIntArray(button)));
            }
            var machine = new Machine(lights, buttons, parseIntArrayList(joltages));
            var pathLength = machine.stepsToTarget();
            System.out.println(pathLength);
            result += pathLength;
        }
        System.out.println(result);
        
    }
}
