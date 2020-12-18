package day18.solver;

public class Constant
    implements ICommand
{
    private final long m_value;
    
    public Constant(long value)
    {
        m_value = value;
    }

    public long execute(StackOfCommands stack)
    {
        return m_value;
    }

    public int getPriority()
    {
        return 0;
    }
}
    
