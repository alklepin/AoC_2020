package day18.solver;

public class CmdMul
    implements ICommand
{
    public long execute(StackOfCommands stack) throws ProcessorException
    {
        return stack.pop().execute(stack) * stack.pop().execute(stack);
    }

    public int getPriority()
    {
        return 1;
    }
}
    
