package common.regexp;

import java.lang.reflect.Field;
import java.util.HashMap;

public class FieldsParser<T>
{
    private String m_pattern;
    private Class<T> m_clazz;
    private HashMap<String, FieldSetter> setters = new HashMap<>();

    public FieldsParser(String pattern, Class<T> clazz)
    {
        m_pattern = pattern;
        m_clazz = clazz;
        for (var field : clazz.getDeclaredFields())
        {
            var fname = field.getName();
            var ftype = field.getClass();
            FieldSetter setter = null;
            if (ftype.equals(String.class))
            {
                setter = new StringSetter(field);
            }
            if (ftype.equals(Integer.TYPE))
            {
                setter = new IntSetter(field);
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
    
    public T parse(String line)
    {
        return null;
    }
}
