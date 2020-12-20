package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class YieldIterable<T> implements Iterable<T>
{
    public Iterator<T> iterator()
    {
        return new YieldIterator();
    }
    
    abstract protected void generate();
    
    protected void yield(T value)
    {
        YieldIterator iter = m_iterator.get();
        iter.yield(value);
    }

    private ThreadLocal<YieldIterator> m_iterator = new ThreadLocal<YieldIterator>();
    
    public interface YieldAction<TValue>
    {
        public void yield(TValue v);
    }

    public interface Code<TValue>
    {
        public void execute(YieldAction<TValue> v);
    }
    
    public static <TValue> Iterable<TValue> withYield(Code<TValue> code)
    {
        return new YieldIterable<TValue>(){
            protected void generate()
            {
                code.execute((v) -> yield(v));
            }
        };
    }

    
    private class YieldIterator implements Iterator<T>
    {
        private Thread m_thread;
        private Object m_lock = new Object();
        private boolean m_hasPrefetched;
        private boolean m_hasNext;
        private T m_prefetched;
        
        public YieldIterator()
        {
            m_hasPrefetched = false;
            m_hasNext = false;
            m_thread = new Thread(() -> execute());
            m_thread.setDaemon(true);
            synchronized (m_lock)
            {
                m_thread.start();
                try 
                {
                    //System.out.println(Thread.currentThread().getName() + " -> wating");
                    m_lock.wait();
                }
                catch (InterruptedException ex)
                {
                    Thread.interrupted();
                }
            }
        }
        
        public void yield(T value)
        {
            m_hasNext = true;
            m_prefetched = value;
            m_hasPrefetched = true;
            synchronized(m_lock)
            {
                //System.out.println(Thread.currentThread().getName() + " -> notifying");

                m_lock.notify();
                try
                {
                    //System.out.println(Thread.currentThread().getName() + " -> wating");

                    m_lock.wait();
                }
                catch(InterruptedException ex)
                {
                    Thread.interrupted();
                }
            }
        }

        @Override
        public boolean hasNext()
        {
            if (!m_hasPrefetched)
            {
                prefetch();
            }
            return m_hasNext;
        }

        @Override
        public T next()
        {
            if (hasNext())
            {
                m_hasPrefetched = false;
                return m_prefetched;
            }
            throw new NoSuchElementException();
        }
        
        private void prefetch()
        {
            synchronized (m_lock)
            {
                //System.out.println(Thread.currentThread().getName() + " -> notifying");
                m_lock.notify();
                try
                {
                    //System.out.println(Thread.currentThread().getName() + " -> wating");
                    m_lock.wait();
                }
                catch (InterruptedException ex)
                {
                    Thread.interrupted();
                }
            }
        }

        private void execute()
        {
            m_iterator.set(this);
            synchronized(m_lock)
            {
                //System.out.println(Thread.currentThread().getName() + " -> notifying");

                m_lock.notify();
                try
                {
                    //System.out.println(Thread.currentThread().getName() + " -> wating");
                    m_lock.wait();
                }
                catch (InterruptedException ex)
                {
                    Thread.interrupted();
                }
            }
            try
            {
                generate();
            }
            finally
            {
                m_hasNext = false;
                m_hasPrefetched = true;
                m_prefetched = null;
                
                synchronized(m_lock)
                {
                    //System.out.println(Thread.currentThread().getName() + " -> notifying end");
                    m_lock.notify();
                }
            }
        }
    }
}
