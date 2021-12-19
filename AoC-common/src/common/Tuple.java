package common;

public class Tuple<T1, T2>
{
    private T1 value1;
    private T2 value2;
    
    public static <T1, T2> Tuple<T1, T2> of (T1 value1, T2 value2)
    {
        return new Tuple<T1, T2>(value1, value2);
    }
    
    public Tuple(T1 value1, T2 value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T1 getValue1()
    {
        return value1;
    }

    public void setValue1(T1 value1)
    {
        this.value1 = value1;
    }

    public T2 getValue2()
    {
        return value2;
    }

    public void setValue2(T2 value2)
    {
        this.value2 = value2;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
        result = prime * result + ((value2 == null) ? 0 : value2.hashCode());
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
        Tuple other = (Tuple)obj;
        if (value1 == null)
        {
            if (other.value1 != null)
                return false;
        }
        else if (!value1.equals(other.value1))
            return false;
        if (value2 == null)
        {
            if (other.value2 != null)
                return false;
        }
        else if (!value2.equals(other.value2))
            return false;
        return true;
    }

    
}
