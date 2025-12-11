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

public class Puzzle2_notBad extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_notBad().solve();
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
            var rows = joltages.size() + 3; // reserve 4 extra rows for synthetic equations
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
            var diagLength = Math.min(rows, cols-1);
            for (var idx = 0; idx < diagLength; idx++)
            {
                if (same(matrixSaved.get(idx, idx), 0))
                    posToTry.add(idx);
            }
            if (posToTry.size() > 4)
            {
//                throw new IllegalStateException();
                System.out.println("Skipped!!!!");
                return 0;
            }
            
            var minSteps = Integer.MAX_VALUE;
            var lim1 = posToTry.size() > 0 ? maxJoltage : 1;
            for (var v1 = 0; v1 < lim1; v1++)
            {
                var lim2 = posToTry.size() > 1 ? maxJoltage : 1;
                for (var v2 = 0; v2 < lim2; v2++)
                {
                    var lim3 = posToTry.size() > 2 ? maxJoltage : 1;
                    for (var v3 = 0; v3 < lim3; v3++)
                    {
                        var lim4 = posToTry.size() > 3 ? maxJoltage : 1;
                        for (var v4 = 0; v4 < lim4; v4++)
                        {
                            if (v1 == 3 && v2 == 1 && v3 == 2)
                            {
                                int test = 12;
                            }
                            
                            matrix = matrixSaved.clone();
                            if (posToTry.size() > 0)
                            {
                                matrix.set(matrix.rows()-1, posToTry.get(0), 1);
                                matrix.inc(matrix.rows()-1, cols-1, v1);
                            }
                            if (posToTry.size() > 1)
                            {
                                matrix.set(matrix.rows()-2, posToTry.get(1), 1);
                                matrix.inc(matrix.rows()-2, cols-1, v2);
                            }
                            if (posToTry.size() > 2)
                            {
                                matrix.set(matrix.rows()-3, posToTry.get(2), 1);
                                matrix.inc(matrix.rows()-3, cols-1, v3);
                            }
                            if (posToTry.size() > 3)
                            {
                                matrix.set(matrix.rows()-4, posToTry.get(3), 1);
                                matrix.inc(matrix.rows()-4, cols-1, v4);
                            }
//                            matrix.print(System.out);
//                            System.out.println("==before=");
                            matrix.gauss();
//                            matrix.print(System.out);
//                            System.out.println("==after=");
                            
                            var steps = decodeMatrix(matrix);
                            if (steps >= 0 && steps < minSteps)
                            {
                                minSteps = steps;
                                
                                matrix.print(System.out);
                                System.out.println("==after=");
                                System.out.println("Steps: "+steps );
                                for (var r = 0; r < matrix.rows(); r++)
                                {
                                    System.out.print(String.format("%5.1f", matrix.get(r, matrix.columns()-1)));
                                }
                                System.out.println();
                            }
                            System.out.println("Steps: "+steps );
                            
                        }
                    }
                }
                
            }
            return minSteps;
        }
        
        boolean same(double a, double b)
        {
            return Math.abs(a-b) < 1e-13;
        }
        
        int decodeMatrix(MatrixD matrix)
        {
            var sum = 0;
            var diag = Math.min(matrix.rows(),  matrix.columns()-1);
            var lastCol = matrix.columns() - 1;
            var rows = matrix.rows();
            for (var r = 0; r < diag; r++)
            {
                var x = matrix.get(r,  r);
                var v = matrix.get(r, lastCol);
//                if (values[r] != -1 && !same(values[r], v))
//                    return -1;
                if (same(x, 0) && !same(v, 0))
                    return -1;
                if (!same(Math.round(v), v))
                    return -1;
                if (Math.round(v) < 0)
                    return -1;
                sum += Math.round(v);
            }
            for (var r = diag; r < rows; r++)
            {
                for (var c = 0; c < matrix.columns(); c++)
                {
                    if (!same(matrix.get(r, c), 0))
                        return -1;
                }
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
//        var inputFile = "input2_test.txt";
//        var inputFile = "input2_test2.txt";
        var inputFile = "input2_test3.txt";
       
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
            System.out.println(line);
            var machine = new Machine(lights, buttons, parseIntArrayList(joltages));
            var pathLength = machine.stepsToTarget();
            System.out.println(pathLength);
            result += pathLength;
            System.out.println("-----------------------");
        }
        System.out.println(result);
        
    }
}
