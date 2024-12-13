package day13;

import common.LineParser;
import common.Numbers;
import common.PuzzleCommon;
import common.boards.LongPair;
import common.queries.Query;

public class Puzzle2_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    static class Data
    {
        public LongPair A;
        public LongPair B;
        public LongPair P;
        public Data(LongPair a, LongPair b, LongPair p)
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
                LongPair.of(A.getX() / nodX, A.getY() / nodY),
                LongPair.of(B.getX() / nodX, B.getY() / nodY),
                LongPair.of(P.getX() / nodX, P.getY() / nodY)
                );
        }
        
        public LongPair solve()
        {
            // attempt to solve via Diophantine equation
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
            
            var besuSolX = Numbers.solveBesuModulo(XA, XB);
            var besuSolY = Numbers.solveBesuModulo(YA, YB);

            var seqXper = besuSolX.a * besuSolX.b / besuSolX.nod; 
            var seqYper = besuSolY.a * besuSolY.b / besuSolY.nod;
            
            var seqXmult = XP % seqXper; 
            var seqYmult = YP % seqYper;
            
            var xA = (besuSolX.x * seqXmult) % seqXper;
            var xB = (besuSolX.y * seqXmult) % seqXper;
            
            var yA = (besuSolY.x * seqYmult) % seqYper;
            var yB = (besuSolY.y * seqYmult) % seqYper;
            
            var seqXfirst = besuSolX.a*xA + besuSolX.b*xB;
            var seqYfirst = besuSolY.a*yA + besuSolY.b*yB;

            var seq1success = (XP - seqXfirst) % seqXper == 0;
            var seq2success = (YP - seqYfirst) % seqYper == 0;
            
            if (!seq1success || !seq2success)
            {
                return null;
            }
            
//            if (c % besuSol.nod != 0)
//                return Query.empty(); // no solutions
//            
//            var nok = a * b / besuSol.nod;
//            var cm = c % nok;
//            return Query.rangeLong(-nok/b, 2*nok/b)
//                .selectMany(aRes -> Query.rangeLong(-nok/a, 2*nok/a).select(bRes -> LongPair.of(aRes, bRes)));
            
            return null;
        }
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
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
                LongPair.of(data1.get(0), data1.get(1)),
                LongPair.of(data2.get(0), data2.get(1)),
                LongPair.of(data3.get(0)+10000000000000l, data3.get(1)+10000000000000l)
                );
            
            System.out.println(data);
            var res = data.solve();
            if (res != null)
            {
                var cost = res.getY() + res.getX()*3;
                System.out.println("Cost: "+cost);
                result += cost;
            }
            else
            {
                System.out.println("Cost: not found");
            }
        }
        
        System.out.println(result);
        
    }
}
