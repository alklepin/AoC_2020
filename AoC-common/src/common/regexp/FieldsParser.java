package common.regexp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FieldsParser<T>
{
    private Pattern m_pattern;
    private Class<T> m_clazz;
    private HashMap<String, FieldSetter> setters = new HashMap<>();

    public static <T> FieldsParser<T> getFor(String pattern, Class<T> clazz)
    {
        return new FieldsParser<T>(pattern, clazz);
    }
    
    public FieldsParser(String pattern, Class<T> clazz)
    {
        m_pattern = Pattern.compile(pattern);
        m_clazz = clazz;
        for (var field : clazz.getDeclaredFields())
        {
            var fname = field.getName();
            var ftype = field.getType();
            FieldSetter setter = null;
            if (ftype.equals(String.class))
            {
                setter = new StringSetter(field);
            }
            if (ftype.equals(Integer.TYPE))
            {
                setter = new IntSetter(field);
            }
            if (ftype.equals(Integer.class))
            {
                setter = new IntegerSetter(field);
            }
            if (ftype.equals(Long.TYPE))
            {
                setter = new LongSetter(field);
            }
            if (setter != null)
            {
                setters.put(fname,  setter);
            }
        }
    }
    
    private static abstract class FieldSetter
    {
        protected Field m_field;
        
        public FieldSetter(Field field)
        {
            m_field = field;
            m_field.setAccessible(true);
        }

        public void setField(Object obj, String value)
        {
            try
            {
                setFieldImpl(obj, value);
            }
            catch (IllegalArgumentException | IllegalAccessException ex)
            {
                throw new IllegalStateException(ex);
            }
        }
        
        protected abstract void setFieldImpl(Object obj, String value)
            throws IllegalArgumentException, IllegalAccessException;
    }
    
    private static class StringSetter extends FieldSetter
    {
        public StringSetter(Field field)
        {
            super(field);
        }
        
        @Override
        public void setFieldImpl(Object obj, String value)
            throws IllegalArgumentException, IllegalAccessException
        {
            m_field.set(obj, value);
        }
    }

    private static class IntSetter extends FieldSetter
    {
        public IntSetter(Field field)
        {
            super(field);
        }
        
        @Override
        public void setFieldImpl(Object obj, String value)
            throws IllegalArgumentException, IllegalAccessException
        {
            var intValue = Integer.parseInt(value);
            m_field.setInt(obj, intValue);
        }
    }

    private static class IntegerSetter extends FieldSetter
    {
        public IntegerSetter(Field field)
        {
            super(field);
        }
        
        @Override
        public void setFieldImpl(Object obj, String value)
            throws IllegalArgumentException, IllegalAccessException
        {
            var intValue = Integer.parseInt(value);
            m_field.set(obj, Integer.valueOf(intValue));
        }
    }

    private static class LongSetter extends FieldSetter
    {
        public LongSetter(Field field)
        {
            super(field);
        }
        
        @Override
        public void setFieldImpl(Object obj, String value)
            throws IllegalArgumentException, IllegalAccessException
        {
            var longValue = Long.parseLong(value);
            m_field.setLong(obj, longValue);
        }
    }
    
    public T parse(String line)
    {
        T result;
        try
        {
            result = m_clazz.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException ex)
        {
            throw new IllegalStateException(ex);
        }
        var matcher = m_pattern.matcher(line);
        if (matcher.find())
        {
            for (var entry : setters.entrySet())
            {
                var name = entry.getKey();
                var setter = entry.getValue();
                var value = matcher.group(name);
                if (value != null)
                    setter.setField(result, value);
            }
        }
        return result;
    }
    
    static class TestDTO
    {
        int row;
        int col;
        @Override
        public String toString()
        {
            return "TestDTO [row=" + row + ", col=" + col + "]";
        }
        
        
    }
    
    public static void main(String [] args)
    {
        var line = "row=5 column=10";
        var parser = FieldsParser.getFor("row=(?<row>\\d+) column=(?<col>\\d+)", TestDTO.class);
        var result = parser.parse(line);
        System.out.println(result);
    }
}
