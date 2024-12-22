package day21;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.Function2;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import static common.graph.ImplicitGraph.SearchState;

public class Puzzle2_1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_1().solve();
        }
        finally
        {
            var end = System.currentTimeMillis();
            System.out.printf("Time spent: %f sec\n", (end - start) / 1000.0);
        }
    }
    
    private static String[] keypadA = new String[] {
        "789", 
        "456",   
        "123",   
        " 0A"   
    };
    
    private static char[] keypadAChars = "0123456789A".toCharArray();
    
    private static Board2D boardA = Board2D.parseAsCharsXY(new LinesGroup(keypadA)); 

    private static String[] keypadB = new String[] {
        " ^A", 
        "<v>"   
    };

    private static char[] keypadBChars = "<>v^A".toCharArray();
    
    private static Board2D boardB = Board2D.parseAsCharsXY(new LinesGroup(keypadB)); 
    
    
    public static HashSet<String> generatePaths(Board2D board, IntPair from, IntPair to)
    {
        if (board.getCharAtXY(from) == ' ')
        {
            var result = new HashSet<String>();
            return result;
        }
        
        if (from.equals(to))
        {
            var result = new HashSet<String>();
            result.add("");
            return result;
        }

        var d = to.minus(from);
        var x = d.getX();
        var y = d.getY();

        var result = new HashSet<String>();
        if (x != 0)
        {
            boolean valid = true;
            var code = new StringBuilder();
            var v = from;
            while (x > 0)
            {
                code.append('>');
                v = v.add(IntPair.RIGHT);
                valid = valid && board.getCharAtXY(v) != ' ';
                x--;
            }
            while (x < 0)
            {
                code.append('<');
                v = v.add(IntPair.LEFT);
                valid = valid && board.getCharAtXY(v) != ' ';
                x++;
            }
            if (valid)
            {
                var paths = generatePaths(board, v, to);
                for (var p : paths)
                {
                    result.add(code+p);
                }
            }
        }
        if (y != 0)
        {
            boolean valid = true;
            var code = new StringBuilder();
            var v = from;
            while (y > 0)
            {
                code.append('v');
                v = v.add(IntPair.UP);
                valid = valid && board.getCharAtXY(v) != ' ';
                y--;
            }
            while (y < 0)
            {
                code.append('^');
                v = v.add(IntPair.DOWN);
                valid = valid && board.getCharAtXY(v) != ' ';
                y++;
            }
            if (valid)
            {
                var paths = generatePaths(board, v, to);
                for (var p : paths)
                {
                    result.add(code+p);
                }
            }
        }
        return result;
    }
    
    public String[] splitCommand(String str)
    {
        var partsList = split(str, "A");
        var parts = partsList.toArray(new String[0]);
        return parts;
    }
    
    public HashMap<String, String> knownCodes = new HashMap<>();
    
    public String encodeCommandB(String cmd)
    {
        if (cmd.startsWith("v<A<AA>>^AvA<^A>AAvA^A"))
        {
            var a = 123;
        }
        var parts = splitCommand(cmd);
        var result = new StringBuilder();
        for (var p : parts)
        {
            p = p + "A";
            var code = knownCodes.get(p);
            if (code == null)
            {
                String best = null;
                var codes = encodeCommandB(p, 'A');
                for (var c : codes)
                {
                    if ((best == null) || best.length() > c.length())
                        best = c;
                }
                code = best;
            }
            knownCodes.put(p, code);
            result.append(code);
        }
        return result.toString();

//        var tmp = encodeCommandB(cmd, 'A');
//        return tmp;
    }
    
    public HashSet<String> encodeCommandB(String cmd, char from)
    {
        var result = new HashSet<String>();
        if (cmd.length() == 0)
        {
            result.add("");
            return result;
        }
        
        var moves = movesB.get(IntPair.of(from, cmd.charAt(0)));
        
        var cont = encodeCommandB(cmd.substring(1), cmd.charAt(0));
        
        for (var m : moves)
        {
            for (var c : cont)
            {
                result.add(m + "A" + c);
            }
        }
        return result;
    }

    public class DPKey
    {
        public final String cmd;
        public final int level;
        public DPKey(String cmd, int level)
        {
            super();
            this.cmd = cmd;
            this.level = level;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((cmd == null) ? 0 : cmd.hashCode());
            result = prime * result + level;
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
            DPKey other = (DPKey)obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (cmd == null)
            {
                if (other.cmd != null)
                    return false;
            }
            else if (!cmd.equals(other.cmd))
                return false;
            if (level != other.level)
                return false;
            return true;
        }
        private Puzzle2_1 getEnclosingInstance()
        {
            return Puzzle2_1.this;
        }
        @Override
        public String toString()
        {
            return "DPKey [cmd=" + cmd + ", level=" + level + "]";
        }
    }
    
    public HashMap<DPKey, Long> knownLengths = new HashMap<>();
    public HashMap<DPKey, String> knownDPCodes = new HashMap<>();
    
    public long getLengthOfCodeB(String cmd, int level)
    {
        if (level == 0)
            return cmd.length();
        
        var parts = splitCommand(cmd);
        long length = 0;
        for (var p : parts)
        {
            p = p + "A";
            var key = new DPKey(p, level);
            var partLength = knownLengths.get(key);
            if (partLength == null)
            {
                var code = knownCodes.get(p);
                if (code == null)
                {
                    String best = null;
                    var codes = encodeCommandB(p, 'A');
                    for (var c : codes)
                    {
                        if ((best == null) || best.length() > c.length())
                            best = c;
                    }
                    code = best;
                }
                knownCodes.put(p, code);
                
                partLength = getLengthOfCodeB(code, level-1);
                knownLengths.put(key, partLength);
            }
            length += partLength;
        }
        return length;
    }

    public String getCodeB(String cmd, int level)
    {
        if (level == 0)
            return cmd;
        
        var parts = splitCommand(cmd);
        long length = 0;
        var result = new StringBuilder();
        for (var p : parts)
        {
            p = p + "A";
            var key = new DPKey(p, level);
            var partLength = knownLengths.get(key);
            var partCode = knownDPCodes.get(key);
            if (partLength == null || partCode == null)
            {
                var code = knownCodes.get(p);
                if (code == null)
                {
                    String best = null;
                    var codes = encodeCommandB(p, 'A');
                    for (var c : codes)
                    {
                        if ((best == null) || best.length() > c.length())
                            best = c;
                    }
                    code = best;
                }
                knownCodes.put(p, code);
                
                partLength = getLengthOfCodeB(code, level-1);
                knownLengths.put(key, partLength);
                
                partCode = getCodeB(code, level-1);
                knownDPCodes.put(key, partCode);
            }
            length += partLength;
            result.append(partCode);
            if (partLength != partCode.length())
            {
                var a = 100;
            }
        }
        return result.toString();
    }
    
    HashMap<IntPair, HashSet<String>> movesA = generateMovesA();
    HashMap<IntPair, HashSet<String>> movesB = generateMovesB();
    
    public HashMap<IntPair, HashSet<String>> generateMovesA()
    {
        var moves = new HashMap<IntPair, HashSet<String>>();
        
        for (var c1 : keypadAChars)
        {
            for (var c2 : keypadAChars)
            {
                var p1 = boardA.findCharXY(c1).first();
                var p2 = boardA.findCharXY(c2).first();
                var paths = generatePaths(boardA, p1, p2);
                var key = IntPair.of(c1, c2);
                moves.put(key, paths);
            }
        }
        
        return moves;
    }
    
    public HashMap<IntPair, HashSet<String>> generateMovesB()
    {
        var moves = new HashMap<IntPair, HashSet<String>>();

        for (var c1 : keypadBChars)
        {
            for (var c2 : keypadBChars)
            {
                var p1 = boardB.findCharXY(c1).first();
                var p2 = boardB.findCharXY(c2).first();
                var paths = generatePaths(boardB, p1, p2);
                var key = IntPair.of(c1, c2);
                moves.put(key, paths);
            }
        }
        
        return moves;
    }
    
    public void printMoves(HashMap<IntPair, HashSet<String>> moves)
    {
        for (var entry : moves.entrySet())
        {
            var key = entry.getKey();
            System.out.println(MessageFormat.format("{0} to {1}", (char)key.getX(), (char)key.getY()));
            for (var s : entry.getValue())
                System.out.println("   "+s);
        }
    }
    
    public long mapSize(HashMap<IntPair, HashSet<String>> map)
    {
        long result = 0;
        for (var e : map.entrySet())
        {
            result += e.getValue().size();
        }
        return result;
    }

    public void solve()
        throws Exception
    {
        var inputFile = "input1.txt";
//        var inputFile = "input1_test.txt";
        
    
//        printMoves(movesA);
//        printMoves(movesB);
        
        
//        System.exit(0);
        
        HashMap<IntPair, HashSet<String>> movesA1 = new HashMap<>();
        for (var entry : movesA.entrySet())
        {
            var key = entry.getKey();
            var set = new HashSet<String>();
            for (var s : entry.getValue())
            {
                set.add(encodeCommandB(s+"A"));
            }
//            System.out.println("   "+best);
            movesA1.put(key, set);
        }
        
        
        HashMap<IntPair, HashSet<String>> movesA2 = new HashMap<>();
        for (var entry : movesA1.entrySet())
        {
            var key = entry.getKey();
            var set = new HashSet<String>();
            for (var s : entry.getValue())
            {
                set.add(encodeCommandB(s));
            }
//            System.out.println("   "+best);
            movesA2.put(key, set);
        }

        HashMap<IntPair, HashSet<String>> movesA3 = new HashMap<>();
        for (var entry : movesA2.entrySet())
        {
            var key = entry.getKey();
            var set = new HashSet<String>();
            for (var s : entry.getValue())
            {
                set.add(encodeCommandB(s));
            }
//            System.out.println("   "+best);
            movesA3.put(key, set);
        }

        HashMap<IntPair, HashSet<String>> movesA4 = new HashMap<>();
        for (var entry : movesA3.entrySet())
        {
            var key = entry.getKey();
            var set = new HashSet<String>();
            for (var s : entry.getValue())
            {
                set.add(encodeCommandB(s));
            }
//            System.out.println("   "+best);
            movesA4.put(key, set);
        }
        
//        for (var e : knownCodes.entrySet())
//        {
//            System.out.println(e.getKey()+" "+e.getValue());
//        }
        
        
        System.out.println(mapSize(movesA));
        System.out.println(mapSize(movesA1));
//        System.out.println(mapSize(movesA2));
//        System.out.println(mapSize(movesA3));
        
        HashMap<IntPair, String> movesAF = new HashMap<>();
        for (var entry : movesA4.entrySet())
        {
            var key = entry.getKey();
            String best = null;
            for (var s : entry.getValue())
            {
                var set = encodeCommandB(s);
                var s1 = set;
                if (best == null || best.length() > s1.length())
                    best = s1;
            }
//            System.out.println(MessageFormat.format("{0} to {1}", (char)key.getX(), (char)key.getY()));
//            System.out.println("   "+best);
            movesAF.put(key, best);
        }
        
        int level = 3;
        HashMap<IntPair, Long> movesAFL = new HashMap<>();
        for (var entry : movesA.entrySet())
        {
            var key = entry.getKey();
            var best = Long.MAX_VALUE;
            String bestCode = null;
            
            for (var s : entry.getValue())
            {
                var cmd = s+"A";
                var length = getLengthOfCodeB(cmd, level);
                var tmpCode = getCodeB(cmd, level);
                System.out.println("> "+printAsChars(key)+": "+tmpCode);
                if (length < best)
                {
                    System.out.println("> "+printAsChars(key)+": chosen as the best");
                    best = length;
                    bestCode = tmpCode;
                }
            }
            System.out.println("   "+best);
            movesAFL.put(key, best);
            
            var code = movesAF.get(key);
            if (code.length() != best)
            {
                System.out.println("!!! "+printAsChars(key)+": "+code + " "+bestCode);
                System.out.println("!!! "+printAsChars(key)+": " + code.length() + " "+best);
            }
            
        }
        
        
        System.out.println(mapSize(movesA));
        System.out.println(mapSize(movesA1));
        System.out.println(movesAF.size());

        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        
        long result = 0;
        for (String line : lines)
        {
            var s = "A"+line;
            var res = new StringBuilder();
            long length = 0;
            for (var idx = 1; idx < s.length(); idx++)
            {
                var key = IntPair.of(s.charAt(idx-1), s.charAt(idx));
                var code = movesAF.get(key);
                var codeLength = movesAFL.get(key); 
                if (code.length() != codeLength)
                {
                    System.out.println("!!! "+printAsChars(key)+": "+code);
                    System.out.println("!!! "+printAsChars(key)+": " + code.length() + " "+codeLength);
                }
                length += codeLength;
                res.append(code);
            }
            System.out.println(MessageFormat.format("{0}: {1}", line, res));
            System.out.println(line + ": " + res.length());
            System.out.println(line + ": " + length);
            
//            result += res.length() * parseInt(line.substring(0, line.length()-1));
            result += length * parseInt(line.substring(0, line.length()-1));
        }
        System.out.println(result);
        
    }
    
    public String printAsChars(IntPair p)
    {
        return new StringBuilder()
            .append('(')
            .append((char)p.getX())
            .append(',')
            .append((char)p.getY())
            .append(')')
            .toString();
    }
    
    
    /**
     * Возвращает, таблицу (c1, c2) -> long
     * Это длина цепочки команд, если рука находилась в состоянии с1 и нужно ввести команду c2  
     * @param keypadChars
     * @param keypadBoard
     * @param moveCosts
     * @return
     */
    public HashMap<IntPair, Long> findKeypadMoveCosts(char [] keypadChars, Board2D keypadBoard, Function2<IntPair, IntPair, Long> moveCosts)
    {
        var keypadMoveCosts = new HashMap<IntPair, Long>();
        for (var c1 : keypadChars)
        {
            for (var c2 : keypadChars)
            {
                var start = keypadBoard.findCharXY(c1).first();
                var end = keypadBoard.findCharXY(c2).first();
                var result = ImplicitGraph.DijkstraLong(start, 
                    s1 -> s1.equals(end), 
                    s2 -> {
                        var d = s2.getDistance();
                        var nodes = keypadBoard.neighbours4XY(s2.getNode())
                            .where(n -> keypadBoard.getCharAtXY(n) != ' ')
                            .select(n -> new SearchState<>(n, d + moveCosts.apply(s2.getNode(), n)));
                        return nodes;
                    });
                var moveKey = IntPair.of(c1, c2);
                keypadMoveCosts.put(moveKey, result.getEndNodeDistance());
            }
        }
        return keypadMoveCosts;
    }
    
    public void printCostMap(char [] keypadChars, HashMap<IntPair, Long> costMap)
    {
        for (var c1 : keypadChars)
        {
            for (var c2 : keypadChars)
            {
                System.out.println(MessageFormat.format("{0} -> {1} : {2}", c1, c2, costMap.get(IntPair.of(c1, c2))));
            }
        }
    }
}
