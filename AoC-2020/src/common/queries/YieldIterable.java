package common.queries;

import java.util.Iterator;

public class YieldIterable<T> implements Iterable<T>
{
    public Iterator<T> iterator()
    {
        return new YieldIterator();
    }
    
    protected void generate()
    {
        
    }
    
    protected void yield(T valule)
    {
        
    }
    
    private class YieldIterator implements Iterator<T>
    {
        private Thread m_thread;
        
        public YieldIterator()
        {
            m_thread = new Thread(() -> generate());
            m_thread.setDaemon(true);
            m_thread.start();
        }
        
        @Override
        public boolean hasNext()
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public T next()
        {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
