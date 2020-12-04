package day4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.PuzzleCommon;

public class Puzzle2 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle2().solve();
    }

    private enum PassportKind
    {
        Valid,
        Invalid,
        NorthPoleCred
    }

    private static Pattern pattern = Pattern.compile("(eyr|hgt|hcl|pid|byr|ecl|ecl|iyr|cid):([^ ]+)");
    
    private static class Passport
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
            String dataLines = join(lines, ' ');
            String [] strFields = dataLines.toString().split(" ");
            for (String field : strFields)
            {
                String[] items = parse(pattern, field);
                if (items != null)
                {
                    fields.put(items[1], items[2]);
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

            if (k != PassportKind.Invalid)
            {
                boolean isValid = true;
                isValid &= fitsRange(fields.get("byr"), 1920, 2002);
                isValid &= fitsRange(fields.get("iyr"), 2010, 2020);
                isValid &= fitsRange(fields.get("eyr"), 2020, 2030);
                isValid &= isValidHeight(fields.get("hgt"));
                isValid &= isValidHairColor(fields.get("hcl"));
                isValid &= isValidEyeColor(fields.get("ecl"));
                isValid &= isValidPassportId(fields.get("pid"));
                
                if (!isValid)
                    k = PassportKind.Invalid;
            }
            
            kind = k;
            return k;
        }
        
        private static Pattern idPattern = Pattern.compile("[0-9]{9}");
        private boolean isValidPassportId(String value)
        {
            Matcher m = idPattern.matcher(value);
            return m.matches();
        }

        private static Pattern eyeColorPattern = Pattern.compile("(amb|blu|brn|gry|grn|hzl|oth)");
        private boolean isValidEyeColor(String value)
        {
            Matcher m = eyeColorPattern.matcher(value);
            return m.matches();
        }

        private static Pattern colorPattern = Pattern.compile("#[0-9a-f]{6}");

        private boolean isValidHairColor(String value)
        {
            Matcher m = colorPattern.matcher(value);
            return m.matches();
        }

        private static Pattern heightPattern = Pattern.compile("([0-9]+)(cm|in)");
        
        private boolean isValidHeight(String sHeight)
        {
            Matcher m = heightPattern.matcher(sHeight);
            if (m.matches())
            {
                if (m.group(2).equals("cm"))
                    return fitsRange(m.group(1), 150, 193);
                else
                    return fitsRange(m.group(1), 59, 76);
            }
            return false;
        }

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
