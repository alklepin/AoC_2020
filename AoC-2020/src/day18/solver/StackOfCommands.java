package day18.solver;

import java.util.Stack;

public class StackOfCommands
{
    private Stack<ICommand> m_stack;
    
    public StackOfCommands()
    {
        m_stack = new Stack<ICommand>();
    }
    
    public void push(ICommand cmd)
    {
        m_stack.push(cmd);
    }
    
    public ICommand pop()
        throws ProcessorException
    {
        if (m_stack.isEmpty())
        {
            throw new ProcessorException(ProcessorException.NO_OPERAND);
        }
        return m_stack.pop();
    }

    public ICommand peek()
        throws ProcessorException
    {
        if (m_stack.isEmpty())
        {
            throw new ProcessorException(ProcessorException.NO_OPERAND);
        }
        return m_stack.peek();
    }
    
    public boolean isEmpty()
    {
        return m_stack.isEmpty();
    }

}
