package day17;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import common.LineParser;
import common.PuzzleCommon;
import day17.Puzzle1.Machine;

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
        var expectedOutput = buf.toString();

//        var machine = new Machine(regA, regB, regC, program);
//        machine.execute();
//        System.out.println(machine.getOutput());
        
        var queue = new LinkedList<BigInteger>();
        queue.add(BigInteger.valueOf(4));
        while (queue.size() > 0)
        {
            var cur = queue.poll();
            cur = cur.multiply(BigInteger.valueOf(8));
            for (var delta = 0; delta < 8; delta++)
            {
                var next = cur.add(BigInteger.valueOf(delta));
                var machine = new Machine(0, regB, regC, program);
                machine.setRegA(next);
                machine.execute();
                var output = machine.getOutput();
                if (expectedOutput.endsWith(output))
                {
                    queue.add(next);
//                    System.out.println("Value: " +next);
//                    System.out.println("Output: " +output);
                }
                if (expectedOutput.equals(output))
                {
                    System.out.println("Value: " +next);
                    queue.clear();
                    break;
                }
            }
        }
        
//        var expectedOutput = "5,0,3,3,0,";
//        var expectedOutput = "0,";
//        for (long a = 0; a < 64*64*8; a++)
//        {
//            machine = new Machine(a, regB, regC, program);
//            machine.execute();
//            var output = machine.getOutput();
//            if (output.equals(expectedOutput))
//            {
//                System.out.println(a);
//                System.out.println(otOctString(a));
//                System.out.println(machine.getOutput());
//            }
//        }
        
        
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
        private BigInteger regA;
        private BigInteger regB;
        private BigInteger regC;
        
        private int[] program;
        
        private StringBuilder output;
        
        private int ip = 0;
        
        public Machine(long regA, long regB, long regC, ArrayList<Integer> program)
        {
            this.regA = BigInteger.valueOf(regA); 
            this.regB = BigInteger.valueOf(regB); 
            this.regC = BigInteger.valueOf(regC); 
            this.program = new int[program.size()];
            for (var idx = 0; idx < this.program.length; idx++)
            {
                this.program[idx] = program.get(idx);
            }
            output = new StringBuilder();
        }
        
        public void setRegA(BigInteger v)
        {
            regA = v;
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
//                        System.out.println("Source:"+regA);
//                        System.out.println("Divisor:"+divisor);
                        regA = regA.divide(BigInteger.valueOf(1 << divisor.longValue()));
//                        System.out.println("Result:"+regA);
                        ip += 2;
                        break;
                    }
                    case 1: //bxl
                    {
                        regB = regB.xor(BigInteger.valueOf(operand));
                        ip += 2;
                        break;
                    }
                    case 2: //bst
                    {
                        regB = getComboOp(operand).remainder(BigInteger.valueOf(8));
                        ip += 2;
                        break;
                    }
                    case 3: //jnz
                    {
                        if (regA.equals(BigInteger.ZERO))
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
//                        System.out.println("xor B: "+regB);
//                        System.out.println("xor C: "+regC);
                        regB = regB.xor(regC);
//                        System.out.println("xor res: "+regB);
                        ip += 2;
                        break;
                    }
                    case 5: //out
                    {
                        //System.out.println("out: "+getComboOp(operand).longValue() % 8);
                        output.append(getComboOp(operand).remainder(BigInteger.valueOf(8))).append(',');
                        ip += 2;
                        break;
                    }
                    case 6: //bdv
                    {
                        var divisor = getComboOp(operand);
                        regB = regA.divide(BigInteger.valueOf(1 << divisor.longValue()));
                        ip += 2;
                        break;
                    }
                    case 7: //cdv
                    {
                        var divisor = getComboOp(operand);
//                        System.out.println("Source:"+regA);
//                        System.out.println("Divisor:"+divisor);
                        regC = regA.divide(BigInteger.valueOf(1 << divisor.longValue()));
//                        System.out.println("Result:"+regC);
                        ip += 2;
                        break;
                    }
                }
            }
        }
        
        private BigInteger getComboOp(int operand)
        {
            return switch (operand)
                {
                    case 0 -> BigInteger.valueOf(operand);
                    case 1 -> BigInteger.valueOf(operand);
                    case 2 -> BigInteger.valueOf(operand);
                    case 3 -> BigInteger.valueOf(operand);
                    case 4 -> regA;
                    case 5 -> regB;
                    case 6 -> regC;
                    case 7 -> throw new IllegalStateException();
                    default -> throw new IllegalStateException();
                };
        }
        
    }
}
