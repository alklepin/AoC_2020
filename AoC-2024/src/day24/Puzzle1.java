package day24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Supplier;

import common.PuzzleCommon;

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
        
        ArrayList<ConstantGate> inputs = new ArrayList<>();
        ArrayList<Gate> outputs = new ArrayList<>();
        HashMap<String, Gate> gates = new HashMap<>();
        var lineBlocks = readAllLineGroups(inputFile);
        
        for (var line : lineBlocks.get(0))
        {
            var parts = line.split(": ");
            var gate = new ConstantGate(parts[0], parseInt(parts[1]) != 0);
            inputs.add(gate);
            gates.put(gate.getName(), gate);
        }
        
        for (var line : lineBlocks.get(1))
        {
            var parts = line.split(" ");
            var in1 = parts[0];
            var op = parts[1];
            var in2 = parts[2];
            var name = parts[4];
            var gate = switch (op)
            {
                case "XOR" -> new XorGate(name, 
                    () -> gates.get(in1), 
                    () -> gates.get(in2));
                case "OR" -> new OrGate(name, 
                    () -> gates.get(in1), 
                    () -> gates.get(in2));
                case "AND" -> new AndGate(name, 
                    () -> gates.get(in1), 
                    () -> gates.get(in2));
                default -> throw new IllegalStateException();
            };
            if (name.startsWith("z"))
                outputs.add(gate);
            gates.put(gate.getName(), gate);
        }
        
        Collections.sort(outputs, GateComparator.instance);

        long result = 0;
        for (var g : outputs)
        {
            var state = g.getState() ? 1 : 0;
            System.out.println(g.name + ": "+state);
            result = result * 2 + state;
        }
        System.out.println(result);
        
    }
    
    public static class GateComparator implements Comparator<Gate>
    {

        public static GateComparator instance = new GateComparator();
        
        @Override
        public int compare(Gate g1, Gate g2)
        {
            return -g1.name.compareTo(g2.name);
        }
        
    }
    
    abstract static class Gate
    {
        private final String name;
        private boolean knowsState = false;
        private boolean state = false;
        
        public Gate(String name)
        {
            this.name = name;
        }
        
        boolean getState()
        {
            if (knowsState)
                return state;
            state = evaluateState();
            knowsState = true;
            return state;
        }

        abstract public boolean evaluateState();

        public String getName()
        {
            return name;
        }
    }
    
    public static class ConstantGate extends Gate
    {
        
        private final boolean value;
        
        public ConstantGate(String name, boolean value)
        {
            super(name);
            this.value = value;
        }
        
        @Override
        public boolean evaluateState()
        {
            return value;
        }
        
    }

    public static class OrGate extends Gate
    {
        private Supplier<Gate> m_input1;
        private Supplier<Gate> m_input2;
        
        public OrGate(String name, Supplier<Gate> input1, Supplier<Gate> input2)
        {
            super(name);
            m_input1 = input1;
            m_input2 = input2;
        }
        
        @Override
        public boolean evaluateState()
        {
            var in1 = m_input1.get().getState();
            var in2 = m_input2.get().getState();
            return in1 | in2;
        }
    }

    public static class AndGate extends Gate
    {
        private Supplier<Gate> m_input1;
        private Supplier<Gate> m_input2;
        
        public AndGate(String name, Supplier<Gate> input1, Supplier<Gate> input2)
        {
            super(name);
            m_input1 = input1;
            m_input2 = input2;
        }
        
        @Override
        public boolean evaluateState()
        {
            var in1 = m_input1.get().getState();
            var in2 = m_input2.get().getState();
            return in1 & in2;
        }
    }

    public static class XorGate extends Gate
    {
        private Supplier<Gate> m_input1;
        private Supplier<Gate> m_input2;

        public XorGate(String name, Supplier<Gate> input1, Supplier<Gate> input2)
        {
            super(name);
            m_input1 = input1;
            m_input2 = input2;
        }

        @Override
        public boolean evaluateState()
        {
            var in1 = m_input1.get().getState();
            var in2 = m_input2.get().getState();
            return in1 ^ in2;
        }
    }
}
