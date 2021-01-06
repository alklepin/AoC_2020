package common.graph;

public class Pair<T1, T2>
{
    private T1 m_item1;
    private T2 m_item2;
    
    public Pair(T1 item1, T2 item2)
    {
        m_item1 = item1;
        m_item2 = item2;
    }
    
    public T1 getItem1()
    {
        return m_item1;
    }
    public void setItem1(T1 item1)
    {
        m_item1 = item1;
    }
    public T2 getItem2()
    {
        return m_item2;
    }
    public void setItem2(T2 item2)
    {
        m_item2 = item2;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_item1 == null) ? 0 : m_item1.hashCode());
        result = prime * result + ((m_item2 == null) ? 0 : m_item2.hashCode());
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
        @SuppressWarnings("rawtypes")
        Pair other = (Pair)obj;
        if (m_item1 == null)
        {
            if (other.m_item1 != null)
                return false;
        }
        else if (!m_item1.equals(other.m_item1))
            return false;
        if (m_item2 == null)
        {
            if (other.m_item2 != null)
                return false;
        }
        else if (!m_item2.equals(other.m_item2))
            return false;
        return true;
    }

    public static <T1,T2> Pair<T1, T2> from(T1 item1, T2 item2)
    {
        return new Pair<T1,T2>(item1, item2);
    }
    
}
