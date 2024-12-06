package day06;

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
        var guardStart = board.findCharXY('^').first();
        var guardStartDir = IntPair.DOWN;

//        System.out.println("guard*:"+guardStart);
//        System.out.println("guardDir*:"+guardStartDir);
        
        board.setAtXY(guardStart, '.');
        var visited = new HashSet<IntPair>();
        var knownStates = new HashSet<State>();
        knownStates.add(new State(guardStart, guardStartDir));
        visited.add(guardStart);
        var stepsCount = 0;
        var guard = guardStart; 
        var guardDir = guardStartDir;
        while (board.containsXY(guard))
        {
            var nextDir = guardDir;
            for (var attempt = 0; attempt < 4; attempt++)
            {
                var next = guard.add(nextDir);
                if (board.containsXY(next))
                {
                    if (board.getCharAtXY(next) == '.')
                    {
                        stepsCount++;
                        guard = next;
                        guardDir = nextDir;
                        visited.add(guard);
                        knownStates.add(new State(guard, guardDir));

//                        System.out.println("guard:"+guard);
//                        System.out.println("guardDir:"+guardDir);
                        break;
                    }
                    else
                    {
                        nextDir = nextDir.rotateAntiClockwise90();
//                        System.out.println("rotate");
                    }
                }
                else
                {
                    guard = next;
                    break;
                }
            }
        }
        
        var possibleObstaclse = new HashSet<IntPair>();
        for (var v : visited)
        {
            for (var o : board.neighbours4XY(v))
                possibleObstaclse.add(o);
        }
        var count = 0;
        for (var o : possibleObstaclse)
        {
            var newBoard = board.clone();
            newBoard.setCharAtXY(o, '@');
            if (traceRoute(newBoard, guardStart, guardStartDir))
            {
                count++;
//                System.out.println("Option found:");
//                newBoard.setCharAtXY(guardStart, '^');
//                newBoard.printAsStrings(System.out);
            }
        }
        
        System.out.println(count);
        
    }

    private static boolean traceRoute(Board2D board, IntPair guardStart, IntPair guardDir)
    {
//        System.out.println("guard*:"+guardStart);
//        System.out.println("guardDir*:"+guardDir);
        
        board.setAtXY(guardStart, '.');
        var visited = new HashSet<IntPair>();
        var knownStates = new HashSet<State>();
        visited.add(guardStart);
        var stepsCount = 0;
        var guard = guardStart; 
        while (board.containsXY(guard))
        {
            if (knownStates.contains(new State(guard, guardDir)))
            {
                return true; // loop found
            }
            knownStates.add(new State(guard, guardDir));
            
            var nextDir = guardDir;
            for (var attempt = 0; attempt < 4; attempt++)
            {
                var next = guard.add(nextDir);
                if (board.containsXY(next))
                {
                    if (board.getCharAtXY(next) == '.')
                    {
                        stepsCount++;
                        guard = next;
                        guardDir = nextDir;
                        visited.add(guard);

//                        System.out.println("guard:"+guard);
//                        System.out.println("guardDir:"+guardDir);
                        break;
                    }
                    else
                    {
                        nextDir = nextDir.rotateAntiClockwise90();
//                        System.out.println("rotate");
                    }
                }
                else
                {
                    guard = next;
                    break;
                }
            }
        }
        return false;
    }
    
    static class State
    {
        public IntPair cell;
        public IntPair dir;
        public State(IntPair cell, IntPair dir)
        {
            super();
            this.cell = cell;
            this.dir = dir;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cell == null) ? 0 : cell.hashCode());
            result = prime * result + ((dir == null) ? 0 : dir.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            State other = (State)obj;
            if (cell == null)
            {
                if (other.cell != null)
                    return false;
            }
            else if (!cell.equals(other.cell))
                return false;
            if (dir == null)
            {
                if (other.dir != null)
                    return false;
            }
            else if (!dir.equals(other.dir))
                return false;
            return true;
        }
       
    }
}
