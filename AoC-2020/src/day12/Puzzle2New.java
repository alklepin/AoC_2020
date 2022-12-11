package day12;
import static common.geometry.Vect2I.vector;

import common.LinesGroup;
import common.PuzzleCommon;
import common.geometry.Vect2I;

public class Puzzle2New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2New().solve();
    }
    
    public void solve()
        throws Exception
    {
        LinesGroup lines = readAllLines("input1.txt");
//        LinesGroup lines = readAllLines("test.txt");
        int result = 0;
        Vect2I current = vector(0,0);
        Vect2I waypoint = vector(10, 1);
        for (String line : lines)
        {
            String direction = line.substring(0, 1);
            int length = parseInt(line.substring(1), -1);
            switch (direction)
            {
                case "N":
                {
                    waypoint = waypoint.add(Vect2I.NORTH.mult(length));
                    break;
                }
                case "S":
                {
                    waypoint = waypoint.add(Vect2I.SOUTH.mult(length));
                    break;
                }
                case "E":
                {
                    waypoint = waypoint.add(Vect2I.EAST.mult(length));
                    break;
                }
                case "W":
                {
                    waypoint = waypoint.add(Vect2I.WEST.mult(length));
                    break;
                }
                case "L":
                {
                    while (length >= 90) 
                    {
                        waypoint = waypoint.rotateLeft();
                        length -= 90;
                    }
                    break;
                }
                case "R":
                {
                    while (length >= 90) 
                    {
                        waypoint = waypoint.rotateRight();
                        length -= 90;
                    }
                    break;
                }
                case "F":
                {
                    current = current.add(waypoint.mult(length));
                    break;
                }
            }
            System.out.println("current: "+current+ "    waypoint: "+ waypoint);
        }
        result = Math.abs(current.getX()) + Math.abs(current.getY());
        System.out.println(result);
    }
}
