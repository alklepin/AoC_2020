package day23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import common.PuzzleCommon;
import common.Tuple;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.Graph;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    public static final int statesCount = 23;
    
    public static Graph<Integer, String> initGraph(int group)
    {
        boolean groupA = group==0;
        boolean groupB = group==1;
        boolean groupC = group==2;
        boolean groupD = group==3;
        
        Graph<Integer, String> result = new Graph<Integer, String>(() -> "");
        for (int i = 1; i <= statesCount; i++)
        {
            result.addNode(i);
        }
        result.addEdgeUndirected(1, 2, 1);
        result.addEdgeUndirected(2, 3, 2);
        result.addEdgeUndirected(3, 4, 2);
        result.addEdgeUndirected(4, 5, 2);
        result.addEdgeUndirected(5, 6, 2);
        result.addEdgeUndirected(6, 7, 1);
        
        result.addEdge(8, 2, groupA, 2);
        result.addEdge(8, 3, groupA, 2);
        
        result.addEdge(12, 8, groupA, 1);
        result.addEdge(13, 12, groupA, 1);
        result.addEdge(14, 13, groupA, 1);
        
        result.addEdge(9, 3, groupB, 2);
        result.addEdge(9, 4, groupB, 2);
        
        result.addEdge(15, 9, groupB, 1);
        result.addEdge(16, 15, groupB, 1);
        result.addEdge(17, 16, groupB, 1);
        
        result.addEdge(10, 4, groupC, 2);
        result.addEdge(10, 5, groupC, 2);
        
        result.addEdge(18, 10, groupC, 1);
        result.addEdge(19, 18, groupC, 1);
        result.addEdge(20, 19, groupC, 1);
        
        result.addEdge(11, 5, groupD, 2);
        result.addEdge(11, 6, groupD, 2);
        
        result.addEdge(21, 11, groupD, 1);
        result.addEdge(22, 21, groupD, 1);
        result.addEdge(23, 22, groupD, 1);
        
        return result;
    }
    

    public static Graph<Integer, String> graphA = initGraph(0);
    public static Graph<Integer, String> graphB = initGraph(1);
    public static Graph<Integer, String> graphC = initGraph(2);
    public static Graph<Integer, String> graphD = initGraph(3);
    
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
        mapLocationToNode(Pair.of(2, 5), 9);
        mapLocationToNode(Pair.of(2, 7), 10);
        mapLocationToNode(Pair.of(2, 9), 11);
        
        mapLocationToNode(Pair.of(3, 3), 12);
        mapLocationToNode(Pair.of(4, 3), 13);
        mapLocationToNode(Pair.of(5, 3), 14);
        
        mapLocationToNode(Pair.of(3, 5), 15);
        mapLocationToNode(Pair.of(4, 5), 16);
        mapLocationToNode(Pair.of(5, 5), 17);
        
        mapLocationToNode(Pair.of(3, 7), 18);
        mapLocationToNode(Pair.of(4, 7), 19);
        mapLocationToNode(Pair.of(5, 7), 20);
        
        mapLocationToNode(Pair.of(3, 9), 21);
        mapLocationToNode(Pair.of(4, 9), 22);
        mapLocationToNode(Pair.of(5, 9), 23);
    }
    
    public static void mapLocationToNode(IntPair location, int node)
    {
        locationToGraphNode.put(location, node);
        graphNodeToLocation.put(node, location);
    }
    
    public static Board2D validStates = fillValidStates();

    public static Board2D fillValidStates()
    {
        Board2D result = new Board2D(16, statesCount + 1);
        result.setAll(0);
        for (var st : new int[] {8, 12, 13, 14})
        {
            for (var n : new int[] {0, 1, 2, 3})
            {
                result.setAtRC(st, n, 1);
            }
        }
        for (var st : new int[] {9, 15, 16, 17})
        {
            for (var n : new int[] {4, 5, 6, 7})
            {
                result.setAtRC(st, n, 1);
            }
        }
        for (var st : new int[] {10, 18, 19, 20})
        {
            for (var n : new int[] {8, 9, 10, 11})
            {
                result.setAtRC(st, n, 1);
            }
        }
        for (var st : new int[] {11, 21, 22, 23})
        {
            for (var n : new int[] {12, 13, 14, 15})
            {
                result.setAtRC(st, n, 1);
            }
        }
        return result;
    }
    
    
    public static class State
    {
        // 0-3 - A, 4-7 - B, 8-11 - C, 12-15 - D
        public int[] locations = new int[16];
        public long energy;
        public State from;
        
        public boolean isFinal()
        {
            for (int i = 0; i < locations.length; i++)
            {
                if (validStates.getAtRC(locations[i], i) == 0)
                    return false;
            }
            return true;
        }
        
        public Iterable<State> nextStates()
        {
            boolean [] busyLocations = new boolean[statesCount+1];
            for (int i = 0; i < locations.length; i++)
            {
                busyLocations[locations[i]] = true;
            }
            ArrayList<State> nextStates = new ArrayList<>();
            for (var i = 0; i < locations.length; i++)
            {
                var gr = i < 4 ? graphA :
                    i < 8 ? graphB :
                        i < 12 ? graphC : graphD;
                LinkedList<State> q = new LinkedList<>();
                q.add(this);
                HashSet<State> visited = new HashSet<>();
                while (!q.isEmpty())
                {
                    var current = q.poll();
                    if (visited.add(current))
                    {
                        if (current != this)
                        {
                            nextStates.add(current);
                        }
                        for (var nextInfo : gr.outboundEdges(current.locations[i]))
                        {
                            var loc = nextInfo.getValue1();
                            if (!busyLocations[loc])
                            {
                                var s = new State();
                                s.locations = current.locations.clone();
                                s.locations[i] = loc;
                                s.energy = current.energy + (decodeIdxEnergy(i) * nextInfo.getValue2().intValue());
                                s.from = this;
                                if (!visited.contains(s))
                                {
                                    nextStates.add(s);
                                }
                            }
                        }
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
        if (idx < 4)
            return 'A';
        if (idx < 8)
            return 'B';
        if (idx < 12)
            return 'C';
        return 'D';
    }

    public static int decodeIdxEnergy(int idx)
    {
        if (idx < 4)
            return 1;
        if (idx < 8)
            return 10;
        if (idx < 12)
            return 100;
        return 1000;
    }
    
    public static void printState(State state)
    {
        Board2D board = new Board2D(13, 7);
        board.setRowAsString(0, "#############");
        board.setRowAsString(1, "#...........#");
        board.setRowAsString(2, "###.#.#.#.###");
        board.setRowAsString(3, "  #.#.#.#.#  ");
        board.setRowAsString(4, "  #.#.#.#.#  ");
        board.setRowAsString(5, "  #.#.#.#.#  ");
        board.setRowAsString(6, "  #########  ");
        for (int i = 0; i < state.locations.length; i++)
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
        int countB = 4;
        int countC = 8;
        int countD = 12;

        int[] result = new int[16];
        for (int i = 1; i <= statesCount; i++)
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
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input1_1.txt", false);
        ArrayList<String> lines = readAllLinesNonEmpty("input4.txt", false);
//        ArrayList<String> lines = readAllLinesNonEmpty("input3.txt", false);
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt", false);
        
        Board2D board = new Board2D(13, 7);
        for (int i = 0; i < 7; i++)
            board.setRowAsString(i, lines.get(i));
        
//        board.printAsStrings(System.out);
        
        var s = parseState(board);
        printState(s);
        
//        System.exit(0);
        
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
