package day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.Graph;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }
    
    static class Node
    {
        public String name;
        public int pressure;
        public ArrayList<String> next = new ArrayList();
        
        public int valveBit;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var nonZeroNodes = new ArrayList<Node>();
        ArrayList<Node> nodes = new ArrayList<>();
//        HashMap<String, Node> nodesMap = new HashMap<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            System.out.println(line);
            // Valve VN has flow rate=0; tunnels lead to valves LW, TK
            var parts = parse("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnels? leads? to valves? (.*)", line);
            var node = new Node();
            node.name = parts[1];
            node.pressure = parseInt(parts[2]);
            Collections.addAll(node.next, parts[3].split(", "));
            nodes.add(node);
  //          nodesMap.put(node.name, node);
            
            if (node.pressure > 0)
            {
                nonZeroNodes.add(node);
            }
        }
        
        Collections.sort(nonZeroNodes, (n1, n2) -> Integer.compare(n2.pressure, n1.pressure));
        
        var graph = new Graph<String, Node>(() -> new Node());
        for (var node : nodes)
        {
            graph.addNode(node.name);
        }
        graph.addNode("**"); // ephemeral node
        for (var node : nodes)
        {
            for (var next : node.next)
            {
                graph.addEdge(node.name, next, 1);
            }
        }

        for (var idx = 0; idx < nonZeroNodes.size(); idx++)
        {
            nonZeroNodes.get(idx).valveBit = idx;
        }
        
        int valveStatesCount = 1 << nonZeroNodes.size();

        var knownStates = new HashMap<State, Integer>();
//        var nextStates = new HashMap<State, State>();
        
        for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
        {
            for (var node : nodes)
            {
                var st = new State(node.name, 0, openedValves);
                knownStates.put(st,0);
            }
        }
        
        ArrayList<Integer> pressureByMinute = new ArrayList<>();
        for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
        {
            var value = openedValves;
            var sum = 0;
            for (var idx = 0; idx < nonZeroNodes.size(); idx++)
            {
                if (value % 2 != 0)
                    sum += nonZeroNodes.get(idx).pressure;
                value /= 2;
            }
            pressureByMinute.add(sum);
        }        
        
        for (int timeRemaining = 1; timeRemaining <= 30; timeRemaining++)
        {
            for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
            {
                for (var cave : nodes)
                {
                    State fromStateMax = null;
                    var maxPressure = 0;
                    for (var from : cave.next)
                    {
                        var fromState = new State(from, timeRemaining - 1, openedValves);
                        var fromPressure = knownStates.get(fromState);
                        var stepPressure = fromPressure + pressureByMinute.get(openedValves);
                        if (stepPressure > maxPressure || 
                            (stepPressure == maxPressure && fromStateMax == null))
                        {
                            maxPressure = Math.max(maxPressure, stepPressure);
                            fromStateMax = fromState;
                        }
                    }
                    
                    if (cave.pressure > 0)
                    {
                        var isOpened = (openedValves >> cave.valveBit) % 2 == 0;
                        if (isOpened)
                        {
                            var changedValves = openedValves | (1 << cave.valveBit);
                            var fromState = new State(cave.name, timeRemaining - 1, changedValves);
                            var fromPressure = knownStates.get(fromState);
                            var stepPressure = fromPressure + pressureByMinute.get(openedValves);
                            if (stepPressure > maxPressure || 
                                (stepPressure == maxPressure && fromStateMax == null))
                            {
                                maxPressure = Math.max(maxPressure, stepPressure);
                                fromStateMax = fromState;
                            }
                        }
                    }
                    var state = new State(cave.name, timeRemaining, openedValves);
                    knownStates.put(state, maxPressure);
                    //nextStates.put(state, fromStateMax);
                    //System.out.println(""+state + " -> "+maxPressure+ ", from: " + fromStateMax.cave);
                }
            }
        }


        var state = new State("AA", 30, 0);
        var pressure = knownStates.get(state);
        System.out.println(pressure);
//        
//        
//        var ts = graph.findPathDijkstra("AA", "**");
//        
//        for (var node : nonZeroNodes)
//        {
//            var pathLength = ts.getNodeValue(node.name);
//            System.out.println("" + node.name + " -> " + node.pressure + ", " + pathLength);
//            
//        }
    }
    
    static class State
    {
        String cave;
        int remainingTime;
        int openedValves; 
        
        public State(String cave, int remainingTime, int openedValves)
        {
            this.cave = cave;
            this.remainingTime = remainingTime;
            this.openedValves = openedValves;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cave == null) ? 0 : cave.hashCode());
            result = prime * result + openedValves;
            result = prime * result + remainingTime;
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
            if (cave == null)
            {
                if (other.cave != null)
                    return false;
            }
            else if (!cave.equals(other.cave))
                return false;
            if (openedValves != other.openedValves)
                return false;
            if (remainingTime != other.remainingTime)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "State [cave=" + cave + ", remainingTime=" + remainingTime
                + ", openedValves=" + openedValves + "]";
        }
    }
}
