package day05;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

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
//        System.out.println(decodeSeat("FFFBBBFRRR"));
//        System.exit(1);
//        
        HashSet<Integer> seatsHash = new HashSet<>();
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
            {
                int maxSeatId = -1;
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine().trim();
                    int seatId = decodeSeat(line);
                    seatsHash.add(seatId);
                    if (seatId > maxSeatId)
                        maxSeatId = seatId;
                }
            }
        }
        for (int i = 0; i < 1024; i++)
        {
            if (!seatsHash.contains(i))
                System.out.println(i);
        }
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
