package day02;

import java.util.HashMap;
import java.util.regex.Pattern;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Vect3I;

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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
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
        for (String line : lines)
        {
            var parts = line.split(":");
            var gameInfo = parts[0].trim();
            var gameNum = parseInt(gameInfo.split(" ")[1]);
            
            var infoSets = parts[1].trim();
            
            var valid = true;
            var sets = infoSets.split(";");
            for (var set : sets)
            {
                var data = parseSet(set);
                if ((data.m_red > 12)
                    || data.m_green > 13
                    || data.m_blue > 14)
                {
                    valid = false;
                }
            }
            if (valid)
                result += gameNum; 
        }
        System.out.println(result);
        
    }
    
    public Data parseSet(String line)
    {
        Pattern p = Pattern.compile("(\\d+) (green|blue|red)(?:\\, )?");
        int red = 0;
        int green = 0;
        int blue = 0;
        var m = p.matcher(line);
        while (m.find())
        {
            var value = parseInt(m.group(1));
            switch (m.group(2))
            {
                case "red":
                {
                    red = value;
                    break;
                }
                case "green":
                {
                    green = value;
                    break;
                }
                case "blue":
                {
                    blue = value;
                    break;
                }
            }
        }
        
        return new Data(red, green, blue);
    }
    
    public static class Data
    {
        int m_red;
        int m_green;
        int m_blue;

        public Data(int red, int green, int blue)
        {
            m_red = red;
            m_green = green;
            m_blue = blue;
        }
    }
}
