package day20;

import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;

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
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        var start = board.findCharXY('S').first();
        var end = board.findCharXY('E').first();
        
        var forwardResult = ImplicitGraph.BFS(start, end, cell -> board.neighbours4XY(cell).where(c -> (board.getCharAtXY(c) != '#')));
        var reversedResult = ImplicitGraph.BFS(end, start, cell -> board.neighbours4XY(cell).where(c -> (board.getCharAtXY(c) != '#')));
        
        var shortestLemgth = forwardResult.getPath().size();
        
        var delta = 100;
        
        HashSet<Cheat> results = new HashSet<>();
        for (var c1 : board.allCellsXY().where(c -> board.getCharAtXY(c) != '#'))
        {
            for (var c2 : board.allCellsXY().where(c -> board.getCharAtXY(c) != '#'))
            {
                if (c1.equals(IntPair.of(9,7))
                    && c2.equals(IntPair.of(11,7)))
                {
                    int a = 1;
                }
                
                if (c1.minus(c2).lengthManh() <= 2 && !c1.equals(c2))
                {
                    var p1 = forwardResult.getPath(c1); 
                    var p2 = reversedResult.getPath(c2); 
                    if (p1 != null && p2 != null 
                        && p1.size() + p2.size() <= shortestLemgth - delta - 1)
                    {
                        results.add(new Cheat(c1, c2));
                    }
                }
            }
        }
        
//        for (var c : results)
//        {
//            System.out.println(c);
//        }
        
        System.out.println(results.size());
        
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
