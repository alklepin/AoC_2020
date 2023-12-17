package day17;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.graph.ImplicitGraph.SearchState;
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
    
    Board2D board;
//    Board2D boardCopy;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        board = Board2D.parseAsCharsXY(lines);
            
        var starts = Query.wrap(
            new State(Pair.of(0, 0), IntPair.RIGHT, -1),
            new State(Pair.of(0, 0), IntPair.UP, -1));
        var result = Integer.MAX_VALUE;
        var end = Pair.of(board.getWidth() -1, board.getHeigth() - 1);
        for (var start : starts)
        {
//            var bb = board.clone();
//            boardCopy = board.clone();
            var searchResult = ImplicitGraph.DijkstraLong(
                start, 
                node -> end.equals(node.cell), 
                this::nextNodes);
            
//            boardCopy.printAsStrings(System.out);

            var pathCost = 0;
            for (var s : searchResult.getPath())
            {
                var v = charToInt(board.getCharAtXY(s.cell));
                pathCost += v;
//                bb.setCharAtXY(s.cell, '#');
            }
//            bb.printAsStrings(System.out);

//            System.out.println();
            result = Math.min(result, pathCost);
        }
        
        System.out.println(result - charToInt(board.getCharAtXY(0,0)));
    }
    
    public Iterable<SearchState<State, Long>> nextNodes(SearchState<State, Long> current)
    {
        var currentState = current.getNode();
        var currentCell = currentState.cell;
//        boardCopy.setCharAtXY(currentCell, '#');
        var dirs = board.directions4XY(currentCell)
            .where(d -> 
            {
                if (d.equals(currentState.dir.reverse()))
                    return false;
                if (d.equals(currentState.dir))
                    return currentState.steps < 9;
                return currentState.steps >= 3;
            })
            .toList();
        
        var nextStates = Query.wrap(dirs)
            .select(d -> currentState.next(d))
            .select(s -> SearchState.of(s, 
                current.getDistance() + charToInt(board.getCharAtXY(s.cell))));
        
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
        IntPair dir;
        int steps;
        public State(IntPair cell, IntPair direction, int steps)
        {
            this.cell = cell;
            this.dir = direction;
            this.steps = steps;
        }
        
        public State next(IntPair dir)
        {
            var newSteps = (dir.equals(dir)) ? steps + 1 : 0;
            return new State(cell.add(dir), dir, newSteps);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cell == null) ? 0 : cell.hashCode());
            result = prime * result
                + ((dir == null) ? 0 : dir.hashCode());
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
            if (dir == null)
            {
                if (other.dir != null)
                    return false;
            }
            else if (!dir.equals(other.dir))
                return false;
            if (steps != other.steps)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "State [cell=" + cell + ", direction=" + dir
                + ", steps=" + steps + "]";
        }
        
        
        
    }
    
}
