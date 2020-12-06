package day4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }

    private enum PassportKind
    {
        Valid,
        Invalid,
        NorthPoleCred
    }

    private static Pattern pattern = Pattern.compile("(eyr|hgt|hcl|pid|byr|ecl|ecl|iyr|cid):([^ ]+)");
    
//    eyr:2021 hgt:168cm hcl:#fffffd pid:180778832 byr:1923 ecl:amb iyr:2019 cid:241
    
    private class Passport
    {
        private ArrayList<String> lines = new ArrayList<>();
        private PassportKind kind = PassportKind.Invalid;
        private HashMap<String, String> fields = new HashMap<>();
        
        public void addLine(String line)
        {
            lines.add(line);
        }
        
        public PassportKind parseData()
        {
            StringBuilder sb = new StringBuilder();
            for (String line : lines)
            {
                sb.append(line).append(' ');
            }
            String [] strFields = sb.toString().split(" ");
            for (String field : strFields)
            {
                Matcher m = pattern.matcher(field);
                if (m.matches())
                {
                    String fieldCode = m.group(1); 
                    String fieldValue = m.group(2); 
                    fields.put(fieldCode, fieldValue);
                }
            }
            PassportKind k;
            if (fields.size() >= 8)
            {
                k = PassportKind.Valid;
            }
            else if (fields.size() < 7)
            {
                k = PassportKind.Invalid;
            }
            else
            {
                if (fields.containsKey("cid"))
                {
                    k = PassportKind.Invalid;
                }
                else
                {
                    k = PassportKind.NorthPoleCred;
                }
            }
            kind = k;
            return k;
        }
        
        @SuppressWarnings("unused")
        public void print()
        {
            System.out.println("===");
            for (String line : lines)
            {
                System.out.println(line);
            }
            System.out.println("---");
            
            for (Map.Entry<String, String> entry : fields.entrySet())
            {
                System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
            }
            
            System.out.println("Size: "+ fields.size());
            System.out.println("Kind: "+ kind);
        }
    }
    
    public void solve()
        throws Exception
    {
        ArrayList<Passport> passports = new ArrayList<>();
        
        try (InputStream fis = loadLocalFile("input1.txt"))
        {
            try (Scanner scanner = new Scanner(fis, "UTF8"))
            {
                Passport current = null;
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine().trim();
                    if (line.length() == 0)
                    {
                        if (current != null)
                        {
                            passports.add(current);
                            current = null;
                        }
                    }
                    else
                    {
                        if (current == null)
                        {
                            current = new Passport();
                        }
                        current.addLine(line);
                    }
                }
                if (current != null)
                {
                    passports.add(current);
                }
            }
        }

        System.out.println(passports.size());
        
        int validCounter = 0;
        for (Passport passport : passports)
        {
            if (passport.parseData() != PassportKind.Invalid)
            {
                validCounter++;
//                passport.print();
            }
            else
            {
//                passport.print();
            }
        }
        
        System.out.println(validCounter);
    }
}
