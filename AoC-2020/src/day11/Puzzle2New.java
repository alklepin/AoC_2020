package day11;

import java.util.ArrayList;

import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
import common.boards.IntPair;
import common.boards.Pair;
import common.queries.Query;

public class Puzzle2New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
//        Integer[] data = new Integer[] {1,1,1,2,3,1,1};
//        for (Integer i : Query.wrapArray(data).takeWhile(i -> i == 1))
//        {
//            System.out.println(i);
//        }
//        
        new Puzzle2New().solve();
    }
    
    
    private static Query<IntPair> deltas = Query.wrap(
            Pair.of(1,1),
            Pair.of(1,0),
            Pair.of(1,-1),
            Pair.of(0,-1),
            Pair.of(-1,-1),
            Pair.of(-1,0),
            Pair.of(-1,1),
            Pair.of(0,1)
        );

    
    public void modelStep(Board2D current, Board2D next)
    {
        int height = current.getHeigth();
        int width = current.getWidth();
        IntPair min = new IntPair(0, 0);
        IntPair max = new IntPair(height-1, width-1);
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                int occupiedCount = 0;
                
//                for (IntPair pair : deltas)
//                {
//                    IntPair p = Query.wrap(Generators.ray(rowIdx, colIdx, pair.getX(), pair.getY(), 105))
//                        .where(cell -> cell.inRectangle(min, max) && current.getCharAt(cell) != '.')
//                        .firstOrDefault();
//                    if (p != null)
//                    {
//                        char value = current.getCharAt(p);
//                        occupiedCount += (value == '#') ? 1 : 0;
//                    }
//                }
                final int r = rowIdx;
                final int c = colIdx;
                occupiedCount = Query.wrap(deltas)
                    .select(delta -> Query.wrap(Generators.ray(r, c, delta.getX(), delta.getY(), 105))
                        .where(cell -> cell.inRectangle(min, max) && current.getCharAt(cell) != '.')
                        .firstOrDefault())
                    .whereNotNull()
                    .count(cell -> current.getCharAt(cell) == '#');
                
                char nextState = current.getCharAt(rowIdx,colIdx);
                switch (nextState)
                {
                    case 'L':
                    {
                        if (occupiedCount == 0)
                            nextState = '#';
                        break;
                    }
                    case '#':
                    {
                        if (occupiedCount >= 5)
                            nextState = 'L';
                        break;
                    }
                }
                next.setCharAt(rowIdx, colIdx,  nextState);
            }
        }
    }
    
    public void solve()
        throws Exception
    {
        
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int height = lines.size();
        int width = lines.get(0).length();
        
        Board2D dataCurrent = new Board2D(width, height);
        Board2D dataNext = dataCurrent.clone();
        
        int result = 0;
        int rowCount = 0;
        for (String line : lines)
        {
            dataCurrent.setRowAsString(rowCount, line);
            rowCount++;
        }

//        dataCurrent.printAsStrings(System.out);
        
        boolean changed = true;
        while (changed)
        {
            changed = false;
            modelStep(dataCurrent, dataNext);
            changed = !dataCurrent.equals(dataNext);

            Board2D temp = dataCurrent;
            dataCurrent = dataNext;
            dataNext = temp;
        }
        
        result = dataCurrent.countValues('#');
            
//        printArray(dataCurrent);
        
        
        System.out.println(result);
        
    }
}
