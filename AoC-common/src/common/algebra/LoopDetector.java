package common.algebra;

import java.nio.channels.IllegalSelectorException;
import java.util.HashMap;
import java.util.function.Function;

public class LoopDetector
{
    public static class LoopDetectionResult<TNode>
    {
        private final long startLength;
        private final long loopLength;
        private final boolean loopFound;
        private final TNode start;
        private final TNode loopStart;
        private final TNode lastProcessed;
        private final long iterationsCount;
        
        
        public LoopDetectionResult(long startLength, long loopLength, TNode start, TNode loopStart, TNode lastProcessed, int lastStep)
        {
            this.startLength = startLength;
            this.loopLength = loopLength;
            this.start = start;
            this.loopStart = loopStart;
            this.lastProcessed = lastProcessed;
            this.iterationsCount = lastStep;
            this.loopFound = true;
        }
        
        private LoopDetectionResult(TNode start, long startLength, TNode lastProcessed)
        {
            this.startLength = startLength;
            this.loopLength = 0;
            this.loopFound = false;
            this.start = start;
            this.loopStart = null;
            this.lastProcessed = lastProcessed;
            this.iterationsCount = startLength - 1;
        }

        public long getStartLength()
        {
            return startLength;
        }

        public long getLoopLength()
        {
            return loopLength;
        }

        public boolean isLoopFound()
        {
            return loopFound;
        }

        

        @Override
        public String toString()
        {
            return "LoopDetectionResult [startLength=" + startLength
                + ", loopLength=" + loopLength + ", loopFound=" + loopFound
                + ", start=" + start + ", loopStart=" + loopStart
                + ", lastProcessed=" + lastProcessed + ", iterationsCount=" + iterationsCount
                + "]";
        }

        public TNode getStart()
        {
            return start;
        }

        public TNode getLoopStart()
        {
            return loopStart;
        }
    }
    
    
    public static <TNode> LoopDetectionResult<TNode> findLoopWithCache(TNode start, Function<TNode, TNode> nextNodeProducer)
    {
        return findLoopWithCache(start, nextNodeProducer, 100000000);
    }

    public static <TNode> LoopDetectionResult<TNode> findLoopWithCache(TNode start, Function<TNode, TNode> nextNodeProducer, int iterationsLimit)
    {
        TNode current = start;
        if (iterationsLimit < 1)
            return new LoopDetectionResult<TNode>(start, 1, start);
        
        HashMap<TNode, Integer> cache = new HashMap<>();
        cache.put(current, 0);
        for (var step = 1; step <= iterationsLimit; step++)
        {
            var next = nextNodeProducer.apply(current);
            var s = cache.get(next);
            if (s != null)
            {
                return new LoopDetectionResult<TNode>(s, step-s, start, next, next, step);
            }
            else
            {
                cache.put(next, step);
                current = next;
            }
        }
        return new LoopDetectionResult<TNode>(start, iterationsLimit+1, current);
    }

//    public static <TNode> LoopDetectionResult<TNode> findLoopNoCache(TNode start, Function<TNode, TNode> nextNodeProducer, int iterationsLimit)
//    {
//        TNode current = start;
//        if (iterationsLimit < 1)
//            return new LoopDetectionResult<TNode>(start, 1, start);
//        
//        TNode current2 = start;
//        long loopLength = -1;
//        long startLimit = -1;
//        for (var step = 1; step <= iterationsLimit; step++)
//        {
//            var next = nextNodeProducer.apply(current);
//            var next2 = nextNodeProducer.apply(nextNodeProducer.apply(current2));
//            if (next.equals(next2))
//            {
//                // Potential loop of length 'step'
//                var check = next;
//                var check2 = next2;
//                var same = true;
//                for (int idx = 0; idx < step; idx++)
//                {
//                    same = check.equals(check2);
//                    if (!same)
//                        break;
//                    check = nextNodeProducer.apply(check);
//                    check2 = nextNodeProducer.apply(nextNodeProducer.apply(check2));
//                }
//                if (same)
//                {
//                    loopLength = step;
//                    startLimit = step;
//                    break;
//                }
//            }
//        }
//        if (loopLength > 0)
//        {
//            var fwdItem = start;
//            for (var idx = 0; idx < loopLength; idx++)
//                fwdItem = nextNodeProducer.apply(fwdItem);
//            
//            var s = cache.get(next);
//            if (s != null)
//            {
//                return new LoopDetectionResult<TNode>(s, step-s, start, next, next, step);
//            }
//            else
//            {
//                cache.put(next, step);
//                current = next;
//            }
//        }
//        return new LoopDetectionResult<TNode>(start, iterationsLimit+1, current);
//    }
    
    public static <TNode> TNode simulateWithCache(TNode start, Function<TNode, TNode> nextNodeProducer, long iterationCount)
    {
        int limit = 100000000;
        if (iterationCount < limit)
            limit = (int) iterationCount;
            
        var res = findLoopWithCache(start, nextNodeProducer, limit);
        if (res.loopFound)
        {
            TNode state = res.loopStart;
            long step = res.startLength + ((iterationCount - res.startLength) / res.loopLength) * res.loopLength;
            while (step < iterationCount)
            {
                state = nextNodeProducer.apply(state);
                step++;
            }
            return state;
        }
        else
        {
            if (res.iterationsCount == iterationCount)
                return res.lastProcessed;
            throw new IllegalStateException();
        }
    }
    
}
