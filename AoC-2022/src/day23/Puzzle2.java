package day23;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Bounds;
import common.boards.Generators;
import common.boards.IntPair;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }
    
    static class Elf
    {
        IntPair position;
        IntPair nextPosition;
        boolean willMove;
        
        public Elf(IntPair position)
        {
            this.position = position;
        }

        public void makeDecision(int currentDecision,
            HashMap<IntPair, Elf> currentState)
        {
            willMove = false;
            for (var cell : Generators.neighbours8(position, null, null))
            {
                if (currentState.containsKey(cell)) 
                {
                    willMove = true;
                    break;
                }
            }
            nextPosition = position;
            if (willMove)
            {
                for (int idx = 0; idx < decisions.length; idx++)
                {
                    var decisionIdx = (currentDecision + idx) % decisions.length;
                    var decisionToTry = decisions[decisionIdx];
                    var selected = true;
                    for (var cell : decisionToTry.toCheck)
                    {
                        if (currentState.containsKey(cell.add(position)))
                        {
                            selected = false;
                            break;
                        }
                    }
                    if (selected)
                    {
                        nextPosition = decisionToTry.toMove.add(position);
                        break;
                    }
                }
                willMove = !nextPosition.equals(position);
            }
        }

        public void stop()
        {
            nextPosition = position;
            willMove = false;
        }

        public void move()
        {
            position = nextPosition;
            willMove = false;
        }
    }
    
    static class Decision
    {
        final IntPair [] toCheck;
        final IntPair toMove;

        public Decision(IntPair [] toCheck, IntPair toMove)
        {
            super();
            this.toCheck = toCheck;
            this.toMove = toMove;
        }
    }
    
    public static final IntPair NORTH = IntPair.DOWN;
    public static final IntPair SOUTH = IntPair.UP;
    public static final IntPair WEST = IntPair.LEFT;
    public static final IntPair EAST = IntPair.RIGHT;

    public static final IntPair NE = NORTH.add(EAST);
    public static final IntPair NW = NORTH.add(WEST);
    public static final IntPair SE = SOUTH.add(EAST);
    public static final IntPair SW = SOUTH.add(WEST);
    
    public static Decision[] decisions = new Decision[]
        {
            new Decision(new IntPair[] {NORTH, NE, NW}, NORTH),
            new Decision(new IntPair[] {SOUTH, SE, SW}, SOUTH),
            new Decision(new IntPair[] {WEST, NW, SW}, WEST),
            new Decision(new IntPair[] {EAST, NE, SE}, EAST),
        };
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
//        var inputFile = "input1_test2.txt";

        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        ArrayList<Elf> elves = new ArrayList<>();
        HashMap<IntPair, Elf> currentState = new HashMap<>();

        for (var cell : board.allCellsXY())
        {
            if (board.getAtXY(cell) == '#')
            {
                var elf = new Elf(cell);
                elves.add(elf);
                currentState.put(cell, elf);
            }
        }
        
        printState(currentState);
        
        
        boolean hasMove = true;
        var currentDecision = 0;
        HashMap<IntPair, Elf> nextState;
        var round = 0;
        while (hasMove && round < 10)
        {
            round++;
            hasMove = false;
            nextState = new HashMap<>();
            
            for (var elf : elves)
            {
                elf.makeDecision(currentDecision, currentState);
            }

            for (var elf : elves)
            {
                var conflictElf = nextState.put(elf.nextPosition, elf);
                if (conflictElf != null)
                {
                    conflictElf.stop();
                    elf.stop();
                }
            }
            nextState = new HashMap<>();
            for (var elf : elves)
            {
                var conflictElf = nextState.put(elf.nextPosition, elf);
                hasMove |= elf.willMove;
                elf.move();
                if (conflictElf != null)
                {
                    throw new IllegalSelectorException();
                }
            }
            
            currentState = nextState;
            
            currentDecision = (currentDecision + 1) % decisions.length;
            //printState(currentState);
        }

        printState(currentState);
        
        Bounds bounds = Bounds.of(currentState.keySet());
        var result = (bounds.width() + 1) * (bounds.height() + 1) - elves.size();
        
        System.out.println(result);
        
    }
    
    static void printState(HashMap<IntPair, Elf> elves)
    {
        Bounds bounds = Bounds.of(elves.keySet());
        Board2D board = new Board2D(bounds.width() + 1, bounds.height() + 1);
        board.setAll('.');
        for (var elf : elves.keySet())
        {
            board.setAtXY(elf.minus(bounds.min()), '#');
        }
        board.printAsStrings(System.out);
        System.out.println("===================================");
    }
}
