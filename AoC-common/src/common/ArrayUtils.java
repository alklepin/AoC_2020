package common;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * This class contains helper methods to work with arrays
 */
public class ArrayUtils
{
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    
    /**
     * Checks whether given byte array starts with given prefix (another array of data)
     * @param data The data where to look for a prefix
     * @param prefix The prefix to check
     * @return true if the data start with given prefix
     */
    public static boolean startsWith(byte[] data, byte[] prefix)
    {
        return isPrefixOf(prefix, data, 0, data.length);
    }

    /**
     * Checks whether given prefix is at beginning of the portion of given buffer starting at given offset and having specified number of bytes
     * @param prefix The prefix to look for
     * @param buffer The buffer where to look for the prefix
     * @param offset The offset of portion of buffer to scan
     * @param count The number of bytes to scan
     * @return true if there is a given prefix starting from specified offset
     */
    public static boolean isPrefixOf(byte [] prefix,  byte [] buffer, int offset, int count)
    {
        if (offset > buffer.length)
            offset = buffer.length;
        
        var maxLength = buffer.length - offset;
        if (count > maxLength)
            count = maxLength;
        
        if (count < prefix.length)
            return false;
        
        var lengthToScan = prefix.length;
        for (var idx = 0; idx < lengthToScan; idx++)
        {
            if (buffer[offset + idx] != prefix[idx])
                return false;
        }
        
        return true;
    }

    /**
     * Checks whether given array is contained by given range of buffer starting at given offset and having specified number of bytes
     * @param value The prefix to look for
     * @param buffer The buffer where to look for the prefix
     * @param offset The offset of portion of buffer to scan
     * @param count The number of bytes to scan
     * @return true if there is a given prefix starting from specified offset
     */
    public static boolean contains(byte [] buffer, byte [] value)
    {
        return contains(buffer, 0, buffer.length, value);
    }
    
    /**
     * Checks whether given array is contained by given range of buffer starting at given offset and having specified number of bytes
     * @param value The prefix to look for
     * @param buffer The buffer where to look for the prefix
     * @param offset The offset of portion of buffer to scan
     * @param count The number of bytes to scan
     * @return true if there is a given prefix starting from specified offset
     */
    public static boolean contains(byte [] buffer, int offset, int count, byte [] value)
    {
        if (offset > buffer.length)
            offset = buffer.length;

        var maxLength = buffer.length - offset;
        if (count > maxLength)
            count = maxLength;

        if (count < value.length)
            return false;

        var lengthToScan = value.length;
        var upBound = offset + count - lengthToScan;
        
        nextPos:
        for (var pos = offset; pos <= upBound; pos++)
        {
            for (var idx = 0; idx < lengthToScan; idx++)
            {
                if (buffer[pos + idx] != value[idx])
                    continue nextPos; 
            }
            return true;
        }

        return false;
    }
    
    /**
     * Utility routine to correctly convert integer value into signed bytes array (Big Endian notation)
     * @param The integer value
     * @return The byte array
     */
    public static byte[] writeInt32BigEndian(int value)
    {
        return new byte[]{(byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)(value)};
    }
    
    /**
     * Utility routine to correctly convert signed bytes array into integer value, 
     * high order bytes should precede low order bytes in array (BigEndian notation)
     * @param buffer The array to read data from
     * @param offset The offset where to start read
     * @return The integer value
     */
    public static int readInt32BigEndian(byte[] buffer, int offset)
    {
        int result = 0;
        result |= (buffer[offset++] << 24);
        result |= (buffer[offset++] << 16) & 0x00FF0000;
        result |= (buffer[offset++] << 8) & 0x0000FF00;
        result |= (buffer[offset]) & 0x000000FF;
        return result;
    }

    /**
     * Utility routine to correctly convert signed bytes array into integer value, 
     * low order bytes should precede high order bytes in array (LittleEndian notation) 
     * @param buffer The array to read data from
     * @param offset The offset where to start read
     * @return The integer value
     */
    public static int readInt32LittleEndian(byte[] buffer, int offset)
    {
        return readInt32LittleEndian(buffer, offset, 4);
    }
    
    /**
     * Utility routine to correctly convert signed bytes array into integer value, 
     * low order bytes should precede high order bytes in array (LittleEndian notation) 
     * @param buffer The array to read data from
     * @param offset The offset where to start read
     * @param count Number of bytes to read
     * @return The integer value
     */
    public static int readInt32LittleEndian(byte[] buffer, int offset, int count)
    {
        int result = 0;
        if (count-- > 0)
            result |= (buffer[offset++] & 0x000000FF);
        if (count-- > 0)
            result |= (buffer[offset++] << 8) & 0x0000FF00;
        if (count-- > 0)
            result |= (buffer[offset++] << 16) & 0x00FF0000;
        if (count > 0)
            result |= (buffer[offset]) << 24;
        return result;
    }

    /**
     * Utility routine to correctly convert signed bytes array into 64-bit integer value,
     * low order bytes should precede high order bytes in array (LittleEndian notation)
     * @param buffer The array to read data from
     * @param offset The offset where to start read
     * @return The long value
     */
    public static long readLongLittleEndian(byte[] buffer, int offset)
    {
        return readLongLittleEndian(buffer, offset, 8);
    }
    
