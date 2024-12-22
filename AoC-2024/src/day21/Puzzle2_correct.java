package day21;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import common.Function2;
import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.Board2D;
import common.boards.IntPair;
import common.graph.ImplicitGraph;
import common.graph.ImplicitGraph.SearchState;

public class Puzzle2_correct extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        var start = System.currentTimeMillis();
        try
        {
            new Puzzle2_correct().solve();
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
            result.add("A");
            return result;
        }

        var d = to.minus(from);
        var result = new HashSet<String>();
        {
            boolean valid = true;
            var code = new StringBuilder();
            var x = d.getX();
            var y = d.getY();
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
                result.add(code+"A");
            }
        }
        {
            boolean valid = true;
            var code = new StringBuilder();
            var x = d.getX();
            var y = d.getY();
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
                result.add(code+"A");
            }
        }
        return result;
    }
    
    public String[] splitCommand(String str)
    {
        var partsList = split(str, "A");
        return partsList.toArray(new String[0]);
    }
    
    public HashMap<String, HashSet<String>> knownCodes = new HashMap<>();
    
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
                result.add(m + c);
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
        private Puzzle2_correct getEnclosingInstance()
        {
            return Puzzle2_correct.this;
        }
        @Override
        public String toString()
        {
            return "DPKey [cmd=" + cmd + ", level=" + level + "]";
        }
    }
    
    public HashMap<DPKey, Long> knownLengths = new HashMap<>();
    public HashMap<DPKey, HashSet<String>> knownDPCodes = new HashMap<>();
    
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
            Long partLength = knownLengths.get(key);
            if (partLength == null)
            {
                var codes = knownCodes.get(p);
                if (codes == null)
                {
                    codes = encodeCommandB(p, 'A');
                }
                knownCodes.put(p, codes);
                
                long best = Long.MAX_VALUE;
                for (String code : codes)
                {
                    var curLength = getLengthOfCodeB(code, level-1);
                    if (curLength < best)
                        best = curLength;
                }
                
                partLength = best;
                knownLengths.put(key, partLength);
            }
            length += partLength;
        }
        return length;
    }

    public HashSet<String> getCodeB(String cmd, int level)
    {
        var result = new HashSet<String>(); 
        if (level == 0)
        {
            result.add(cmd);
            return result;
        }
        
        var parts = splitCommand(cmd);
        for (var p : parts)
        {
            p = p + "A";
            var key = new DPKey(p, level);
            var partCodes = knownDPCodes.get(key);
            if (partCodes == null)
            {
                var codes = knownCodes.get(p);
                if (codes == null)
                {
                    codes = encodeCommandB(p, 'A');
                }
                knownCodes.put(p, codes);
                
                partCodes = new HashSet<>();
                
                for (String code : codes)
                {
                    partCodes.addAll(getCodeB(code, level-1));
                }
                knownDPCodes.put(key, partCodes);
            }
            var tmp = new HashSet<String>();
            for (var s1 : result)
            {
                for (var s2 : partCodes)
                {
                    tmp.add(s1 + s2);
                }
            }
            result = tmp;
        }
        return result;
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
        
        
        
        int level = 25;
        HashMap<IntPair, Long> movesAFL = new HashMap<>();
        for (var entry : movesA.entrySet())
        {
            var key = entry.getKey();
            var best = Long.MAX_VALUE;
            String bestCode = null;
            
            for (var s : entry.getValue())
            {
                var cmd = s;
                long length = getLengthOfCodeB(cmd, level);
//                var tmpCode = getCodeB(cmd, level);
//                System.out.println("> "+printAsChars(key)+": "+tmpCode);
                if (length < best)
                {
//                    System.out.println("> "+printAsChars(key)+": chosen as the best");
//                    bestCode = tmpCode;
                    best = length;
                }
            }
//            System.out.println("   "+best);
            movesAFL.put(key, best);
            
//            var code = movesAF.get(key);
//            if (code.length() != best)
//            {
//                System.out.println("!!! "+printAsChars(key)+": "+code + " "+bestCode);
//                System.out.println("!!! "+printAsChars(key)+": " + code.length() + " "+best);
//            }
            
        }
        
        
        LinesGroup lines = readAllLinesNonEmpty(inputFile);
        
        long result = 0;
        for (String line : lines)
        {
            var s = "A"+line;
            long length = 0;
            for (var idx = 1; idx < s.length(); idx++)
            {
                var key = IntPair.of(s.charAt(idx-1), s.charAt(idx));
                long codeLength = movesAFL.get(key); 
                length += codeLength;
            }
            System.out.println(line + ": " + length);
            
//            result += res.length() * parseInt(line.substring(0, line.length()-1));
            result += length * parseInt(line.substring(0, line.length()-1));
        }
        System.out.println(result);
        
//        printDecoded("v<A<AA>>^AvAA<^A>Av<A<A>>^Av<<A>>^AvAA<^A>AA<vA^>AAv<<A>^A>AvA^A<vA<AA>>^AvAA^<A>Av<A>^A<A>Av<A<AA>>^AvAA^<A>Av<<A>A^>AvA^A<A>AAv<<A>>^AvA^Av<A<AA>>^AvA^AvA^<A>AAA<vA^>Av<<A>^A>AvA^A", level);
//        printDecoded("<vA<AA>>^AvAA^<A>Av<<A>A>^Av<<A>>^AvAA^<A>AA<vA>^AA<Av<A>>^AvA^A<vA<AA>>^AvAA^<A>A<vA>^A<A>A<vA<AA>>^AvAA^<A>A<vA>^Av<<A>>^AvA<^A>AAv<<A>>^AvA^A<vA<AA>>^AvA^AvA<^A>AAA<vA>^Av<<A>^A>AvA^A", level);
        
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
    
    public void printDecoded(String code, int level)
    {
        System.out.println("Decoding: "+code);
        for (var idx = 0; idx < level; idx++)
        {
            code = decodeB(code);
            System.out.println("Level #"+idx+ ": "+code);
        }
    }
    
    public String decodeB(String code)
    {
        var pos = boardB.findCharXY('A').first();
        var codeChars = code.toCharArray();
        var result = new StringBuilder();
        for (var c : codeChars)
        {
            if (c == 'A')
            {
                var boardChar = boardB.getCharAtXY(pos);
                result.append(boardChar);
            }
            else
            {
                var dir = IntPair.decodeDirectionVInv_XY(c);
                pos = pos.add(dir);
                if (!boardB.containsXY(pos))
                {
                    throw new IllegalStateException("out: "+pos);
                }
                var boardChar = boardB.getCharAtXY(pos);
                if (boardChar == ' ')
                {
                    throw new IllegalStateException("illegal cell: "+pos);
                }
            }
        }
        return result.toString();
    }
}
