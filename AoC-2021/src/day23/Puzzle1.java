package day23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.Graph;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
//    public static Graph<Integer, String> graph = initGraph();
//    public static Graph<Integer, String> initGraph()
//    {
//        Graph<Integer, String> result = new Graph<Integer, String>(() -> "");
//        for (int i = 1; i <= 15; i++)
//        {
//            result.addNode(i);
//        }
//        result.addEdgeUndirected(1, 2, 1);
//        result.addEdgeUndirected(2, 3, 2);
//        result.addEdgeUndirected(3, 4, 2);
//        result.addEdgeUndirected(4, 5, 2);
//        result.addEdgeUndirected(5, 6, 2);
//        result.addEdgeUndirected(6, 7, 1);
//
//        result.addEdgeUndirected(2, 8, 2);
//        result.addEdgeUndirected(8, 3, 2);
//        result.addEdgeUndirected(8, 9, 1);
//        
//        result.addEdgeUndirected(3, 10, 2);
//        result.addEdgeUndirected(10, 4, 2);
//        result.addEdgeUndirected(10, 11, 1);
//        
//        result.addEdgeUndirected(4, 12, 2);
//        result.addEdgeUndirected(12, 5, 2);
//        result.addEdgeUndirected(12, 13, 1);
//        
//        result.addEdgeUndirected(5, 14, 2);
//        result.addEdgeUndirected(14, 6, 2);
//        result.addEdgeUndirected(14, 15, 1);
//        
//        return result;
//    }

    public static Graph<Integer, String> graphA = initGraphA();
    public static Graph<Integer, String> initGraphA()
    {
        Graph<Integer, String> result = new Graph<Integer, String>(() -> "");
        for (int i = 1; i <= 15; i++)
        {
            result.addNode(i);
        }
        result.addEdgeUndirected(1, 2, 1);
        result.addEdgeUndirected(2, 3, 2);
        result.addEdgeUndirected(3, 4, 2);
        result.addEdgeUndirected(4, 5, 2);
        result.addEdgeUndirected(5, 6, 2);
        result.addEdgeUndirected(6, 7, 1);
        
        result.addEdgeUndirected(2, 8, 2);
        result.addEdgeUndirected(8, 3, 2);
        result.addEdgeUndirected(8, 9, 1);
        
        result.addEdge(10, 3, 2);
        result.addEdge(10, 4, 2);
        result.addEdge(11, 10, 1);
        
        result.addEdge(12, 4, 2);
        result.addEdge(12, 5, 2);
        result.addEdge(13, 12, 1);
        
        result.addEdge(14, 5, 2);
        result.addEdge(14, 6, 2);
        result.addEdge(15, 14, 1);
        
        return result;
    }

    public static Graph<Integer, String> graphB = initGraphB();
    public static Graph<Integer, String> initGraphB()
    {
        Graph<Integer, String> result = new Graph<Integer, String>(() -> "");
        for (int i = 1; i <= 15; i++)
        {
            result.addNode(i);
        }
        result.addEdgeUndirected(1, 2, 1);
        result.addEdgeUndirected(2, 3, 2);
        result.addEdgeUndirected(3, 4, 2);
        result.addEdgeUndirected(4, 5, 2);
        result.addEdgeUndirected(5, 6, 2);
        result.addEdgeUndirected(6, 7, 1);
        
        result.addEdge(8, 2, 2);
        result.addEdge(8, 3, 2);
        result.addEdge(9, 8, 1);
        
        result.addEdgeUndirected(10, 3, 2);
        result.addEdgeUndirected(10, 4, 2);
        result.addEdgeUndirected(11, 10, 1);
        
        result.addEdge(12, 4, 2);
        result.addEdge(12, 5, 2);
        result.addEdge(13, 12, 1);
        
        result.addEdge(14, 5, 2);
        result.addEdge(14, 6, 2);
        result.addEdge(15, 14, 1);
        
        return result;
    }
    
    public static Graph<Integer, String> graphC = initGraphC();
    public static Graph<Integer, String> initGraphC()
    {
        Graph<Integer, String> result = new Graph<Integer, String>(() -> "");
        for (int i = 1; i <= 15; i++)
        {
            result.addNode(i);
        }
        result.addEdgeUndirected(1, 2, 1);
        result.addEdgeUndirected(2, 3, 2);
        result.addEdgeUndirected(3, 4, 2);
        result.addEdgeUndirected(4, 5, 2);
        result.addEdgeUndirected(5, 6, 2);
        result.addEdgeUndirected(6, 7, 1);
        
        result.addEdge(8, 2, 2);
        result.addEdge(8, 3, 2);
        result.addEdge(9, 8, 1);
        
        result.addEdge(10, 3, 2);
        result.addEdge(10, 4, 2);
        result.addEdge(11, 10, 1);
        
        result.addEdgeUndirected(12, 4, 2);
        result.addEdgeUndirected(12, 5, 2);
        result.addEdgeUndirected(13, 12, 1);
        
        result.addEdge(14, 5, 2);
        result.addEdge(14, 6, 2);
        result.addEdge(15, 14, 1);
        
        return result;
    }

    public static Graph<Integer, String> graphD = initGraphD();
    public static Graph<Integer, String> initGraphD()
    {
        Graph<Integer, String> result = new Graph<Integer, String>(() -> "");
        for (int i = 1; i <= 15; i++)
        {
            result.addNode(i);
        }
        result.addEdgeUndirected(1, 2, 1);
        result.addEdgeUndirected(2, 3, 2);
        result.addEdgeUndirected(3, 4, 2);
        result.addEdgeUndirected(4, 5, 2);
        result.addEdgeUndirected(5, 6, 2);
        result.addEdgeUndirected(6, 7, 1);

        result.addEdge(8, 2, 2);
        result.addEdge(8, 3, 2);
        result.addEdge(9, 8, 1);
        
        result.addEdge(10, 3, 2);
        result.addEdge(10, 4, 2);
        result.addEdge(11, 10, 1);
        
        result.addEdge(12, 4, 2);
        result.addEdge(12, 5, 2);
        result.addEdge(13, 12, 1);
        
        result.addEdgeUndirected(14, 5, 2);
        result.addEdgeUndirected(14, 6, 2);
        result.addEdgeUndirected(15, 14, 1);
        
        return result;
    }
    
    public static HashMap<IntPair, Integer> locationToGraphNode = new HashMap<>();
    public static HashMap<Integer, IntPair> graphNodeToLocation = new HashMap<>();
    {
        mapLocationToNode(Pair.of(1, 1), 1);
        mapLocationToNode(Pair.of(1, 2), 2);
        mapLocationToNode(Pair.of(1, 4), 3);
        mapLocationToNode(Pair.of(1, 6), 4);
        mapLocationToNode(Pair.of(1, 8), 5);
        mapLocationToNode(Pair.of(1, 10), 6);
        mapLocationToNode(Pair.of(1, 11), 7);
        mapLocationToNode(Pair.of(2, 3), 8);
        mapLocationToNode(Pair.of(3, 3), 9);
        mapLocationToNode(Pair.of(2, 5), 10);
        mapLocationToNode(Pair.of(3, 5), 11);
        mapLocationToNode(Pair.of(2, 7), 12);
        mapLocationToNode(Pair.of(3, 7), 13);
        mapLocationToNode(Pair.of(2, 9), 14);
        mapLocationToNode(Pair.of(3, 9), 15);

        mapLocationToNode(Pair.of(1, 3), 16);
        mapLocationToNode(Pair.of(1, 5), 17);
        mapLocationToNode(Pair.of(1, 7), 18);
        mapLocationToNode(Pair.of(1, 9), 19);
    }
    
    public static void mapLocationToNode(IntPair location, int node)
    {
        locationToGraphNode.put(location, node);
        graphNodeToLocation.put(node, location);
    }
    
    
    public static class State
    {
        // 0,1 - A, 2,3 - B, 4,5 - C, 6,7 - D
        public int[] locations = new int[8];
        public long energy;
        public State from;
        
        public boolean isFinal()
        {
            return (locations[0] == 8 || locations[0] == 9)
                && (locations[1] == 8 || locations[1] == 9)
                && (locations[2] == 10 || locations[2] == 11)
                && (locations[3] == 10 || locations[3] == 11)
                && (locations[4] == 12 || locations[4] == 13)
                && (locations[5] == 12 || locations[5] == 13)
                && (locations[6] == 14 || locations[5] == 15)
                && (locations[7] == 14 || locations[7] == 15);
        }
        
        public Iterable<State> nextStates()
        {
            boolean [] busyLocations = new boolean[16];
            for (int i = 0; i < locations.length; i++)
            {
                busyLocations[locations[i]] = true;
            }
            ArrayList<State> nextStates = new ArrayList<>();
            for (var i = 0; i < locations.length; i++)
            {
                var gr = i < 2 ? graphA :
                    i < 4 ? graphB :
                        i < 6 ? graphC : graphD;
                for (var nextInfo : gr.outboundEdges(locations[i]))
                {
                    var loc = nextInfo.getValue1();
                    if (!busyLocations[loc])
                    {
                        var s = new State();
                        s.locations = locations.clone();
                        s.locations[i] = loc;
                        s.energy = energy + (decodeIdxEnergy(i) * nextInfo.getValue2().intValue());
                        s.from = this;
                        nextStates.add(s);
                    }
                }
            }
            return nextStates;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(locations);
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
            if (!Arrays.equals(locations, other.locations))
                return false;
            return true;
        }
        
        
    }
    
    public static char decodeIdx(int idx)
    {
        if (idx < 2)
            return 'A';
        if (idx < 4)
            return 'B';
        if (idx < 6)
            return 'C';
        return 'D';
    }

    public static int decodeIdxEnergy(int idx)
    {
        if (idx < 2)
            return 1;
        if (idx < 4)
            return 10;
        if (idx < 6)
            return 100;
        return 1000;
    }
    
    public static void printState(State state)
    {
        Board2D board = new Board2D(13, 5);
        board.setRowAsString(0, "#############");
        board.setRowAsString(1, "#...........#");
        board.setRowAsString(2, "###.#.#.#.###");
        board.setRowAsString(3, "  #.#.#.#.#  ");
        board.setRowAsString(4, "  #########  ");
        for (int i = 0; i < 8; i++)
        {
            var loc = graphNodeToLocation.get(state.locations[i]);
            board.setAtRC(loc, decodeIdx(i));
        }
        board.printAsStrings(System.out);
        System.out.printf("Energy used: %s\n", state.energy);
    }

    public static State parseState(Board2D board)
    {
        int countA = 0;
        int countB = 2;
        int countC = 4;
        int countD = 6;

        int[] result = new int[8];
        for (int i = 1; i <= 15; i++)
        {
            var loc = graphNodeToLocation.get(i);
            var c = board.getAtRC(loc);
            if (c == 'A')
            {
                result[countA] = i;
                countA++;
            }
            else if (c == 'B')
            {
                result[countB] = i;
                countB++;
            }
            else if (c == 'C')
            {
                result[countC] = i;
                countC++;
            }
            else if (c == 'D')
            {
                result[countD] = i;
                countD++;
            }
        }
        var state = new State();
        state.locations = result;
        return state;
    }
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        LinesGroup lines = readAllLines("input1.txt");
        
        LinesGroup lines = readAllLinesNonEmpty("input1.txt", false);
