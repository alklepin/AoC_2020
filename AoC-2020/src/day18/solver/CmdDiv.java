package day18.solver;

public class CmdDiv
    implements ICommand
{
    public long execute(StackOfCommands stack) throws ProcessorException
    {
        try
        {
            long iOp2 = stack.pop().execute(stack);
            long iOp1 = stack.pop().execute(stack);
            return  iOp1 / iOp2;
        }
        catch (ArithmeticException ex)
        {
            throw new ProcessorException(ProcessorException.DIV_BY_ZERO);
        }
    }

    public int getPriority()
    {
        return 1;
    }
}
    
