package day21;

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

    interface Value
    {
        long getValue();
        Value getHumanTree();
        Value getNonHumanTree();
        String getName();
    }
    
    static class Constant implements Value
    {
        Long value;
        String name;
        
        public Constant(String name, Long value)
        {
            super();
            this.value = value;
            this.name = name;
        }


        @Override
        public long getValue()
        {
            return value;
        }


        @Override
        public Value getHumanTree()
        {
            if (name.equals("humn"))
                return this;
            return null;
        }


        @Override
        public Value getNonHumanTree()
        {
            return null;
        }


        @Override
        public String getName()
        {
            return name;
        }
    }

    static class Operation implements Value
    {
        Value op1;
        Value op2;
        char op;
        String name;
        int humanTree = -1;
        
        
        public Operation(String name, Value op1, Value op2, char op)
        {
            super();
            this.op1 = op1;
            this.op2 = op2;
            this.op = op;
            this.name = name;
        }



        @Override
        public long getValue()
        {
            var v1 = op1.getValue();
            var v2 = op2.getValue();
            var result = switch (op)
                {
                    case '+' -> v1 + v2;
                    case '-' -> v1 - v2;
                    case '*' -> v1 * v2;
                    case '/' -> v1 / v2;
                    default -> throw new IllegalStateException();
                };
              System.out.printf("%s: %s %s %s = %s\n", name, v1, op, v2, result);
                
            return result;
        }

        @Override
        public Value getHumanTree()
        {
            if (humanTree < 0)
            {
                humanTree = 0;
                var result = op1.getHumanTree();
                if (result != null)
                {
                    humanTree = 1;
                }
                result = op2.getHumanTree();
                if (result != null)
                {
                    humanTree = 2;
                }
            }
            return switch (humanTree)
                {
                case 0 -> null;
                case 1 -> op1;
                case 2 -> op2;
                default -> throw new IllegalStateException();
                };
        }


        @Override
        public Value getNonHumanTree()
        {
            var result = op1.getHumanTree();
            if (result != null)
                return op2;
            result = op2.getHumanTree();
            if (result != null)
                return null;
            return null;
        }



        @Override
        public String getName()
        {
            return name;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
        HashMap<String, String> exprs = new HashMap<>();
        HashMap<String, Value> computers = new HashMap<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            var parts = line.split(":");
            exprs.put(parts[0].trim(), parts[1].trim());
        }
        Value value = parse("root", exprs, computers);

        String humanName = "humn";
        
        for (var c : computers.values())
        {
            if (! (c instanceof Operation))
                continue;
            var op = (Operation)c;
            if (op.name.equals(humanName))
                continue;
            if (op.op1.getHumanTree() == null)
            {
                var v1 = op.op1.getValue();
                op.op1 = new Constant(op.op1.getName(), v1);
            }
            if (op.op2.getHumanTree() == null)
            {
                var v2 = op.op2.getValue();
                op.op2 = new Constant(op.op2.getName(), v2);
            }
        }
        
        Value root = computers.get("root");
        var nonHumanTree = root.getNonHumanTree();
        var rootExpectedValue = nonHumanTree.getValue();
        System.out.println(rootExpectedValue);
        
        var humanTree = root.getHumanTree();
        var human = (Constant)computers.get(humanName);
        traverse(humanTree.getName(), rootExpectedValue, computers);

//        var base = 1;
//        for (long v = base; v < base + 1; v++)
//        {
//            v = 302;
//            human.value = v;
//            System.out.println("Human");
//            var hv = humanTree.getValue();
//            System.out.printf("v: %s hv: %s\n", v, hv);
//            if (hv == rootExpectedValue)
//            {
//                System.out.println("Correct: "+v);
//                break;
//                
//            }
//        }
        System.out.println("Not found");
        
    }
    
    static void traverse(String name, long expectedValue, HashMap<String, Value> computers)
    {
//        System.out.printf("Expect %s at %s\n", expectedValue, name);
        var c = computers.get(name);
        if (c instanceof Constant)
        {
            if (c.getName().equals("humn"))
            {
                System.out.println("Found: "+expectedValue);
            }
        }
        else
        {
            var op = (Operation)c;
            var op1 = op.op1;
            var op2 = op.op2;
            switch (op.op)
            {
                case '+':
                {
                    if (op1.getHumanTree() != null)
                        traverse(op1.getName(), expectedValue - op2.getValue(), computers);
                    if (op2.getHumanTree() != null)
                        traverse(op2.getName(), expectedValue - op1.getValue(), computers);
                    break;
                }
                case '-':
                {
                    if (op1.getHumanTree() != null)
                        traverse(op1.getName(), expectedValue + op2.getValue(), computers);
                    if (op2.getHumanTree() != null)
                        traverse(op2.getName(), op1.getValue() - expectedValue, computers);
                    break;
                }
                case '*':
                {
                    if (op1.getHumanTree() != null)
                    {
                        if (expectedValue % op2.getValue() == 0)
                            traverse(op1.getName(), expectedValue / op2.getValue(), computers);
                    }
                    if (op2.getHumanTree() != null)
                    {
                        if (expectedValue % op1.getValue() == 0)
                            traverse(op2.getName(), expectedValue / op1.getValue(), computers);
                    }
                    break;
                }
                case '/':
                {
                    if (op1.getHumanTree() != null)
                    {
                        var v2 = op2.getValue();
                        for (var rem = 0; rem < v2; rem++)
                        {
                            traverse(op1.getName(), expectedValue * v2 + rem, computers);
                        }
                    }
//                    if (op2.getHumanTree() != null)
//                    {
//                        if (expectedValue % op1.getValue() == 0)
//                            traverse(op2.getName(), expectedValue / op1.getValue(), computers);
//                    }
                    break;
                }
            }
        }
    }
    
    static Value parse(String name, HashMap<String, String> exprs, HashMap<String, Value> computers)
    {
        var expr = exprs.get(name);
        if (expr == null)
            throw new IllegalStateException();
        var complex = parse("(\\w+) ([+\\-*/]) (\\w+)", expr);
        Value result;
        if (complex != null)
        {
            result = new Operation(name, parse(complex[1], exprs, computers), parse(complex[3], exprs, computers), complex[2].charAt(0));
        }
        else
        {
            result = new Constant(name, parseLong(expr));
        }
        computers.put(name, result);
        return result;
    }
}
