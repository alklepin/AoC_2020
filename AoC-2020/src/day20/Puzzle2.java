package day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import common.PuzzleCommon;
import common.boards.IntPair;
import day20.Puzzle1.Tile;

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
    
    
    public String[] monsterStrings = new String[] {
                                    "                  # ", 
                                    "#    ##    ##    ###", 
                                    " #  #  #  #  #  #   "};
    
    public Pattern monster = new Pattern(encodeMonster(monsterStrings));

    private static ArrayList<IntPair> encodeMonster(String[] monsterString)
    {
        ArrayList<IntPair> result = new ArrayList<>();
        for (int r = 0; r < monsterString.length; r++)
        {
            String line = monsterString[r];
            for (int c = 0; c < line.length(); c++)
            {
                if (line.charAt(c) == '#')
                {
                    result.add(new IntPair(r,c));
                }
            }
        }
        return result;
    }
    
//    private static ArrayList<IntPair> flip(ArrayList<IntPair> source)
//    {
//        
//    }

    public static class Pattern
    {
        public ArrayList<IntPair> m_cells = new ArrayList<IntPair>();
        public int m_maxR;
        public int m_maxC;
        
        public Pattern(ArrayList<IntPair> source)
        {
            int minR = Integer.MAX_VALUE;
            int minC = Integer.MAX_VALUE;
            for (IntPair pair : source)
            {
                minR = Math.min(minR, pair.getX());
                minC = Math.min(minC, pair.getY());
            }
            m_maxR = Integer.MIN_VALUE;
            m_maxC = Integer.MIN_VALUE;
            for (IntPair pair : source)
            {
                IntPair toAdd = new IntPair(pair.getX() - minR, pair.getY() - minC);
                m_cells.add(toAdd);
                m_maxR = Math.max(m_maxR, toAdd.getX());
                m_maxC = Math.max(m_maxC, toAdd.getY());
            }
        }
        
        public Pattern flip()
        {
            ArrayList<IntPair> newCells = new ArrayList<IntPair>();
            for (IntPair pair : m_cells)
            {
                newCells.add(new IntPair(-pair.getX(), pair.getY()));
            }
            return new Pattern(newCells);
        }
        
        public Pattern rotateRight()
        {
            ArrayList<IntPair> newCells = new ArrayList<IntPair>();
            for (IntPair pair : m_cells)
            {
                newCells.add(new IntPair(m_maxC-pair.getY(), pair.getX()));
            }
            return new Pattern(newCells);
        }
        
        public void print()
        {
            char[][] matrix = new char[m_maxR+1][];
            for (int i = 0; i < matrix.length; i++)
            {
                matrix[i] = new char[m_maxC+1];
                Arrays.fill(matrix[i], '.');
            }
            for (IntPair pair : m_cells)
            {
                matrix[pair.getX()][pair.getY()] = '#';
            }
            for (int i = 0; i < matrix.length; i++)
            {
                System.out.println(new String(matrix[i]));
            }
        }
        
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<String> lines = readAllLines("input2.txt");
//        ArrayList<String> lines = readAllLines("test2.txt");

        char[][] matrix1 = new char[lines.size()][];
        char[][] matrix2 = new char[lines.size()][];
        
        int result = 0;
        int idx = 0;
        for (String line : lines)
        {
            matrix1[idx] = line.toCharArray();
            matrix2[idx] = line.toCharArray();
            idx++;
        }
        
        ArrayList<Pattern> monsters = new ArrayList<Pattern>();
        monsters.add(monster);
        monsters.add(monster.flip());
        monsters.add(monster.rotateRight());
        monsters.add(monster.rotateRight().flip());
        monsters.add(monster.rotateRight().rotateRight());
        monsters.add(monster.rotateRight().rotateRight().flip());
        monsters.add(monster.rotateRight().rotateRight().rotateRight());
        monsters.add(monster.rotateRight().rotateRight().rotateRight().flip());
        
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                for (Pattern m : monsters)
                {
                    boolean hasMatch = true;
                    for (IntPair cell : m.m_cells)
                    {
                        int x = r + cell.getX();
                        int y = c + cell.getY();
                        if (x >= rows || y >= cols)
                        {
                            hasMatch = false;
                            break;
                        }
                        if (matrix1[x][y] != '#')
                        {
                            hasMatch = false;
                            break;
                        }
                    }
                    if (hasMatch)
                    {
                        System.out.printf("r: %d;  c: %d\n", r, c);
                        m.print();
                        for (IntPair cell : m.m_cells)
                        {
                            int x = r + cell.getX();
                            int y = c + cell.getY();
                            matrix2[x][y] = '0';
                        }
                        for (char[] line : matrix2)
                        {
                            System.out.println(new String(line));
                        }
                        System.out.println();
                    }
                }
            }
        }
        int count = 0;
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                if (matrix2[r][c] == '#')
                {
                    count++;
                }
            }
        }
      System.out.println(count);
        
        
        
        
//        monster.print();
//        System.out.println("--");
//        monster.rotateRight().print();
//        System.out.println("--");
//        monster.rotateRight().rotateRight().print();
//        System.out.println("----");
//        monster.flip().print();
//        System.out.println(result);
        
    }

}