    /**
     * Utility routine to correctly convert signed bytes array into 64-bit integer value,
     * low order bytes should precede high order bytes in array (LittleEndian notation)
     * @param buffer The array to read data from
     * @param offset The offset where to start read
     * @param count Number of bytes to read
     * @return The long value
     */
    public static long readLongLittleEndian(byte[] buffer, int offset, int count)
    {
        long result = 0;
        if (count-- > 0)
            result |= (buffer[offset++] & 0x00000000000000FFL);
        if (count-- > 0)
            result |= ((long)buffer[offset++] << 8) & 0x000000000000FF00L;
        if (count-- > 0)
            result |= ((long)buffer[offset++] << 16) & 0x0000000000FF0000L;
        if (count-- > 0)
            result |= ((long)buffer[offset++] << 24) & 0x00000000FF000000L;
        if (count-- > 0)
            result |= ((long)buffer[offset++] << 32) & 0x000000FF00000000L;
        if (count-- > 0)
            result |= ((long)buffer[offset++] << 40) & 0x0000FF0000000000L;
        if (count-- > 0)
            result |= ((long)buffer[offset++] << 48) & 0x00FF000000000000L;
        if (count > 0)
            result |= ((long)buffer[offset] << 56);
        return result;
    }
    
    /**
     * Calculates the sum of elements of a given array
     * @param data
     * @return
     */
    public static long sum(int[] data)
    {
        return sum(data, 0, data.length);
    }

    /**
     * Calculates the sum of elements of a given range of an array
     * @param data
     * @param from The start of range to sum (inclusive)
     * @param to The end of range to sum (exclusive)
     * @return
     */
    public static long sum(int[] data, int from, int to)
    {
        var result = 0;
        for (int idx = from; idx < to; idx++)
        {
            result += data[idx];
        }
        return result;
    }
    
    public static <T> T[] toArray(Collection<? extends T> source, Class<T> arrayElementType)
    {
        @SuppressWarnings("unchecked")
        var result = (T[])Array.newInstance(arrayElementType, source.size());
        return source.toArray(result);
    }

    public static <T> T[] toArray(Collection<? extends T> source, IntFunction<T[]> arrayGenerator) 
    {
        var result = arrayGenerator.apply(source.size());
        return source.toArray(result);
    }
    
    
    public static <T> boolean contains(T[] values, T value)
    {
        for (T item : values)
        {
            if (Objects.equals(item, value))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates new array with partial sums of elements of source arrays (i.e. element of the resulting array with index k
     * contains sum of elements of the source arrays with indices from 0 to k-1)
     * @param arr
     * @return
     */
    public static int[] buildPartialSums(int[] arr)
    {
        var n = arr.length;
        var sums = new int[n + 1];
        var sum = 0;

        for (var i = 0; i < n; i++)
        {
            sums[i] = sum;
            sum += arr[i];
        }
        sums[n] = sum;

        return sums;
    }

    public static int[] toArray(final List<Integer> arr)
    {
        if (arr == null)
            return null;
        
        var result = new int[arr.size()];
        for (int idx = 0; idx < result.length; idx++)
        {
            result[idx] = arr.get(idx);
        }
        return result;
    }
    
    public static final List<Integer> toList(int[] values)
    {
        var result = new ArrayList<Integer>(values.length);
        for (var value : values)
        {
            result.add(value);
        }
        return result;
    }

    public static final Collection<Integer> toReadonlyCollection(int[] values)
    {
        return new ReadonlyCollectionInt(values);
    }

    private static class ReadonlyCollectionInt extends AbstractCollection<Integer>
    {
        private final int[] values;

        public ReadonlyCollectionInt(int[] values)
        {
            this.values = values;
        }

        @Override
        public int size()
        {
            return values.length;
        }

        @Override
        public Iterator<Integer> iterator()
        {
            return new ArrayOfIntIterator();
        }

        private class ArrayOfIntIterator implements Iterator<Integer>
        {
            private int idx = 0;

            @Override
            public boolean hasNext()
            {
                return idx < values.length;
            }

            @Override
            public Integer next()
            {
                if (hasNext())
                {
                    return values[idx++];
                }
                throw new NoSuchElementException();
            }
        }


        @Override
        public boolean add(Integer e)
        {
            throw new IllegalStateException("Can not modify readonly object");
        }

        @Override
        public boolean remove(Object o)
        {
            throw new IllegalStateException("Can not modify readonly object");
        }

        @Override
        public boolean addAll(Collection<? extends Integer> c)
        {
            throw new IllegalStateException("Can not modify readonly object");
        }

        @Override
        public boolean removeAll(Collection<?> c)
        {
            throw new IllegalStateException("Can not modify readonly object");
        }

        @Override
        public boolean retainAll(Collection<?> c)
        {
            throw new IllegalStateException("Can not modify readonly object");
        }

        @Override
        public void clear()
        {
            throw new IllegalStateException("Can not modify readonly object");
        }
    }
    
    @SafeVarargs
    public static <T> T[] toArray(T... items) {
        return items.clone();
    }
    
    public static float[] floatArrayOf(double... data)
    {
        var result = new float[data.length];
        for (var idx = 0; idx < data.length; idx++)
        {
            result[idx] = (float)data[idx];
        }
        return result;
    }

    public static void appendToList(ArrayList<Integer> list, int[] dataToAppend)
    {
        for (var v : dataToAppend)
            list.add(v);
    }
}
