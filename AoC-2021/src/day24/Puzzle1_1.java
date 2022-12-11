package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1_1().solve();
    }
    
    
    public void solve()
        throws Exception
    {
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input1_1.txt.1");
        int result = 0;
        ArrayList<Command> program = new ArrayList<>();
        for (String line : lines)
        {
            if (line.startsWith("#"))
                continue;
            program.add(Command.parse(line));
        }
  
        for (int digit = 9; digit >= 1; digit--)
        {
            tryExecute(program, 1, digit);
        }
    }
    
    public boolean tryExecute(ArrayList<Command> program, int level, long prefix)
    {
        if (level < 14)
        {
            Value value = new Value();
            digitsProvider = new DigitsProvider(prefix);
            int curLevel = 0;
            for (var cmd : program)
            {
                if (cmd instanceof InputCmd)
                {
                    curLevel++;
                    if (curLevel >= level)
                    {
                        if (value.get(2) > 26*26)
                            return false;
                        break;
                    }
                }
                value = cmd.apply(value);
            }
            for (int digit = 9; digit >= 0; digit--)
            {
                if (tryExecute(program, level+1, prefix * 10 + digit))
                    return true;
            }
        }
        else
        {
            Value value = new Value();
            digitsProvider = new DigitsProvider(prefix);
            for (var cmd : program)
            {
                value = cmd.apply(value);
            }
            if (value.get(2) == 0)
            {
                System.out.println(prefix);
                return true;
            }
        }
        return false;
    }


    static DigitsProvider digitsProvider = new DigitsProvider("00000000000000");
    
    public static int nextDigit()
    {
        return digitsProvider.nextDigit();
    }
    
    public static class DigitsProvider
    {
        public String digits = "13579246899999";
        public int pos = 0;
        
        public DigitsProvider(long value)
        {
            this.digits = String.valueOf(value); 
        }
        
        public DigitsProvider(String digits)
        {
            this.digits = digits; 
        }
        
        public int nextDigit()
        {
            if (pos < digits.length())
            {
                return digits.charAt(pos++) - '0';
            }
            throw new IllegalStateException();
        }
    }
    
    
    public static int index(String s)
    {
        switch (s)
        {
            case "x":
            {
                return 0;
            }
            case "y":
            {
                return 1;
            }
            case "z":
            {
                return 2;
            }
            case "w":
            {
                return 3;
            }
            default:
            {
                return -1;
            }
        }
    }
    
    static String [] opNames = new String[]{"x", "y", "z", "w"};
    public static String opName(int i)
    {
        return opNames[i];
    }
    
    public static interface Command
    {
        Value apply(Value v);
        
        static Command parse(String command)
        {
            var parts = command.split("\\s+");
            var cmd = parts[0];
            var op1str = parts[1];
            var op2str = parts.length > 2 ? parts[2] : "0";
            var op1idx = index(op1str);
            var op2idx = index(op2str);
            var op2value = op2idx < 0 ? parseInt(op2str) : 0;
            switch (cmd)
            {
                case "inp":
                {
                    return new InputCmd(op1idx);
                }
                case "add":
                {
                    return op2idx >= 0 ? new AddRegCmd(op1idx, op2idx) : new AddValCmd(op1idx, op2value);
                }
                case "mul":
                {
                    return op2idx >= 0 ? new MulRegCmd(op1idx, op2idx) : new MulValCmd(op1idx, op2value);
                }
                case "div":
                {
                    return op2idx >= 0 ? new DivRegCmd(op1idx, op2idx) : new DivValCmd(op1idx, op2value);
                }
                case "mod":
                {
                    return op2idx >= 0 ? new ModRegCmd(op1idx, op2idx) : new ModValCmd(op1idx, op2value);
                }
                case "eql":
                {
                    return op2idx >= 0 ? new EqlRegCmd(op1idx, op2idx) : new EqlValCmd(op1idx, op2value);
                }
            }
            throw new IllegalStateException();
        }
    }

    public static class InputCmd implements Command
    {
        private int op1;

        public InputCmd(int op1)
        {
            super();
            this.op1 = op1;
        }
        
        @Override
        public Value apply(Value v)
        {
            var result = new Value(v);
            result.set(op1, nextDigit());
            return result;
        }
        
        public String toString()
        {
            return "inp " + opName(op1);
        }
    }
    
    public static class AddRegCmd implements Command
    {
        private int op1;
        private int op2;
        
        public AddRegCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.add(op1, op2);
        }

        public String toString()
        {
            return "add " + opName(op1) + " " + opName(op2);
        }
    }
    
    public static class AddValCmd implements Command
    {
        private int op1;
        private int op2;
        
        public AddValCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.addValue(op1, op2);
        }

        public String toString()
        {
            return "add " + opName(op1) + " " + op2;
        }
    }
    
    public static class MulRegCmd implements Command
    {
        private int op1;
        private int op2;
        
        public MulRegCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.mul(op1, op2);
        }

        public String toString()
        {
            return "mul " + opName(op1) + " " + opName(op2);
        }
    }

    public static class MulValCmd implements Command
    {
        private int op1;
        private int op2;
        
        public MulValCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }

        @Override
        public Value apply(Value v)
        {
            return v.mulValue(op1, op2);
        }
        
        public String toString()
        {
            return "mul " + opName(op1) + " " + op2;
        }
    }
    
    public static class DivRegCmd implements Command
    {
        private int op1;
        private int op2;
        
        public DivRegCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.div(op1, op2);
        }
        
        public String toString()
        {
            return "div " + opName(op1) + " " + opName(op2);
        }
    }
    
    public static class DivValCmd implements Command
    {
        private int op1;
        private int op2;
        
        public DivValCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.divValue(op1, op2);
        }
        
        public String toString()
        {
            return "div " + opName(op1) + " " + op2;
        }
    }
    
    public static class ModRegCmd implements Command
    {
        private int op1;
        private int op2;
        
        public ModRegCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.mod(op1, op2);
        }
        
        public String toString()
        {
            return "mod " + opName(op1) + " " + opName(op2);
        }
    }
    
    public static class ModValCmd implements Command
    {
        private int op1;
        private int op2;
        
        public ModValCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.modValue(op1, op2);
        }
        
        public String toString()
        {
            return "mod " + opName(op1) + " " + op2;
        }
    }
    
    public static class EqlRegCmd implements Command
    {
        private int op1;
        private int op2;
        
        public EqlRegCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }
        
        @Override
        public Value apply(Value v)
        {
            return v.eql(op1, op2);
        }
        
        public String toString()
        {
            return "eql " + opName(op1) + " " + opName(op2);
        }
    }

    public static class EqlValCmd implements Command
    {
        private int op1;
        private int op2;
        
        public EqlValCmd(int op1, int op2)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
        }

        @Override
        public Value apply(Value v)
        {
            return v.eqlValue(op1, op2);
        }
        
        public String toString()
        {
            return "eql " + opName(op1) + " " + op2;
        }
    }

    public static class Value
    {
        private int step;
        private long[] data;
        
        public Value()
        {
            data = new long[4];
        }

        public Value(Value other)
        {
            data = other.data.clone();
        }
        
        public void set(int idx, long value)
        {
            data[idx] = value;
        }

        public long get(int idx)
        {
            return data[idx];
        }
        
        public Value add(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) + result.get(arg2));
            return result;
        }

        public Value mul(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) * result.get(arg2));
            return result;
        }

        public Value div(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) / result.get(arg2));
            return result;
        }

        public Value mod(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) % result.get(arg2));
            return result;
        }

        public Value eql(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) == result.get(arg2) ? 1 : 0);
            return result;
        }
        
        public Value addValue(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) + arg2);
            return result;
        }

        public Value mulValue(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) * arg2);
            return result;
        }

        public Value divValue(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) / arg2);
            return result;
        }

        public Value modValue(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) % arg2);
            return result;
        }

        public Value eqlValue(int arg1, int arg2)
        {
            var result = new Value(this);
            result.set(arg1, result.get(arg1) == arg2 ? 1 : 0);
            return result;
        }
        
        public String toString()
        {
            return String.format("x = %s, y = %s, z = %s, w = %s", data[0], data[1], data[2], data[3]);
        }
    }
}
