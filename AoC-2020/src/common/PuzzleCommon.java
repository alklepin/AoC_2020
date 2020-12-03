package common;

import java.io.InputStream;

public class PuzzleCommon
{
    public InputStream loadLocalFile(String fileName)
    {
        ClassLoader cl = getClass().getClassLoader();
        if (cl == null)
            cl = ClassLoader.getSystemClassLoader();
        String localPath = getClass().getPackageName().replace('.', '/');
        return cl.getResourceAsStream(localPath + '/' + fileName);        
    }
}
