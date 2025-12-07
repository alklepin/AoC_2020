package day07;

import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2().solve();
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
          var next = new HashMap<IntPair, Long>();
          var current = new HashMap<IntPair, Long>();
          next.put(start, 1L);
          
          while (next.size() > 0)
          {
              current = next;
              next = new HashMap<>();
              for (var entry : current.entrySet())
              {
                  var v = entry.getKey();
                  var tlCount = entry.getValue();
                  var nextV = v.add(IntPair.UP);
                  if (board.containsXY(nextV))
                  {
                      if (board.getCharAtXY(nextV) == '^')
                      {
                          var left = nextV.add(IntPair.LEFT);
                          if (board.containsXY(left))
                          {
                              var count = next.get(left);
                              if (count != null)
                                  next.put(left, count + tlCount);
                              else 
                                  next.put(left, tlCount);
                          }
                          var right = nextV.add(IntPair.RIGHT);
                          if (board.containsXY(right))
                          {
                              var count = next.get(right);
                              if (count != null)
                                  next.put(right, count + tlCount);
                              else 
                                  next.put(right, tlCount);
                          }
                      }
                      else
                      {
                          var count = next.get(nextV);
                          if (count != null)
                              next.put(nextV, count + tlCount);
                          else 
                              next.put(nextV, tlCount);
                      }
                  }
              }
          }
        long result = 0L;
        for (var c : current.values())
        {
            result += c;
        }
        
        System.out.println(result);
        
    }
}
