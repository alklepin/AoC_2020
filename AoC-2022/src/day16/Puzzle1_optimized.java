package day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.Graph;

public class Puzzle1_optimized extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1_optimized().solve();
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
        public ArrayList<String> nextList = new ArrayList();
        public int[] next;

        public int nodeIdx;
        public int valveBit;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var nonZeroNodes = new ArrayList<Node>();
        ArrayList<Node> nodes = new ArrayList<>();
        HashMap<String, Node> nodesMap = new HashMap<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        int nodeIdx = 0;
        for (String line : lines)
        {
            System.out.println(line);
            // Valve VN has flow rate=0; tunnels lead to valves LW, TK
            var parts = parse("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnels? leads? to valves? (.*)", line);
            var node = new Node();
            node.nodeIdx = nodeIdx;
            node.name = parts[1];
            node.pressure = parseInt(parts[2]);
            Collections.addAll(node.nextList, parts[3].split(", "));
            nodes.add(node);
            nodeIdx++;
            
            nodesMap.put(node.name, node);
            
            if (node.pressure > 0)
            {
                nonZeroNodes.add(node);
            }
        }
        
        for (var n : nodes)
        {
            int[] nextNodes = new int[n.nextList.size()];
            for (int idx = 0; idx <nextNodes.length; idx++)
            {
                nextNodes[idx] = nodesMap.get(n.nextList.get(idx)).nodeIdx;
            }
            n.next = nextNodes;
        }
        
        
        Collections.sort(nonZeroNodes, (n1, n2) -> Integer.compare(n2.pressure, n1.pressure));
        
        for (var idx = 0; idx < nonZeroNodes.size(); idx++)
        {
            nonZeroNodes.get(idx).valveBit = idx;
        }
        
        int valveStatesCount = 1 << nonZeroNodes.size();

        
        int [] pressureByMinute = new int[valveStatesCount];
        
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
            pressureByMinute[openedValves] = sum;
        }        
        
        
        var currentStates = new int[1 << 27];
        var prevStates = new int[1 << 27];
        
        for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
        {
            for (var node : nodes)
            {
                var state = encodeState(node.nodeIdx, 0, openedValves);
                currentStates[state] = 0;
            }
        }
        
        for (int timeRemaining = 1; timeRemaining <= 30; timeRemaining++)
        {
            prevStates = currentStates;
            currentStates = new int[1 << 27];
            
            for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
            {
                for (var cave : nodes)
                {
                    var maxPressure = 0;
                    for (var nextIdx = cave.next.length - 1; nextIdx >= 0; nextIdx--)
                    {
                        var fromIdx = cave.next[nextIdx];
                        var from = nodes.get(fromIdx);
                        var fromState = encodeState(fromIdx, 0, openedValves);
                        var fromPressure = prevStates[fromState];
                        var stepPressure = fromPressure + pressureByMinute[openedValves];
                        if (stepPressure > maxPressure)
                        {
                            maxPressure = Math.max(maxPressure, stepPressure);
                        }
                    }
                    
                    if (cave.pressure > 0)
                    {
                        var isClosed = (openedValves >> cave.valveBit) % 2 == 0;
                        if (isClosed)
                        {
                            var changedValves = openedValves | (1 << cave.valveBit);
                            var fromState = encodeState(cave.nodeIdx, 0, changedValves);
                            var fromPressure = prevStates[fromState];
                            var stepPressure = fromPressure + pressureByMinute[openedValves];
                            if (stepPressure > maxPressure)
                            {
                                maxPressure = Math.max(maxPressure, stepPressure);
                            }
                        }
                    }
                    var state = encodeState(cave.nodeIdx, 0, openedValves);
                    currentStates[state] = maxPressure;
                    //System.out.println(""+state + " -> "+maxPressure+ ", from: " + fromStateMax.cave);
                }
            }
        }


        var state = encodeState(nodesMap.get("AA").nodeIdx, 0, 0);
        var pressure = currentStates[state];
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
    
    public int encodeState(int me, int elephant, int openedValves)
    {
        return openedValves + (me << 15) + (elephant << 21); 
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
