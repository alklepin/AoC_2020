package day20;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;

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
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        var start = board.findCharXY('S').first();
        var end = board.findCharXY('E').first();
        
        var forwardResult = ImplicitGraph.BFS(start, end, 
            cell -> board.neighbours4XY(cell).where(c -> (board.getCharAtXY(c) != '#')));
        var reversedResult = ImplicitGraph.BFS(end, start, 
            cell -> board.neighbours4XY(cell).where(c -> (board.getCharAtXY(c) != '#')));
        
        var shortestLemgth = forwardResult.getPath().size();
        
        var delta = 100;
        
        long result = 0;
        for (var c1 : board.allCellsXY().where(c -> board.getCharAtXY(c) != '#'))
        {
            for (var c2 : board.allCellsXY().where(c -> board.getCharAtXY(c) != '#'))
            {
                var cheatDist = c1.minus(c2).lengthManh();
                if (cheatDist <= 20 && !c1.equals(c2))
                {
                    var d1 = forwardResult.getDistance(c1); 
                    var d2 = reversedResult.getDistance(c2); 
                    if (d1 >= 0 && d2 >= 0 
                        && d1 + d2 <= shortestLemgth - delta - 1 - cheatDist)
                    {
                        result++;
                    }
                }
            }
        }
        
        System.out.println(result);
    }
    
    static class Cheat
    {
        IntPair start;
        IntPair end;

        public Cheat(IntPair start, IntPair end)
        {
            super();
            this.start = start;
            this.end = end;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((end == null) ? 0 : end.hashCode());
            result = prime * result + ((start == null) ? 0 : start.hashCode());
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
            Cheat other = (Cheat)obj;
            if (end == null)
            {
                if (other.end != null)
                    return false;
            }
            else if (!end.equals(other.end))
                return false;
            if (start == null)
            {
                if (other.start != null)
                    return false;
            }
            else if (!start.equals(other.start))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Cheat [start=" + start + ", end=" + end + "]";
        }
        
        
    }
}
