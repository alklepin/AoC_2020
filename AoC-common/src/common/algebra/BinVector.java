package common.algebra;

public class BinVector implements Field<BinVector>
{
    private int data[];
    
    public BinVector(int... data)
    {
        this.data = data;
    }

    public BinVector(int size)
    {
        this.data = new int[size];
    }
    
    public int get(int idx)
    {
        return data[idx];
    }
    
    public void set(int idx, int value)
    {
        data[idx] = value % 2;
    }
    
    public int length()
    {
        return data.length;
    }
    
    public BinVector add(BinVector other)
    {
        if (length() != other.length())
            throw new IllegalArgumentException();
        var length = length();
        var result = new int[length];
        for (int idx = 0; idx < length; idx++)
        {
            result[idx] = (data[idx] + other.data[idx]) % 2;
        }
        return new BinVector(result);
    }

    public BinVector sub(BinVector other)
    {
        if (length() != other.length())
            throw new IllegalArgumentException();
        var length = length();
        var result = new int[length];
        for (int idx = 0; idx < length; idx++)
        {
            result[idx] = (data[idx] - other.data[idx]) % 2;
        }
        return new BinVector(result);
    }

    public BinVector mul(BinVector other)
    {
        if (length() != other.length())
            throw new IllegalArgumentException();
        var length = length();
        var result = new int[length];
        for (int idx = 0; idx < length; idx++)
        {
            result[idx] = (data[idx] * other.data[idx]) % 2;
        }
        return new BinVector(result);
    }
    
    public BinVector zero()
    {
        return new BinVector(new int[length()]);
    }

    public BinVector one()
    {
        return new BinVector(new int[length()]);
    }
}
