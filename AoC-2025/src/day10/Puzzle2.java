package day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import common.LinesGroup;
import common.PuzzleCommon;
import common.algebra.MatrixD;
import common.graph.Graph;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
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
        
        
        static int sum(int[] list)
        {
            var result = 0;
            for (var v : list)
                result += v;
            return result;
        }

        long stepsToTarget()
        {
            var cols = buttonLists.size() + 1;
            var rows = Math.max(cols-1, joltages.size());
            int maxJoltage = Collections.max(joltages);
            
            var matrixSrc = new MatrixD(rows, cols);
            for (var col = 0; col < buttonLists.size(); col++)
            {
                for (var v : buttonLists.get(col))
                {
                    matrixSrc.set(v, col, 1);
                }
            }
            for (var row = 0; row < joltages.size(); row++)
            {
                matrixSrc.set(row, buttonLists.size(), joltages.get(row));
            }
            
            matrixSrc.print(System.out);
            System.out.println("===");
            
            var matrix = matrixSrc.clone();
            matrix.gauss();

            matrix.print(System.out);
            System.out.println("===");

//            var diff = buttonLists.size() - joltages.size();
//            if (diff <= 0)
//            {
//                return (long)matrix.sumColumn(cols-1);
//            }
            var matrixSaved = matrix.clone();
            var posToTry = new ArrayList<Integer>();
            for (var idx = 0; idx < cols-1; idx++)
            {
                if (matrixSaved.get(idx, idx) == 0)
                    posToTry.add(idx);
            }
            if (posToTry.size() > 3)
                throw new IllegalStateException();
            var diff = posToTry.size();
            
            var minSteps = Integer.MAX_VALUE;
            var base = joltages.size();
            var values = new int[rows];
            Arrays.fill(values, -1);
            for (var v1 = 0; v1 < maxJoltage; v1++)
            {
                values[posToTry.get(0)] = v1;
                for (var v2 = 0; v2 < maxJoltage; v2++)
                {
                    if (posToTry.size() > 1)
                        values[posToTry.get(1)] = v2;
                    for (var v3 = 0; v3 < maxJoltage; v3++)
                    {
                        if (posToTry.size() > 2)
                            values[posToTry.get(2)] = v3;
                        if (v1 == 3 && v2 == 1 && v3 == 2)
                        {
                            int test = 12;
                        }
                        
                        matrix = matrixSaved.clone();
                        matrix.set(posToTry.get(0), posToTry.get(0), 1);
                        matrix.inc(posToTry.get(0), cols-1, v1);
                        if (diff > 1)
                        {
                            matrix.set(posToTry.get(1), posToTry.get(1), 1);
                            matrix.inc(posToTry.get(1), cols-1, v2);
                        }
                        if (diff > 2)
                        {
                            matrix.set(posToTry.get(2), posToTry.get(2), 1);
                            matrix.inc(posToTry.get(2), cols-1, v3);
                        }
//                        matrix.print(System.out);
//                        System.out.println("==before=");
//                        matrix.gauss();
//                        matrix.print(System.out);
//                        System.out.println("==after=");
                        
                        var steps = decodeMatrix(matrix, values);
                        if (steps >= 0 && steps < minSteps)
                            minSteps = steps;
                    }
                }
                
            }
            return minSteps;
        }
        
        boolean same(double a, double b)
        {
            return Math.abs(a-b) < 1e-10;
        }
        
        int decodeMatrix(MatrixD matrix, int[] values)
        {
            var sum = 0;
            var lastCol = matrix.columns()-1;
            var rows = matrix.rows();
            for (var r = 0; r < lastCol; r++)
            {
                var x = matrix.get(r,  r);
                var v = matrix.get(r, lastCol);
                if (values[r] != -1 && !same(values[r], v))
                    return -1;
                if (x == 0 && !same(v, 0))
                    return -1;
                if (v < 0)
                    return -1;
                sum += v;
            }
            return sum;
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
//        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        var inputFile = "input2_test.txt";
       
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
            System.out.println(pathLength);
            result += pathLength;
            System.out.println(line);
        }
        System.out.println(result);
        
    }
}
