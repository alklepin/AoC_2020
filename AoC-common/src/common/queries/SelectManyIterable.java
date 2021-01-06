package common.queries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SelectManyIterable<TSource, TTarget> implements Iterable<TTarget>
{
    private Iterable<TSource> m_source;
    private Converter<? super TSource, Iterable<? extends TTarget>> m_converter;
    
    public SelectManyIterable(Iterable<TSource> source, Converter<? super TSource, Iterable<? extends TTarget>> converter)
    {
        m_source = source;
        m_converter = converter;
    }
    
    public Iterator<TTarget> iterator()
    {
        return new SelectManyIterator(m_source.iterator());
    }
    
    private class SelectManyIterator implements Iterator<TTarget>
    {
        private Iterator<TSource> m_iterator;
        private Iterator<? extends TTarget> m_currentIterator = EmptyIterator.instance();

        public SelectManyIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            return (m_currentIterator != null && m_currentIterator.hasNext()) || m_iterator.hasNext();
        }

        @Override
        public TTarget next()
        {
            if (m_currentIterator.hasNext())
                return m_currentIterator.next();
            while (!m_currentIterator.hasNext() && m_iterator.hasNext())
                m_currentIterator = m_converter.convert(m_iterator.next()).iterator();
            if (m_currentIterator.hasNext())
                return m_currentIterator.next();
            throw new NoSuchElementException();
        }
    }
    
}
