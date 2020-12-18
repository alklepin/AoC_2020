package day18.solver;


public class Processor
{
//    public static void main(String [] args)
//    {
//        new Processor().run(args);
//    }
//    
//    private void run(String [] args)
//    {
//        try
//        {
//            System.out.println(process(args[0]));
//        }
//        catch (Exception ex)
//        {
//            System.err.println(ex.getMessage());
//        }
//    }
//    
    public static long process(String expression)
        throws ProcessorException
    {
        StackOfCommands stack = new StackOfCommands();
        StackOfCommands currOps = new StackOfCommands();

        char [] chars = expression.toCharArray();
        
        StringBuffer currentToken = new StringBuffer();
        for (int iCurPos = 0; iCurPos < chars.length; iCurPos++)
        {
            ICommand cmd = null;
            switch (chars[iCurPos])
            {
                case '+':
                {
                    String strToken = currentToken.toString().trim();
                    if (strToken.length() > 0)
                    {
                        cmd = new CmdAdd();
                    }
                    else
                    {
                        currentToken.append("+");
                    }
                    break;
                }
                case '-':
                {
                    String strToken = currentToken.toString().trim();
                    if (strToken.length() > 0)
                    {
                        cmd = new CmdSub();
                    }
                    else
                    {
                        currentToken.append("-");
                    }
                    break;
                }
                case '*':
                {
                    cmd = new CmdMul();
                    break;
                }
                case '/':
                {
                    cmd = new CmdDiv();
                    break;
                }
                case '(':
                {
                    int subExpStart = iCurPos+1;
                    int braceBalance = 1;
                    int subExpEnd = subExpStart;
                    while (braceBalance > 0 && subExpEnd < chars.length)
                    {
                        if (chars[subExpEnd] == '(')
                        {
                            braceBalance++;
                        }
                        if (chars[subExpEnd] == ')')
                        {
                            braceBalance--;
                        }
                        subExpEnd++;
                    }
                    if (braceBalance == 0)
                    {
                        subExpEnd--;
                    }
                    iCurPos = subExpEnd;
                    long value = process(expression.substring(subExpStart, subExpEnd));
                    currentToken.setLength(0);
                    currentToken.append(value);
                    //cmd = new Constant(value);
                    break;
                }
                default:
                {
                    currentToken.append(chars[iCurPos]);
                }
            }
            
            if (cmd != null)
            {
                String strToken = currentToken.toString().trim();
                if (strToken.length() > 0)
                {
                    try
                    {
                        stack.push(new Constant(Long.parseLong(strToken)));
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new ProcessorException(ProcessorException.NOT_A_NUMBER, 
                            new String[]{strToken});
                    }
                    currentToken.setLength(0);
                }
                while (!currOps.isEmpty() 
                    && currOps.peek().getPriority() >= cmd.getPriority())
                {
                    stack.push(currOps.pop());
                }
                currOps.push(cmd);
            }
        }
        String strToken = currentToken.toString().trim();
        if (strToken.length() > 0)
        {
            try
            {
                stack.push(new Constant(Integer.parseInt(strToken)));
            }
            catch (NumberFormatException ex)
            {
                throw new ProcessorException(ProcessorException.NOT_A_NUMBER, 
                    new String[]{strToken});
            }
            currentToken.setLength(0);
        }
        while (!currOps.isEmpty()) 
        {
            stack.push(currOps.pop());
        }
        if (!stack.isEmpty())
        {
            return stack.pop().execute(stack);
        }
        else
        {
            throw new IllegalStateException();
        }
    }
}
