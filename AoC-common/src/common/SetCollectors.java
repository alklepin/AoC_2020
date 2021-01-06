package common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SetCollectors
{
    public static <T> IntersectCollector<T> Intersect()
    {
        return new IntersectCollector<T>();
    }
    
    public static <T> UnionCollector<T> Union()
    {
        return new UnionCollector<T>();
    }
    
    public static class UnionCollector<T> implements Collector<Collection<T>, HashSet<T>, HashSet<T>>
    {
        private static HashSet<Characteristics> characteristics = new HashSet<Collector.Characteristics>();
        {
            characteristics.add(Characteristics.IDENTITY_FINISH);
            characteristics.add(Characteristics.UNORDERED);
        }
        
        private HashSet<T> hsAccumulator; 
        
        private HashSet<T> init()
        {
            hsAccumulator = new HashSet<>();
            return hsAccumulator;
        }
        
        private void consume(HashSet<T> set, Collection<T> col)
        {
            hsAccumulator.addAll(col);
        }
        
        @Override
        public Supplier<HashSet<T>> supplier()
        {
            return this::init;
        }
        
        @Override
        public BiConsumer<HashSet<T>, Collection<T>> accumulator()
        {
            return this::consume;
        }
        
        @Override
        public BinaryOperator<HashSet<T>> combiner()
        {
            return (u, h) -> h;
        }
        
        @Override
        public Function<HashSet<T>, HashSet<T>> finisher()
        {
            return (h) -> h;
        }
        
        @Override
        public Set<Characteristics> characteristics()
        {
            return characteristics;
        }
        
        public static <T> IntersectCollector<T> instance()
        {
            return new IntersectCollector<T>();
        }
    }
    
    public static class IntersectCollector<T> implements Collector<Collection<T>, HashSet<T>, HashSet<T>>
    {
        private static HashSet<Characteristics> characteristics = new HashSet<Collector.Characteristics>();
        {
            characteristics.add(Characteristics.IDENTITY_FINISH);
            characteristics.add(Characteristics.UNORDERED);
        }
        
        private boolean dataProcessed = false;
        private HashSet<T> hsAccumulator; 

        private HashSet<T> init()
        {
            hsAccumulator = new HashSet<>();
            return hsAccumulator;
        }
        
        private void consume(HashSet<T> set, Collection<T> col)
        {
            if (dataProcessed)
            {
                hsAccumulator.retainAll(col);
            }
            else
            {
                hsAccumulator.addAll(col);
                dataProcessed = true;
            }
        }
        
        @Override
        public Supplier<HashSet<T>> supplier()
        {
            return this::init;
        }

        @Override
        public BiConsumer<HashSet<T>, Collection<T>> accumulator()
        {
            return this::consume;
        }

        @Override
        public BinaryOperator<HashSet<T>> combiner()
        {
            return (u, h) -> h;
        }

        @Override
        public Function<HashSet<T>, HashSet<T>> finisher()
        {
            return (h) -> h;
        }

        @Override
        public Set<Characteristics> characteristics()
        {
            return characteristics;
        }
        
        public static <T> IntersectCollector<T> instance()
        {
            return new IntersectCollector<T>();
        }
    }

}
