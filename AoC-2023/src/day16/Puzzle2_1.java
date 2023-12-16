package day16;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    static Board2D board;
    static Board2D boardCopy;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        board = Board2D.parseAsCharsXY(lines);
        boardCopy = board.clone();
        
        int result = 0;
        
        var starts = Query.sequenceOf( 
            board.rowCellsXY(0).select(c -> new State(c, IntPair.UP)),
            board.rowCellsXY(board.getHeigth()-1).select(c -> new State(c, IntPair.DOWN)),
            board.colCellsXY(0).select(c -> new State(c, IntPair.RIGHT)),
            board.colCellsXY(board.getWidth()-1).select(c -> new State(c, IntPair.LEFT)))
            ;
            
//        var starts = Query.wrap(new State(Pair.of(0, 0), IntPair.RIGHT));
        var bb = board.clone();
        
        for (var start : starts)
        {
            var searchResult = ImplicitGraph.BFS(start, null, this::nextNodes);
            var visitedCells = new HashSet<IntPair>();
            for (var s : searchResult.visited())
            {
                visitedCells.add(s.cell);
            }
            result = Math.max(result, visitedCells.size());
            for (var c : visitedCells)
            {
                bb.setCharAtXY(c, '#');
            }
        }
        
            
        
        bb.printAsStrings(System.out);
///        boardCopy.printAsStrings(System.out);
        
        System.out.println(result+1);
        
    }
    
    public Iterable<State> nextNodes(State current)
    {
        IntPair nextDir;
        IntPair nextCell;
//        boardCopy.setCharAtXY(current.cell, '#');
        Query<State> result = Query.empty();
        switch (board.getCharAtXY(current.cell))
        {
            case '.':
            {
                boardCopy.setCharAtXY(current.cell, current.direction.asDirectionCharVInv());
                result = Query.wrap(current.next(current.direction));
                break;
            }
            case '\\':
            {
                if (current.direction.equals(IntPair.UP))
                    result.add(current.next(IntPair.RIGHT));
                else if (current.direction.equals(IntPair.LEFT))
                    result.add(current.next(IntPair.DOWN));
                else if (current.direction.equals(IntPair.DOWN))
                    result.add(current.next(IntPair.LEFT));
                else //if (current.direction.equals(IntPair.RIGHT))
                    result.add(current.next(IntPair.UP));
                break;
            }
            case '/':
            {
                if (current.direction.equals(IntPair.UP))
                    result.add(current.next(IntPair.LEFT));
                else if (current.direction.equals(IntPair.LEFT))
                    result.add(current.next(IntPair.UP));
                else if (current.direction.equals(IntPair.DOWN))
                    result.add(current.next(IntPair.RIGHT));
                else //if (current.direction.equals(IntPair.RIGHT))
                    result.add(current.next(IntPair.DOWN));
                break;
            }
            case '|':
            {
                var r = new ArrayList<State>();
                if ((current.direction.equals(IntPair.UP))
                    || (current.direction.equals(IntPair.DOWN)))
                {
                    result.add(current.next(current.direction));
                }
                else
                {
                    result.add(current.next(IntPair.UP));
                    result.add(current.next(IntPair.DOWN));
                }
                break;
            }
            case '-':
            {
                var r = new ArrayList<State>();
                if ((current.direction.equals(IntPair.LEFT))
                    || (current.direction.equals(IntPair.RIGHT)))
                {
                    result.add(current.next(current.direction));
                }
                else
                {
                    result.add(current.next(IntPair.LEFT));
                    result.add(current.next(IntPair.RIGHT));
                }
                break;
            }
            default:
                throw new IllegalSelectorException();
        }
        return result.where(s -> board.containsXY(s.cell));
    }
    
    static class State
    {
        IntPair cell;
        IntPair direction;
        public State(IntPair cell, IntPair direction)
        {
            this.cell = cell;
            this.direction = direction;
        }
        
        public State next(IntPair dir)
        {
            return new State(cell.add(dir), dir);
        }
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cell == null) ? 0 : cell.hashCode());
            result = prime * result
                + ((direction == null) ? 0 : direction.hashCode());
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
            if (direction == null)
            {
                if (other.direction != null)
                    return false;
            }
            else if (!direction.equals(other.direction))
                return false;
            return true;
        }
        
        
    }
}
