package common.geometry;

import common.boards.IntPair;

public class SegmentInt
{
    private final IntPair m_point1;
    private final IntPair m_point2;
    
    public SegmentInt(IntPair point1, IntPair point2)
    {
        this.m_point1 = point1;
        this.m_point2 = point2;
    }
    
    public IntPair getPoint1()
    {
        return m_point1;
    }
    
    public IntPair getPoint2()
    {
        return m_point2;
    }
    
    public Segment asSegment()
    {
        return Segment.of(m_point1, m_point2);
    }
    
    @Override
    public String toString()
    {
        return "Segment [m_point1=" + m_point1 + ", m_point2=" + m_point2 + "]";
    }
    
}
