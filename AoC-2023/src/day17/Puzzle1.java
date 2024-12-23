package day17;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.graph.ImplicitGraph.SearchState;
import common.queries.Query;

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
    
    Board2D board;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        board = Board2D.parseAsCharsXY(lines);

//        var starts = Query.sequenceOf( 
//            board.rowCellsXY(0).select(c -> new State(c, IntPair.UP)),
//            board.rowCellsXY(board.getHeigth()-1).select(c -> new State(c, IntPair.DOWN)),
//            board.colCellsXY(0).select(c -> new State(c, IntPair.RIGHT)),
//            board.colCellsXY(board.getWidth()-1).select(c -> new State(c, IntPair.LEFT)))
//            ;
            
        var starts = Query.wrap(
            new State(Pair.of(0, 0), IntPair.LEFT, 0),
            new State(Pair.of(0, 0), IntPair.DOWN, 0));
        var result = Integer.MAX_VALUE;
        var end = Pair.of(board.getWidth() -1, board.getHeigth() - 1);
        for (var start : starts)
        {
            var bb = board.clone();
            var searchResult = ImplicitGraph.DijkstraLong(
                start, 
                node -> end.equals(node.cell), 
                this::nextNodes);
            
            var pathCost = 0;
            for (var s : searchResult.getPath())
            {
                var v = (board.getAtXY(s.cell) - '0');
                pathCost += v;
                bb.setCharAtXY(s.cell, '#');
            }
            bb.printAsStrings(System.out);
            System.out.println();
            result = Math.min(result, pathCost);
        }
        
        System.out.println(result - (board.getAtXY(0,0) - '0'));
        
    }
    
    public Iterable<SearchState<State, Long>> nextNodes(SearchState<State, Long> current)
    {
//        boardCopy.setCharAtXY(current.cell, '#');
        var currentCell = current.getNode().cell;
        var currentState = current.getNode();
        var dirs = Query.wrap(board.neighbours4XY(currentCell))
            .select(c -> c.minus(currentCell))
            .where(d -> !d.equals(currentState.direction.mult(-1))
                && (currentState.steps < 2 || !d.equals(currentState.direction)))
            .toList();
        
        var nextStates = Query.wrap(dirs)
            .select(d -> currentState.next(d))
            .select(s -> SearchState.of(s, 
                (long)board.getAtXY(s.cell)-'0'+current.getDistance()));
        
//        System.out.println("From: "+current.getNode());
//        for (var s : nextStates)
//        {
//            System.out.println("To: "+s.getNode());
//        }
        
        return nextStates;
    }
    
    
    static class State
    {
        IntPair cell;
        IntPair direction;
        int steps;
        public State(IntPair cell, IntPair direction, int steps)
        {
            this.cell = cell;
            this.direction = direction;
            this.steps = steps;
        }
        
        public State next(IntPair dir)
        {
            var newSteps = (direction.equals(dir)) ? steps + 1 : 0;
            return new State(cell.add(dir), dir, newSteps);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cell == null) ? 0 : cell.hashCode());
            result = prime * result
                + ((direction == null) ? 0 : direction.hashCode());
            result = prime * result + steps;
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
            if (steps != other.steps)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "State [cell=" + cell + ", direction=" + direction
                + ", steps=" + steps + "]";
        }
        
        
        
    }
    
}
