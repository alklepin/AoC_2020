package day10;

import java.util.ArrayList;
import java.util.Arrays;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.Graph;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2_bad extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_bad().solve();
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
        ArrayList<ArrayList<Integer>> buttonLists;
        ArrayList<Integer> joltages;

        public Machine(String lights, ArrayList<ArrayList<Integer>> buttonLists, ArrayList<Integer> joltages)
        {
            sTargetState = lights;
            targetState = lightsAsANumber(lights);
            this.buttonLists = buttonLists;
            this.buttons = Query.wrap(buttonLists).select(list -> numsAsNumber(list)).toList();
            this.joltages = joltages;
        }
        
        static class State implements Comparable<State>
        {
            int [] values;
            
            public State(int length)
            {
                values = new int[length];
            }

            private State(int [] values)
            {
                this.values = values;
            }
            
            public State apply(ArrayList<Integer> button)
            {
                var newState = new int[values.length];
                System.arraycopy(values, 0, newState, 0, values.length);
                for (var v : button)
                {
                    newState[v]++;
                }
                return new State(newState);
            }

            @Override
            public int hashCode()
            {
                final int prime = 31;
                int result = 1;
                result = prime * result + Arrays.hashCode(values);
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
                State other = (State)obj;
                return Arrays.equals(values, other.values);
            }

            @Override
            public String toString()
            {
                return "State [values=" + Arrays.toString(values) + "]";
            }

            @Override
            public int compareTo(State o)
            {
                return Arrays.compare(values, o.values);
            }
        }
        
        static int sum(int[] list)
        {
            var result = 0;
            for (var v : list)
                result += v;
            return result;
        }
        
        long stepsToTarget()
        {
            var start = new State(joltages.size());
            
            var end = new State(toIntArray(joltages));
            var result = ImplicitGraph.BFS(start, end, node -> {
                System.out.println("" + node+ " -> " + sum(node.values));
                return Query.wrap(buttonLists)
                    .select(b -> node.apply(b))
                    .where(n -> n.compareTo(end) <= 0);
            });
            var path = result.getPath();
//            if (path != null)
//            {
//                for (var p : path)
//                {
//                    System.out.println(p);
//                }
//            }
//            else
//            {
//                System.out.println("Something wrong");
//            }
//            System.out.println("----");
            return result.getPath().size()-1;
        }
    }
    
    static int[] toIntArray(ArrayList<Integer> list)
    {
        var result = new int[list.size()];
        var idx = 0;
        for (var val : list)
        {
            result[idx++] = val;
        }
        return result;
    }
    
    static int lightsAsANumber(String lights)
    {
        var result = 0;
        for (var idx = 0; idx < lights.length(); idx++)
        {
            if (lights.charAt(idx) == '#')
                result += 1 << idx;
        }
        return result;
    }
    
    static int numsAsNumber(ArrayList<Integer> nums)
    {
        var result = 0;
        for (var n : nums)
        {
            result |= 1 << n;
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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
       
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
            var buttons = new ArrayList<ArrayList<Integer>>();
            for (var button : Query.wrap(buttonStrings)
                .select(b -> b.trim())
                .where(s -> s.length() > 0)
                .select(b -> b.substring(0, b.length()-1))
                .where(s -> s.length() > 0))
            {
                buttons.add(parseIntArrayList(button));
            }
            var machine = new Machine(lights, buttons, parseIntArrayList(joltages));
            var pathLength = machine.stepsToTarget();
            //System.out.println(pathLength);
            result += pathLength;
            System.out.println(line);
        }
        System.out.println(result);
        
    }
}
