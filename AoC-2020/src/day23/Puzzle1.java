package day23;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public class Element
    {
        int m_value;
        Element m_next;
        public Element(int value, Element next)
        {
            m_value = value;
            m_next = next;
        }
        
        public void setNext(Element next)
        {
            m_next = next;
        }
    }
    
    private final static String input = "327465189";
    private final static int circleLength = input.length();
    
    public void printCircle(Element element)
    {
        Element test = element;
        for (int i = 0; i < circleLength; i++)
        {
            System.out.print(test.m_value);
            test = test.m_next;
        }
        System.out.println();
    }

    public Element findElement(Element element)
    {
        int valueToLookup = element.m_value-1;
        Element max1 = null;
        Element max2 = null;
        Element test = element;
        for (int i = 0; i < circleLength; i++)
        {
            if (test.m_value <= valueToLookup)
                if (max1 == null || max1.m_value < test.m_value)
                    max1 = test;

            if (max2 == null || max2.m_value < test.m_value)
                max2 = test;
            test = test.m_next;
        }
        if (max1 != null)
            return max1;
        return max2;
    }
    
    public void solve()
        throws Exception
    {
        Element circle = null;
        Element current = null;
        
        ArrayList<Integer> cups = new ArrayList<>();
        for (char c : input.toCharArray())
        {
            Element next = new Element(parseInt(""+c), null);
            if (current != null)
                current.setNext(next);
            current = next;
            if (circle == null)
                circle = next;
        }
        current.setNext(circle);
        
        current = circle;
        for (int i = 0; i < 100; i++)
        {
            Element removedSegment = current.m_next;
            Element endOfSegment = removedSegment.m_next.m_next.m_next;
            current.m_next = endOfSegment;
            
            Element found = findElement(current);
            removedSegment.m_next.m_next.setNext(found.m_next);
            found.setNext(removedSegment);
            printCircle(current);
            current = current.m_next;
        }
        Element test = current;
        while (test.m_value != 1)
        {
            test = test.m_next;
        }
        for (int i = 0; i < circleLength; i++)
        {
            System.out.print(test.m_value);
            test = test.m_next;
        }
    }
}
