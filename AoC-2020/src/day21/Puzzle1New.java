package day21;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import common.LinesGroup;
import common.PuzzleCommon;
import common.graph.BipartiteGraph;

public class Puzzle1New extends PuzzleCommon
{

    public static void main(String [] args)
        throws Exception
    {
        new Puzzle1New().solve();
    }
    
    public void solve()
        throws Exception
    {
        HashSet<String> ingredients = new HashSet<>();
        HashSet<String> allergens = new HashSet<>();
        HashMap<String, String> alergToIngr = new HashMap<>();
        HashMap<String, HashSet<String>> alergToIngrSet = new HashMap<>();
        
        LinesGroup lines = readAllLines("input1.txt");
//        LinesGroup lines = readAllLines("test.txt");
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

        BipartiteGraph<String, String> graph = new BipartiteGraph<>();
        for (String alerg : allergens)
        {
            HashSet<String> knownIngrs = alergToIngrSet.get(alerg);
            System.out.println("Allergen: "+alerg);
            for (String ingr : knownIngrs)
            {
                System.out.println("  -> "+ingr);
                graph.addRelation(alerg, ingr);
            }
            
        }
        graph.findBestMatching();
        for (String alerg : allergens)
        {
            String ingr = graph.getMatch(alerg);
            
            System.out.println("Allergen: "+alerg + " -> " + ingr);
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
