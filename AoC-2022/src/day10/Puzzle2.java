package day10;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;

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
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt");
//        LinesGroup lines = readAllLinesNonEmpty("input1_test.txt");
        ArrayList<Long> stepValue = new ArrayList<>();
        long currentValue = 1;
        int result = 0;
        for (String line : lines)
        {
            var parts = line.split(" ");
            switch (parts[0])
            {
                case "addx":
                {
                    stepValue.add(currentValue);
                    stepValue.add(currentValue);
                    currentValue += parseInt(parts[1]);
                    System.out.println("Step: " + stepValue.size() + " value: " + currentValue);
                    break;
                }
                case "noop":
                {
                    stepValue.add(currentValue);
                    break;
                }
            }
        }
//        var sum = 0l;
//        for (var idx = 20; idx < stepValue.size(); idx+= 40)
//        {
//            var stepVal = stepValue.get(idx-1);
//            sum += idx * stepVal; 
//        }
        
//        System.out.println(sum);
        Board2D board = new Board2D(40, 6);
        for (int step = 1; step <= stepValue.size(); step++)
        {
            var x = (step - 1) % 40;
            var y = (step - 1) / 40;
            long stepVal = stepValue.get(step-1);
            char c;
            if (stepVal - 1 <= x && stepVal + 1 >= x)
            {
                c = '#';
            }
            else
                c = '.';
            board.setAtXY(x, y, c);
        }
        
        board.printAsStrings(System.out);
    }
}
