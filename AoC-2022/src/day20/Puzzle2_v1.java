package day20;

import java.util.ArrayList;
import java.util.Arrays;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle2_v1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2_v1().solve();
    }
    
    static class State
    {
        long value;
        int position;
        
        public State(long value, int position)
        {
            super();
            this.value = value;
            this.position = position;
        }
        
        public String toString()
        {
            return String.valueOf(value);
        }
    }
    
    public void solve()
        throws Exception
    {
//        var inputFile = "input1.txt";
        var inputFile = "input1_test.txt";
//        var inputFile = "input1_test2.txt";
        
        var states = new ArrayList<State>();
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        State zeroState = null;
        for (String line : lines)
        {
            var value = parseLong(line)*811589153l;
//            var value = parseInt(line);
            var state = new State(value, states.size());
            states.add(state);
            if (state.value == 0)
                zeroState = state;
        }
        State[] list = states.toArray(new State[0]);
        var modulo = list.length;
        
        for (var step = 0; step < 10; step++)
        {
            for (var s : states)
            {
                var shift = s.value;
//                var shift = 22;

                var div = shift / modulo;
                var count = (shift) % modulo;
                
//                System.out.println(Arrays.toString(list));
                
                State[] tmp = new State[list.length-1];
                for (var idx = 0; idx < tmp.length; idx++)
                {
                    if (idx < s.position)
                    {
                        tmp[idx] = list[idx]; 
                    }
                    else 
                    {
                        tmp[idx] = list[idx+1]; 
                    }
                }

//                System.out.println(Arrays.toString(tmp));
                
                State[] tmp1 = new State[list.length-1];
                for (var idx = 0; idx < tmp1.length; idx++)
                {
                    var st = tmp[toRange(idx+div, tmp1.length)];
                    st.position = idx;
                    tmp1[idx] = st;
                }

//                System.out.println(Arrays.toString(tmp1));

                State[] tmp2 = new State[list.length];
                for (var idx = 0; idx < tmp2.length; idx++)
                {
                    if (idx < s.position)
                    {
                        tmp2[idx] = tmp1[idx]; 
                    }
                    else if (idx > s.position)
                    {
                        tmp2[idx] = tmp1[idx-1]; 
                    }
                    else
                    {
                        tmp2[idx] = s;
                    }
                }
                list = tmp2;
                
//                System.out.println(Arrays.toString(list));
                
                if (count > 0)
                {
                    for (var idx = s.position; idx < s.position + count; idx++)
                    {
                        var c = list[toRange(idx+1, modulo)];
                        c.position = toRange(c.position - 1, modulo);
                        list[toRange(idx, modulo)] = c;
                    }
                    s.position = toRange(s.position + count, modulo);
                    list[s.position] = s;
                }
                else if (count < 0)
                {
                    for (var idx = s.position; idx > s.position + count; idx--)
                    {
                        var c = list[toRange(idx-1, modulo)];
                        c.position = toRange(c.position + 1, modulo);
                        list[toRange(idx, modulo)] = c;
                    }
                    s.position = toRange(s.position + count, modulo);
                    list[s.position] = s;
                }
//                System.out.println(Arrays.toString(list));
//                System.exit(0);

            }
          System.out.println(Arrays.toString(list));
        }
        var result = 
            list[(zeroState.position + 1000) % modulo].value
            + list[(zeroState.position + 2000) % modulo].value
            + list[(zeroState.position + 3000) % modulo].value;
        System.out.println(result);
        
    }
    
    static int toRange(long value, int modulo)
    {
        var res = (value % modulo);
        if (res >= 0)
            return (int) res;
        else
            return (int)((res + modulo) % modulo);
            
    }
}
