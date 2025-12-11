package common.algebra;

import java.io.PrintStream;

/**
 * Matrix of double. Indexed from 0.
 *
 */
public class MatrixD implements Cloneable 
{
    private double[] data;
    private int rows;
    private int columns;
    
    public MatrixD(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        data = new double[rows * columns];
    }
    
    private int cellIdx(int row, int col)
    {
        return row * columns + col;
    }
    
    public void set(int row, int col, double value)
    {
        data[cellIdx(row, col)] = value;
    }

    public void inc(int row, int col, double value)
    {
        data[cellIdx(row, col)] += value;
    }
    
    public double get(int row, int col)
    {
        return data[cellIdx(row, col)];
    }
    
    public MatrixD clone()
    {
        try
        {
            var result = (MatrixD)super.clone();
            result.data = data.clone();
            return result;
        }
        catch (CloneNotSupportedException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    public void gauss()
    {
        var rowLimit = Math.min(rows, columns-1);
        for (var row = 0; row < rowLimit; row++)
        {
            var idx = row;
            while (idx < rows && get(idx, row) == 0)
            {
                idx++;
            }
            if (idx < rows && idx != row)
                swapRows(row, idx);
            var value = data[cellIdx(row, row)];
            if (value != 0)
            {
                rowDiv(row, value);
                for (var r = 0; r < rows; r++)
                {
                    if (r != row)
                        rowSubMult(r, row, get(r, row));
                }
            }
        }
    }
    
    public void rowSubMult(int target, int source, double mul)
    {
        for (var col = 0; col < columns; col++)
        {
            data[cellIdx(target, col)] -= data[cellIdx(source, col)] * mul;
        }
    }

    public void rowDiv(int r, double divisor)
    {
        for (var col = 0; col < columns; col++)
        {
            var idx = cellIdx(r, col);
            data[idx] = data[idx] / divisor;
        }
    }
    
    public void swapRows(int r1, int r2)
    {
        for (var col = 0; col < columns; col++)
        {
            var idx1 = cellIdx(r1, col);
            var idx2 = cellIdx(r2, col);
            var tmp = data[idx1];
            data[idx1] = data[idx2];
            data[idx2] = tmp;
        }
    }
    
    public void setRow(int row, double [] rowData)
    {
        if (rowData.length != columns)
            throw new IllegalStateException();
        var startIdx = cellIdx(row, 0);
        System.arraycopy(rowData, 0, data, startIdx, columns);
    }
    
    public static void main(String [] args)
    {
        var matrix = new MatrixD(2,3);
        matrix.setRow(0, new double[] {2, 5, 9});
        matrix.setRow(1, new double[] {3, 2, 8});
        matrix.gauss();
        System.out.println(matrix.get(0, 2));
        System.out.println(matrix.get(1, 2));
    }

    public double sumColumn(int col)
    {
        double result = 0;
        for (var row = 0; row < rows; row++)
            result += get(row, col);
        return result;
    }
    
    public void print(PrintStream ps)
    {
        for (var r = 0; r < rows; r++)
        {
            for (var c = 0; c < columns; c++)
            {
                ps.print(String.format("%5.1f ", get(r,c)));
            }
            ps.println();
        }
    }

    public int columns()
    {
        return columns;
    }

    public int rows()
    {
        return rows;
    }
}
