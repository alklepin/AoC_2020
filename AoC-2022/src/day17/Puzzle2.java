package day17;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;

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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        char[] commands = lines.get(0).toCharArray();
        
        int height = 0;
        int shapeIdx = 0;
        int commandIdx = 0;
        HashSet<IntPair> chamber = new HashSet<>();
//        var expectedLoopLength = commands.length * Shape.SHAPES.length * 2000;
        var expectedLoopLength = 8705;
        int[] heightAtAtep = new int[expectedLoopLength];
        HashMap<Integer, Integer> rows = new HashMap<>(); 
        HashMap<Long, Integer> savedStates = new HashMap<>();
        int lastStep = 0;
        int lastHeight = 0;
        for (int step = 0; step < expectedLoopLength; step++)
        {
//            System.out.println("Step: "+step);
            var rock = Shape.SHAPES[shapeIdx];
            var rockPos = Pair.of(2, height + 3);
            boolean canMove = true;;
            while (canMove)
            {
                var hdelta = commands[commandIdx] == '<' ? IntPair.LEFT : IntPair.RIGHT;
//                System.out.println("v-move try" + hdelta);
                var newPos = rockPos.add(hdelta);
                var hasIntersection = false;
                for (var p: rock.blocks)
                {
                    var newPoint = p.add(newPos);
                    hasIntersection |= chamber.contains(newPoint) 
                        || newPoint.getX() < 0 || newPoint.getX() >= 7;
                }
                if (!hasIntersection)
                {
                    rockPos = newPos;
//                    System.out.println("v-move " + hdelta);
                }

                newPos = rockPos.add(IntPair.DOWN);
//                System.out.println("h-move try");
                hasIntersection = false;
                for (var p: rock.blocks)
                {
                    var newPoint = p.add(newPos);
                    hasIntersection |= newPoint.getY() < 0 || chamber.contains(newPoint);
                }
                if (!hasIntersection)
                {
                    rockPos = newPos;
//                    System.out.println("h-move");
                }
                else
                {
                    canMove = false;
                }
                commandIdx = (commandIdx + 1) % commands.length;
            }
            int truncateAt = -1;
            for (var p: rock.blocks)
            {
                var newPoint = p.add(rockPos);
                chamber.add(newPoint);
                height = Math.max(height, newPoint.getY()+1);
                var rowObj = rows.get(newPoint.getY());
                var row = rowObj != null ? rowObj.intValue() : 0;
                row |= 1 << newPoint.getX();
                rows.put(newPoint.getY(), row);
                if (row == 127)
                {
                    truncateAt = Math.max(truncateAt,newPoint.getY()-1);
                }
            }
            shapeIdx = (shapeIdx + 1) % Shape.SHAPES.length;
            heightAtAtep[step] = height;
            
            if (truncateAt > 0)
            {
                Iterator<IntPair> it = chamber.iterator();
                while (it.hasNext())
                {
                    var p = it.next();
                    if (p.getY() < truncateAt)
                        it.remove();
                }

                Iterator<Integer> it1 = rows.keySet().iterator();
                while (it1.hasNext())
                {
                    var p = it1.next();
                    if (p < truncateAt - 4)
                        it1.remove();
                }
            }

            if (commandIdx == 109 && shapeIdx == 0)
//              if ((commandIdx >= 100) && (commandIdx <= 110))
              {
                  var state = encodeState(rows, height);
                  var knownStep = savedStates.get(state);
                  if (knownStep != null)
                  {
                      System.out.println("Loop step: "+ knownStep + " global step: "+step);
                      System.out.println("size: " + savedStates.size() + " add state: " + state + " commandIdx: " + commandIdx + " shape: " + shapeIdx);
                      System.out.println("step diff: " + (step-lastStep) + " height diff: " + (height - lastHeight));
                      System.out.println("height: " + heightAtAtep[step]);
                      lastHeight = height;
                      lastStep = step;
                  }
                  else
                  {
                      savedStates.put(state, step);
                  }
              }
              
            
//            print(chamber, height);
        }

