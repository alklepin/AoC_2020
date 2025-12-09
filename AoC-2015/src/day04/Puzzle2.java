package day04;

import java.security.MessageDigest;
import java.util.HashMap;

import common.LinesGroup;
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
        String input = "iwrupvqb";
//        String input = "pqrstuv"; // test input
        for (long num = 0; num < 10000000000L; num++)
        {
            String s = input+num;
            var data = s.getBytes("ascii");
            var md = MessageDigest.getInstance("MD5");
            var md5 = md.digest(data);
            if (md5[0] == 0 && md5[1] == 0 && md5[2] == 0)
            {
                System.out.println(num);
                break;
            }
        }
    }
}
