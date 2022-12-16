package common;

import java.util.ArrayList;

public class Convert
{
    public static ArrayList<Character> toListOfChars(String line)
    {
        ArrayList<Character> result = new ArrayList<>(line.length());
        for (int i = 0; i < line.length(); i++)
        {
            result.add(line.charAt(i));
        }
        return result;
    }

    public static int[] toListOfInt(ArrayList<? extends Number> values)
    {
        if (values == null)
            return null;

        int [] result = new int[values.size()]; 
        for (int idx = 0; idx < result.length; idx++)
        {
            result[idx] = values.get(idx).intValue();
        }
        return result;
    }
}
