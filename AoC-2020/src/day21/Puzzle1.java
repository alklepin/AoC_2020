package day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import common.PuzzleCommon;

public class Puzzle1 extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1().solve();
    }
    
    public void solve()
        throws Exception
    {
        HashSet<String> ingredients = new HashSet<>();
        HashSet<String> allergens = new HashSet<>();
        HashMap<String, String> alergToIngr = new HashMap<>();
        HashMap<String, HashSet<String>> alergToIngrSet = new HashMap<>();
        
        ArrayList<String> lines = readAllLines("input1.txt");
//        ArrayList<String> lines = readAllLines("test.txt");
        int result = 0;
        for (String line : lines)
        {
            String [] parts = parse("(.+)\\(contains (.+)\\)", line);
            
            for (String alerg : parts[2].split(",? "))
            {
                allergens.add(alerg);
                HashSet<String> ingrs = new HashSet<>();
                for (String ingr : parts[1].split(",? "))
                {
                    ingrs.add(ingr);
                    ingredients.add(ingr);
                    alergToIngr.put(alerg, ingr);
                }
                HashSet<String> knownIngrs = alergToIngrSet.get(alerg);
                if (knownIngrs == null)
                {
                    knownIngrs = ingrs;
                    alergToIngrSet.put(alerg, knownIngrs);
                }
                else
                {
                    knownIngrs.retainAll(ingrs);
                }
            }
        }
        for (String alerg : allergens)
        {
            HashSet<String> knownIngrs = alergToIngrSet.get(alerg);
            System.out.println("Allergen: "+alerg);
            for (String ingr : knownIngrs)
            {
                System.out.println("  -> "+ingr);
            }
            
        }

        HashSet<String> forbiddenIngr = new HashSet<>();
        forbiddenIngr.addAll(Arrays.asList(
            "kgbzf", "fllssz", "zcdcdf", "fvvrc", "qpxhfp", "kpsdtv", "dqbjj", "pzmg"));
        

        result = 0;
        for (String line : lines)
        {
            String [] parts = parse("(.+)\\(contains (.+)\\)", line);
            
            for (String ingr : parts[1].split(",? "))
            {
                if (!forbiddenIngr.contains(ingr))
                    result++;
            }
        }
        
        System.out.println(ingredients.size());
        System.out.println(allergens.size());
        System.out.println(result);
        
    }
}
