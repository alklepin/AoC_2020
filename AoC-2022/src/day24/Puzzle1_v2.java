package day24;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle1_v2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1_v2().solve();
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
        
        public void move(IntPair dimensions)
        {
            var pos = this.position.add(IntPair.DOWN_LEFT);
            pos = pos.add(direction);
            pos = pos.componentModulo(dimensions);
            pos = pos.add(IntPair.UP_RIGHT);

            position = pos;
        }
    }

    Board2D board = null;
    ArrayList<Blizzard> blizzards = new ArrayList<>();
    IntPair start = null;
    IntPair end = null;
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        board = Board2D.parseAsCharsXY(lines);
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

        Board2D interior = new Board2D(board.getWidth(), board.getHeigth());
        interior.setAll('.');
        for (var x = 0; x < interior.getWidth(); x++)
        {
            interior.setAtXY(x, 0, '#');
            interior.setAtXY(x, interior.getHeigth()-1, '#');
        }
        for (var y = 0; y < interior.getHeigth(); y++)
        {
            interior.setAtXY(0, y, '#');
            interior.setAtXY(interior.getWidth()-1, y, '#');
        }
        interior.setAtXY(start, '.');
        interior.setAtXY(end, '.');
        
        
        HashSet<IntPair> blizzardPositions = new HashSet<>();
//        for (var b : blizzards)
//        {
//            blizzardPositions.add(b.position);
//        }

        var dimensions = board.dimensions().minus(Pair.of(2, 2));

        blizzardPositions.clear();
        for (var b : blizzards)
        {
            b.move(dimensions);
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
                    .where(
                        n -> {
                            return interior.getAtXY(n) == '.' && !blizzardPositions.contains(n);
                        })
                    .select(n -> 
                    {
                        var r = new State(node.step + 1, n);
                        System.out.println("Try: "+r);
                        return r;
                    })
                    ;
            }, 
            () -> 
            {
                stepsCount[0]++;
                System.out.println("Step "+stepsCount[0]);
                printState();
                blizzardPositions.clear();
                for (var b : blizzards)
                {
                    b.move(dimensions);
                    blizzardPositions.add(b.position);
                }
                System.out.println("==");
                printState();
                System.out.println("==============================");
            });
        var path = result.getPath(new State(stepsCount[0]+1, end));
        
        System.out.println(path.size());
        
    }
    
    void printState()
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
