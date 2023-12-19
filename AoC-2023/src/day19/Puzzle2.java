package day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import common.LinesGroup;
import common.PuzzleCommon;
import common.RangeLong;
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
    
    static RangeLong FULL_RANGE = new RangeLong(1, 4001);
    static ArrayList<State> results = new ArrayList<>();
    static HashMap<String, Workflow> wfs;
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        LinesGroup lines = readAllLines(inputFile);
        var groups = lines.splitByEmptyLines();
        
        wfs = parseWorkflows(groups.get(0));

        State start = new State(FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE);
        getRanges("in", start);
        long result = 0;
        for (var r : results)
        {
            result += r.size();
        }
//        var start = new SrchState("in", -1);
//        var res = ImplicitGraph.BFS(start, null, state ->
//        {
//            var wf = wfs.get(state.wf);
//            var result = new ArrayList<SrchState>();
//            var acts = new ArrayList<WFAction>();
//            var ruleIdx = 0;
//            for (var c : wf.actions)
//            {
//                var next = new SrchState(c.target, ruleIdx);
//                ruleIdx++;
//            }
//            return  result;
//        });
        
        System.out.println(result);
        
    }

    
    public void getRanges(String wfname, State state)
    {
        if (wfname.equals("A"))
        {
            results.add(state);
            return;
        }
        if (wfname.equals("R"))
        {
            return;
        }
        var wf = wfs.get(wfname);
        if (wf == null)
            throw new IllegalStateException();
        var currentRanges = state.copy();
        for (var a : wf.actions)
        {
            var nextState = currentRanges.copy().intersect(a.getAcceptedState());
            getRanges(a.target, nextState);
            
            currentRanges = currentRanges.intersect(a.getDeclinedState());
            if (currentRanges.isEmpty())
                break;
        }
    }
    
    static class State
    {
        public static State EMPTY = new State(RangeLong.EMPTY, RangeLong.EMPTY, RangeLong.EMPTY, RangeLong.EMPTY);
        public static State ANY = new State(FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE);
        
        RangeLong rangeX;
        RangeLong rangeM;
        RangeLong rangeA;
        RangeLong rangeS;
        
        public State(RangeLong rangeX, RangeLong rangeM,
            RangeLong rangeA, RangeLong rangeS)
        {
            super();
            this.rangeX = rangeX;
            this.rangeM = rangeM;
            this.rangeA = rangeA;
            this.rangeS = rangeS;
        }

        public State(char c, RangeLong range)
        {
            this.rangeX = FULL_RANGE;
            this.rangeM = FULL_RANGE;
            this.rangeA = FULL_RANGE;
            this.rangeS = FULL_RANGE;
            switch (c)
            {
                case 'x':
                    rangeX = range;
                    break;
                case 'm':
                    rangeM = range;
                    break;
                case 'a':
                    rangeA = range;
                    break;
                case 's':
                    rangeS = range;
                    break;
            }
        }
        
        public State(State state)
        {
            this.rangeX = state.rangeX;
            this.rangeM = state.rangeM;
            this.rangeA = state.rangeA;
            this.rangeS = state.rangeS;
        }
        
        public State copy()
        {
            return new State(this);
        }
        
        public long size()
        {
            return rangeX.length() 
                * rangeM.length() 
                * rangeA.length() 
                * rangeS.length(); 
        }
        
        
        public boolean isEmpty()
        {
            return rangeX.isEmpty()
                && rangeM.isEmpty()
                && rangeA.isEmpty()
                && rangeS.isEmpty(); 
        }
        
        public State intersect(State other)
        {
            return new State(
                rangeX.intersect(other.rangeX),
                rangeM.intersect(other.rangeM),
                rangeA.intersect(other.rangeA),
                rangeS.intersect(other.rangeS)
                );
        }
        
    }
    

    private HashMap<String, Workflow> parseWorkflows(LinesGroup linesGroup)
    {
        HashMap<String, Workflow> result = new HashMap<>();
        for (var line : linesGroup)
        {
            var wf = new Workflow(line);
            result.put(wf.name,  wf);
        }
        return result;
    }

    static class WFAction
    {
        public char source;
        public RangeLong acceptedRange;
        public RangeLong declinedRange;
        public String target;

        public WFAction()
        {
            
        }
        public WFAction(String p)
        {
            var parts = p.split("\\:");
            if (parts.length > 1)
            {
                source = parts[0].charAt(0);
                var value = Integer.parseInt(parts[0].substring(2));
                var c = parts[0].charAt(1);
                
                if (c == '>')
                {
                    acceptedRange = FULL_RANGE.intersect(new RangeLong(value+1, Integer.MAX_VALUE));
                    declinedRange = FULL_RANGE.intersect(new RangeLong(Integer.MIN_VALUE, value + 1));
                }
                else if (c == '<')
                {
                    acceptedRange = FULL_RANGE.intersect(new RangeLong(Integer.MIN_VALUE, value));
                    declinedRange = FULL_RANGE.intersect(new RangeLong(value, Integer.MAX_VALUE));
                }
                target = parts[1].trim();
            }
            else
            {
                source = '-';
                acceptedRange = FULL_RANGE;
                declinedRange = RangeLong.EMPTY;
                target = p.trim();
            }
        }
        
        public State getAcceptedState()
        {
            return new State(source, acceptedRange);
        }

        public State getDeclinedState()
        {
            return new State(source, declinedRange);
        }

        @Override
        public String toString()
        {
            return "WFAction [source=" + source + ", acceptedRange="
                + acceptedRange + ", declinedRange=" + declinedRange
                + ", target=" + target + "]";
        }

    }
    
    static class Workflow
    {
        public String name;
        public ArrayList<WFAction> actions = new ArrayList<>();
        
        public Workflow(String line)
        {
            var parts = line.split("[\\{\\}]");
            name = parts[0];
            var actions = parts[1];
            var actParts = actions.split(",");
            for (var p : actParts)
            {
                var act = new WFAction(p);
                this.actions.add(act);
            }
            
        }
        
        
    }
}
