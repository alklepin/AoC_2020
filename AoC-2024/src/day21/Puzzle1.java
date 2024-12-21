package day21;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.function.Function;

import common.Function2;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import static common.graph.ImplicitGraph.SearchState;

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
    
    private static String[] keypadA = new String[] {
        "789", 
        "456",   
        "123",   
        " 0A"   
    };
    
    private static char[] keypadAChars = "0123456789A".toCharArray();
    
    private static Board2D boardA = Board2D.parseAsCharsXY(new LinesGroup(keypadA)); 

    private static String[] keypadB = new String[] {
        " ^A", 
        "<v>"   
    };

    private static char[] keypadBChars = "<>v^A".toCharArray();
    
    private static Board2D boardB = Board2D.parseAsCharsXY(new LinesGroup(keypadB)); 
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);

        var firstRobotMoveCosts = findKeypadMoveCosts(keypadBChars, boardB, (from, to) -> 1l);
        printCostMap(keypadBChars, firstRobotMoveCosts);    

//        var secondRobotMoveCosts = findKeypadMoveCosts(keypadAChars, boardA, (from, to) -> {
//            var dir = to.minus(from);
//            if (dir.equals(IntPair.DOWN)) // '^'
//            {
//                var m1 = IntPair.of('A', '^');
//                var m2 = IntPair.of('^', 'A');
//                return firstRobotMoveCosts.get(m1) 
//                
//            }
//        });
//        printCostMap(keypadBChars, secondRobotMoveCosts);    
        
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
    
    
    /**
     * Возвращает, таблицу (c1, c2) -> long
     * Это длина цепочки команд, если рука находилась в состоянии с1 и нужно ввести команду c2  
     * @param keypadChars
     * @param keypadBoard
     * @param moveCosts
     * @return
     */
    public HashMap<IntPair, Long> findKeypadMoveCosts(char [] keypadChars, Board2D keypadBoard, Function2<IntPair, IntPair, Long> moveCosts)
    {
        var keypadMoveCosts = new HashMap<IntPair, Long>();
        for (var c1 : keypadChars)
        {
            for (var c2 : keypadChars)
            {
                var start = keypadBoard.findCharXY(c1).first();
                var end = keypadBoard.findCharXY(c2).first();
                var result = ImplicitGraph.DijkstraLong(start, 
                    s1 -> s1.equals(end), 
                    s2 -> {
                        var d = s2.getDistance();
                        var nodes = keypadBoard.neighbours4XY(s2.getNode())
                            .where(n -> keypadBoard.getCharAtXY(n) != ' ')
                            .select(n -> new SearchState<>(n, d + moveCosts.apply(s2.getNode(), n)));
                        return nodes;
                    });
                var moveKey = IntPair.of(c1, c2);
                keypadMoveCosts.put(moveKey, result.getEndNodeDistance());
            }
        }
        return keypadMoveCosts;
    }
    
    public void printCostMap(char [] keypadChars, HashMap<IntPair, Long> costMap)
    {
        for (var c1 : keypadChars)
        {
            for (var c2 : keypadChars)
            {
                System.out.println(MessageFormat.format("{0} -> {1} : {2}", c1, c2, costMap.get(IntPair.of(c1, c2))));
            }
        }
    }
}
