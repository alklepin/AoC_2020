package day01;

import java.util.HashMap;
import java.util.regex.Pattern;

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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input2_test.txt";
//        var inputFile = "input3_test.txt";
        
//        ArrayList<LinesGroup> groups = readAllLineGroups(inputFile);
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
//        var pattern = Pattern.compile("zero|one|two|three|four|five|six|seven|eight|nine|1|2|3|4|5|6|7|8|9|0");
        var pattern = Pattern.compile("zero|one|two|three|four|five|six|seven|eight|nine|1|2|3|4|5|6|7|8|9|0");
        for (String line : lines)
        {
            var matcher = pattern.matcher(line);
            var first = -1;
            var last = -1;
            var idx = 0;
            while (matcher.find(idx))
            {
                var val = parseNumText(matcher.group());
                if (first == -1)
                    first = val;
                last = val;
                idx = matcher.start()+1;
            }
//            var tokens = parse(".*(?:(zero|one|two|three|four|five|six|seven|eight|nine).*)", line);
//            
//            var value = parseNumText(tokens[0]) * 10 + parseNumText(tokens[tokens.length-1]);
            var value = first * 10 + last;
            System.out.println("line: " + line + " value: "+value);
            result += value;
        }
        System.out.println(result);
        
    }
    
    public static int parseNumText(String line)
    {
        return switch (line)
        {
            case "0" -> 0;
            case "1" -> 1;
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            case "9" -> 9;
            case "zero" -> 0;
            case "one" -> 1;
            case "two" -> 2;
            case "three" -> 3;
            case "four" -> 4;
            case "five" -> 5;
            case "six" -> 6;
            case "seven" -> 7;
            case "eight" -> 8;
            case "nine" -> 9;
            default -> throw new IllegalStateException();
        };
    }
}
