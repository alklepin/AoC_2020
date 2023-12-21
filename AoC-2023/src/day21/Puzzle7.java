package day21;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.graph.ImplicitGraph.SearchResult;
import common.queries.Query;

public class Puzzle7 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle7().solve();
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
//        var inputFile = "input1_test2.txt";
//        var inputFile = "input1_test3.txt";
//        var inputFile = "input1_test4.txt";
//        var inputFile = "input1_test5.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        
        var startCell = board.allCellsXY().where(c -> board.getCharAtXY(c) == 'S').single(0);
        var queue = new LinkedList<IntPair>();
        queue.add(startCell);
        var dirs = new IntPair[] {IntPair.UP, IntPair.DOWN, IntPair.LEFT, IntPair.RIGHT};
        var dims = new IntPair(board.getWidth(), board.getHeigth());
        
        var startBFSResult = new BoardData(board, ImplicitGraph.BFS(startCell, null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')), false);
        
        var fromSResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.DOWN.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));
        
        var fromNResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.UP.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));
        
        var fromEResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.RIGHT.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));
        
        var fromWResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.LEFT.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));

        var fromSEResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.DOWN_RIGHT.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));

        var fromNEResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.UP_RIGHT.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));

        var fromSWResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.DOWN_LEFT.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));

        var fromNWResult = new BoardData(board, ImplicitGraph.BFS(startCell.add(IntPair.UP_LEFT.mult(board.getHeigth()/2)), null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));
        
        
        long result = 0;
        var bound = 26501365;
//        var bound = 589;
        var bbound  = (bound - board.getWidth()/2) / board.getWidth() + 1;
        var boardSize = board.getWidth();
        
        var targets = Query.sequenceOf(
            Generators.ray(Pair.of(0, bbound), IntPair.DOWN_RIGHT, Math.min(1, bbound), true),
            Generators.ray(Pair.of(bbound, 0), IntPair.DOWN_LEFT, Math.min(1, bbound), true),
            Generators.ray(Pair.of(0, -bbound), IntPair.UP_LEFT, Math.min(1, bbound), true),
            Generators.ray(Pair.of(-bbound, 0), IntPair.UP_RIGHT, Math.min(1, bbound), true),
            Generators.ray(Pair.of(0, bbound-1), IntPair.DOWN_RIGHT, Math.min(1, bbound-1), true),
            Generators.ray(Pair.of(bbound-1, 0), IntPair.DOWN_LEFT, Math.min(1, bbound-1), true),
            Generators.ray(Pair.of(0, -bbound+1), IntPair.UP_LEFT, Math.min(1, bbound-1), true),
            Generators.ray(Pair.of(-bbound+1, 0), IntPair.UP_RIGHT, Math.min(1, bbound-1), true),
            Generators.ray(Pair.of(0, bbound+1), IntPair.DOWN_RIGHT, Math.min(1, bbound+1), false),
            Generators.ray(Pair.of(bbound+1, 0), IntPair.DOWN_LEFT, Math.min(1, bbound+1), false),
            Generators.ray(Pair.of(0, -bbound-1), IntPair.UP_LEFT, Math.min(1, bbound+1), false),
            Generators.ray(Pair.of(-bbound-1, 0), IntPair.UP_RIGHT, Math.min(1, bbound+1), false)
            ).distinct();
        
