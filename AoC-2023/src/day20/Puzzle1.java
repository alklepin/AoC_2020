package day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.queries.Query;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1().solve();
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
//        var inputFile = "input1_test2.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        int result = 0;
        HashMap<String, Module> modules = new HashMap<>(); 
        for (String line : lines)
        {
            var parts = line.split(" -> ");
            var name = parts[0];
            var targets = parts[1].split("[\\s,]+");
            Module m;
            if (name.equals("broadcaster"))
            {
                m = new Broadcaster(name, targets);
            }
            else if (name.startsWith("%"))
            {
                m = new FlipFlop(name.substring(1), targets);
            }
            else
            {
                m = new Conjunction(name.substring(1), targets);
            }
            modules.put(m.name, m);
        }
        ArrayList<String> untyped = new ArrayList<>();
        for (var m : modules.values())
        {
            for (var target : m.targets)
            {
                var t = modules.get(target);
                if (t == null)
                {
                    untyped.add(target);
                }
            }
        }
        for (var m : untyped)
        {
            modules.put(m, new Untyped(m));
        }
        
        for (var m : modules.values())
        {
            for (var target : m.targets)
            {
                var t = modules.get(target);
                if (t == null)
                {
                    throw new IllegalStateException();
                }
                t.addSource(m.name);
            }
        }
        
        long lowPulseCounter = 0;
        long highPulseCounter = 0;
        var start = "broadcaster";
        LinkedList<PulseToModule> pulsesTo = new LinkedList<>();
        var repCount = 1000;
        for (int c = 0; c < repCount; c++)
        {
            pulsesTo.add(new PulseToModule(false, "button", start));
            lowPulseCounter++;
            while (!pulsesTo.isEmpty())
            {
                var nextPulse = pulsesTo.poll();
//                System.out.println(nextPulse);
                var nextMod = modules.get(nextPulse.toModuleName);
                if (nextMod == null)
                {
                    var a = 1;
                }
                var nextPulses = nextMod.pulse(nextPulse);
                for (var p : nextPulses)
                {
                    if (p.pulseType)
                        highPulseCounter++;
                    else
                        lowPulseCounter++;
                }
                nextPulses.forEach((p) -> pulsesTo.add(p));
            }
//            System.out.println("---");
        }
        
        System.out.println(lowPulseCounter * highPulseCounter);
        
    }
    
    public class PulseToModule
    {
        boolean pulseType;
        String fromModuleName;
        String toModuleName;
        public PulseToModule(boolean pulseType, String from, String to)
        {
            super();
            this.pulseType = pulseType;
            this.fromModuleName = from;
            this.toModuleName = to;
        }
        @Override
        public String toString()
        {
            return "PulseToModule [pulseType=" + pulseType + ", fromModuleName="
                + fromModuleName + ", toModuleName=" + toModuleName + "]";
        }
        
        
    }
    
    public abstract class Module
    {
        public String name;
        public String[] targets;
        public Module(String name, String [] targets)
        {
            super();
            this.name = name;
            this.targets = targets;
        }

        public abstract void addSource(String name);

        public abstract Query<PulseToModule> pulse(PulseToModule pulse);
    }
    
    public class Broadcaster extends Module
    {
        public Broadcaster(String name, String [] targets)
        {
            super(name, targets);
        }
        
        public void addSource(String name)
        {
            throw new IllegalStateException();
        }
        
        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            return Query.wrap(targets).select(t -> new PulseToModule(false, name, t));
        }
    }
    public class Untyped extends Module
    {
        public Untyped(String name)
        {
            super(name, new String[0]);
        }

        public void addSource(String name)
        {
        }
        
        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            return Query.empty();
        }
    }
    
    public class FlipFlop extends Module
    {
        boolean state = false;
        
        public FlipFlop(String name, String [] targets)
        {
            super(name, targets);
        }

        public void addSource(String name)
        {
        }
        
        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            if (pulse.pulseType)
                return Query.empty();
            
            state = !state;
            return Query.wrap(targets).select(t -> new PulseToModule(state, name, t));
        }
    }

    public class Conjunction extends Module
    {
        public HashMap<String, Boolean> knownSources = new HashMap<>();
        
        public Conjunction(String name, String [] targets)
        {
            super(name, targets);
        }

        public void addSource(String name)
        {
            knownSources.put(name, false);
        }
        
        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            knownSources.put(pulse.fromModuleName, pulse.pulseType);
            var state = 
                Query.wrap(knownSources.values()).all(s -> s == Boolean.TRUE);
            
            return Query.wrap(targets).select(t -> new PulseToModule(!state, name, t));
        }
    }
    
    
}
