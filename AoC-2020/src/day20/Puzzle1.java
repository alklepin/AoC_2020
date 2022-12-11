package day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.boards.IntPair;
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
    
    public static class Tile
    {
        int m_id;
        char [][] m_data;
        
        int m_upId;
        int m_downId;
        int m_leftId;
        int m_rightId;
        
        public Tile(LinesGroup group)
        {
            String first = group.get(0);
            String id = parse(".+ ([0-9]+):.*", first)[1];
            m_id = parseInt(id);
            m_data = new char[10][];
            int idx = 0;
            for (String line : Query.wrap(group).skip(1))
            {
                m_data[idx] = line.toCharArray();
                idx++;
            }
            calculateIdx();
        }

        public Tile(int id, char[][] data)
        {
            m_id = id;
            m_data = data;
            calculateIdx();
        }        
        
        private void calculateIdx()
        {
            String upLine = new String(m_data[0]);
            upLine = upLine.replace('.', '0').replace('#', '1');
            m_upId = Integer.parseInt(upLine, 2);

            String downLine = new String(m_data[9]);
            downLine = downLine.replace('.', '0').replace('#', '1');
            m_downId = Integer.parseInt(downLine, 2);

            StringBuilder leftLineSB = new StringBuilder();
            StringBuilder rightLineSB = new StringBuilder();
            for (int iIdx = 0; iIdx < m_data.length; iIdx++)
            {
                leftLineSB.append(m_data[iIdx][0]);
                rightLineSB.append(m_data[iIdx][m_data[iIdx].length - 1]);
            }
            String leftLine = leftLineSB.toString();
            String rightLine = rightLineSB.toString();
            
            leftLine = leftLine.replace('.', '0').replace('#', '1');
            rightLine = rightLine.replace('.', '0').replace('#', '1');

            m_leftId = Integer.parseInt(leftLine, 2);
            m_rightId = Integer.parseInt(rightLine, 2);
        }
        
        private String reverse(String line)
        {
            StringBuilder result = new StringBuilder(line.length());
            for (int iIdx = line.length() - 1; iIdx >= 0; iIdx--)
            {
                result.append(line.charAt(iIdx));
            }
            return result.toString();
        }
        
        public void print()
        {
            System.out.println("Tile: "+m_id);
            for (char[] line : m_data)
            {
                System.out.println(new String(line));
            }
        }
        
        public Tile rotateRight()
        {
            char[][] result = (char[][])m_data.clone();
            for (int i = 0; i < result.length; i++)
            {
                result[i] = result[i].clone();
            }
            int size = m_data.length;
            for (int r = 0; r < size; r++)
            {
                for (int c = 0; c < size; c++)
                {
                    result[r][c] = m_data[size-c-1][r];
                }
            }
            return new Tile(m_id, result);
        }
        
        public Tile flip()
        {
            char[][] result = (char[][])m_data.clone();
            int size = m_data.length;
            for (int i = 0; i < result.length; i++)
            {
                result[i] = m_data[size - i - 1].clone();
            }
            return new Tile(m_id, result);
        }
        
        public int getUpId()
        {
            return m_upId;
        }
        
        public String toString()
        {
            return ""+m_id;
        }
        
    }

    HashMap<Integer, Tile> m_idToTile = new HashMap<>();
    HashMap<Integer, ArrayList<Tile>> m_upHashes = new HashMap<>();
    HashMap<Integer, ArrayList<Tile>> m_downHashes = new HashMap<>();
    HashMap<Integer, ArrayList<Tile>> m_leftHashes = new HashMap<>();
    HashMap<Integer, ArrayList<Tile>> m_rightHashes = new HashMap<>();

    ArrayList<Tile> m_tiles = new ArrayList<>();
    ArrayList<Tile> m_Alltiles = new ArrayList<>();
    
    
    public void solve()
        throws Exception
    {
        ArrayList<LinesGroup> groups = readAllLineGroups("input1.txt");
//        ArrayList<LinesGroup> groups = readAllLineGroups("test.txt");
        // System.out.println(groups.size());
        
        long result = 0;
        for (LinesGroup group : groups)
        {
            m_tiles.add(new Tile(group));
        }
        
//        for (Tile tile : tiles)
//        {
//            tile.print();
//        }
        
        for (Tile tile : m_tiles)
        {
            m_idToTile.put(tile.m_id, tile);

            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);

            tile = tile.rotateRight();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);
            
            tile = tile.rotateRight();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);

            tile = tile.rotateRight();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);

            tile = tile.rotateRight().flip();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);

            tile = tile.rotateRight();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);
            
            tile = tile.rotateRight();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);

            tile = tile.rotateRight();
            m_Alltiles.add(tile);
            updateHashes(m_upHashes, tile.m_upId, tile);
            updateHashes(m_downHashes, tile.m_downId, tile);
            updateHashes(m_leftHashes, tile.m_leftId, tile);
            updateHashes(m_rightHashes, tile.m_rightId, tile);
        }
        
