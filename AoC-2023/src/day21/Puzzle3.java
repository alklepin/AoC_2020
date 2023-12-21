package day21;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.ImplicitGraph;
import common.graph.ImplicitGraph.SearchResult;

public class Puzzle3 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle3().solve();
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
//        var inputFile = "input1.txt";
        var inputFile = "input1_test2.txt";
        
//        LinesGroup lines = readAllLines(inputFile);
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        Board2D board = Board2D.parseAsCharsXY(lines);
        
        var startCell = board.allCellsXY().where(c -> board.getCharAtXY(c) == 'S').single(0);
        var queue = new LinkedList<IntPair>();
        queue.add(startCell);
        var visited = new HashMap<IntPair, Integer>();
        var dirs = new IntPair[] {IntPair.UP, IntPair.DOWN, IntPair.LEFT, IntPair.RIGHT};
        var dims = new IntPair(board.getWidth(), board.getHeigth());
        for (int idx = 0; idx < 1; idx++)
        {
//            System.out.println("Step: "+idx + " Size: "+visited.size() + " QSize: "+queue.size());
            var nextQueue = new LinkedList<IntPair>();
            for (var node : queue)
            {
                for (var dir : dirs)
                {
                    var nextNode = node.add(dir);
                    var testNode = nextNode.componentModulo(dims);
                    if (board.getCharAtXY(testNode) != '#')
                    {
                        if (!visited.containsKey(nextNode))
                        {
                            nextQueue.add(nextNode);
                            visited.put(nextNode, idx);
                        }
                    }
                }
            }
            queue = nextQueue;
        }
        
        var startBFSResult = new BoardData(board, ImplicitGraph.BFS(startCell, null, 
            cell -> board.neighbours4XY(cell).where(c -> board.getCharAtXY(c) != '#')));
        
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
//        var bound = 5;
        var bbound  = (bound - board.getWidth()/2) / board.getWidth() + 1;
        var boardSize = board.getWidth();
        for (var y = -bbound; y <= bbound; y++)
        {
            for (var x = -bbound; x <= bbound; x++)
            {
                var bdist = Math.abs(x) + Math.abs(y); 

                if (bdist > bbound)
                    continue;

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
                    
                
                if (bdist < bbound)
                {
                    if (bdist % 2 == bound % 2)
                    {
                        result += data.evenCount;
                    }
                    else
                    {
                        result += data.oddCount;
                    }
                }
                else
                {
                    var signX = x > 0 ? 1 : x < 0 ? -1 : 0;
                    var signY = y > 0 ? 1 : y < 0 ? -1 : 0;
                    var baseDist = bdist * boardSize - board.getWidth()/2;
                    for (var by = 0; by < board.getHeigth(); by ++)
                    {
                        for (var bx = 0; bx < board.getWidth(); bx ++)
                        {
                            var cell = Pair.of(
                                (x * boardSize) + bx, 
                                (y * boardSize) + by
                                ); //.add(startCell);
                            var testCell = cell.componentModulo(dims);
                            var p = data.result.getPath(testCell);
                            if (p != null 
                                && (baseDist + p.size() - 1 <= bound)
                                && (baseDist + p.size() - 1) % 2 == bound % 2
                                )
                            {
                                result++;
//                                System.out.println("X= "+x+" Y= "+y+ " bx= "+bx+ " by= "+by+" cell: "+cell);
                            }
                        }
                    }
//                    System.out.println("---");    
                }
            }
        }
            
        
//        
//        
//        
//        var res = startBFSResult;
//        
//        
//        for (var y = 0; y < board.getHeigth(); y++)
//        {
//            for (var x = 0; x < board.getWidth(); x++)
//            {
//                var cell = Pair.of(x, y);
//                var res1 = fromNWResult.result;
//                var p = res1.getPath(cell);
//                if (p != null)
//                {
//                    var l = p.size();
//                    System.out.printf("%4d", l);
////                    var diff = cell.minus(startCell);
////                    if (Math.abs(diff.getX()) + Math.abs(diff.getY()) + 1 != l)
////                    {
////                        var t = 1;
////                    }
//                }
//                else
//                {
//                    System.out.printf("%4s", "#");
//                }
//            }
//            System.out.println();
//        }
//        
//        var result = 0;
//        for (var e : visited.entrySet())
//        {
//            var s = e.getValue();
//            if (s % 2 == 1)
//                result++;
//        }
        System.out.println(result);
        
    }
    
    static class BoardData
    {
        SearchResult<IntPair> result;
        int evenCount;
        int oddCount;
        
        public BoardData(Board2D board, SearchResult<IntPair> res)
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
                            oddCount++;
                    }
                }
            }
        }
    }
}
