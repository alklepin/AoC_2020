package day24;

import java.util.ArrayList;
import java.util.Collections;

import common.PuzzleCommon;
import common.queries.Query;
import common.queries.SubsetGenerator;

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
//        var inputFile = "input2_test.txt";
        
        GateNetwork gateNetwork = new GateNetwork();
        var lineBlocks = readAllLineGroups(inputFile);
        
        var inputsX = new ArrayList<String>();
        var inputsY = new ArrayList<String>();
        
        for (var line : lineBlocks.get(0))
        {
            var parts = line.split(": ");
            var name = parts[0];
//            gateNetwork.addConstantGate(parts[0], () -> parseInt(parts[1]) != 0);

            if (name.startsWith("x"))
                inputsX.add(name);
            if (name.startsWith("y"))
                inputsY.add(name);
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
            if (name.startsWith("z"))
                gateNetwork.addOutputGate(name);
        }
        
        var x = new long[]{0};
        var y = new long[]{0};
        
        for (var n : inputsX)
        {
            var idx = parseInt(n.replace("x", "1"))-100;
            gateNetwork.addConstantGate(n, () -> (x[0] >> idx) % 2 == 1);
        }

        for (var n : inputsY)
        {
            var idx = parseInt(n.replace("y", "1"))-100;
            gateNetwork.addConstantGate(n, () -> (y[0] >> idx) % 2 == 1);
        }
        
        // Manually recovered swaps (see solution.txt)
        gateNetwork.swapGates("z12", "kwb");
        gateNetwork.swapGates("z16", "qkf");
        gateNetwork.swapGates("z24", "tgr");
        gateNetwork.swapGates("jqn", "cph");
        
        for (var g : Query.wrap(gateNetwork.allGateNames()).sorted())
        {
            var dep0 = gateNetwork.dependsOn(g);
            System.out.println(""+g+": "+Query.wrap(dep0).sorted().join());
        }
        
        for (var bit = 0; bit < 44; bit++)
        {
            for (var val = 0; val < 4; val++)
            {                
                x[0] = ((long)(val % 2)) << bit;
                y[0] = ((long)(val / 2)) << bit;
                gateNetwork.reset();

                var z = getZ(gateNetwork);
                var zBit = (z >> bit) % 2;
                var valid = zBit == (((x[0] + y[0]) >> bit) % 2);
                if (!valid)
                    System.out.println("Bit "+bit+" error!");
            }
        }
        
//      valid = z == x[0] + y[0];
        
        
        System.exit(0);
        
        
        var gates = Query.wrap(gateNetwork.allGateNames())
            .where(name -> !name.startsWith("x")
                && !name.startsWith("y")
                ).toList();
        Collections.sort(gates);
        
//        long limit = 1l << 45;
        long limit = 1l << 5;
//        for (var s : SubsetGenerator.subsets(gates, 8))
        for (var s : SubsetGenerator.subsets(gates, 4))
        {
            if (s.get(0).equals("z00") && s.get(1).equals("z01"))
            {
                var a = 1;
            }
            gateNetwork.swapGates(s.get(0), s.get(1));
            gateNetwork.swapGates(s.get(2), s.get(3));
//            gateNetwork.swapGates(s.get(4), s.get(5));
//            gateNetwork.swapGates(s.get(6), s.get(7));
            var valid = true;
            check_loop:
            for (x[0] = 1; x[0] < limit; x[0] = x[0] << 1)
            {
                for (y[0] = 1; y[0] < limit; y[0] = y[0] << 1)
                {
                    gateNetwork.reset();
                    try
                    {
                        var z = getZ(gateNetwork);
//                        valid = z == x[0] + y[0];
                        valid = z == (x[0] & y[0]);
                    }
                    catch (GateNetwork.LoopDetectedException ex)
                    {
                        valid = false;
                    }
                    if (!valid)
                        break check_loop;
                }
            }
            if (valid)
            {
                System.out.println(Query.wrap(s).join());
            }

            gateNetwork.swapGates(s.get(0), s.get(1));
            gateNetwork.swapGates(s.get(2), s.get(3));
//            gateNetwork.swapGates(s.get(4), s.get(5));
//            gateNetwork.swapGates(s.get(6), s.get(7));
        
        }
        
        
//        var outputs = new ArrayList<String>();
//        for (var name : gateNetwork.getOutputNames())
//        {
//            outputs.add(name);
//        }
//        
//        Collections.sort(outputs, (s1, s2) -> -(s1.compareTo(s2)));
//
//        long result = 0;
//        for (var name : outputs)
//        {
//            var state = gateNetwork.getState(name) ? 1 : 0;
//            System.out.println(name + ": "+state);
//            result = result * 2 + state;
//        }
//        System.out.println(result);
//        System.out.println(getZ(gateNetwork));
        
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
