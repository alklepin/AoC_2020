package common.queries;

import java.util.Iterator;

public class ConvertingIterable<TSource, TTarget> implements Iterable<TTarget>
{
    private Iterable<TSource> m_source;
    private Converter<? super TSource, ? extends TTarget> m_converter;
    
    public ConvertingIterable(Iterable<TSource> source, Converter<? super TSource, ? extends TTarget> converter)
    {
        m_source = source;
        m_converter = converter;
    }
    
    public Iterator<TTarget> iterator()
    {
        return new ConvertingIterator(m_source.iterator());
    }
    
    private class ConvertingIterator implements Iterator<TTarget>
    {
        private Iterator<TSource> m_iterator;

        public ConvertingIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            return m_iterator.hasNext();
        }

        @Override
        public TTarget next()
        {
            return m_converter.convert(m_iterator.next());
        }
    }
    
}
