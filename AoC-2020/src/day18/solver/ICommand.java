package day18.solver;

public interface ICommand 
{
    long execute(StackOfCommands stack) throws ProcessorException;
    
    int getPriority();
}
