package day24;

import common.PuzzleCommon;

public class Puzzle1_good extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle1_good().solve();
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
        
        GateNetwork gateNetwork = new GateNetwork();
        var lineBlocks = readAllLineGroups(inputFile);
        
        for (var line : lineBlocks.get(0))
        {
            var parts = line.split(": ");
            gateNetwork.addConstantGate(parts[0], () -> parseInt(parts[1]) != 0);
        }
        
        for (var line : lineBlocks.get(1))
        {
            var parts = line.split(" ");
            var in1 = parts[0];
            var op = parts[1];
            var in2 = parts[2];
            var name = parts[4];
            switch (op)
            {
                case "XOR" -> gateNetwork.addXorGate(name, in1, in2); 
                case "OR" -> gateNetwork.addOrGate(name, in1, in2);
                case "AND" -> gateNetwork.addAndGate(name, in1, in2);
                default -> throw new IllegalStateException();
            };
            if (name.startsWith("x"))
                gateNetwork.addInputGate(name);
            if (name.startsWith("z"))
                gateNetwork.addOutputGate(name);
        }
        System.out.println(getZ(gateNetwork));
    }

    private long getZ(GateNetwork net)
    {
        long result = 0;
        for (var n : net.getOutputNames())
        {
            var idx = parseInt(n.replace("z", "1"))-100;
            if (net.getState(n))
                result |= 1l << idx;
        }
        return result;
    }
}
