package day22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.Range;
import common.RangeInt;
import common.boards.IntPair;
import common.boards.IntTriple;
import common.boards.Pair;

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
//        var inputFile = "input1_test2.txt";
//        var inputFile = "input1_test3.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        ArrayList<Brick> bricks1 = new ArrayList<>();
        int result = 0;
        IntTriple max = IntTriple.ZERO;
        for (String line : lines)
        {
            var parts = line.split("~");
            var brick = new Brick(IntTriple.from(parts[0]),IntTriple.from(parts[1]));
            bricks1.add(brick);
            max = max.componentMax(brick.a);
            max = max.componentMax(brick.b);
        }
        
        ArrayList<Brick> bricksSorted = new ArrayList<>();
        bricksSorted.addAll(bricks1);
        Collections.sort(bricksSorted, this::comparerByZ);
        
        HashMap<IntPair, Brick> occupiedBy = new HashMap<>();
        
        var bricksToDestroy = new HashSet<Brick>();
        var bricksToKeep = new HashSet<Brick>();
        var uncoveredBricks = new HashSet<Brick>();
        
//        HashMap<Brick, ArrayList<Brick>> laysOn = new HashMap<>();
        for (var brick : bricksSorted)
        {
            var rangeX = brick.rangeX;
            var rangeY = brick.rangeY;
            var targetHeight = 1;
            var belowBricks = new HashSet<Brick>();
            for (var x = rangeX.start(); x < rangeX.end(); x++)
            {
                for (var y = rangeY.start(); y < rangeY.end(); y++)
                {
                    var cell = Pair.of(x, y);
                    var brickBelow = occupiedBy.get(cell);
                    if (brickBelow != null)
                    {
//                        var newHeight = brickBelow.b.getZ() + 1;
                        var newHeight = Math.max(brickBelow.b.getZ(), brickBelow.a.getZ()) + 1;
                        if (newHeight > targetHeight)
                        {
                            targetHeight = newHeight;
                            belowBricks.clear();
                            belowBricks.add(brickBelow);
                        }
                        else if (newHeight == targetHeight)
                        {
                            belowBricks.add(brickBelow);
                        }
                    }
                    occupiedBy.put(cell, brick);
                }
            }
            for (var b : belowBricks)
            {
                b.addDependent(brick);
                brick.dependsOn(b);
            }
            brick.downTo(targetHeight);
            uncoveredBricks.removeAll(belowBricks);
            uncoveredBricks.add(brick);
            if (belowBricks.size() > 1)
                bricksToDestroy.addAll(belowBricks);
            else if (belowBricks.size() == 1)
                bricksToKeep.addAll(belowBricks);
        }
        for (var b : bricksToDestroy)
            b.markDestroyed();
        for (var b : uncoveredBricks)
            b.markUncovered();
        
        bricksToDestroy.removeAll(bricksToKeep);
        bricksToDestroy.addAll(uncoveredBricks);
        
        result = bricksToDestroy.size();
        
        for (var brick : bricksSorted)
        {
            System.out.println(brick);
        }        
        System.out.println("===");
        for (var brick : bricksToDestroy)
        {
            System.out.println(brick);
        }        
        
        System.out.println(max);
        System.out.println(result);
        
        long sum = 0;
        var bricksToMove = new HashSet<Brick>();
        bricksToMove.addAll(bricksSorted);
        bricksToMove.removeAll(bricksToDestroy);
        for (var b : bricksToMove)
        {
            var dependent = new HashSet<Brick>();
            dependent.add(b);
            var queue = new LinkedList<Brick>();
            queue.add(b);
            while (queue.size() > 0)
            {
                var next = queue.poll();
                for (var bb : next.dependent)
                {
                    if (dependent.containsAll(bb.dependsOn))
                    {
                        if (dependent.add(bb))
                        {
                            queue.add(bb);
                        }
                    }
                }
            }
            sum += dependent.size()-1;
        }
        System.out.println("Sum: "+sum);
    }
    
    
    
    public int comparerByZ(Brick a, Brick b)
    {
        var x = a.a.getZ();
        var y = b.a.getZ();
        
        return x < y ? -1 : x > y ? 1 : 0;
    }
    
    static class Brick
    {
        static int counter = 0;
        
        IntTriple a;
        IntTriple b;
        RangeInt rangeX;
        RangeInt rangeY;
        int state = 0;
        HashSet<Brick> dependent = new HashSet<>();
        HashSet<Brick> dependsOn = new HashSet<>();

        private int index;

        public Brick(IntTriple a, IntTriple b)
        {
            super();
            if (b.getZ() >= a.getZ())
            {
                this.a = a;
                this.b = b;
            }
            else
            {
                this.a = b;
                this.b = a;
            }
            rangeX = RangeInt.ofInclusive(a.getX(), b.getX());
            rangeY = RangeInt.ofInclusive(a.getY(), b.getY());
            this.index = (++counter); 
        }

        public void dependsOn(Brick brick)
        {
            dependsOn.add(brick);
        }

        public void addDependent(Brick brick)
        {
            dependent.add(brick);
        }

        public void markUncovered()
        {
            state = 1;
        }

        public void markDestroyed()
        {
            state = 2;
        }

        public void downTo(int targetHeight)
        {
            var diff = a.getZ() - targetHeight;
            if (diff > 0)
            {
                a = IntTriple.of(a.getX(), a.getY(), a.getZ() - diff);
                b = IntTriple.of(b.getX(), b.getY(), b.getZ() - diff);
            }
            if (diff < 0)
            {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public String toString()
        {
            return "Brick [a=" + a + ", b=" + b + ", index=" + index + ", state=" + state + "]";
        }
        
    }
}
