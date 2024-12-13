package day13;

import common.LineParser;
import common.Numbers;
import common.PuzzleCommon;
import common.boards.LongPair;
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
//            var data = simplify();
            var data = this;
            
            long XA = data.A.getX();
            long YA = data.A.getY();
            long XB = data.B.getX();
            long YB = data.B.getY();
            long XP = data.P.getX();
            long YP = data.P.getY();

            var pa = (XP*YB - YP*XB)/(double)(XA*YB - XB*YA);
            var pb = (YP*XA - XP*YA)/(double)(XA*YB - XB*YA);
            long pal = Math.round(pa);
            long pbl = Math.round(pb);
            
            System.out.println(""+pal+ "   "+pbl);
            var resX = pal * XA + pbl * XB; 
            var resY = pal * YA + pbl * YB; 
            System.out.println("res "+resX+ "   "+resY);
            System.out.println("Prz "+XP+ "   "+YP);
            System.out.println("dif "+(XP-resX)+ "   "+(YP-resY));
            
            if (XP-resX == 0 && YP-resY == 0)
                return new LongPair(pal, pbl);
            
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
