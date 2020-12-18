package day18.solver;

import java.text.MessageFormat;

public class ProcessorException
    extends Exception
{
    public static final int NO_OPERAND = 0;

    public static final int DIV_BY_ZERO = 1;

    public static final int NOT_A_NUMBER = 2;
    
    private final int m_iErrorCode;
    private final String [] m_arrParams;
    
    public ProcessorException(int iErrorCode)
    {
        this(iErrorCode, null);
    }

    public ProcessorException(int iErrorCode, String [] params)
    {
        m_iErrorCode = iErrorCode;
        if (params != null)
        {
            m_arrParams = params;
        }
        else
        {
            m_arrParams = new String[0];
        }
    }
    
    @Override
    public String getMessage()
    {
        switch (m_iErrorCode)
        {
            case NO_OPERAND:
            {
                return "Недостаточно данных";
            }
            case DIV_BY_ZERO:
            {
                return "Деление на ноль";
            }
            case NOT_A_NUMBER:
            {
                return MessageFormat.format("Строка ''{0}'' не является числом", (Object[])m_arrParams);
            }
            default:
            {
                return "Неизвестная ошибка";
            }
        }
    }
}
