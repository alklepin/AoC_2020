package day15;

import java.util.ArrayList;
import java.util.HashMap;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.boards.Pair;
import common.graph.Graph;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        ArrayList<String> lines = readAllLines("input1.txt");

        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        var dim = lines.get(0).length();
        Board2D board = new Board2D(dim, dim);
        int result = 0;
        {
            int row = 0;
            for (String line : lines)
            {
                int col = 0;
                for (var c : line.toCharArray())
                {
                    board.setAtRC(row,  col, c - '0');
                    col++;
                }
                row++;
            }
        }

        Graph<IntPair, Integer> graph = new Graph<IntPair, Integer>(()->Integer.valueOf(Integer.MAX_VALUE));
        for (var row = 0; row < board.getHeigth(); row++)
        {
            for (var col = 0; col < board.getWidth(); col++)
            {
                var cell = Pair.of(row, col);
                graph.addNode(cell);
            }
        }
        for (var row = 0; row < board.getHeigth(); row++)
        {
            for (var col = 0; col < board.getWidth(); col++)
            {
                var cell = Pair.of(row, col);
                for (var next : board.neighbours4RC(cell))
                {
                    graph.addEdge(cell, next, board.getAtRC(next));
                }
            }
        }
        var path = graph.findPathDijkstra(Pair.of(0, 0), Pair.of(dim-1, dim-1));
        var cell = Pair.of(dim-1, dim-1);
        System.out.println(path.getNodeValue(cell));

        var start = Pair.of(0, 0);
        while (!start.equals(cell))
        {
            result += board.getAtRC(cell);
            cell = path.visitedFrom(cell);
        }
        
        System.out.println(result);
        
    }
}
