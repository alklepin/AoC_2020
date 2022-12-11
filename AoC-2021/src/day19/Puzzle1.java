package day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import common.LinesGroup;
import common.PuzzleCommon;
import common.Tuple;
import common.geometry.Rotations3D;
import common.geometry.Vect3I;
import common.queries.Query;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public int processGroup(LinesGroup group)
    {
        HashMap<Character, Integer> chars = new HashMap<>();
        for (String line : group)
        {
            for (int i = 0; i < line.length(); i++)
            {
                char key = line.charAt(i);
                int count = 0;
                if (chars.get(key) != null)
                {
                    count = chars.get(key);
                }
                chars.put(key, count + 1);
            }
        }
        int count = 0;
        int groupSize = group.size();
        for (Integer v : chars.values())
        {
            if (v == groupSize)
            {
                count++;
            }
        }
        return count;
    }
    
    public static class Footptint
    {
        private long[] footprint;
        
        public Footptint(long[] footprint)
        {
            this.footprint = footprint;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(footprint);
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
            Footptint other = (Footptint)obj;
            if (!Arrays.equals(footprint, other.footprint))
                return false;
            return true;
        }
    }
    
    public static class ScannerInfo
    {
        private int scannerId;
        public ArrayList<Vect3I> visibleBeacons;
        public HashSet<Vect3I> beaconsSet;
        public ArrayList<ScannerInfo> rotations;
        public Vect3I location;
        

        public ScannerInfo(int scannerId)
        {
            this.scannerId = scannerId;
            visibleBeacons = new ArrayList<Vect3I>();
            beaconsSet = new HashSet<>();
            rotations = new ArrayList<ScannerInfo>();
        }

        public void addVisibleBeacon(Vect3I p)
        {
            visibleBeacons.add(p);
            beaconsSet.add(p);
        }
        
        public ScannerInfo shift(Vect3I shift)
        {
            var result = new ScannerInfo(scannerId);
            for (var b : visibleBeacons)
            {
                result.addVisibleBeacon(b.add(shift));
            }
            return result;
        }
        
        public void generateRotations()
        {
            for (var m : Rotations3D.ROTATIONS_3D_RIGHT)
            {
                var si = new ScannerInfo(scannerId);
                for (var p : visibleBeacons)
                {
                    si.addVisibleBeacon(m.applyTo(p));
                }
                si.rotations = rotations; // shared
                rotations.add(si);
            }
        }
        
        public Tuple<Vect3I, ScannerInfo> intersects(ScannerInfo other)
        {
            for (var si2 : other.rotations)
            {
                var r = intersectsNoRot(this, si2);
                if (r != null)
                    return Tuple.of(r, si2);
            }
            return null;
        }

        private static Vect3I intersectsNoRot(ScannerInfo si1, ScannerInfo si2)
        {
            for (var p1 : si1.visibleBeacons)
            {
                for (var p2 : si2.visibleBeacons)
                {
                    var shift = p1.minus(p2);
                    var si3 = si2.shift(shift);
                    var intersection = new HashSet<Vect3I>(si1.beaconsSet);
                    intersection.retainAll(si3.beaconsSet);
                    if (intersection.size() >= 12)
                    {
                        System.out.printf("Found %s -> %s shift=%s\n", si1.scannerId, si2.scannerId, shift);
                        //System.out.println("Found! shift= "+ shift);
                        
                        for (var p : si1.visibleBeacons)
                        {
                            System.out.println("SI1>" + p);
                        }

                        for (var p : si2.visibleBeacons)
                        {
                            System.out.println("SI2>" + p);
                        }
                        
                        for (var p : intersection)
                        {
                            System.out.println("I>" + p);
                        }
                        return shift;
                    }
                }
            }
            return null;
        }
    }
    
    public void solve()
        throws Exception
    {
        
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());

        ArrayList<ScannerInfo> scanners = new ArrayList<>();
        int result = 0;
        System.out.println("Preprocessing...");
        for (LinesGroup group : groups)
        {
            var v = parse("--- scanner (\\d+) ---", group.get(0));
            var scannerId = parseInt(v[1]);
            var si = new ScannerInfo(scannerId);
            scanners.add(si);
            for (var line : Query.wrap(group).skip(1))
            {
                v = line.split(",");
                var p = new Vect3I(
                    parseInt(v[0].trim()),
                    parseInt(v[1].trim()),
                    parseInt(v[2].trim())
                    );
                si.addVisibleBeacon(p);
            }
            si.generateRotations();
//            System.out.println(si.visibleBeacons.size());
//            System.out.println(si.beaconGroups.size());
//            System.out.println(si.beaconGroups.get(0).print());

//            result += group.processGroup(this::processGroup);
        }
        
        HashSet<Vect3I> beacons = new HashSet<>();
        HashSet<Integer> visited = new HashSet<>();
        var queue = new LinkedList<ScannerInfo>();
        var zeroScanner = scanners.get(0);
        zeroScanner.location = new Vect3I(0,0,0);
        queue.add(zeroScanner);
        visited.add(zeroScanner.scannerId);
        while (queue.size() > 0)
        {
            var current = queue.poll();
            System.out.printf("Scanner #%s:\n", current.scannerId);
            for (var b : current.visibleBeacons)
            {
                var b1 = b.add(current.location);
                if (beacons.add(b1))
                {
                    System.out.printf("  Beacon %s (%s)\n", b1, b);
                }
                else
                {
                    System.out.printf("  Beacon ignored %s (%s)\n", b1, b);
                }
            }
            
            for (var si : scanners)
            {
                if (visited.contains(si.scannerId))
                    continue;
                var intersection = current.intersects(si);
                if (intersection != null)
                {
                    var shift = intersection.getValue1();
                    var scanner = intersection.getValue2();
                    scanner.location = current.location.add(shift);
                    queue.add(scanner);
                    visited.add(scanner.scannerId);
                    System.out.printf("%s -> %s shift %s\n", current.scannerId, si.scannerId, shift);
                    System.out.printf("%s -> %s shift %s\n", si.scannerId, current.scannerId, shift.negate());
                }
            }
        }
        System.out.println(visited.size());
        System.out.println(beacons.size());
//        System.out.println(result);
        
    }
}
