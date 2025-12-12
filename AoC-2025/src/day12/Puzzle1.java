package day12;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;

import common.ArrayUtils;
import common.LineParser;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
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
    
    static class Shape
    {
        int width;
        int height;
        int areaUsed;
        ArrayList<Board2D> shapes = new ArrayList<>();
        
        public Shape(LinesGroup lines)
        {
            this.width = lines.get(0).length();
            this.height = lines.size();
            var set = new HashSet<Board2D>();
            lines.removeLineAt(0);
            var shape = Board2D.parseAsCharsXY(lines);
            areaUsed = shape.countCells(c -> c == '#');
                
            set.add(shape);
            shapes.add(shape);
            var shapeFlipped = shape.flipH();
            if (set.add(shapeFlipped))
                shapes.add(shapeFlipped);
            
            for (var idx = 0; idx < 3; idx++)
            {
                shape = shape.rotateLeft();
                if (set.add(shape))
                    shapes.add(shape);

                shapeFlipped = shapeFlipped.rotateLeft();
                if (set.add(shapeFlipped))
                    shapes.add(shapeFlipped);
            }
            
        }
        
        ArrayList<Board2D> shaped()
        {
            return shapes;
        }
    }
    
    public boolean solve(IntPair dims, ArrayList<Integer> numbers)
    {
        var totalUsedArea = 0; 
        for (var idx = 0; idx < numbers.size(); idx++)
        {
            var n = numbers.get(idx);
            totalUsedArea += n * shapes.get(idx).areaUsed;
        }
        if (totalUsedArea > dims.getX() * dims.getY())
            return false;
        
        var board = new Board2D(dims.getX(), dims.getY());
        board.fill('.');
        
        return solve(board, ArrayUtils.toArray(numbers), true);
    }

    public boolean solve(Board2D board, int[] numbers, boolean firstStep)
    {
        board.printAsStrings(System.out);
        System.out.println("-------------");
        
        
        var newNumbers = numbers.clone();
        boolean allUsed = true;
        for (var shapeIdx = 0; shapeIdx < numbers.length; shapeIdx++)
        {
            if (numbers[shapeIdx] > 0)
                allUsed = false;
        }
        if (allUsed)
            return true;
        
        for (var shapeIdx = 0; shapeIdx < numbers.length; shapeIdx++)
        {
            if (numbers[shapeIdx] > 0)
            {
                newNumbers[shapeIdx]--;
                var shape = shapes.get(shapeIdx);
                for (var p : board.allCellsXY())
                {
                    for (var s : shape.shapes)
                    {
                        if (board.hasSpaceFor(p, s))
                        {
                            var touches = false;
                            var canBePlaced = true;
                            for (var pointAtShape : s.allCellsXY())
                            {
                                var pointAtBoard = p.add(pointAtShape);
                                if (s.getAtXY(pointAtShape) == '#')
                                {
                                    if (board.getAtXY(pointAtBoard) != '.')
                                    {
                                        canBePlaced = false;
                                        break;
                                    }
                                    touches |= firstStep ||
                                        board.neighbours4XY(pointAtBoard).any(cell -> board.getAtXY(cell) != '.');
                                }
                            }
                            if (!touches)
                            {
                                canBePlaced = false;
                                break;
                            }
                                
                            if (canBePlaced)
                            {
                                for (var p1 : s.allCellsXY())
                                {
                                    if (s.getAtXY(p1) == '#')
                                    {
                                        board.setAtXY(p.add(p1), 'A'+ shapeIdx);
                                    }
                                }
                                if (solve(board, newNumbers, false))
                                    return true;
                                for (var p1 : s.allCellsXY())
                                {
                                    if (s.getAtXY(p1) == '#')
                                    {
                                        board.setAtXY(p.add(p1), '.');
                                    }
                                }
                            }
                        }
                    }
                }
                newNumbers[shapeIdx]++;
            }
        }
        return false;
    }
    
    ArrayList<Shape> shapes = new ArrayList<>();
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var groups = readAllLineGroups(inputFile);
        
        
        for (var idx = 0; idx < groups.size() -1 ; idx++)
        {
            var group = groups.get(idx);
            var shape = new Shape(group);
            shapes.add(shape);
            
//            System.out.println("----------------");
//            for (var s : shape.shapes)
//            {
//                s.printAsStrings(System.out);
//                System.out.println();
//            }
//            System.out.println("----------------");
        }
        
        int result = 0;
        var lineIdx = 1;
        var linesCount = groups.get(groups.size()-1).size();
        for (String line : groups.get(groups.size()-1))
        {
            var parts = line.split(":");
            var dims = IntPair.from(parts[0].trim(), "x");
            var parser = new LineParser(parts[1]);
            var numbers = parser.listOfInts();
            System.out.println(MessageFormat.format("Line: {0} of {1}", lineIdx++, linesCount));
            if (solve(dims, numbers))
            {   
                System.out.println("fits");
                result++;
            }
        }
        System.out.println(result);
        
    }
}