//        System.out.println(m_idToTile.size());
//        System.out.println(m_upHashes.size());
//        System.out.println(m_downHashes.size());
//        System.out.println(m_leftHashes.size());
//        System.out.println(m_rightHashes.size());

//        for (Tile tile : tiles)
//        {
//            int count = 0;
//            ArrayList<Tile> list;
//            String s = "";
//            
//            list = m_leftHashes.get(tile.m_rightId); 
//            if (list.size() > 1)
//            {
//                count++;
//                s += "R";
//            }
//            
//            list = m_rightHashes.get(tile.m_leftId); 
//            if (list.size() > 1)
//            {
//                count++;
//                s += "L";
//            }
//
//            list = m_upHashes.get(tile.m_downId); 
//            if (list.size() > 1)
//            {
//                count++;
//                s += "D";
//            }
//
//            list = m_downHashes.get(tile.m_upId); 
//            if (list.size() > 1)
//            {
//                count++;
//                s += "U";
//            }
//
//            if (count < 4)
//                System.out.println(""+tile.m_id +": "+s);
//        }
        
        Tile[] board = new Tile[m_tiles.size()];
        boolean success = tryFillBoard(board);
        
        result = ((long)1) * board[encode(0,0)].m_id * board[encode(11,0)].m_id * board[encode(0,11)].m_id * board[encode(11,11)].m_id;
        
        if (success)
        {
            System.out.println("Board:");
            for (Tile t : board)
            {
                System.out.print(""+t+", ");
            }
            System.out.println();
        }
        
        for (int r = 0; r < 12; r++)
        {
            StringBuilder [] lines = new StringBuilder[10];
            for (int i = 0; i < lines.length; i++)
                lines[i] = new StringBuilder();
            for (int c = 0; c < 12; c++)
            {
                Tile t = board[encode(r, c)];
                for (int i = 0; i < lines.length; i++)
                {
                    String line = new String(t.m_data[i]);
                    lines[i].append(line.substring(1, line.length()-1));
                }
            }
            for (int i = 1; i < lines.length-1; i++)
                System.out.println(lines[i]);
        }
        
        
        System.out.println(success);
        
        System.out.println("-------------");
        
//        Tile tile = tiles.get(0);
//        tile.print();
//        tile = tile.rotateRight();
//        tile.print();
//        tile = tile.rotateRight();
//        tile.print();
//        tile = tile.rotateRight();
//        tile.print();
//        tile = tile.flip();
//        tile.print();
        
        
        System.out.println(result);
    }
    
    public ArrayList<IntPair> m_fillOrder = generateFillOrder(12,12);
//    public ArrayList<IntPair> m_fillOrder = generateFillOrder(3,3);
    private ArrayList<IntPair> generateFillOrder(int rows, int cols)
    {
        ArrayList<IntPair> result = new ArrayList<IntPair>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result.add(new IntPair(r,c));
        return result;
    }
    
    private boolean tryFillBoard(Tile [] board)
    {
        HashSet<Integer> usedTiles = new HashSet<>();
        usedTiles.clear();
        return tryFillBoard(board, 0, usedTiles);
    }
    
    private boolean tryFillBoard(Tile [] board, int cellIdx, HashSet<Integer> usedTiles)
    {
        if (cellIdx >= m_fillOrder.size())
            return true;
        
        if (board[0] != null && board[1] != null
            && board[0].m_id == 1951
            && board[1].m_id == 2311)
        {
            int i = 0;
        }
        
        for (Tile tile : m_Alltiles)
        {
            if (!usedTiles.contains(tile.m_id))
            {
                boolean canUse = true;
                IntPair cell = m_fillOrder.get(cellIdx);
                if (cell.getX() > 0)
                {
                    canUse &= tile.m_upId == board[encode(cell.getX()-1, cell.getY())].m_downId;
                }
                if (cell.getY() > 0)
                {
                    canUse &= tile.m_leftId == board[encode(cell.getX(), cell.getY()-1)].m_rightId;
                }
                if (canUse)
                {
                    board[encode(cell.getX(), cell.getY())] = tile;
                    usedTiles.add(tile.m_id);
//                    System.out.println("Board:");
//                    for (Tile t : board)
//                    {
//                        System.out.print(""+t+", ");
//                    }
//                    System.out.println();
                    if (tryFillBoard(board, cellIdx+1, usedTiles))
                    {
                        return true;
                    }
                    usedTiles.remove(tile.m_id);
                    board[encode(cell.getX(), cell.getY())] = null;
                }
            }
        }
        return false;
    }


    public int encode(int r, int c)
    {
        return r * 12 + c;
//        return r * 3 + c;
    }
    
    private void updateHashes(HashMap<Integer, ArrayList<Tile>> hashes, int key, Tile tile)
    {
        ArrayList<Tile> list = hashes.get(key);
        if (list == null)
        {
            list = new ArrayList<Tile>();
            hashes.put(key, list);
        }
        list.add(tile);
    }
    
}
