package day11;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.Generators;
import common.boards.IntPair;
import common.queries.Query;

public class Puzzle1New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1New().solve();
    }
    
    public void modelStep(Board2D current, Board2D next)
    {
        int height = current.getHeigth();
        int width = current.getWidth();
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                int occupiedCount = Query.wrap(
                    Generators.neighbours8(rowIdx, colIdx, 0, 0, height-1, width-1))
                    .where(cell -> current.getCharAt(cell) == '#')
                    .count();

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
                        if (occupiedCount >= 4)
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
