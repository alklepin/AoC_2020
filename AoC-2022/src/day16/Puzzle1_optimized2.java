package day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import common.Convert;
import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.Graph;
import common.queries.Query;

public class Puzzle1_optimized2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1_optimized2().solve();
    }
    
    static class Cave
    {
        public String name;
        public int pressure;
        public ArrayList<String> nextList = new ArrayList<>();
        public int[] next;

        public int nodeIdx;
        public int valveBit;
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var nonZeroNodes = new ArrayList<Cave>();
        ArrayList<Cave> nodes = new ArrayList<>();
        HashMap<String, Cave> nodesMap = new HashMap<>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int nodeIdx = 0;
        for (String line : lines)
        {
            System.out.println(line);
            // Valve VN has flow rate=0; tunnels lead to valves LW, TK
            var parts = parse("Valve (\\w{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.*)", line);
            var cave = new Cave();
            cave.nodeIdx = nodeIdx;
            cave.name = parts[1];
            cave.pressure = parseInt(parts[2]);
            Collections.addAll(cave.nextList, parts[3].split(", "));
            nodes.add(cave);
            nodeIdx++;
            
            nodesMap.put(cave.name, cave);
            
            if (cave.pressure > 0)
            {
                nonZeroNodes.add(cave);
            }
        }
        
        for (var n : nodes)
        {
            var nextList = Query.wrap(n.nextList)
                .select(name -> nodesMap.get(name))
                .select(cave -> cave.nodeIdx).toList();
            n.next = Convert.toListOfInt(nextList);
            n.next = Convert.toListOfInt(nextList);
        }
        
//        Collections.sort(nonZeroNodes, (n1, n2) -> Integer.compare(n2.pressure, n1.pressure));
        
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
        
        
        var nextStates = new int[1 << 27];
        int[] currentStates = null;
        
        Arrays.fill(nextStates, -1);
        var initState = encodeState(nodesMap.get("AA").nodeIdx, 0);
        nextStates[initState] = 0;
        
        for (int time = 1; time <= 30; time++)
        {
            currentStates = nextStates;
            nextStates = new int[1 << 27];
            Arrays.fill(nextStates, -1);
            
            for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
            {
                for (var cave : nodes)
                {
                    var currentState = encodeState(cave.nodeIdx, openedValves);
                    var currentPressure = currentStates[currentState];
                    if (currentPressure < 0)
                        continue; // unreachable
                    
                    for (var nextIdx = cave.next.length - 1; nextIdx >= 0; nextIdx--)
                    {
                        var nextNodeIdx = cave.next[nextIdx];
                        var nextState = encodeState(nodes.get(nextNodeIdx).nodeIdx, openedValves);
                        var nextPressure = currentPressure + pressureByMinute[openedValves];
                        if (nextPressure > nextStates[nextState])
                        {
                            nextStates[nextState] = nextPressure;
                            
//                            System.out.println("Time: " + time + " cave: " + cave.name + "(" + currentPressure + ", " 
//                                + Integer.toString(openedValves, 2) + ") -> " + nextPressure+ ", to: " + nodes.get(nextNodeIdx).name);
                        }
                    }
                    
                    if (cave.pressure > 0)
                    {
                        var isClosed = (openedValves >> cave.valveBit) % 2 == 0;
                        if (isClosed)
                        {
                            var changedValves = openedValves | (1 << cave.valveBit);
                            var nextState = encodeState(cave.nodeIdx, changedValves);
                            var nextPressure = currentPressure + pressureByMinute[openedValves];
                            if (nextPressure > nextStates[nextState])
                            {
                                nextStates[nextState] = nextPressure;
//                                System.out.println("Time: " + time + " cave: " + cave.name + "(" + currentPressure + ", " 
//                                    + Integer.toString(openedValves, 2) + ") -> " + nextPressure+ " v: "+Integer.toString(changedValves, 2));
                            }
                        }
                    }
                    //System.out.println(""+state + " -> "+maxPressure+ ", from: " + fromStateMax.cave);
                }
            }
        }

        var maxPressure = 0;
        for (var openedValves = 0; openedValves < valveStatesCount; openedValves++)
        {
            for (var cave : nodes)
            {
                var state = encodeState(cave.nodeIdx, openedValves);
                var pressure = nextStates[state];
                if (pressure >= maxPressure)
                    maxPressure = pressure;
            }
        }        

        System.out.println(maxPressure);
    }
    
    public int encodeState(int me, int openedValves)
    {
        return openedValves + (me << 15); 
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
