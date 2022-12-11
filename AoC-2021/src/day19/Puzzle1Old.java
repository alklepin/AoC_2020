package day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntTriple;
import common.queries.Query;

public class Puzzle1Old extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1Old().solve();
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
    
    public static class BeaconGroup
    {
        private ArrayList<IntTriple> beacons;
        private Footptint footprint;
        
        public BeaconGroup()
        {
            beacons = new ArrayList<>();
        }
        
        public void add(IntTriple p)
        {
            beacons.add(p);
        }
        
        public void removeLast()
        {
            beacons.remove(beacons.size()-1);
        }
        
        public boolean isComplete()
        {
            return beacons.size() >= 12;
        }
        
        public String print()
        {
            var sb = new StringBuilder();
            sb.append("[ ");
            for (var p : beacons)
            {
                sb.append(p).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(" ]\n");
            return sb.toString();
        }
        
        public void generateFootprint()
        {
            var distances = new long[beacons.size() * (beacons.size() - 1) / 2];
            var idx = 0;
            for (int pos1 = 0; pos1 < beacons.size(); pos1++)
            {
                for (int pos2 = pos1+1; pos2 < beacons.size(); pos2++)
                {
                    var d = beacons.get(pos1).minus(beacons.get(pos2)).lengthSquared();
                    distances[idx] = d;
                    idx++;
                }
            }
            Arrays.sort(distances);
            footprint = new Footptint(distances);
        }
        
        public Footptint getFootprint()
        {
            return footprint;
        }
    }

    public static class ScannerInfo
    {
        private int m_scannerId;
        public ArrayList<IntTriple> visibleBeacons;
        public ArrayList<BeaconGroup> beaconGroups;
        public HashMap<Footptint, BeaconGroup> footprints;

        public ScannerInfo(int scannerId)
        {
            m_scannerId = scannerId;
            visibleBeacons = new ArrayList<IntTriple>();
            beaconGroups = new ArrayList<>();
            footprints = new HashMap<>();
        }

        public void addVisibleBeacon(IntTriple p)
        {
            visibleBeacons.add(p);
        }

        public void generateBeaconGroups()
        {
            var group = new Stack<IntTriple>();
            generateBeaconGroupsImpl(group, visibleBeacons.size() - 1);
        }

        public void generateBeaconGroupsImpl(Stack<IntTriple> group, int idx)
        {
            if (group.size() >= 12)
            {
                var bg = new BeaconGroup();
                for (var p : group)
                {
                    bg.add(p);
                }
                beaconGroups.add(bg);
                return;
            }
            if (idx < 0 || (group.size() + idx < 12))
            {
                return;
            }
            generateBeaconGroupsImpl(group, idx - 1);
            group.add(visibleBeacons.get(idx));
            generateBeaconGroupsImpl(group, idx - 1);
            group.pop();
        }

        public void generateFootprints()
        {
            for (var bg : beaconGroups)
            {
                bg.generateFootprint();
                footprints.put(bg.footprint, bg);
            }
        }
        
        public boolean hasFootprint(Footptint fp)
        {
            return footprints.containsKey(fp);
        }
        
        public BeaconGroup hasCommonFootprint(ScannerInfo other)
        {
            for (var fp : footprints.entrySet())
            {
                if (other.hasFootprint(fp.getKey()))
                    return fp.getValue();
            }
            return null;
        }
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
        // System.out.println(groups.size());

        ArrayList<ScannerInfo> input = new ArrayList<>();
        int result = 0;
        System.out.println("Preprocessing...");
        for (LinesGroup group : groups)
        {
            var v = parse("--- scanner (\\d+) ---", group.get(0));
            var scannerId = parseInt(v[1]);
            var si = new ScannerInfo(scannerId);
            input.add(si);
            for (var line : Query.wrap(group).skip(1))
            {
                v = line.split(",");
                var p = new IntTriple(
                    parseInt(v[0].trim()),
                    parseInt(v[1].trim()),
                    parseInt(v[2].trim())
                    );
                si.addVisibleBeacon(p);
            }
            si.generateBeaconGroups();
            si.generateFootprints();
//            for (var bg : si.beaconGroups)
//            {
//                System.out.println(bg.print());
//            }
//            System.out.println(si.visibleBeacons.size());
//            System.out.println(si.beaconGroups.size());
//            System.out.println(si.beaconGroups.get(0).print());

//            result += group.processGroup(this::processGroup);
        }
        System.out.println("Preprocessing done.");
        var scannerZero = input.get(0);
        for (var idx = 1; idx < input.size(); idx++)
        {
            if (scannerZero.hasCommonFootprint(input.get(idx)) != null)
            {
                System.out.printf("Found connection between %s and %s\n", 0, idx);
            }
        }
        System.out.println(result);
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
//        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        int result = 0;
//        for (String line : lines)
//        {
//        }
//        System.out.println(result);
        
    }
}
