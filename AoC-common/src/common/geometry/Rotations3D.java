package common.geometry;

import java.util.ArrayList;

public class Rotations3D
{
    public static ArrayList<Matrix3D> ROTATIONS_3D = initRotations();

    public static ArrayList<Matrix3D> ROTATIONS_3D_RIGHT = initRotations(true);
    public static ArrayList<Matrix3D> ROTATIONS_3D_LEFT = initRotations(false);
    
    public static ArrayList<Matrix3D> initRotations()
    {
        var result = new ArrayList<Matrix3D>();
        var dir = new int [] {-1, 1};
        
        for (var d1 : dir)
        {
            for (var d2 : dir)
            {
                for (var d3 : dir)
                {
                    for (var idx1 = 0; idx1 < 3; idx1++)
                    {
                        for (var idx2 = 0; idx2 < 3; idx2++)
                        {
                            if (idx1 != idx2)
                            {
                                for (var idx3 = 0; idx3 < 3; idx3++)
                                {
                                    if (idx1 != idx3 && idx2 != idx3)
                                    {
                                        var m = new Matrix3D();
                                        m.set(0, idx1, d1);
                                        m.set(1, idx2, d2);
                                        m.set(2, idx3, d3);
                                        result.add(m);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<Matrix3D> initRotations(boolean isRight)
    {
        var result = new ArrayList<Matrix3D>();
        var dir = new int [] {-1, 1};

        for (var d1 : dir)
        {
            for (var d2 : dir)
            {
                for (var d3 : dir)
                {
                    for (var idx1 = 0; idx1 < 3; idx1++)
                    {
                        for (var idx2 = 0; idx2 < 3; idx2++)
                        {
                            if (idx1 != idx2)
                            {
                                for (var idx3 = 0; idx3 < 3; idx3++)
                                {
                                    if (idx1 != idx3 && idx2 != idx3)
                                    {
                                        var right = (d1 * d2 * d3 > 0) ^ (idx1 < idx2) ^ (idx2 < idx3) ^ (idx1 < idx3);
                                        if (right ^ isRight)
                                        {
                                            var m = new Matrix3D();
                                            m.set(0, idx1, d1);
                                            m.set(1, idx2, d2);
                                            m.set(2, idx3, d3);
                                            result.add(m);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
