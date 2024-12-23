package day11;

import java.util.ArrayList;
import java.util.Arrays;

import common.LinesGroup;
import common.PuzzleCommon;
import common.queries.Query;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public Monkey parseGroup(LinesGroup group)
    {
        var monkey = new Monkey();
        monkey.items = Query.wrap(group.get(1).split(": |, "))
            .skip(1)
            .select(item -> parseInt(item)).toList();
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
        ArrayList<Integer> items = new ArrayList<>();
        OpType opType;
        int operand;
        int div = 1;
        int ifTrue;
        int ifFalse;
        
        int worryLevelForItem(int idx)
        {
            int level = items.get(idx);
            level = switch (opType)
                {
                    case Add -> level + operand;
                    case Mul -> level * operand;
                    case Sq -> level * level;
                };
//            double l3 = level / 3.0;
//            level = (int)Math.round(l3);
            level = level / 3;
            return level;
        }

        int dispatchTo(int level)
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
        int result = 0;
        for (LinesGroup group : groups)
        {
            monkeys.add(group.processGroup(this::parseGroup));
        }
        
        System.out.println("Initial stat");
        printMonkeys(monkeys);
        
        int[] inspected = new int[monkeys.size()];
        
        for (int round = 1; round <= 20; round++)
        {
            for (int mIdx = 0; mIdx < monkeys.size(); mIdx++)
            {
                var monkey = monkeys.get(mIdx);
                inspected[mIdx] += monkey.items.size();
                for (int itemIdx = 0; itemIdx < monkey.items.size(); itemIdx++)
                {
                    var level = monkey.worryLevelForItem(itemIdx);
                    var dispatchTo = monkey.dispatchTo(level);
                    monkeys.get(dispatchTo).items.add(level);
                }
                monkey.items.clear();
            }
            
            System.out.println("Round "+round);
            printMonkeys(monkeys);
        }
        
        Arrays.sort(inspected);
        result = inspected[inspected.length-1] * inspected[inspected.length-2]; 
        
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
