package day11;

import java.util.ArrayList;
import java.util.Arrays;

import common.LinesGroup;
import common.PuzzleCommon;
import common.Strings;
import common.queries.Query;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    public Monkey parseGroup(LinesGroup group)
    {
        var monkey = new Monkey();
        monkey.items = Strings.tokenize(group.line(1), ": |, ")
            .skip(1)
            .select(item -> Long.valueOf(parseInt(item))).toList();
        var opLine = group.get(2);
        var opLineParts = opLine.split(" ");
        if (opLine.indexOf("+") >= 0)
        {
            monkey.operand = parseInt(opLineParts[5]);
            monkey.opType = OpType.Add;
        }
        else
        {
            var operand = opLineParts[5];
            if (operand.equals("old"))
            {
                monkey.opType = OpType.Sq;
                monkey.operand = 1;
            }
            else
            {
                monkey.operand = parseInt(operand);
                monkey.opType = OpType.Mul;
            }
        }
        monkey.div = parseInt(group.get(3).split(" ")[3]);
        monkey.ifTrue = parseInt(group.get(4).split(" ")[5]);
        monkey.ifFalse = parseInt(group.get(5).split(" ")[5]);
        
        return monkey;
    }
    
    static enum OpType
    {
        Add, Mul, Sq;
    }
    
    static class Monkey
    {
        ArrayList<Long> items = new ArrayList<>();
        OpType opType;
        int operand;
        int div = 1;
        int ifTrue;
        int ifFalse;
        
        long worryLevelForItem(int idx, long modulo)
        {
            long level = items.get(idx);
            level = switch (opType)
                {
                    case Add -> level + operand;
                    case Mul -> level * operand;
                    case Sq -> level * level;
                };
//            double l3 = level / 3.0;
//            level = (int)Math.round(l3);
            level = level % modulo;
            return level;
        }

        int dispatchTo(long level)
        {
            if (level % div == 0)
                return ifTrue;
            else
                return ifFalse;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
        // System.out.println(groups.size());
        
        ArrayList<Monkey> monkeys = new ArrayList<>();
        for (LinesGroup group : groups)
        {
            monkeys.add(group.processGroup(this::parseGroup));
        }
        
        System.out.println("Initial state");
        printMonkeys(monkeys);

        var modulo = 1;
        for (int mIdx = 0; mIdx < monkeys.size(); mIdx++)
        {
            modulo *= monkeys.get(mIdx).div;
        }
        
        long[] inspected = new long[monkeys.size()];
        
        for (int round = 1; round <= 10000; round++)
        {
            for (int mIdx = 0; mIdx < monkeys.size(); mIdx++)
            {
                var monkey = monkeys.get(mIdx);
                inspected[mIdx] += monkey.items.size();
                for (int itemIdx = 0; itemIdx < monkey.items.size(); itemIdx++)
                {
                    var level = monkey.worryLevelForItem(itemIdx, modulo);
                    var dispatchTo = monkey.dispatchTo(level);
                    monkeys.get(dispatchTo).items.add(level);
                }
                monkey.items.clear();
            }
            
//            System.out.println("Round "+round);
//            printMonkeys(monkeys);
        }

        System.out.println("Final state");
        printMonkeys(monkeys);
        
        Arrays.sort(inspected);
        long result = inspected[inspected.length-1] * inspected[inspected.length-2]; 
        
        System.out.println(result);
    }
    
    public static void printMonkeys(ArrayList<Monkey> monkeys)
    {
        for (int mIdx = 0; mIdx < monkeys.size(); mIdx++)
        {
            System.out.println("Monkey "+mIdx);
            var monkey = monkeys.get(mIdx);
            var list = monkey.items;
            for (var i : list)
            {
                System.out.print(""+i+", ");
            }
            System.out.println();
        }
    }
}
