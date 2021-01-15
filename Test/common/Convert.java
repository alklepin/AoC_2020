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
}