//        for (var bb : targets)
//        {
//            var x = bb.getX();
//            var y = bb.getY();
//            System.out.println("X= "+x+" Y= "+y);
//        }
        for (var bb : targets)
        {
            var x = bb.getX();
            var y = bb.getY();
            var bdist = Math.abs(x) + Math.abs(y);
            var mult = (x == 0) || (y == 0) ? 1 : bdist-1;
            
            BoardData data = null;
            if (bdist == 0)
                data = startBFSResult;
            else if (y == 0)
            {
                if (x < 0)
                    data = fromEResult;
                else
                    data = fromWResult;
            }
            else if (x == 0)
            {
                if (y < 0)
                    data = fromNResult;
                else
                    data = fromSResult;
            }
            else if (y > 0) 
            {
                if (x < 0)
                    data = fromSEResult;
                else
                    data = fromSWResult;
            }
            else if (y < 0) 
            {
                if (x < 0)
                    data = fromNEResult;
                else
                    data = fromNWResult;
            }
            
            long count = 0;
            var baseDist = bdist * boardSize;
            if (x != 0 || y != 0)
                baseDist = baseDist - board.getWidth()/2;
            if (x != 0 && y != 0)
                baseDist = baseDist - board.getWidth()/2;
            
            for (var by = 0; by < board.getHeigth(); by ++)
            {
                for (var bx = 0; bx < board.getWidth(); bx ++)
                {
                    var cell = Pair.of(
                        (x * boardSize) + bx, 
                        (y * boardSize) + by
                        );
                    var testCell = cell.componentModulo(dims);
                    var p = data.result.getPath(testCell);
                    if (p != null 
                        && (baseDist + p.size() - 1 <= bound)
                        && (baseDist + p.size() - 1) % 2 == bound % 2
                        )
                    {
                        count++;
                        //System.out.println("X= "+x+" Y= "+y+ " bx= "+bx+ " by= "+by+" cell: "+cell);
                    }
                }
            }
            result += count * mult;
            System.out.println("X= "+x+" Y= "+y+ " count: " +count+" mult= "+mult);
            System.out.println("---");    
        }
        System.out.println("Perimeter:" + result);    
        
        long result1 = 0;
        if (bbound > 1)
            result1 += startBFSResult.oddCount;
        for (var idx = 1; idx < bbound-1; idx++)
        {
            long evenSumUp = fromEResult.evenCount + fromWResult.evenCount + fromNResult.evenCount + fromSResult.evenCount;
            long evenSumDiag = fromSEResult.evenCount + fromSWResult.evenCount + fromNEResult.evenCount + fromNWResult.evenCount;
            long oddSumUp = fromEResult.oddCount + fromWResult.oddCount + fromNResult.oddCount + fromSResult.oddCount;
            long oddSumDiag = fromSEResult.oddCount + fromSWResult.oddCount + fromNEResult.oddCount + fromNWResult.oddCount;
            long c;
            if ((boardSize/2) % 2 == 0)
            {
                if (idx % 2 == 1)
                    c = evenSumUp + (idx-1)*evenSumDiag;
                else
                    c = oddSumUp + (idx-1)*oddSumDiag;
                
            }
            else
            {
                if (idx % 2 == 1)
                    c = oddSumUp + (idx-1)*evenSumDiag;
                else
                    c = evenSumUp + (idx-1)*oddSumDiag;
                
            }
            
//            var evenSum = fromEResult.evenCount + fromWResult.evenCount + fromNResult.evenCount + fromSResult.evenCount
//                + (idx-1)*(fromSEResult.oddCount + fromSWResult.oddCount + fromNEResult.oddCount + fromNWResult.oddCount); 
//            var oddSum = fromEResult.oddCount + fromWResult.oddCount + fromNResult.oddCount + fromSResult.oddCount 
//                + (idx-1)*(fromSEResult.evenCount + fromSWResult.evenCount + fromNEResult.evenCount + fromNWResult.evenCount); 
////            var c = idx % 2 == 1 ? evenSum : oddSum; 
//            var c = idx % 2 == (boardSize/2) % 2 ? oddSum : evenSum; 
            result1 += c;
        }
        
        System.out.println("Body:" + result1);    
        System.out.println("Result:"+(result+result1));
        System.out.println("Expected:" + (((long)bound+1)*(1+bound)));
        
    }
    
    static class BoardData
    {
        SearchResult<IntPair> result;
        int evenCount;
        int oddCount;
        
        public BoardData(Board2D board, SearchResult<IntPair> res)
        {
            this(board, res, false);
        }
        
        public BoardData(Board2D board, SearchResult<IntPair> res, boolean trace)
        {
            result = res;
            evenCount = 0;
            oddCount = 0;

            for (var y = 0; y < board.getHeigth(); y++)
            {
                for (var x = 0; x < board.getWidth(); x++)
                {
                    var cell = Pair.of(x, y);
                    var p = res.getPath(cell);
                    if (p != null)
                    {
                        var l = p.size() - 1;
                        if (l % 2 == 0)
                            evenCount++;
                        else
                        {
                            oddCount++;
                            if (trace)
                                System.out.println("Cell "+cell+" is odd");
                        }
                    }
                }
            }
        }
    }
}
