package common.geometry;

public class Plane3D
{
    private Vect3D point;
    private Vect3D direction1;
    private Vect3D direction2;
    private Vect3D norm;
    public Plane3D(Vect3D point, Vect3D direction1, Vect3D direction2)
    {
        super();
        this.point = point;
        this.direction1 = direction1;
        this.direction2 = direction2;
        norm = direction1.vectorMult(direction2);
        
        if (norm.length() < Line3D.EPS)
            throw new IllegalArgumentException("Collinear vectors detected");
    }
    
    public Plane3D(Vect3D point, Line3D line)
    {
        if (line.contains(point))
            throw new IllegalArgumentException("Point lays on a line");
        this.point = point;
        this.direction1 = line.getPoint().minus(point);
        this.direction2 = line.getDirection();
        norm = direction1.vectorMult(direction2);
    }

    public Vect3D intersectWith(Line3D line)
    {
        var s = norm.scalarMult(line.getPoint().minus(point))/norm.length();
        var vs = norm.scalarMult(line.getDirection())/norm.length();
        var pnt = line.getPoint().add(line.getDirection().mult(s/vs));
        return pnt;
    }
    
}
