package day05;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }

    public void solve()
        throws Exception
    {
//        System.out.println(decodeSeat("FFFBBBFRRR"));
//        System.exit(1);
//        
//        

//        try (InputStream fis = loadLocalFile("input1.txt"))
//        {
//            try (Scanner scanner = new Scanner(fis, "UTF8"))
//            {
//                int maxSeatId = -1;
//                while (scanner.hasNextLine())
//                {
//                    String line = scanner.nextLine().trim();
//                    int seatId = decodeSeat(line);
//                    if (seatId > maxSeatId)
//                        maxSeatId = seatId;
//                }
//                System.out.println(maxSeatId);
//            }
//        }

        LinesGroup lines = readAllLines("input1.txt");
        int maxSeatId = -1;
        for (String line : lines)
        {
            int seatId = decodeSeat(line);
            if (seatId > maxSeatId)
                maxSeatId = seatId;
        }
        System.out.println(maxSeatId);
    }

    private int decodeSeat(String line)
    {
        String rowLine = line.substring(0, 7);
        String colLine = line.substring(7);
        int row = Integer.parseInt(rowLine.replace('F', '0').replace('B', '1'), 2);
        int col = Integer.parseInt(colLine.replace('R', '1').replace('L', '0'), 2);
//        System.out.println(row);
//        System.out.println(col);
        return row*8+col;
    }
}
