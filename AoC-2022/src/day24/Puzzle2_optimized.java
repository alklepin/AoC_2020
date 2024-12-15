package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Bounds;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2_optimized extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_optimized().solve();
    }

    static class Blizzard
    {
        final IntPair position;
        final IntPair direction;
        
        public Blizzard(IntPair position, IntPair direction)
        {
            this.position = position;
            this.direction = direction;
        }
        
        public IntPair atStep(int step, Bounds blizzardArea)
        {
            return position.addBounded(direction.mult(step), blizzardArea);
        }
    }

    Board2D board = null;
    Bounds blizzardsArea = null;
    ArrayList<Blizzard> blizzards = new ArrayList<>();
    IntPair start = null;
    IntPair end = null;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
//        var inputFile = "input1_test2.txt";
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        board = Board2D.parseAsCharsXY(lines);
        for (var cell : board.allCellsXY())
        {
            char c = board.getCharAtXY(cell);
            var dir = IntPair.decodeDirectionVInv_XY(c);
            if (dir != null)
            {
                blizzards.add(new Blizzard(cell, dir));
            }
            else if (c == '.')
            {
                if (cell.getY() == 0)
                {
                    start = cell;
                }
                if (cell.getY() == board.getHeigth()-1)
                {
                    end = cell;
                }
            }
        }

        blizzardsArea = Bounds.of(IntPair.of(1,1), board.dimensions().minus(IntPair.of(1,1)));

        
        System.out.println("Initial state:");
        printState(0);

        var result1 = ImplicitGraph.BFSCond(
            new State(0, start), 
            (next) -> next.position.equals(end),
            (node) ->
            {
                return 
                    Query.wrap(board.neighbours4XY(node.position))
                    .concat(Query.wrap(node.position))                        
                    .select(n -> new State(node.step + 1, n))
                    .where(n -> isValidState(n, board, blizzardsArea))
                    ;
            });
        System.out.println(result1.getPath().size()-1);
        
        var result2 = ImplicitGraph.BFSCond(
            result1.getEnd(), 
            (next) -> next.position.equals(start),
            (node) ->
            {
                return 
                    Query.wrap(board.neighbours4XY(node.position))
                    .concat(Query.wrap(node.position))                        
                    .select(n -> new State(node.step + 1, n))
                    .where(n -> isValidState(n, board, blizzardsArea))
                    ;
            });
        System.out.println(result2.getPath().size()-1);
        
        var result3 = ImplicitGraph.BFSCond(
            result2.getEnd(), 
            (next) -> next.position.equals(end),
            (node) ->
            {
                return 
                    Query.wrap(board.neighbours4XY(node.position))
                    .concat(Query.wrap(node.position))                        
                    .select(n -> new State(node.step + 1, n))
                    .where(n -> isValidState(n, board, blizzardsArea))
                    ;
            });
        System.out.println(result3.getPath().size()-1);

        System.out.println(result3.getEnd().step);
        
    }
    
    static boolean isValidState(State state, Board2D board, Bounds blizzardArea)
    {
        boolean result = true;
        int step = state.step;
        IntPair bpos;
        
        var pos = state.position;
        if ((pos.getX() == 0 || pos.getX() == board.getWidth()-1 
            || pos.getY() == 0 || pos.getY() == board.getHeigth()-1))
            return board.getCharAtXY(pos) != '#';

        bpos = state.position.addBounded(IntPair.UP.mult(step), blizzardArea);
        result &= (board.getCharAtXY(bpos) != '^');
        
        bpos = state.position.addBounded(IntPair.DOWN.mult(step), blizzardArea);
        result &= (board.getCharAtXY(bpos) != 'v');
        
        bpos = state.position.addBounded(IntPair.RIGHT.mult(step), blizzardArea);
        result &= (board.getCharAtXY(bpos) != '<');
        
        bpos = state.position.addBounded(IntPair.LEFT.mult(step), blizzardArea);
        result &= (board.getCharAtXY(bpos) != '>');
        
        return result;
    }
    
    void printState(int step)
    {
        Board2D boardTest = new Board2D(board.getWidth(), board.getHeigth());
        boardTest.setAll('.');
        boardTest.setBorder('#');
        
        boardTest.setAtXY(start, 'S');
        boardTest.setAtXY(end, 'E');
        for (var b : blizzards)
        {
            char c = IntPair.asDirectionCharVInv(b.direction);
            var pos = b.atStep(step, blizzardsArea);
            boardTest.setAtXY(pos, c);
        }
        
        boardTest.printAsStrings(System.out);
    }
    
    static class State
    {
        IntPair position;
        int step;
        public State(int step, IntPair position)
        {
            this.position = position;
            this.step = step;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result
                + ((position == null) ? 0 : position.hashCode());
            result = prime * result + step;
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
            if (position == null)
            {
                if (other.position != null)
                    return false;
            }
            else if (!position.equals(other.position))
                return false;
            if (step != other.step)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "State [position=" + position + ", step=" + step + "]";
        }
    }
}
