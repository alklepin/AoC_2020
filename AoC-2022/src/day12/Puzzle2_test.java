package day12;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import common.queries.Query;

public class Puzzle2_test extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_test().solve();
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        var board = Board2D.parseAsChars(lines);
        
        IntPair start = board.findCharXY('S').first();
        IntPair end = board.findCharXY('E').first();;

        board.setAtXY(start, 'a');
        board.setAtXY(end, 'z');
        
        var bfrRes = ImplicitGraph.BFS(start, end, 
            (current) -> {
                char currentHeight = board.getCharAtXY(current);
                return Query.wrap(board.neighbours4XY(current))
                    .where(next -> board.getCharAtXY(next) - currentHeight <= 1);
            });
        
        var result = bfrRes.getPath();

        System.out.println(result.size()-1);
        
    }
}
