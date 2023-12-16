package day16;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import common.queries.Query;

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
        
        var starts = Query.wrap(board.rowCellsXY(0)).select(c -> new State(c, IntPair.UP))
            .concat(Query.wrap(board.rowCellsXY(board.getHeigth()-1)).select(c -> new State(c, IntPair.DOWN)))
            .concat(Query.wrap(board.colCellsXY(0)).select(c -> new State(c, IntPair.RIGHT)))
            .concat(Query.wrap(board.colCellsXY(board.getWidth()-1)).select(c -> new State(c, IntPair.LEFT)))
            ;
            
        
        for (var start : starts)
        {
            var searchResult = ImplicitGraph.BFS(start, null, this::nextNodes);
            var visitedCells = new HashSet<IntPair>();
            for (var s : searchResult.visited())
            {
                visitedCells.add(s.cell);
            }
            result = Math.max(result, visitedCells.size());
//            var bb = board.clone();
//            for (var c : visitedCells)
//            {
//                bb.setCharAtXY(c, '#');
//            }
        }
        
            
        
//        bb.printAsStrings(System.out);
///        boardCopy.printAsStrings(System.out);
        
        System.out.println(result+1);
        
    }
    
    
    
    static char dirToChar(IntPair dir)
    {
        if (dir == IntPair.DOWN)
            return '^';
        if (dir == IntPair.UP)
            return 'v';
        if (dir == IntPair.LEFT)
            return '<';
        if (dir == IntPair.RIGHT)
            return '>';
        throw new IllegalStateException();
    }
    
    public Iterable<State> nextNodes(State current)
    {
        IntPair nextDir;
        IntPair nextCell;
//        boardCopy.setCharAtXY(current.cell, '#');
        switch (board.getCharAtXY(current.cell))
        {
            case '.':
            {
                boardCopy.setCharAtXY(current.cell, dirToChar(current.direction));
                
                nextCell = current.cell.add(current.direction);
                nextDir = current.direction;
                if (board.containsXY(nextCell))
                    return Query.wrap(new State(nextCell, nextDir));
                else
                    return Collections.EMPTY_LIST;
            }
            case '\\':
            {
                if (current.direction.equals(IntPair.UP))
                    nextDir = IntPair.RIGHT;
                else if (current.direction.equals(IntPair.LEFT))
                    nextDir = IntPair.DOWN;
                else if (current.direction.equals(IntPair.DOWN))
                    nextDir = IntPair.LEFT;
                else //if (current.direction.equals(IntPair.RIGHT))
                    nextDir = IntPair.UP;
                
                nextCell = current.cell.add(nextDir);
                if (board.containsXY(nextCell))
                    return Query.wrap(new State(nextCell, nextDir));
                else
                    return Collections.EMPTY_LIST;
            }
            case '/':
            {
                if (current.direction.equals(IntPair.UP))
                    nextDir = IntPair.LEFT;
                else if (current.direction.equals(IntPair.LEFT))
                    nextDir = IntPair.UP;
                else if (current.direction.equals(IntPair.DOWN))
                    nextDir = IntPair.RIGHT;
                else //if (current.direction.equals(IntPair.RIGHT))
                    nextDir = IntPair.DOWN;
                
                nextCell = current.cell.add(nextDir);
                if (board.containsXY(nextCell))
                    return Query.wrap(new State(nextCell, nextDir));
                else
                    return Collections.EMPTY_LIST;
            }
            case '|':
            {
                var r = new ArrayList<State>();
                if ((current.direction.equals(IntPair.UP))
                    || (current.direction.equals(IntPair.DOWN)))
                {
                    nextCell = current.cell.add(current.direction);
                    r.add(new State(nextCell, current.direction));
                }
                else
                {
                    nextDir = IntPair.UP;
                    nextCell = current.cell.add(nextDir);
                    r.add(new State(nextCell, nextDir));
                    nextDir = IntPair.DOWN;
                    nextCell = current.cell.add(nextDir);
                    r.add(new State(nextCell, nextDir));
                }
                return Query.wrap(r).where(s -> board.containsXY(s.cell));
            }
            case '-':
            {
                var r = new ArrayList<State>();
                if ((current.direction.equals(IntPair.LEFT))
                    || (current.direction.equals(IntPair.RIGHT)))
                {
                    nextCell = current.cell.add(current.direction);
                    r.add(new State(nextCell, current.direction));
                }
                else
                {
                    nextDir = IntPair.LEFT;
                    nextCell = current.cell.add(nextDir);
                    r.add(new State(nextCell, nextDir));
                    nextDir = IntPair.RIGHT;
                    nextCell = current.cell.add(nextDir);
                    r.add(new State(nextCell, nextDir));
                }
                return Query.wrap(r).where(s -> board.containsXY(s.cell));
            }
            default:
                throw new IllegalSelectorException();
        }
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
