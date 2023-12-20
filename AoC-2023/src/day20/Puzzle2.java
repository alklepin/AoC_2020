package day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.queries.Query;

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

//        for (var m : modules.values())
//        {
//            var sources = Query.wrap(m.knownSources.keySet()).join();
//            System.out.println("" + m.getType() + m.name + " <- " + sources);
//        }
//        System.out.println("===");
        
        long lowPulseCounter = 0;
        long highPulseCounter = 0;
        var start = "broadcaster";
        LinkedList<PulseToModule> pulsesTo = new LinkedList<>();
        var repCount = 1000000;
        var seqCount = 0;
        var prevStep = 0;
        for_loop:
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
                    // Here we have to find periods of pulsing
                    // of modules which are at distance 2 from rx
                    // and then multiply the found periods
//                    var modName = "mp";
//                    var modName = "ng";
//                    var modName = "qb";
//                    var modName = "qt";
                    var modName = "ck";
//                    if (p.toModuleName.equals(nm))
                    if (p.fromModuleName.equals(modName) && !p.pulseType)
                    {
                        seqCount++;
                        var delta = (c+1 - prevStep);
                        prevStep = c+1;

//                        if ((seqCount % 4) == 0)
                        {
                            System.out.println("Nm: "+modName+" pulseType:"+p.pulseType);
                            System.out.println("Presses: "+ (c+1) + " delta: "+delta);
                        }
                        //break for_loop;
                    }
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
        public TreeMap<String, Boolean> knownSources = new TreeMap<>();
        
        public Module(String name, String [] targets)
        {
            super();
            this.name = name;
            this.targets = targets;
        }

        public void addSource(String name)
        {
            knownSources.put(name, false);
        }

        public abstract Query<PulseToModule> pulse(PulseToModule pulse);
        public abstract char getType();
    }
    
    public class Broadcaster extends Module
    {
        public Broadcaster(String name, String [] targets)
        {
            super(name, targets);
        }
        
        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            return Query.wrap(targets).select(t -> new PulseToModule(false, name, t));
        }

        public char getType()
        {
            return '$';
        }
    }
    
    public class Untyped extends Module
    {
        public Untyped(String name)
        {
            super(name, new String[0]);
        }

        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            return Query.empty();
        }
        
        public char getType()
        {
            return '^';
        }
    }
    
    public class FlipFlop extends Module
    {
        boolean state = false;
        
        public FlipFlop(String name, String [] targets)
        {
            super(name, targets);
        }

        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            if (pulse.pulseType)
                return Query.empty();
            
            state = !state;
            return Query.wrap(targets).select(t -> new PulseToModule(state, name, t));
        }

        public char getType()
        {
            return '%';
        }
    }

    public class Conjunction extends Module
    {
        public Conjunction(String name, String [] targets)
        {
            super(name, targets);
        }

        public Query<PulseToModule> pulse(PulseToModule pulse)
        {
            knownSources.put(pulse.fromModuleName, pulse.pulseType);
            var state = 
                Query.wrap(knownSources.values()).all(s -> s == Boolean.TRUE);
            
//            StringBuilder sb = new StringBuilder();
//            for (var e : knownSources.entrySet())
//            {
//                sb.append(e.getValue() ? '1' : '0');
//            }
//            System.out.println("Con: " + name + " state: "+ sb);
            return Query.wrap(targets).select(t -> new PulseToModule(!state, name, t));
        }
        
        public char getType()
        {
            return '&';
        }
    }
    
    
}
