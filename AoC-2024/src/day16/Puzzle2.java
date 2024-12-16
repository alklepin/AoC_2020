package day16;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;

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
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        var start = board.findCharXY('S').first();
        var end = board.findCharXY('E').first();
        var startState = new ImplicitGraph.SearchState<>(new Reindeer(start, IntPair.RIGHT), 0);
        var searchResult = ImplicitGraph.Dijkstra(startState, 
            s -> s.position.equals(end), 
            s -> {
                var nextStates = new ArrayList<ImplicitGraph.SearchState<Reindeer, Integer>>();
                nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(s.getNode().position, s.getNode().direction.rotateClockwise90()), s.getDistance() + 1000));
                nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(s.getNode().position, s.getNode().direction.rotateAntiClockwise90()), s.getDistance() + 1000));
                var next = s.getNode().position.add(s.getNode().direction);
                if (board.containsXY(next) && board.getCharAtXY(next) != '#')
                    nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(next, s.getNode().direction), s.getDistance() + 1));
                return nextStates;
            }
            );
        var endNode = searchResult.getEndNode();
        var pathLength = searchResult.getEndNodeDistance();
        
        var count = 0;
        var result = 0;
        for (var cell : board.allCellsXY().where(c -> board.getCharAtXY(c) != '#'))
        {
            var searchResult1 = ImplicitGraph.Dijkstra(startState, 
                s -> s.position.equals(cell), 
                s -> {
                    var nextStates = new ArrayList<ImplicitGraph.SearchState<Reindeer, Integer>>();
                    nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(s.getNode().position, s.getNode().direction.rotateClockwise90()), s.getDistance() + 1000));
                    nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(s.getNode().position, s.getNode().direction.rotateAntiClockwise90()), s.getDistance() + 1000));
                    var next = s.getNode().position.add(s.getNode().direction);
                    if (board.containsXY(next) && board.getCharAtXY(next) != '#')
                        nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(next, s.getNode().direction), s.getDistance() + 1));
                    return nextStates;
                }
                );
            var firstEnd = searchResult1.getEndNode();
            var searchResult2 = ImplicitGraph.Dijkstra(
                new ImplicitGraph.SearchState<>(firstEnd, 0), 
                s -> s.position.equals(end), 
                s -> {
                    var nextStates = new ArrayList<ImplicitGraph.SearchState<Reindeer, Integer>>();
                    nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(s.getNode().position, s.getNode().direction.rotateClockwise90()), s.getDistance() + 1000));
                    nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(s.getNode().position, s.getNode().direction.rotateAntiClockwise90()), s.getDistance() + 1000));
                    var next = s.getNode().position.add(s.getNode().direction);
                    if (board.containsXY(next) && board.getCharAtXY(next) != '#')
                        nextStates.add(new ImplicitGraph.SearchState<>(new Reindeer(next, s.getNode().direction), s.getDistance() + 1));
                    return nextStates;
                }
                );
            count++;
            System.out.println(count);
            if (searchResult1.getEndNodeDistance() + searchResult2.getEndNodeDistance() == pathLength)
                result++;
        }
//        var b = board.clone();
//        for (var n : searchResult.getPath())
//        {
//            b.setCharAtXY(n.position, '*');
//        }
//        b.printAsStrings(System.out);
        System.out.println(pathLength);
        System.out.println(result);
        
    }
    
    static class Reindeer
    {
        IntPair position;
        IntPair direction;
        public Reindeer(IntPair position, IntPair direction)
        {
            super();
            this.position = position;
            this.direction = direction;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result
                + ((direction == null) ? 0 : direction.hashCode());
            result = prime * result
                + ((position == null) ? 0 : position.hashCode());
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
            Reindeer other = (Reindeer)obj;
            if (direction == null)
            {
                if (other.direction != null)
                    return false;
            }
            else if (!direction.equals(other.direction))
                return false;
            if (position == null)
            {
                if (other.position != null)
                    return false;
            }
            else if (!position.equals(other.position))
                return false;
            return true;
        }
        
        
    }
}
