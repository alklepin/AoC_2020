package day24;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle1_v3_optimized extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1_v3_optimized().solve();
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
        
        public IntPair atStep(int step, IntPair dimensions)
        {
            var pos = this.position.add(IntPair.DOWN_LEFT);
            pos = pos.add(direction.mult(step));
            pos = pos.componentModulo(dimensions);
            pos = pos.add(IntPair.UP_RIGHT);

            return pos;
        }
    }

    Board2D board = null;
    IntPair dimensions = null;
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
        dimensions = board.dimensions().minus(Pair.of(2, 2));

        System.out.println("Initial state:");
        printState(0);

        var startState = new State(0, start);
        
        var result = ImplicitGraph.BFSCond(
            startState, 
            (next) -> next.position.equals(end),
            (node) ->
            {
                return 
                    Query.wrap(board.neighbours4XY(node.position))
                    .concat(Query.wrap(node.position))                        
                    .select(n -> new State(node.step + 1, n))
                    .where(n -> isValidState(n, board, dimensions))
                    ;
            });
        var path = result.getPath();
        System.out.println(path.size()-1);
        
    }
    
    static boolean isValidState(State state, Board2D board, IntPair blizzardDim)
    {
        boolean result = true;
        int step = state.step;
        IntPair bpos;
        
        var pos = state.position;
        if ((pos.getX() == 0 || pos.getX() == board.getWidth()-1 
            || pos.getY() == 0 || pos.getY() == board.getHeigth()-1))
            return board.getCharAtXY(pos) != '#';
        
        bpos = state.position
            .add(IntPair.DOWN_LEFT)
            .add(IntPair.UP.mult(step))
            .componentModulo(blizzardDim)
            .add(IntPair.UP_RIGHT);
        result &= (board.getCharAtXY(bpos) != '^');
        
        bpos = state.position
            .add(IntPair.DOWN_LEFT)
            .add(IntPair.DOWN.mult(step))
            .componentModulo(blizzardDim)
            .add(IntPair.UP_RIGHT);
        result &= (board.getCharAtXY(bpos) != 'v');
        
        bpos = state.position
            .add(IntPair.DOWN_LEFT)
            .add(IntPair.RIGHT.mult(step))
            .componentModulo(blizzardDim)
            .add(IntPair.UP_RIGHT);
        result &= (board.getCharAtXY(bpos) != '<');
        
        bpos = state.position
            .add(IntPair.DOWN_LEFT)
            .add(IntPair.LEFT.mult(step))
            .componentModulo(blizzardDim)
            .add(IntPair.UP_RIGHT);
        result &= (board.getCharAtXY(bpos) != '>');
        
        return result;
    }
    
    void printState(int step)
    {
        Board2D boardTest = new Board2D(board.getWidth(), board.getHeigth());
        boardTest.setAll('.');
        for (var x = 0; x < boardTest.getWidth(); x++)
        {
            boardTest.setAtXY(x, 0, '#');
            boardTest.setAtXY(x, boardTest.getHeigth()-1, '#');
        }
        for (var y = 0; y < boardTest.getHeigth(); y++)
        {
            boardTest.setAtXY(0, y, '#');
            boardTest.setAtXY(boardTest.getWidth()-1, y, '#');
        }
        
        
        boardTest.setAtXY(start, 'S');
        boardTest.setAtXY(end, 'E');
        for (var b : blizzards)
        {
            char c = IntPair.asDirectionCharVInv(b.direction);
            var pos = b.atStep(step, dimensions);
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
