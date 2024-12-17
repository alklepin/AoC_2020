package day17;

import java.util.ArrayList;
import java.util.Collections;

import common.LineParser;
import common.PuzzleCommon;

public class Puzzle2_2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_2().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var groups = readAllLineGroups(inputFile);
        
        long regA = parseInt(groups.get(0).get(0).split(" ")[2]);
        long regB = parseInt(groups.get(0).get(1).split(" ")[2]);
        long regC = parseInt(groups.get(0).get(2).split(" ")[2]);
        
        var program = new LineParser(groups.get(1).get(0).split(" ")[1]).listOfInts();
        var buf = new StringBuilder();
        for (var i : program)
        {
            buf.append(i).append(',');
        }
        //var expectedOutput = buf.toString();
        
        var expectedOutput = "5,0,3,3,0,";
//        var expectedOutput = "0,";
        for (long a = 0; a < 64*64*8; a++)
        {
            var machine = new Machine(a, regB, regC, program);
            machine.execute();
            var output = machine.getOutput();
            if (output.equals(expectedOutput))
            {
                System.out.println(a);
                System.out.println(otOctString(a));
                System.out.println(machine.getOutput());
            }
        }
        
        
        //        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
    
    private static String otOctString(long v)
    {
        ArrayList<Integer> digits = new ArrayList<>();
        while (v > 0)
        {
            digits.add((int)(v % 8));
            v = v / 8;
        }
        Collections.reverse(digits);
        var buf = new StringBuilder();
        for (var d : digits)
        {
            buf.append(d).append(',');
        }
        return buf.toString();
    }
    
    public static class Machine
    {
        private long regA;
        private long regB;
        private long regC;
        
        private int[] program;
        
        private StringBuilder output;
        
        private int ip = 0;
        
        public Machine(long regA, long regB, long regC, ArrayList<Integer> program)
        {
            this.regA = regA; 
            this.regB = regB; 
            this.regC = regC; 
            this.program = new int[program.size()];
            for (var idx = 0; idx < this.program.length; idx++)
            {
                this.program[idx] = program.get(idx);
            }
            output = new StringBuilder();
        }
        
        public String getOutput()
        {
            return output.toString();
        }
        
        public void execute()
        {
            while (ip < program.length)
            {
                var opcode = program[ip];
                var operand = program[ip+1];
                switch (opcode)
                {
                    case 0: //adv
                    {
                        var divisor = getComboOp(operand);
                        regA = regA >> divisor;
                        ip += 2;
                        break;
                    }
                    case 1: //bxl
                    {
                        regB = regB ^ operand;
                        ip += 2;
                        break;
                    }
                    case 2: //bst
                    {
                        regB = getComboOp(operand) % 8;
                        ip += 2;
                        break;
                    }
                    case 3: //jnz
                    {
                        if (regA == 0)
                        {
                            ip += 2;
                        }
                        else
                        {
                            ip = operand;
                        }
                        break;
                    }
                    case 4: //bxc
                    {
                        regB = regB ^ regC;
                        ip += 2;
                        break;
                    }
                    case 5: //out
                    {
                        output.append(getComboOp(operand) % 8).append(',');
                        ip += 2;
                        break;
                    }
                    case 6: //bdv
                    {
                        var divisor = getComboOp(operand);
                        regB = regA >> divisor;
                        ip += 2;
                        break;
                    }
                    case 7: //cdv
                    {
                        var divisor = getComboOp(operand);
                        regC = regA >> divisor;
                        ip += 2;
                        break;
                    }
                }
            }
        }
        
        private long getComboOp(int operand)
        {
            return switch (operand)
                {
                    case 0 -> operand;
                    case 1 -> operand;
                    case 2 -> operand;
                    case 3 -> operand;
                    case 4 -> regA;
                    case 5 -> regB;
                    case 6 -> regC;
                    case 7 -> throw new IllegalStateException();
                    default -> throw new IllegalStateException();
                };
        }
        
    }
}