//        int[] line = new int[expectedLoopLength];
//        for (var p : chamber)
//        {
//            if (p.getY() < expectedLoopLength)
//                line[p.getY()] |= 1 << p.getX();
//        }
////        int delta = 13405;
//        int delta = 161456000;
//        for (var idx = 2; idx < expectedLoopLength; idx++)
//        {
//            while (idx + delta < expectedLoopLength && line[idx] == line[idx+delta])
//                idx++;
//            if (idx + delta < expectedLoopLength)
//                delta++;
//        }
//        
//        System.out.println("Delta:" + delta);

//        for (int idx = 0; idx < 2 * delta; idx++)
//        for (int idx = 0; idx < 200; idx++)
//        {
//            System.out.println(heightAtAtep[idx+delta] - heightAtAtep[idx]);
//        }
        
        
//        Loop step: 1745 global step: 5225
//        size: 1 add state: 30380064 commandIdx: 109 shape: 0
//        step diff: 1740 height diff: 2681
        
        var stepNeeded = 1000000000000l - 1;
        var baseStep = 6964;
        var s1 = stepNeeded - baseStep;
        var loopSize = 1740;
        long loopCount = s1 / loopSize;
        long loopedHeight = loopCount * 2681;
        var sourceStep = (int)(stepNeeded - loopCount * loopSize);
        var result = loopedHeight + heightAtAtep[sourceStep]; 
        
        System.out.println(result);
    }
    
    private long encodeState(int [] rows, int height)
    {
        return (long)rows[height-1] + rows[height-2] << 7 + rows[height-3] << 14 + rows[height-4] << 21;
    }
    
    private long encodeState(HashMap<Integer, Integer> rows, int height)
    {
        var result = (long)rows.get(height-1).longValue() 
            + (rows.get(height-2).intValue() << 7) 
            + (rows.get(height-3).intValue() << 14) 
            + (rows.get(height-4).intValue() << 21);
        return result;
    }

    private static void print(HashSet<IntPair> chamber, int height)
    {
        Board2D board = new Board2D(9, height+1);
        board.setAll('.');
        for (var idx = 0; idx <= height; idx++)
        {
            board.setAtXY(Pair.of(0, idx), '|');
            board.setAtXY(Pair.of(8, idx), '|');
        }
        for (var p : chamber)
            board.setAtXY(p.add(IntPair.RIGHT), '#');
        board.printAsStringsRev(System.out);
    }
    
    static class Shape
    {
        public static Shape H_LINE = new Shape(new IntPair[] {Pair.of(0, 0),
            Pair.of(1, 0), Pair.of(2, 0), Pair.of(3,0)});

        public static Shape PLUS = new Shape(new IntPair[] {Pair.of(1, 0),
            Pair.of(0, 1), Pair.of(1, 1), Pair.of(2,1), Pair.of(1, 2)});
        
        public static Shape ANGLE = new Shape(new IntPair[] {Pair.of(0, 0),
            Pair.of(1, 0), Pair.of(2, 0), Pair.of(2,1), Pair.of(2, 2)});
        
        public static Shape V_LINE = new Shape(new IntPair[] {Pair.of(0, 0),
            Pair.of(0, 1), Pair.of(0, 2), Pair.of(0,3)});

        public static Shape SQUARE = new Shape(new IntPair[] {Pair.of(0, 0),
            Pair.of(0, 1), Pair.of(1, 0), Pair.of(1,1)});
        
        public static Shape[] SHAPES = new Shape[] {H_LINE, PLUS, ANGLE, V_LINE, SQUARE};
        
        public IntPair[] blocks;
        
        public Shape(IntPair[] blocks)
        {
            this.blocks = blocks;
        }
    }
    
    static <T> boolean intersects(HashSet<T> set, T[] values)
    {
        for (var v : values)
        {
            if (set.contains(v))
                return true;
        }
        return false;
    }
}
