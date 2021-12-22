package common.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import common.boards.IntTriple;
import common.queries.Query;

public class CubeInt
{
    private final IntTriple m_point1;
    private final IntTriple m_point2;

    public static CubeInt of(int x1, int x2, int y1, int y2, int z1, int z2)
    {
        return new CubeInt(x1, x2, y1, y2, z1, z2);
    }

    public CubeInt(int x1, int x2, int y1, int y2, int z1, int z2)
    {
        this(new IntTriple(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2)),
            new IntTriple(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2))
            );
    }
    
    public CubeInt(IntTriple point1, IntTriple point2)
    {
        this.m_point1 = point1;
        this.m_point2 = point2;
    }
    

    public IntTriple getPoint1()
    {
        return m_point1;
    }
    
    public IntTriple getPoint2()
    {
        return m_point2;
    }
    
    public boolean in(CubeInt other)
    {
        return other.m_point1.componentLessEq(m_point1)
            && other.m_point2.componentGreaterEq(m_point2);
    }
    
    public CubeInt intersect(CubeInt other)
    {
        var point1 = m_point1.componentMax(other.m_point1);
        var point2 = m_point2.componentMin(other.m_point2);
        if (point1.componentLessEq(point2))
        {
            return new CubeInt(point1, point2);
        }
        return null;
    }

    private static ArrayList<Integer> unique(ArrayList<Integer> data)
    {
        if (data.size() == 0)
            return data;
        for (var pos = 1; pos < data.size(); pos++)
        {
            if (data.get(pos).equals(data.get(pos-1)))
            {
                data.remove(pos);
            }
        }
        return data;
    }
    
    private int unique(int [] data)
    {
        if (data.length == 0)
            return 0;
        
        var idx = 0; 
        for (var pos = 1; pos < data.length; pos++)
        {
            if (data[pos] != data[idx])
            {
                idx++;
                data[idx] = data[pos];
            }
        }
        return idx+1;
    }
    
    
    public ArrayList<CubeInt> sub(CubeInt other)
    {
        ArrayList<CubeInt> result = new ArrayList<>();
        var parts = splitSpace(other);
        for (var p : parts)
        {
            if (p.in(this) && !p.in(other))
                result.add(p);
        }
        return result;
    }
    
    public static Iterable<CubeInt> unionAll(Iterable<CubeInt> cubes)
    {
        var xpoints = new ArrayList<Integer>();
        var ypoints = new ArrayList<Integer>();
        var zpoints = new ArrayList<Integer>();
        for (var c : cubes)
        {
            xpoints.add(c.getPoint1().getX());
            xpoints.add(c.getPoint2().getX());
            ypoints.add(c.getPoint1().getY());
            ypoints.add(c.getPoint2().getY());
            zpoints.add(c.getPoint1().getZ());
            zpoints.add(c.getPoint2().getZ());
        }
        
        Collections.sort(xpoints);
        Collections.sort(ypoints);
        Collections.sort(zpoints);
        xpoints = unique(xpoints);
        ypoints = unique(ypoints);
        zpoints = unique(zpoints);
        
        HashSet<CubeInt> result = new HashSet<>();
        
        for (int p1 = 1; p1 < xpoints.size(); p1++)
        {
            System.out.println("UnionAll: "+p1);
            for (int p2 = 1; p2 < ypoints.size(); p2++)
            {
                for (int p3 = 1; p3 < zpoints.size(); p3++)
                {
                    var cube = CubeInt.of(
                        xpoints.get(p1-1), xpoints.get(p1), 
                        ypoints.get(p2-1), ypoints.get(p2), 
                        zpoints.get(p3-1), zpoints.get(p3) 
                        );
                    for (var c : cubes)
                    {
                        if (cube.in(c))
                        {
                            result.add(cube);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
    public Iterable<CubeInt> union(Iterable<CubeInt> cubes)
    {
        var xpoints = new ArrayList<Integer>();
        var ypoints = new ArrayList<Integer>();
        var zpoints = new ArrayList<Integer>();
        for (var c : cubes)
        {
            xpoints.add(c.getPoint1().getX());
            xpoints.add(c.getPoint2().getX());
            ypoints.add(c.getPoint1().getY());
            ypoints.add(c.getPoint2().getY());
            zpoints.add(c.getPoint1().getZ());
            zpoints.add(c.getPoint2().getZ());
        }
        xpoints.add(getPoint1().getX());
        xpoints.add(getPoint2().getX());
        ypoints.add(getPoint1().getY());
        ypoints.add(getPoint2().getY());
        zpoints.add(getPoint1().getZ());
        zpoints.add(getPoint2().getZ());
        
        Collections.sort(xpoints);
        Collections.sort(ypoints);
        Collections.sort(zpoints);
        xpoints = unique(xpoints);
        ypoints = unique(ypoints);
        zpoints = unique(zpoints);
        
        HashSet<CubeInt> result = new HashSet<>();
        
        for (int p1 = 1; p1 < xpoints.size(); p1++)
        {
            for (int p2 = 1; p2 < ypoints.size(); p2++)
            {
                for (int p3 = 1; p3 < zpoints.size(); p3++)
                {
                    var cube = CubeInt.of(
                        xpoints.get(p1-1), xpoints.get(p1), 
                        ypoints.get(p2-1), ypoints.get(p2), 
                        zpoints.get(p3-1), zpoints.get(p3) 
                        );
                    if (cube.in(this))
                    {
                        result.add(cube);
                        continue;
                    }
                    for (var c : cubes)
                    {
                        if (cube.in(c))
                        {
                            result.add(cube);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
    
    public ArrayList<CubeInt> union(CubeInt other)
    {
        ArrayList<CubeInt> result = new ArrayList<>();
        var parts = splitSpace(other);
        for (var p : parts)
        {
            if (p.in(this) || p.in(other))
                result.add(p);
        }
        return result;
    }
    
    public ArrayList<CubeInt> splitSpace(CubeInt other)
    {
        var x = new int[] {m_point1.getX(), m_point2.getX(), other.m_point1.getX(), other.m_point2.getX()};
        var y = new int[] {m_point1.getY(), m_point2.getY(), other.m_point1.getY(), other.m_point2.getY()};
        var z = new int[] {m_point1.getZ(), m_point2.getZ(), other.m_point1.getZ(), other.m_point2.getZ()};
        Arrays.sort(x);
        Arrays.sort(y);
        Arrays.sort(z);
        int countX = unique(x);
        int countY = unique(y);
        int countZ = unique(z);
        ArrayList<CubeInt> result = new ArrayList<>();
        for (int p1 = 1; p1 < countX; p1++)
        {
            for (int p2 = 1; p2 < countY; p2++)
            {
                for (int p3 = 1; p3 < countZ; p3++)
                {
                    result.add(CubeInt.of(
                        x[p1-1], x[p1], 
                        y[p2-1], y[p2], 
                        z[p3-1], z[p3] 
                        ));
                }
            }
        }
        return result;
    }

//    public ArrayList<CubeInt> union(CubeInt other)
//    {
//        var point1 = m_point1.componentMax(other.m_point1);
//        var point2 = m_point2.componentMin(other.m_point2);
//        if (point1.componentLessEq(point2))
//        {
//            return new CubeInt(point1, point2);
//        }
//        return null;
//    }
    
    
    public Iterable<IntTriple> points()
    {
        int minX = m_point1.getX();
        int minY = m_point1.getY();
        int minZ = m_point1.getZ();
        int dimX = m_point2.getX() - minX;
        int dimY = m_point2.getY() - minY;
        int dimZ = m_point2.getZ() - minZ;
        
        return Query.range(0, dimX * dimY * dimZ).select(i -> 
        new IntTriple(
            minX + i % dimX, 
            minY + (i / dimX) % dimY, 
            minZ + (i / dimX / dimY)));
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((m_point1 == null) ? 0 : m_point1.hashCode());
        result = prime * result
            + ((m_point2 == null) ? 0 : m_point2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CubeInt other = (CubeInt)obj;
        if (m_point1 == null)
        {
            if (other.m_point1 != null)
                return false;
        }
        else if (!m_point1.equals(other.m_point1))
            return false;
        if (m_point2 == null)
        {
            if (other.m_point2 != null)
                return false;
        }
        else if (!m_point2.equals(other.m_point2))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "CubeInt [m_point1=" + m_point1 + ", m_point2=" + m_point2 + "]";
    }
    
    public static void main(String[] args)
    {
//        CubeInt a = new CubeInt(1, 2, 3, 4, -10, 10);
//        CubeInt b = new CubeInt(2, 2, -10, 10, 5, 6);
        CubeInt a = new CubeInt(1, 2, 3, 4, -10, 10);
        CubeInt b = new CubeInt(1, 2, -10, 10, 5, 6);
        var parts = a.sub(b);
        for (var p : parts)
        {
            System.out.println(p);
        }
        
//        CubeInt c = new CubeInt(1, 2, 3, 5, 5, 7);
//        System.out.println(c.volume());
    }

    public long volume()
    {
        var diff = m_point2.minus(m_point1);
        return (long)diff.getX() * (long)diff.getY() * (long)diff.getZ();
    }

    public Iterable<CubeInt> splitInSpace(ArrayList<Integer> xpoints,
        ArrayList<Integer> ypoints, ArrayList<Integer> zpoints)
    {
        var posX = Collections.binarySearch(xpoints, m_point1.getX()) + 1;
        var posY = Collections.binarySearch(ypoints, m_point1.getY()) + 1;
        var posZ = Collections.binarySearch(zpoints, m_point1.getZ()) + 1;
        var result = new ArrayList<CubeInt>();
        for (var px = posX; px < xpoints.size() && xpoints.get(px) <= m_point2.getX(); px++)
        {
            for (var py = posY; py < ypoints.size() && ypoints.get(py) <= m_point2.getY(); py++)
            {
                for (var pz = posZ; pz < zpoints.size() && zpoints.get(pz) <= m_point2.getZ(); pz++)
                {
                    result.add(CubeInt.of(
                        xpoints.get(px-1), xpoints.get(px), 
                        ypoints.get(py-1), ypoints.get(py), 
                        zpoints.get(pz-1), zpoints.get(pz) 
                        ));
                }
            }
        }
        
        return result;
    }

    
}
