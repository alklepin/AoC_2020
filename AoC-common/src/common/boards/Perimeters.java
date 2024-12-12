package common.boards;

import java.util.HashSet;
import java.util.Set;

public class Perimeters
{
    /**
     * Calculates the length of a perimeter for given set of cells (on a 2D board)
     * @param cells
     * @return
     */
    public static long length(Iterable<IntPair> cells)
    {
        Set<IntPair> region;
        if (cells instanceof Set)
            region = (Set<IntPair>)cells;
        else
        {
            region = new HashSet<IntPair>();
            for (var c : cells)
            {
                region.add(c);
            }
        }
        
        var perimeter = 0l;
        for (var c : region)
        {
            for (var dir : IntPair.FOUR_CROSS_DIRECTIONS)
            {
                var next = c.add(dir);
                if (!region.contains(next))
                {
                    perimeter++;
                }
            }
        }
        return perimeter;
    }
    
    /**
     * Calculates the number of sides of a perimeter for given set of cells (on a 2D board)
     * (sequence of elements of a perimeter going in the same direction make one side)
     * @param cells
     * @return
     */
    public static long sideCount(Iterable<IntPair> cells)
    {
        Set<IntPair> region;
        if (cells instanceof Set)
            region = (Set<IntPair>)cells;
        else
        {
            region = new HashSet<IntPair>();
            for (var c : cells)
            {
                region.add(c);
            }
        }

        var sideCount = 0l;
        for (var c : region)
        {
            var inAngle = 0;
            var outAngle = 0;
            // Number of sides is equal to the number of angles at a perimiters
            for (var d : IntPair.FOUR_DIAGONALS)
            {
                var nd = c.add(d);
                var nX = c.add(d.projectToX());
                var nY = c.add(d.projectToY());
                
                // Detect outer angles
                if (!region.contains(nX) && !region.contains(nY))
                {
                    outAngle++;
                }

                // Detect inner angles
                if (region.contains(nX)
                    && region.contains(nY)
                    && !region.contains(nd)
                    )
                {
                    inAngle++;
                }
            }
            if (outAngle > 4)
                inAngle = outAngle - 4;
            
            sideCount += inAngle + outAngle;
        }
        return sideCount;
    }
    
    
}
