package day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntQuadruple;
import common.graph.ImplicitGraph;

public class Puzzle1_v2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1_v2().solve();
    }
    
    static class Blueprint
    {
        public IntQuadruple[] robotCosts;
        
        public Blueprint()
        {
            robotCosts = new IntQuadruple[4];
            Arrays.fill(robotCosts, IntQuadruple.ZERO);
        }
        
        public String toString()
        {
            var result = new StringBuilder();
            for (var v : robotCosts)
            {
                result.append(v).append(' ');
            }
            return result.toString();    
        }
    }
    
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
        var blueprints = new ArrayList<Blueprint>();
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        for (String line : lines)
        {
            var blueprint = new Blueprint();
            
            var parts = line.split("Each (ore|clay|obsidian|geode) robot costs ");
            for (var idx = 1; idx < parts.length; idx++)
            {
                var resStrings = parts[idx].split("(\\.|and| )+");
                var res = IntQuadruple.ZERO;
                for (var resIdx = 0; resIdx < resStrings.length; resIdx+=2)
                {
                    var amount = parseInt(resStrings[resIdx]);
                    switch (resStrings[resIdx+1])
                    {
                        case "ore":
                            res = res.add(IntQuadruple.of(1, 0, 0, 0).mult(amount));
                            break;
                        case "clay":
                            res = res.add(IntQuadruple.of(0, 1, 0, 0).mult(amount));
                            break;
                        case "obsidian":
                            res = res.add(IntQuadruple.of(0, 0, 1, 0).mult(amount));
                            break;
                    }
                }
                blueprint.robotCosts[idx-1] = res;
            }
            blueprints.add(blueprint);
            System.out.println(blueprint);
        }
        
        for (int idx = 0; idx < blueprints.size(); idx++)
        {
            var blueprint = blueprints.get(idx);
            var processor = new Processor(blueprint);
            var cost = processor.evaluate();
            System.out.println(String.format("Bp: %s cost: %s", idx+1, cost));
        }
        
        System.out.println(result);
        
    }

    static class State
    {
        IntQuadruple resources;
        IntQuadruple robots;
        int step;

        public State(int step, IntQuadruple resources, IntQuadruple robots)
        {
            this.resources = resources;
            this.robots = robots;
            this.step = step;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result
                + ((resources == null) ? 0 : resources.hashCode());
            result = prime * result
                + ((robots == null) ? 0 : robots.hashCode());
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
            if (resources == null)
            {
                if (other.resources != null)
                    return false;
            }
            else if (!resources.equals(other.resources))
                return false;
            if (robots == null)
            {
                if (other.robots != null)
                    return false;
            }
            else if (!robots.equals(other.robots))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "State [resources=" + resources + ", robots=" + robots
                + ", step=" + step + "]";
        }
    }
    
    
    static class Processor
    {
        private Blueprint blueprint;
        
        public Processor(Blueprint blueprint)
        {
            this.blueprint = blueprint;
        }
        
        private Iterable<State> nextMoves(State state)
        {
            if (state.step >= 24)
                return Collections.emptyList();

            var nextResources = state.resources.add(state.robots);
            
            var initialState = new State(0, nextResources, IntQuadruple.of(0, 0, 0, 0));

            var result = new ArrayList<State>();
//                
//                .select(s -> new State(state.step+1, s.resources, state.robots.add(s.robots)))
//                .toList();
            
            var res = state.resources;
            var rob = state.robots;
            IntQuadruple newRes;

            newRes = res.minus(blueprint.robotCosts[0]);
            if (newRes.componentGreaterEq(IntQuadruple.ZERO))
            {
                newRes = newRes.add(state.robots);
                result.add(new State(state.step+1, newRes, rob.add(IntQuadruple.of(1, 0, 0, 0))));
            }
            
            newRes = res.minus(blueprint.robotCosts[1]);
            if (newRes.componentGreaterEq(IntQuadruple.ZERO))
            {
                newRes = newRes.add(state.robots);
                result.add(new State(state.step+1, newRes, rob.add(IntQuadruple.of(0, 1, 0, 0))));
            }
            
            newRes = res.minus(blueprint.robotCosts[2]);
            if (newRes.componentGreaterEq(IntQuadruple.ZERO))
            {
                newRes = newRes.add(state.robots);
                result.add(new State(state.step+1, newRes, rob.add(IntQuadruple.of(0, 0, 1, 0))));
            }
            
            newRes = res.minus(blueprint.robotCosts[3]);
            if (newRes.componentGreaterEq(IntQuadruple.ZERO))
            {
                newRes = newRes.add(state.robots);
                result.add(new State(state.step+1, newRes, rob.add(IntQuadruple.of(0, 0, 0, 1))));
            }
            if (result.size() < 3)
            {
                result.add(new State(state.step+1, nextResources, state.robots));
            }
            
            System.out.println("From state "+ state);
            for (var r : result)
            {
                System.out.println("    "+ r);
            }
            
//            if (count++ % 1000 == 0)
//              System.out.println("From state "+ state);
            
            return result;
        }
        
        static long count = 0;
        
        private int evaluate()
        {
            var initialState = new State(0, IntQuadruple.of(0, 0, 0, 0), new IntQuadruple(1, 0, 0, 0));
            var currentStates = new HashSet<State>();
            var nextStates = new HashSet<State>();
            nextStates.add(initialState);
            for (int step = 0; step < 24; step++)
            {
                System.out.println("Step: "+step);
                currentStates = nextStates;
                nextStates = new HashSet<State>();
                for (var s : currentStates)
                {
                    for (var n : nextMoves(s))
                    {
                        nextStates.add(n);
                    }
                }
                System.out.println("Front size: "+nextStates.size());
            }
            var max = 0;
            for (var s : nextStates)
            {
                max = Math.max(max, s.robots.getK());
            }
                
            return max;
        }
    }
}
