package day17;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;

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
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        char[] commands = lines.get(0).toCharArray();
        
        int height = 0;
        int shapeIdx = 0;
        int commandIdx = 0;
        HashSet<IntPair> chamber = new HashSet<>();
        for (int step = 0; step < 2022; step++)
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
            for (var p: rock.blocks)
            {
                var newPoint = p.add(rockPos);
                chamber.add(newPoint);
                height = Math.max(height, newPoint.getY()+1);
            }
            shapeIdx = (shapeIdx + 1) % Shape.SHAPES.length;
//            print(chamber, height);
        }
        
        System.out.println(height);
        
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
