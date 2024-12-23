package day13;

import common.LineParser;
import common.LinesGroup;
import common.Numbers;
import common.PuzzleCommon;
import common.boards.IntPair;

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
    
    static class Data
    {
        public IntPair A;
        public IntPair B;
        public IntPair P;
        public Data(IntPair a, IntPair b, IntPair p)
        {
            super();
            A = a;
            B = b;
            P = p;
        }
        @Override
        public String toString()
        {
            return "Data [A=" + A + ", B=" + B + ", P=" + P + "]";
        }
        
        public Data simplify()
        {
            var nodX = Numbers.nod(A.getX(), B.getX(), P.getX());
            var nodY = Numbers.nod(A.getY(), B.getY(), P.getY());
            return new Data(
                IntPair.of(A.getC() / nodX, A.getC() / nodY),
                IntPair.of(B.getC() / nodX, B.getC() / nodY),
                IntPair.of(P.getC() / nodX, P.getC() / nodY)
                );
        }
        
        public IntPair solve()
        {
            var data = simplify();
            
            var XA = data.A.getX();
            var YA = data.A.getY();
            var XB = data.B.getX();
            var YB = data.B.getY();
            var XP = data.P.getX();
            var YP = data.P.getY();
            
            var nodX = Numbers.nod(XA, XB);
            var nodY = Numbers.nod(YA, YB);
            if (XP % nodX != 0 
                || YP % nodY != 0 
                )
            {
                return null;
            }
            
            var modX = Numbers.nok(XA, XB);
            var modY = Numbers.nok(YA, YB);
            
//            var XA0 = XA * XP
            
            var nX = XP / (XA * XB);  
            var nY = YP / (YA * YB);
            XP = XP % (XA * XB);
            YP = YP % (YA * YB);
            
            return null;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var lineGroups = readAllLineGroups(inputFile, true);
        var result = 0l;
        for (var group : lineGroups)
        {
            var line1 = group.get(0).replace("Button A: X+", "").replace(" Y+", "");
            var line2 = group.get(1).replace("Button B: X+", "").replace(" Y+", "");
            var line3 = group.get(2).replace("Prize: X=", "").replace(" Y=", "");
            var data1 = new LineParser(line1).listOfInts();
            var data2 = new LineParser(line2).listOfInts();
            var data3 = new LineParser(line3).listOfInts();
            var data = new Data(
                IntPair.of(data1.get(0), data1.get(1)),
                IntPair.of(data2.get(0), data2.get(1)),
                IntPair.of(data3.get(0), data3.get(1))
                );
            
            System.out.println(data);
            var found = false;
            var cost = -1;
            for (var b = 0; b < 100; b++)
            {
                for (var a = 0; a < 100; a++)
                {
                    var res = data.A.mult(a).add(data.B.mult(b));
                    if (res.equals(data.P))
                    {
                        found = true;
                        cost = b + 3*a;
                    }
                }
            }
            System.out.println("Cost: "+cost);
            if (found)
                result += cost;
        }
        
        System.out.println(result);
        
    }
}
