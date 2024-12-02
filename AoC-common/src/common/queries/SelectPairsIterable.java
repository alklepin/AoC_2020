package common.queries;

import java.util.Iterator;

import common.Tuple;

public class SelectPairsIterable<TSource, TTarget> implements Iterable<TTarget>
{
    private Iterable<TSource> m_source;
    private Converter<Tuple<TSource, TSource>, ? extends TTarget> m_converter;
    
    public SelectPairsIterable(Iterable<TSource> source, Converter<Tuple<TSource, TSource>, ? extends TTarget> converter)
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
        private TSource m_prevValue;

        public ConvertingIterator(Iterator<TSource> iterator)
        {
            m_iterator = iterator;
            if (iterator.hasNext())
                m_prevValue = iterator.next();
        }

        @Override
        public boolean hasNext()
        {
            return m_iterator.hasNext();
        }

        @Override
        public TTarget next()
        {
            var current = m_iterator.next();
            var result = m_converter.convert(Tuple.of(m_prevValue, current));
            m_prevValue = current;
            return result;
        }
    }
    
}
