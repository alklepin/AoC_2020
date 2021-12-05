package common;

public class IntValue
{
    private int m_value;

    public IntValue(int value)
    {
        super();
        m_value = value;
    }

    public int getValue()
    {
        return m_value;
    }

    public void setValue(int value)
    {
        m_value = value;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_value;
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
        IntValue other = (IntValue)obj;
        if (m_value != other.m_value)
            return false;
        return true;
    }
    
    

    @Override
    public String toString()
    {
        return String.valueOf(m_value);
    }

    public void inc()
    {
        m_value++;
    }
    
    public void dec()
    {
        m_value--;
    }

    public void inc(int v)
    {
        m_value += v;
    }

    public void dec(int v)
    {
        m_value -= v;
    }
}
