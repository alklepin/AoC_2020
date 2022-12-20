package day20;

import java.util.ArrayList;

import common.LinesGroup;
import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    static class State
    {
        int value;
        int position;
        
        public State(int value, int position)
        {
            super();
            this.value = value;
            this.position = position;
        }
    }
    
    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
        var states = new ArrayList<State>();
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        State zeroState = null;
        for (String line : lines)
        {
            var state = new State(parseInt(line), states.size());
            states.add(state);
            if (state.value == 0)
                zeroState = state;
        }
        State[] list = states.toArray(new State[0]);
        var modulo = list.length;
        
        for (var s : states)
        {
            var count = s.value;
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
            if (count < 0)
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
        }
        var result = 
            list[(zeroState.position + 1000) % modulo].value
            + list[(zeroState.position + 2000) % modulo].value
            + list[(zeroState.position + 3000) % modulo].value;
        System.out.println(result);
        
    }
    
    static int toRange(int value, int modulo)
    {
        var res = (value % modulo);
        if (res >= 0)
            return res;
        else
            return (res + modulo) % modulo;
            
    }
}
