
package day24;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

import common.queries.Query;

public class GateNetwork
{
    public static class LoopDetectedException extends RuntimeException
    {
        private static final long serialVersionUID = 3021803109074523542L;
    }
    
    private ArrayList<Gate> inputs = new ArrayList<>();
    private HashSet<Gate> outputs = new HashSet<>();
    private HashMap<String, Gate> gates = new HashMap<>();
    
    private static String[] EMPTY_STRING_ARRAY = new String[0];
    
    abstract static class Gate
    {
        private String name;
        private boolean knowsState = false;
        private boolean evaluating = false;
        private boolean state = false;
        private String[] dependsOn;
        
        public Gate(String name, String... dependencies)
        {
            this.name = name;
            dependsOn = dependencies;
        }
        
        boolean getState()
        {
            if (knowsState)
                return state;
            if (evaluating)
                throw new LoopDetectedException();
            evaluating = true;
            try
            {
                state = evaluateState();
            }
            finally
            {
                evaluating = false;
            }
            knowsState = true;
            return state;
        }

        abstract protected boolean evaluateState();

        public String getName()
        {
            return name;
        }
        
        public String toString()
        {
            return name;
        }
        
        public String[] dependsOn()
        {
            return dependsOn;
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
        protected boolean evaluateState()
        {
            return value;
        }
        
    }

    public static class ExternalConstantGate extends Gate
    {
        
        private final Supplier<Boolean> value;
        
        public ExternalConstantGate(String name, Supplier<Boolean> value)
        {
            super(name);
            this.value = value;
        }
        
        @Override
        protected boolean evaluateState()
        {
            return value.get();
        }
        
    }

    public class OrGate extends Gate
    {
        private String m_name1;
        private String m_name2;
        
        public OrGate(String name, String name1, String name2)
        {
            super(name, name1, name2);
            m_name1 = name1;
            m_name2 = name2;
        }
        
        @Override
        protected boolean evaluateState()
        {
            var in1 = gates.get(m_name1).getState();
            var in2 = gates.get(m_name2).getState();
            return in1 | in2;
        }
    }

    public class AndGate extends Gate
    {
        private String m_name1;
        private String m_name2;
        
        public AndGate(String name, String name1, String name2)
        {
            super(name, name1, name2);
            m_name1 = name1;
            m_name2 = name2;
        }
        
        @Override
        protected boolean evaluateState()
        {
            var in1 = gates.get(m_name1).getState();
            var in2 = gates.get(m_name2).getState();
            return in1 & in2;
        }
    }

    public class XorGate extends Gate
    {
        private String m_name1;
        private String m_name2;
        
        public XorGate(String name, String name1, String name2)
        {
            super(name, name1, name2);
            m_name1 = name1;
            m_name2 = name2;
        }

        @Override
        protected boolean evaluateState()
        {
            var in1 = gates.get(m_name1).getState();
            var in2 = gates.get(m_name2).getState();
            return in1 ^ in2;
        }
    }

    public void reset()
    {
        for (var g : gates.values())
        {
            g.knowsState = false;
        }
    }

    public void addInputGate(String name)
    {
        inputs.add(gates.get(name));
    }
    
    public void addOutputGate(String name)
    {
        outputs.add(gates.get(name));
    }
    
    public void addConstantGate(String name, boolean value)
    {
        var gate = new ConstantGate(name, value);
        gates.put(gate.getName(), gate);
    }
    
    public void addConstantGate(String name, Supplier<Boolean> value)
    {
        var gate = new ExternalConstantGate(name, value);
        gates.put(gate.getName(), gate);
    }

    public void addOrGate(String name, String op1Name, String op2Name)
    {
        var gate = new OrGate(name, op1Name, op2Name);
        gates.put(name, gate);
    }

    public void addXorGate(String name, String op1Name, String op2Name)
    {
        var gate = new XorGate(name, op1Name, op2Name);
        gates.put(name, gate);
    }

    public void addAndGate(String name, String op1Name, String op2Name)
    {
        var gate = new AndGate(name, op1Name, op2Name);
        gates.put(name, gate);
    }
    
    public HashSet<String> dependsOn(String name)
    {
        HashSet<String> result = new HashSet<>();
        LinkedList<String> queue = new LinkedList<>();
        queue.add(name);
        result.add(name);
        while (queue.size() > 0)
        {
            var n = queue.poll();
            var dependencies = gates.get(n).dependsOn;
            for (var next : dependencies)
            {
                if (result.add(next)) 
                {
                    queue.add(next);
                }
            }
        }
        return result;
    }
    
    public Iterable<String> getOutputNames()
    {
        return Query.wrap(outputs).select(g -> g.getName());
    }

    public Iterable<String> getInputNames()
    {
        return Query.wrap(inputs).select(g -> g.getName());
    }

    public boolean getState(String name)
    {
        return gates.get(name).getState();
    }

    public Set<String> allGateNames()
    {
        return gates.keySet();
    }

    public void swapGates(String name1, String name2)
    {
        var gate1 = gates.get(name1);
        var gate2 = gates.get(name2);
        gate1.name = name2;
        gate2.name = name1;
        gates.put(name1,  gate2);
        gates.put(name2,  gate1);
        if (outputs.contains(gate1) && !outputs.contains(gate2)) 
        {
            outputs.remove(gate1);
            outputs.add(gate2);
        }
        else if (outputs.contains(gate2) && !outputs.contains(gate1))
        {
            outputs.remove(gate2);
            outputs.add(gate1);
        }
    }
}

