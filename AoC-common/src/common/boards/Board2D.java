package common.boards;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import common.LinesGroup;
import common.boards.Generators.Neighbours8Generator;
import common.geometry.Vect2I;
import common.queries.Query;
import common.queries.Sequence;

public class Board2D
    implements Cloneable
{
    private int[][] m_data;
    private final int m_width;
    private final int m_heigth;
    private final IntPair m_dims;

    public Board2D(int width, int heigth)
    {
        m_width = width;
        m_heigth = heigth;
        m_dims = IntPair.of(m_width, m_heigth);
        m_data = createDataArray(getWidth(), getHeigth());
    }

    public Board2D(Bounds bounds)
    {
        m_width = bounds.width() + 1;
        m_heigth = bounds.height() + 1;
        m_dims = IntPair.of(m_width, m_heigth);
        m_data = createDataArray(getWidth(), getHeigth());
    }
    
    public Board2D(Board2D other)
    {
        m_width = other.m_width;
        m_heigth = other.m_heigth;
        m_dims = IntPair.of(m_width, m_heigth);
        m_data = duplicateArray(other.m_data);
    }

    public int getWidth()
    {
        return m_width;
    }

    public int getHeigth()
    {
        return m_heigth;
    }

    public IntPair dimensions()
    {
        return m_dims;
    }
    
    public int getAtRC(int row, int col)
    {
        return m_data[row][col];
    }

    public int getAtRC(IntPair cell)
    {
        return getAtRC(cell.getX(), cell.getY());
    }
    
    public void setAtRC(int row, int col, int data)
    {
        m_data[row][col] = data;
    }

    public void setAtRC(IntPair cell, int data)
    {
        setAtRC(cell.getX(), cell.getY(), data);
    }

    public int getAtXY(int x, int y)
    {
        return m_data[y][x];
    }
    
    public void setAtXY(int x, int y, int data)
    {
        m_data[y][x] = data;
    }

    public int getAtXY(IntPair cell)
    {
        return getAtXY(cell.getX(), cell.getY());
    }

    public int getAtXY(Vect2I cell)
    {
        return getAtXY(cell.getX(), cell.getY());
    }
    
    public void setAtXY(IntPair cell, int data)
    {
        setAtXY(cell.getX(), cell.getY(), data);
    }
    
    public void setAtXY(Vect2I cell, int data)
    {
        setAtXY(cell.getX(), cell.getY(), data);
    }
    
    public char getCharAt(int row, int col)
    {
        return (char)m_data[row][col];
    }

    public void setCharAt(int row, int col, char value)
    {
        m_data[row][col] = value;
    }

    public void setCharAtXY(IntPair cell, char value)
    {
        m_data[cell.getY()][cell.getX()] = value;
    }

    public void setCharAtXY(int x, int y, char value)
    {
        m_data[y][x] = value;
    }
    
    public char getCharAt(IntPair cell)
    {
        return (char)m_data[cell.getX()][cell.getY()];
    }

    public char getCharAtXY(IntPair cell)
    {
        return (char)m_data[cell.getY()][cell.getX()];
    }

    public char getCharAtXY(int x, int y)
    {
        return (char)m_data[y][x];
    }
    
    public String getRowAsString(int row)
    {
        int[] rowData = m_data[row];
        StringBuilder sb = new StringBuilder(rowData.length);
        for (int i = 0; i < rowData.length; i++)
        {
            sb.append((char)rowData[i]);
        }
        return sb.toString();
    }

    public void setRowAsString(int row, String data)
    {
        int[] rowData = m_data[row];
        int length = Math.min(rowData.length, data.length());
        for (int i = 0; i < length; i++)
        {
            rowData[i] = data.charAt(i);
        }
    }
    
    public Iterable<Integer> rowInts(int row)
    {
        return Sequence.of(0, m_width, i -> m_data[row][i]);
    }

    public Iterable<Integer> colInts(int col)
    {
        return Sequence.of(0, m_width, i -> m_data[i][col]);
    }
    
    public void forEachCell(IntConsumer action)
    {
        for (int row = 0; row < m_heigth; row++)
        {
            var rowData = m_data[row];
            for (int col = 0; col < m_width; col++)
            {
                action.accept(rowData[col]);
            }
        }
    }

    @FunctionalInterface
    public interface CellUpdater
    {
        public int update(IntPair cell, int currentValue);
    }
    
    public void modifyEachCellRC(CellUpdater updater)
    {
        for (var cell : allCellsRC())
        {
            setAtRC(cell, updater.update(cell, getAtRC(cell)));
        }
    }

    public void modifyEachCellXY(CellUpdater updater)
    {
        for (var cell : allCellsXY())
        {
            setAtXY(cell, updater.update(cell, getAtXY(cell)));
        }
    }
    
    public void setAll(int value)
    {
        for (int row = 0; row < m_heigth; row++)
        {
            var rowData = m_data[row];
            for (int col = 0; col < m_width; col++)
            {
                rowData[col] = value;
            }
        }
    }

    public void setAll(char value)
    {
        for (int row = 0; row < m_heigth; row++)
        {
            var rowData = m_data[row];
            for (int col = 0; col < m_width; col++)
            {
                rowData[col] = value;
            }
        }
    }
    
    public void fillColumnXY(int x, int value)
    {
        for (var cell : colCellsXY(x))
            setAtXY(cell, value);
    }
    
    public void fillColumnRC(int x, int value)
    {
        for (var cell : colCellsRC(x))
            setAtXY(cell, value);
    }
    
    public void fillRowXY(int y, int value)
    {
        for (var cell : rowCellsXY(y))
            setAtXY(cell, value);
    }

    public void fillRowRC(int y, int value)
    {
        for (var cell : colCellsRC(y))
            setAtXY(cell, value);
    }
    
    public void printAsInts(PrintStream ps)
    {
        StringBuilder sb = new StringBuilder();
        for (int rowIdx = 0; rowIdx < m_data.length; rowIdx++)
        {
            int[] rowData = m_data[rowIdx];
            
            sb.setLength(0);
            for (int i = 0; i < rowData.length; i++)
            {
                sb.append(rowData[i]).append(',').append(' ');
            }
            if (rowData.length > 0)
                sb.setLength(sb.length() - 2);
            ps.println(sb.toString());
        }
    }
    
    public void printAsStrings(PrintStream ps)
    {
        StringBuilder sb = new StringBuilder();
        for (int rowIdx = 0; rowIdx < m_data.length; rowIdx++)
        {
            int[] rowData = m_data[rowIdx];
            sb.setLength(0);
            for (int i = 0; i < rowData.length; i++)
            {
                sb.append((char)rowData[i]);
            }
            ps.println(sb.toString());
        }
    }

    public void printAsStringsRev(PrintStream ps)
    {
        StringBuilder sb = new StringBuilder();
        for (int rowIdx = m_data.length-1; rowIdx >= 0; rowIdx--)
        {
            int[] rowData = m_data[rowIdx];
            sb.setLength(0);
            for (int i = 0; i < rowData.length; i++)
            {
                sb.append((char)rowData[i]);
            }
            ps.println(sb.toString());
        }
    }
    
    @Override
    public Board2D clone()
    {
        Board2D result;
        try
        {
            result = (Board2D)super.clone();
            result.m_data = duplicateArray(result.m_data);
            return result;
        }
        catch (CloneNotSupportedException ex)
        {
            throw new IllegalStateException();
        }
    }
    
    public Iterable<IntPair> allCellsRC()
    {
        return allCellsRCImpl();
    }
    
    public Iterable<IntPair> allCellsXY()
    {
        return allCellsXYImpl();
    }

    public Iterable<IntPair> findAllXY(Predicate<? super IntPair> predicateXY)
    {
        return Query.wrap(allCellsXY()).where(predicateXY);
    }

    public Iterable<IntPair> findAllRC(Predicate<? super IntPair> predicateRC)
    {
        return Query.wrap(allCellsRC()).where(predicateRC);
    }

    private Query<IntPair> allCellsRCImpl()
    {
        return Query.range(0, m_width).selectMany(
            col -> Query.range(0, m_heigth).select(row -> Pair.of(row, col)));
    }

    private Query<IntPair> allCellsXYImpl()
    {
        return Query.range(0, m_width).selectMany(
            col -> Query.range(0, m_heigth).select(row -> Pair.of(col, row)));
    }

    public Query<IntPair> rowCellsXY(int y)
    {
        return rowCellsXYImpl(y);
    }
    
    public Query<IntPair> rowCellsRC(int y)
    {
        return rowCellsRCImpl(y);
    }
    
    private Query<IntPair> rowCellsXYImpl(int y)
    {
        return Query.range(0, m_width)
            .select(x -> Pair.of(x, y));
    }
    
    private Query<IntPair> rowCellsRCImpl(int row)
    {
        return Query.range(0, m_width).select(
            col -> Pair.of(col, row));
    }
    
    public Query<IntPair> colCellsXY(int x)
    {
        return colCellsXYImpl(x);
    }

    public Query<IntPair> colCellsRC(int x)
    {
        return colCellsRCImpl(x);
    }

    private Query<IntPair> colCellsXYImpl(int x)
    {
        return Query.range(0, m_heigth)
            .select(y -> Pair.of(x, y));
    }

    private Query<IntPair> colCellsRCImpl(int col)
    {
        return Query.range(0, m_heigth).select(
            row -> Pair.of(col, row));
    }
    
    public Query<IntPair> findXY(Predicate<? super IntPair> predicate)
    {
        return allCellsXYImpl().where(predicate);
    }

    public Query<IntPair> findRC(Predicate<? super IntPair> predicate)
    {
        return allCellsRCImpl().where(predicate);
    }

    public Query<IntPair> findCharXY(char value)
    {
        return allCellsXYImpl().where(cell -> getCharAtXY(cell) == value);
    }

    public Query<IntPair> findCharRC(char value)
    {
        return allCellsRCImpl().where(cell -> getCharAtXY(cell) == value);
    }
    
    
    public ArrayList<ArrayList<IntPair>> connectedComponentsXY(ArrayList<IntPair> starts, 
        Function<IntPair, Iterable<IntPair>> movesGenerator,
        BiPredicate<IntPair, IntPair> canMoveFromTo)
    {
        Board2D used = new Board2D(m_width, m_heigth);
        
        Iterable<IntPair> startNodes;
        if (starts != null)
        {
            startNodes = starts;
        }
        else
        {
            startNodes = allCellsXY();
        }
        
        ArrayList<ArrayList<IntPair>> result = new ArrayList<>();
        LinkedList<IntPair> front = new LinkedList<>();
        for (var current : startNodes)
        {
            if (used.getAtXY(current) == 0)
            {
                ArrayList<IntPair> component = new ArrayList<>();
                front.add(current);
                component.add(current);
                while ((current = front.poll()) != null)
                {
                    for (var next : movesGenerator.apply(current))
                    {
                        if (used.getAtXY(next) == 0 && canMoveFromTo.test(current, next))
                        {
                            used.setAtXY(next, 1);
                            front.add(next);
                            component.add(current);
                        }
                    }
                }
                result.add(component);
            }
        }
        return result;
    }
    
    public ArrayList<ArrayList<IntPair>> connectedComponentsRC(ArrayList<IntPair> starts,
        Function<IntPair, Iterable<IntPair>> movesGenerator,
        BiPredicate<IntPair, IntPair> canMoveFromTo)
    {
        Board2D used = new Board2D(m_width, m_heigth);
        
        Iterable<IntPair> startNodes;
        if (starts != null)
        {
            startNodes = starts;
        }
        else
        {
            startNodes = allCellsRC();
        }
        
        ArrayList<ArrayList<IntPair>> result = new ArrayList<>();
        LinkedList<IntPair> front = new LinkedList<>();
        for (var current : startNodes)
        {
            if (used.getAtRC(current) == 0)
            {
                ArrayList<IntPair> component = new ArrayList<>();
                front.add(current);
                component.add(current);
                while ((current = front.poll()) != null)
                {
                    for (var next : movesGenerator.apply(current))
                    {
                        if (used.getAtRC(next) == 0 && canMoveFromTo.test(current, next))
                        {
                            used.setAtRC(next, 1);
                            front.add(next);
                            component.add(current);
                        }
                    }
                }
                result.add(component);
            }
        }
        return result;
    }

    
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(m_data);
        result = prime * result + getHeigth();
        result = prime * result + getWidth();
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board2D other = (Board2D)obj;
        if (!Arrays.deepEquals(m_data, other.m_data))
            return false;
        if (getHeigth() != other.getHeigth())
            return false;
        if (getWidth() != other.getWidth())
            return false;
        return true;
    }

    public int countValues(char value)
    {
        return countValues((int)value);
    }
    
    public int countValues(int value)
    {
        int height = m_heigth;
        int width = m_width;
        int result = 0;
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                if (m_data[rowIdx][colIdx] == value)
                    result++;
            }
        }
        return result;
    }

    public int countCells(IntPredicate condition)
    {
        int height = m_heigth;
        int width = m_width;
        int result = 0;
        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                if (condition.test(m_data[rowIdx][colIdx]))
                    result++;
            }
        }
        return result;
    }

    public static int[][] createArray(int dim1, int dim2)
    {
        int[][] result = new int[dim1][];
        for (int rowIdx = 0; rowIdx < result.length; rowIdx++)
        {
            result[rowIdx] = new int[dim2]; 
        }            
        return result;
    }
    
    public static int[][] duplicateArray(int[][] data)
    {
        int[][] result = data.clone();
        for (int rowIdx = 0; rowIdx < result.length; rowIdx++)
        {
            result[rowIdx] = result[rowIdx].clone(); 
        }            
        return result;
    }
    
    public static void fillArray(int[][] data, int value)
    {
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++)
        {
            Arrays.fill(data[rowIdx], value); 
        }            
    }
    
    public static int[][] createDataArray(int width, int height)
    {
        return createDataArray(width, height, 0);
    }
    
    public static int[][] createDataArray(int width, int height, int defaultValue)
    {
        int[][] data = new int[height][];
        int [] row = new int[width];
        if (defaultValue != 0)
        {
            Arrays.fill(row, defaultValue);
        }
        data[0] = row;
        for (int rowIdx = 1; rowIdx < height; rowIdx++)
        {
            data[rowIdx] = row.clone();
        }
        return data;
    }

    public static long[][] createDataArrayLong(int width, int height, int defaultValue)
    {
        long[][] data = new long[height][];
        long [] row = new long[width];
        if (defaultValue != 0)
        {
            Arrays.fill(row, defaultValue);
        }
        data[0] = row;
        for (int rowIdx = 1; rowIdx < height; rowIdx++)
        {
            data[rowIdx] = row.clone();
        }
        return data;
    }

    public static Board2D parseAsChars(List<String> lines)
    {
        Board2D board = new Board2D(lines.get(0).length(), lines.size());
        for (var row = 0; row < board.getHeigth(); row++)
        {
            var line = lines.get(row);
            for (var col = 0; col < line.length(); col++)
            {
                board.setAtRC(row,  col, line.charAt(col));
            }
        }
        return board;
    }
    
    public static Board2D parseAsChars(LinesGroup lines)
    {
        var width = 0;
        for (var line : lines)
        {
            width = Math.max(width, line.length());
        }
        Board2D board = new Board2D(width, lines.size());
        for (var row = 0; row < board.getHeigth(); row++)
        {
            var line = lines.get(row);
            for (var col = 0; col < line.length(); col++)
            {
                board.setAtRC(row,  col, line.charAt(col));
            }
        }
        return board;
    }

    public static Board2D parseAsCharsXY(LinesGroup lines)
    {
        return parseAsCharsXY(lines, ' ');
    }
    
    public static Board2D parseAsCharsXY(LinesGroup lines, char defaultChar)
    {
        var width = 0;
        for (var line : lines)
        {
            width = Math.max(width, line.length());
        }
        Board2D board = new Board2D(width, lines.size());
        board.setAll(defaultChar);
        for (var y = 0; y < board.getHeigth(); y++)
        {
            var line = lines.get(y);
            for (var x = 0; x < line.length(); x++)
            {
                board.setAtXY(x,  y, line.charAt(x));
            }
        }
        return board;
    }

    public static Board2D parseAsInts(List<String> lines)
    {
        var width = lines.get(0).split(" ").length;
        Board2D board = new Board2D(lines.get(0).length(), width);
        for (var row = 0; row < board.getHeigth(); row++)
        {
            var numbers = lines.get(row).split(" ");
            for (var col = 0; col < numbers.length; col++)
            {
                board.setAtRC(row,  col, Integer.parseInt(numbers[col]));
            }
        }
        return board;
    }
    
    public static void printArray(char [][] data)
    {
        System.out.println("==========================================");
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++)
        {
            System.out.println(new String(data[rowIdx]));
        }
        System.out.println("==========================================");
    }

    public static void printArray(int [][] data)
    {
        System.out.println("==========================================");
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++)
        {
            for (int colIdx = 0; colIdx < data.length; colIdx++)
            {
                System.out.printf("%4s, ", data[rowIdx][colIdx]);
            }
            System.out.println();
        }
        System.out.println("==========================================");
    }

    public static void printArray(long [][] data)
    {
        System.out.println("==========================================");
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++)
        {
            for (int colIdx = 0; colIdx < data[rowIdx].length; colIdx++)
            {
                System.out.printf("%8s, ", data[rowIdx][colIdx]);
            }
            System.out.println();
        }
        System.out.println("==========================================");
    }

    public static Query<IntPair> neighbours8(IntPair start, IntPair min, IntPair max)
    {
        return Query.wrap(new Neighbours8Generator(start, min, max));
    }
    
    public Query<IntPair> neighbours4RC(IntPair start)
    {
        return Generators.neighbours4(start, IntPair.ZERO, Pair.of(m_heigth-1, m_width-1));
    }
    
    public Query<IntPair> neighbours4XY(IntPair start)
    {
        return Generators.neighbours4(start, IntPair.ZERO, Pair.of(m_width-1, m_heigth-1));
    }

    public Query<IntPair> directions4XY(IntPair start)
    {
        return Generators.neighbours4(start, IntPair.ZERO, Pair.of(m_width-1, m_heigth-1))
            .select(c -> c.minus(start));
    }
    
    public Query<IntPair> neighbours8RC(IntPair start)
    {
        return Generators.neighbours8(start, IntPair.ZERO, Pair.of(m_heigth-1, m_width-1));
    }
    
    public Query<IntPair> neighbours8XY(IntPair start)
    {
        return Generators.neighbours8(start, IntPair.ZERO, Pair.of(m_width-1, m_heigth-1));
    }

    public Query<IntPair> rayRC(IntPair currentCell, IntPair delta)
    {
        var count = Math.max(getWidth(), getHeigth());
        return Query.wrap(Generators.ray(currentCell, delta, count))
            .takeWhile(pointRC -> containsRC(pointRC));
    }
    
    public Query<IntPair> rayXY(IntPair currentCell, IntPair delta)
    {
        var count = Math.max(getWidth(), getHeigth());
        return Query.wrap(Generators.ray(currentCell, delta, count))
            .takeWhile(pointRC -> containsXY(pointRC));
    }
    
    public boolean containsRC(IntPair cell)
    {
        return cell.getX() >= 0 && cell.getX() < getHeigth() && cell.getY() >= 0 && cell.getY() < getWidth();
    }

    public boolean containsXY(IntPair cell)
    {
        return cell.getX() >= 0 && cell.getX() < getWidth() && cell.getY() >= 0 && cell.getY() < getHeigth();
    }

    public boolean containsXY(Vect2I cell)
    {
        return cell.getX() >= 0 && cell.getX() < getWidth() && cell.getY() >= 0 && cell.getY() < getHeigth();
    }

    public void setBorder(char borderChar)
    {
        Arrays.fill(m_data[0], borderChar);
        Arrays.fill(m_data[getHeigth()-1], borderChar);
        for (var y = 1; y < getHeigth()-1; y++)
        {
            setAtXY(0, y, '#');
            setAtXY(getWidth()-1, y, borderChar);
        }
    }
}
