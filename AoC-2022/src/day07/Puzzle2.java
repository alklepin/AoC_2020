package day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;

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
    
    private class FileInfo
    {
        String name;
        String path;
        int size;
    }
    
    private HashMap <String, FileInfo> fileInfos = new HashMap<>();
    private HashMap<String, HashSet<String>> dirs = new HashMap<>();
    private HashMap<String, HashSet<String>> filesInDir = new HashMap<>();
    
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
        
        var currentPath = "/";
        ArrayList<String> lines = readAllLinesNonEmpty("input1.txt");
//        ArrayList<String> lines = readAllLinesNonEmpty("input1_test.txt");
        for (int lineIdx = 0 ; lineIdx < lines.size(); lineIdx++)
        {
            var line = lines.get(lineIdx);
            var parts = line.split(" ");
            switch (parts[1])
            {
                case "cd":
                {
                    var dirName = parts[2];
                    if (dirName.equals("/"))
                    {
                        currentPath = "/";
                    }
                    else if (dirName.equals(".."))
                    {
                        if (currentPath.length() > 1)
                        {
                            var lastSlash = currentPath.lastIndexOf('/', currentPath.length() - 2);
                            currentPath = currentPath.substring(0, lastSlash + 1);
                        }
                    }
                    else
                    {
                        var newPath = currentPath + dirName + "/";
                        var currentSubdirs = dirs.get(currentPath);
                        if (currentSubdirs == null)
                        {
                            currentSubdirs = new HashSet<String>();
                            dirs.put(currentPath, currentSubdirs);
                        }
                        currentSubdirs.add(newPath);
                        currentPath = newPath;
                    }
                }
                case "ls":
                {
                    lineIdx++;
                    while (lineIdx < lines.size())
                    {
                        line = lines.get(lineIdx);
                        parts = line.split(" ");
                        if (parts[0].equals("$"))
                        {
                            lineIdx--;
                            break;
                        }
                        var info = parts[0];
                        var size = parseInt(info, -1);
                        var name = parts[1];
                        var fullPath = currentPath + name;
                        if (size >= 0)
                        {
                            var fileInfo = new FileInfo();
                            fileInfo.name = name;
                            fileInfo.path = fullPath;
                            fileInfo.size = size;
                            fileInfos.put(fullPath, fileInfo);
                            
                            var dir = filesInDir.get(currentPath);
                            if (dir == null)
                            {
                                dir = new HashSet<>();
                                filesInDir.put(currentPath, dir);
                            }
                            dir.add(fullPath);
                        }
                        lineIdx++;
                    }
                }
            }
        }
        
        long result = 0;
        processDirs("/");

        var s = new ArrayList<Long>(sizes.values());
        Collections.sort(s);
        
        var rootSize = sizes.get("/");
        var freeBefore = 70000000 - rootSize;
        var needToFree = 30000000 - freeBefore;
        
        for (var cs : s)
        {
            if (cs > needToFree)
            {
                result = cs;
                break;
            }
        }
        
        System.out.println(result);
        
    }
    
    
    private HashMap<String, Long> sizes = new HashMap<>();
    
    private void processDirs(String path)
    {
        long dirSize = 0;
        
        var files = filesInDir.get(path);
        if (files != null)
        {
            for (var f : files)
            {
                var fi = fileInfos.get(f);
                dirSize += fi.size;
            }
        }
        
        var subDirs = dirs.get(path);
        if (subDirs != null)
        {
            for (var s : subDirs)
            {
                processDirs(s);
                dirSize += sizes.get(s);
            }
        }
        sizes.put(path, dirSize);
    }
}
