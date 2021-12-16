package day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.PuzzleCommon;
import common.queries.Query;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
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
    
    public static HashMap<Character, String> hexToBinary = new HashMap<>();
    static
    {
        hexToBinary.put('0', "0000");
        hexToBinary.put('1', "0001");
        hexToBinary.put('2', "0010");
        hexToBinary.put('3', "0011");
        hexToBinary.put('4', "0100");
        hexToBinary.put('5', "0101");
        hexToBinary.put('6', "0110");
        hexToBinary.put('7', "0111");
        hexToBinary.put('8', "1000");
        hexToBinary.put('9', "1001");
        hexToBinary.put('A', "1010");
        hexToBinary.put('B', "1011");
        hexToBinary.put('C', "1100");
        hexToBinary.put('D', "1101");
        hexToBinary.put('E', "1110");
        hexToBinary.put('F', "1111");
    }
    
    public void solve()
        throws Exception
    {
//        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        // System.out.println(groups.size());
//        
//        int result = 0;
//        for (LinesGroup group : groups)
//        {
//            result += group.processGroup(this::processGroup);
//        }
//        System.out.println(result);
        
//        ArrayList<String> lines = readAllLines("input1.txt");
        
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input2.txt");
        int result = 0;
        var line = lines.get(0);
        var buffer = new StringBuilder();
        for (var c : line.toCharArray())
        {
            buffer.append(hexToBinary.get(c));
        }
        var binaryData = buffer.toString();
        System.out.println(binaryData);
        
        var packets = Packet.parse(binaryData, 0, false);
        for (var p : packets)
        {
            long l = p.evaluate();
            System.out.println(l);
        }
    }
    
    public int verSum = 0;
    
    public void sum(Packet packet)
    {
        verSum += packet.version;
        for (var p : packet.packets)
        {
            sum(p);
        }
    }
    
    public static class Packet
    {
        public int version;
        public int type;
        public int length;
        public long number;
        public ArrayList<Packet> packets = new ArrayList<>();
        
        
        public static ArrayList<Packet> parse(String data, int fromPos, boolean singlePacket)
        {
            var resultPackets = new ArrayList<Packet>();
            var currentPos = fromPos;
            while (currentPos < data.length() - 7 && (!singlePacket || resultPackets.size() == 0))
            {   
                var lastPost = currentPos;
                var versionString = data.substring(currentPos, currentPos+3);
                currentPos += 3;
                var typeString = data.substring(currentPos, currentPos+3);
                currentPos += 3;
                
                var version = Integer.parseInt(versionString, 2);
                var type = Integer.parseInt(typeString, 2);
                if (type == 4)
                {
                    long number = 0;
                    var stop = false;
                    while (!stop)
                    {
                        var segmentType = data.charAt(currentPos);
                        var segment = data.substring(currentPos + 1, currentPos+5);
                        currentPos += 5;
                        number = number * 16 + Integer.parseInt(segment, 2);
                        if (segmentType == '0')
                            stop = true;
                    }
                    var result = new Packet(version, type, currentPos - lastPost);
                    result.number = number;
                    resultPackets.add(result);
                    System.out.printf("Parsed number: (%s, %s) - %s\n", lastPost, currentPos, data.substring(lastPost, currentPos));
                }
                else
                {
                    var kind = data.charAt(currentPos);
                    var packets = new ArrayList<Packet>();
                    currentPos++;
                    if (kind == '0')
                    {
                        var length = Integer.parseInt(data.substring(currentPos, currentPos + 15), 2);
                        currentPos += 15;
                        System.out.printf("Parsing subpackages: (pos: %s length: %s) - %s\n", lastPost, length, data.substring(lastPost, currentPos + length));
                        var innerPackets = parse(data.substring(currentPos, currentPos+length), 0, false);
                        for (var p : innerPackets)
                        {
                            currentPos += p.length;
                            length += p.length;
                            packets.add(p);
                        }
                        System.out.printf("Parsed %s packets\n", innerPackets.size());
                    }
                    else
                    {
                        var length = 0;
                        var count = Integer.parseInt(data.substring(currentPos, currentPos + 11), 2);
                        currentPos += 11;
                        System.out.printf("Parsing subpackages: (pos: %s count: %s)\n", lastPost, count);
                        while (count > 0)
                        {
                            var innerPackets = parse(data, currentPos, true);
                            for (var p : innerPackets)
                            {
                                currentPos += p.length;
                                length += p.length;
                                packets.add(p);
                            }
                            count--;
                        }
                        System.out.printf("Parsed %s packets - %s\n", packets.size(), data.substring(lastPost, currentPos));
                    }
                    var result = new Packet(version, type, currentPos - lastPost);
                    result.packets = packets;
                    resultPackets.add(result);
                }
                
            }
            return resultPackets;
        }


        public Packet(int version, int type, int length)
        {
            super();
            this.version = version;
            this.type = type;
            this.length = length;
        }
        
        public long evaluate()
        {
            switch (type)
            {
                case 0:
                {
                    long value = 0;
                    for (var p : packets)
                    {
                        long op = p.evaluate();
                        System.out.printf("Sum: %s + %s = %s\n", value, op, value + op);
                        value += op;
                    }
                    return value;
                }
                case 1:
                {
                    long value = 1;
                    for (var p : packets)
                    {
                        long op = p.evaluate();
                        System.out.printf("Mul: %s * %s = %s\n", value, op, value * op);
                        value *= op;
                    }
                    return value;
                }
                case 2:
                {
                    long value = Long.MAX_VALUE;
                    for (var p : packets)
                    {
                        value = Math.min(value, p.evaluate());
                    }
                    return value;
                }
                case 3:
                {
                    long value = Long.MIN_VALUE;
                    for (var p : packets)
                    {
                        value = Math.max(value, p.evaluate());
                    }
                    return value;
                }
                case 4:
                {
                    return number;
                }
                case 5:
                {
                    var v1 = packets.get(0).evaluate();
                    var v2 = packets.get(1).evaluate();
                    return v1 > v2 ? 1 : 0;
                }
                case 6:
                {
                    var v1 = packets.get(0).evaluate();
                    var v2 = packets.get(1).evaluate();
                    return v1 < v2 ? 1 : 0;
                }
                case 7:
                {
                    var v1 = packets.get(0).evaluate();
                    var v2 = packets.get(1).evaluate();
                    return v1 == v2 ? 1 : 0;
                }
            }
            throw new IllegalStateException();
        }

        @Override
        public String toString()
        {
            return "Packet [version=" + version + ", type=" + type + ", length="
                + length + ", number=" + number + "]";
        }


        
    }
}
