package day21;

import java.util.HashMap;

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

    interface Value
    {
        long getValue();
    }
    
    static class Constant implements Value
    {
        Long value;
        
        
        public Constant(Long value)
        {
            super();
            this.value = value;
        }


        @Override
        public long getValue()
        {
            return value;
        }
    }

    static class Operation implements Value
    {
        Value op1;
        Value op2;
        char op;
        
        
        
        public Operation(Value op1, Value op2, char op)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
            this.op = op;
        }



        @Override
        public long getValue()
        {
            return switch (op)
                {
                    case '+' -> op1.getValue() + op2.getValue();
                    case '-' -> op1.getValue() - op2.getValue();
                    case '*' -> op1.getValue() * op2.getValue();
                    case '/' -> op1.getValue() / op2.getValue();
                    default -> throw new IllegalStateException();
                };
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
        HashMap<String, String> exprs = new HashMap<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            var parts = line.split(":");
            exprs.put(parts[0].trim(), parts[1].trim());
        }
        Value value = parse("root", exprs);
        System.out.println(value.getValue());
        
    }
    
    static Value parse(String name, HashMap<String, String> exprs)
    {
        var expr = exprs.get(name);
        if (expr == null)
            throw new IllegalStateException();
        var complex = parse("(\\w+) ([+\\-*/]) (\\w+)", expr);
        if (complex != null)
        {
            return new Operation(parse(complex[1], exprs), parse(complex[3], exprs), complex[2].charAt(0));
        }
        return new Constant(parseLong(expr));
    }
}