//        LinesGroup lines = readAllLinesNonEmpty("input3.txt", false);
//        LinesGroup lines = readAllLinesNonEmpty("input2.txt", false);
        
        Board2D board = new Board2D(13, 5);
        for (int i = 0; i < 5; i++)
            board.setRowAsString(i, lines.get(i));
        
//        board.printAsStrings(System.out);
        
        var s = parseState(board);
        printState(s);
        
        HashMap<State, Long> visitedStates = new HashMap<>();
        PriorityQueue<State> queue = new PriorityQueue<State>((s1, s2) -> Long.compare(s1.energy, s2.energy));

        long step = 0;
        queue.add(s);
        while (!queue.isEmpty())
        {
            var state = queue.poll();
            if (visitedStates.containsKey(state))
                continue;
            
            visitedStates.put(state, state.energy);
            if (state.isFinal())
            {
                System.out.println("Found!!!");
                printState(state);
                
                var prev = state;
                while (prev != null)
                {
                    printState(prev);
                    prev = prev.from;
                }
                break;
            }
            
            step++;
            if (step % 100000 == 0)
            {
                System.out.println("Step: "+step+ " queue size: "+queue.size()+" visited: "+visitedStates.size());
                printState(state);
            }
//            if (visitedStates.size() > 1000000)
//            {
//                for (var s1 : visitedStates.keySet())
//                {
//                    System.out.println(Arrays.toString(s1.locations));
//                }
//                System.exit(0);
//            }
            
            for (var ns : state.nextStates())
            {
                if (!visitedStates.containsKey(ns)) 
                {
                    queue.add(ns);
                }
            }
        }
//        System.out.println("=================");
//        for (var ns : s.nextStates())
//        {
//            printState(ns);
//        }
        
        
    }
}
