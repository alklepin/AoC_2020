package day24;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }

    static class Blizzard
    {
        IntPair position;
        IntPair direction;
        
        public Blizzard(IntPair position, IntPair direction)
        {
            this.position = position;
            this.direction = direction;
        }
        
        public void move(Board2D board)
        {
            var pos = this.position.minus(IntPair.DOWN_LEFT)
                .add(direction).componentModulo(board.dimensions()).add(IntPair.UP_RIGHT);
            position = pos;
        }
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        ArrayList<Blizzard> blizzards = new ArrayList<>();
        IntPair start = null;
        IntPair end = null;
        for (var cell : board.allCellsXY())
        {
            switch (board.getAtXY(cell)) 
            {
                case '^':
                {
                    blizzards.add(new Blizzard(cell, IntPair.DOWN));
                    break;
                }
                case 'v':
                {
                    blizzards.add(new Blizzard(cell, IntPair.UP));
                    break;
                }
                case '<':
                {
                    blizzards.add(new Blizzard(cell, IntPair.LEFT));
                    break;
                }
                case '>':
                {
                    blizzards.add(new Blizzard(cell, IntPair.RIGHT));
                    break;
                }
                case '.':
                {
                    if (cell.getY() == 0)
                    {
                        start = cell;
                    }
                    if (cell.getY() == board.getHeigth()-1)
                    {
                        end = cell;
                    }
                    break;
                }
            }
        }
        
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
        
        Board2D interior = boardTest.clone();
        
        boardTest.setAtXY(start, 'S');
        boardTest.setAtXY(end, 'E');
        for (var b : blizzards)
        {
            char c = '*';
            if (b.direction == IntPair.UP)
            {
                c = 'v';
            } 
            else if (b.direction == IntPair.DOWN)
            {
                c = '^';
            } 
            else if (b.direction == IntPair.LEFT)
            {
                c = '<';
            } 
            else if (b.direction == IntPair.RIGHT)
            {
                c = '>';
            } 
            else 
            {
                throw new IllegalSelectorException();
            }
            boardTest.setAtXY(b.position, c);
        }
        
        boardTest.printAsStrings(System.out);

        
        HashSet<IntPair> blizzardPositions = new HashSet<>();
        for (var b : blizzards)
        {
            blizzardPositions.add(b.position);
        }
        
        final IntPair endf = end;
        
        int[] stepsCount = new int[1];
        stepsCount[0] = 0;
        var startState = new State(0, start);
        var result = ImplicitGraph.BFSSteppedCond(
            startState, 
            (next) ->
            {
                return next.position.equals(endf);
            },
            (node) ->
            {
                return 
                    Query.wrap(board.neighbours4XY(node.position))
                    .concat(Query.wrap(node.position))                        
                    .where(n -> interior.getAtXY(n) == '.' && !blizzardPositions.contains(n))
                    .select(n -> new State(node.step + 1, n))
                    ;
            }, 
            () -> 
            {
                stepsCount[0]++;
                blizzardPositions.clear();
                for (var b : blizzards)
                {
                    b.move(board);
                    blizzardPositions.add(b.position);
                }
                
            });
        var path = result.getPath(new State(stepsCount[0]+1, end));
        
        System.out.println(path.size());
        
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
    }
}
