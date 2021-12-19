package common.geometry;

public class Matrix3D
{
    private int[] values;
    private int dim; 
    
    public Matrix3D()
    {
        values = new int[9];
        dim = 3;
    }

    public Matrix3D(int [] data)
    {
        values = data;
        if (data.length != 9)
            throw new IllegalArgumentException();
        dim = 3;
    }
    
    public int get(int r, int c)
    {
        return values[r * dim + c];
    }

    public void set(int r, int c, int value)
    {
        values[r * dim + c] = value;
    }
    
    public Vect3I applyTo(Vect3I source)
    {
        var x = source.getX() * get(0, 0) +
            source.getY() * get(0, 1) + 
            source.getZ() * get(0, 2); 
        var y = source.getX() * get(1, 0) +
            source.getY() * get(1, 1) + 
            source.getZ() * get(1, 2); 
        var z = source.getX() * get(2, 0) +
            source.getY() * get(2, 1) + 
            source.getZ() * get(2, 2); 
        return new Vect3I(x, y, z);
    }
    
    public String print()
    {
        var result = new StringBuilder();
        result.append(String.format("%3s, %3s, %3s\n", get(0, 0), get(0, 1), get(0, 2)));
        result.append(String.format("%3s, %3s, %3s\n", get(1, 0), get(1, 1), get(1, 2)));
        result.append(String.format("%3s, %3s, %3s\n", get(2, 0), get(2, 1), get(2, 2)));
        return result.toString();
    }
}
