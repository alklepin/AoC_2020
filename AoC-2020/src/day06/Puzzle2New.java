package day06;

import static common.PuzzleCommon.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import common.Convert;
import common.PuzzleCommon;
import common.SetCollectors;

public class Puzzle2New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2New().solve();
    }
    
    public static <R> Set<R> intersect(Set<R> set, Collection<R> other)
    {
        set.retainAll(other);
        return set;
    }
    
    
    public <T> Set<T> intersect(Stream<Collection<T>> stream)
    {
        return stream.collect(SetCollectors.Intersect());
    }
    
    public int processGroup(LinesGroup group)
    {
        
//        HashSet<Character> hash = new HashSet<>(toListOfChars(EnglishCharsLow));
//        group.stream().forEach(line -> h.retainAll(toListOfChars(line)));
        
//        HashSet<Character> hash = group.stream()
//            .map(PuzzleCommon::toListOfChars)
//            .reduce(
//                new HashSet<Character>(toListOfChars(EnglishCharsLow)),
//                (u,v) -> intersect(u,v),
//                (u, h) -> h);

        HashSet<Character> hash = group.stream().map(Convert::toListOfChars).collect(SetCollectors.Intersect());
        long testCount = hash.size();
        
//        HashSet<Integer> hash = new HashSet<>();
//        "abcdefghijklmnopqrstuvwxyz".chars().forEach(c -> hash.add(c));
//        group.stream().forEach(line -> hash.retainAll(line.chars().mapToObj(c -> Integer.valueOf(c)).collect(Collectors.toList())));
//        long testCount = hash.size();
//        long testCount = group.stream().map(line -> line.chars()).distinct().count();
        
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        
        System.out.printf("%d %d %s\n", testCount, count, testCount == count);
        return count;
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        System.out.println(groups.size());
        
        int sum = 0;
        for (LinesGroup group : groups)
        {
            sum += group.processGroup(this::processGroup);
        }
        System.out.println(sum);
    }
}
