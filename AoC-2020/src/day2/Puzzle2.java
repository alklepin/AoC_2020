package day2;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    public void solve()
        throws Exception
    {
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
            {
                Pattern pattern = Pattern.compile("([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)");
                int validPwdCounter = 0;
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
//                    System.out.println(line);
                    Matcher m = pattern.matcher(line);
                    if (m.matches())
                    {
                        int num1 = Integer.parseInt(m.group(1));
                        int num2 = Integer.parseInt(m.group(2));
                        char c = m.group(3).charAt(0);
                        String pwd = m.group(4);
//                        System.out.printf("%d-%d %s: %s\n", num1, num2, c, pwd);
                        if (isPwdValid(pwd, c, num1, num2))
                        {
                            validPwdCounter++;
                        }
                    }
                }
                System.out.printf("%d", validPwdCounter);
            }
        }

    }

    private static boolean isPwdValid(String pwd, char c, int num1, int num2)
    {
        int count = 0;
        if (pwd.charAt(num1-1) == c)
            count++;
        if (pwd.charAt(num2-1) == c)
            count++;
        return count == 1;
    }

}
