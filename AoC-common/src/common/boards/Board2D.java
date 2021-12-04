package common.boards;

import java.io.PrintStream;
import java.util.Arrays;

import common.queries.Sequence;

public class Board2D
    implements Cloneable
{
    private int[][] m_data;
    private int m_width;
    private int m_heigth;
    
    public Board2D(int width, int heigth)
    {
        m_width = width;
        m_heigth = heigth;
        m_data = createDataArray(getWidth(), getHeigth());
    }

    public int getWidth()
    {
        return m_width;
    }

    public int getHeigth()
    {
        return m_heigth;
    }

    public int getAt(int row, int col)
    {
        return m_data[row][col];
    }
    
    public void setAt(int row, int col, int data)
    {
        m_data[row][col] = data;
    }

    public char getCharAt(int row, int col)
    {
        return (char)m_data[row][col];
    }

    public void setCharAt(int row, int col, char value)
    {
        m_data[row][col] = value;
    }
    
    public char getCharAt(IntPair cell)
    {
        return (char)m_data[cell.getX()][cell.getY()];
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
        for (int i = 0; i < rowData.length; i++)
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
    
    public static void printArray(char [][] data)
    {
        System.out.println("==========================================");
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++)
        {
            System.out.println(new String(data[rowIdx]));
        }
        System.out.println("==========================================");
    }
}
