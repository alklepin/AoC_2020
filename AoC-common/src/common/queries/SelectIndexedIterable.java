package common.queries;

import java.util.Iterator;

public class SelectIndexedIterable<TSource, TTarget> implements Iterable<TTarget>
{
    private Iterable<TSource> m_source;
    private ConverterIndexed<? super TSource, ? extends TTarget> m_converter;
    
    public SelectIndexedIterable(Iterable<TSource> source, ConverterIndexed<? super TSource, ? extends TTarget> converter)
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
        private int m_index;

        public ConvertingIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
            m_index = -1;
        }

        @Override
        public boolean hasNext()
        {
            return m_iterator.hasNext();
        }

        @Override
        public TTarget next()
        {
            return m_converter.convert(++m_index, m_iterator.next());
        }
    }
    
}
