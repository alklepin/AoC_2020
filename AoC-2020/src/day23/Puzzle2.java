package day23;

import java.util.ArrayList;
import java.util.HashMap;

import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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
//    private final static int circleLength = input.length();
    private final static int circleLength = 1000000;
    
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

    ArrayList<Element> valueToElement = new ArrayList<>();
    
    public Element findElement(Element element)
    {
        int valueToLookup = element.m_value-1;
        int idx = valueToLookup;
        Element found = null;
        if (idx > 0 && valueToElement.get(idx) != null)
            return valueToElement.get(idx);
        idx--;
        if (idx > 0 && valueToElement.get(idx) != null)
            return valueToElement.get(idx);
        idx--;
        if (idx > 0 && valueToElement.get(idx) != null)
            return valueToElement.get(idx);
        idx = 1000000;
        if (idx > 0 && valueToElement.get(idx) != null)
            return valueToElement.get(idx);
        idx--;
        if (idx > 0 && valueToElement.get(idx) != null)
            return valueToElement.get(idx);
        idx--;
        if (idx > 0 && valueToElement.get(idx) != null)
            return valueToElement.get(idx);
        throw new IllegalStateException();
    }
    
    public Element findElementSlow(Element element)
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

        valueToElement.ensureCapacity(1000000+1);
        for (int i = 0; i <= 1000000; i++)
        {
            valueToElement.add(null);
        }
        
        ArrayList<Integer> cups = new ArrayList<>();
        for (char c : input.toCharArray())
        {
            Element next = new Element(parseInt(""+c), null);
            valueToElement.set(next.m_value, next);
            if (current != null)
                current.setNext(next);
            current = next;
            if (circle == null)
                circle = next;
        }
        for (int value = 10; value <= 1000000; value++)
        {
            Element next = new Element(value, null);
            valueToElement.set(next.m_value, next);
            if (current != null)
                current.setNext(next);
            current = next;
        }
        current.setNext(circle);
        
        current = circle;
        for (int i = 0; i < 10000000; i++)
        {
            Element removedSegment = current.m_next;
            valueToElement.set(removedSegment.m_value, null);
            valueToElement.set(removedSegment.m_next.m_value, null);
            valueToElement.set(removedSegment.m_next.m_next.m_value, null);
            Element endOfSegment = removedSegment.m_next.m_next.m_next;
            current.m_next = endOfSegment;
            
            Element found = findElement(current);
            removedSegment.m_next.m_next.setNext(found.m_next);
            found.setNext(removedSegment);
            valueToElement.set(removedSegment.m_value, removedSegment);
            valueToElement.set(removedSegment.m_next.m_value, removedSegment.m_next);
            valueToElement.set(removedSegment.m_next.m_next.m_value, removedSegment.m_next.m_next);
            //printCircle(current);
            current = current.m_next;
        }
        Element test = current;
        while (test.m_value != 1)
        {
            test = test.m_next;
        }
        printCircle(test);
        test = test.m_next;
        long v1 = test.m_value;
        test = test.m_next;
        long v2 = test.m_value;
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v1*v2);
    }
}
