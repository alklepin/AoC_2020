package day06;

import java.util.ArrayList;
import java.util.HashMap;

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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
//        for (String line : lines)
//        {
//        }
        ArrayList<Integer> current = new ArrayList<>();
        String[] input = lines.get(0).split(",");
        for (var s : input)
        {
            current.add(parseInt(s));
        }
        
        long[][][] data = new long[7][][];
        for (int i = 0; i < data.length; i++)
        {
            data[i] = create(i);
            populate(data[i]);
        }
        
        long result = 0;
        int days = 256;
        for (int iIdx = 0; iIdx < current.size(); iIdx++)
        {
            int nextV = current.get(iIdx);
            for (int x = 0; x < 9; x++)
                result += data[nextV][days][x];
        }
        System.out.println(result);
        
    }
    
    public long[][] create(int v)
    {
        long[][] data = new long[257][];
        for (int i = 0; i < data.length; i++)
        {
            data[i] = new long[9];
        }
        for (int i = 0; i < 9; i++)
        {
            data[0][i] = i == v ? 1 : 0;
        }
        return data;
    }
    
    public void populate(long[][] data)
    {
        for (var day = 1; day < 257; day++)
        {
            for (int i = 0; i < 9; i++)
            {
                switch (i)
                {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    {
                        data[day][i] = data[day-1][i+1];
                        break;
                    }
                    case 6:
                    {
                        data[day][i] = data[day-1][i+1] + data[day-1][0];
                        break;
                    }
                    case 7:
                    {
                        data[day][i] = data[day-1][i+1];
                        break;
                    }
                    case 8:
                    {
                        data[day][8] = data[day-1][0];
                        break;
                    }
                }
            }
        }
    }
}
