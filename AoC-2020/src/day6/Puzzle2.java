package day6;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    private class GroupInfo
    {
        private ArrayList<String> lines = new ArrayList<>();
        
        public void addLine(String line)
        {
            lines.add(line);
        }
        
        public int processGroup()
        {
            HashMap<Character, Integer> chars = new HashMap<>();
            for (String line : lines)
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
            int groupSize = lines.size();
            for (Integer v : chars.values())
            {
                if (v == groupSize)
                {
                    count++;
                }
            }
            return count;
        }
    }

    public void solve()
        throws Exception
    {
        ArrayList<GroupInfo> groups = new ArrayList<>();
        
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
            {
                GroupInfo current = null;
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine().trim();
                    if (line.length() == 0)
                    {
                        if (current != null)
                        {
                            groups.add(current);
                            current = null;
                        }
                    }
                    else
                    {
                        if (current == null)
                        {
                            current = new GroupInfo();
                        }
                        current.addLine(line);
                    }
                }
                if (current != null)
                {
                    groups.add(current);
                }
            }
        }

        System.out.println(groups.size());
        
        int sum = 0;
        for (GroupInfo info : groups)
        {
            sum += info.processGroup();
        }
        System.out.println(sum);
    }
}
