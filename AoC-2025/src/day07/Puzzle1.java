package day07;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
          LinesGroup lines = readAllLines(inputFile);
          var board = Board2D.parseAsCharsXY(lines);
          var start = board.findCharXY('S').first();
          var next = new HashSet<IntPair>();
          next.add(start);
          
          var splitCount = 0;
          while (next.size() > 0)
          {
              var current = next;
              next = new HashSet<>();
              for (var v : current)
              {
                  var nextV = v.add(IntPair.UP);
                  if (board.containsXY(nextV))
                  {
                      if (board.getCharAtXY(nextV) == '^')
                      {
                          splitCount++;
                          var left = nextV.add(IntPair.LEFT);
                          if (board.containsXY(left))
                              next.add(left);
                          var right = nextV.add(IntPair.RIGHT);
                          if (board.containsXY(right))
                              next.add(right);
                      }
                      else
                      {
                          next.add(nextV);
                      }
                  }
              }
          }
          
        System.out.println(splitCount);
        
    }
}
