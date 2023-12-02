package day25;

import java.util.ArrayList;
import java.util.List;

class MyStringBuilder
{
    private final List<Character> chars;

    public MyStringBuilder() {
        chars = new ArrayList<>();
    }
 
    public void addToEnd(String s) {
        List<Character> list = new ArrayList<>();
        for (char c : s.toCharArray()) {
            list.add(c);
        }
        chars.addAll(list);
    }
 
    public void addToStart(String s) {
        List<Character> list = new ArrayList<>();
        for (char c : s.toCharArray()) {
            list.add(c);
        }
        chars.addAll(0, list);
    }

    public void reverse() {
        List<Character> source = new ArrayList<Character>(chars);
        chars.clear();
        source.stream().forEach(c -> chars.add(0, c));
    }
    
    public Character get(int index) {
        return chars.get(index);
    }
 
    @Override
    public String toString() {
        var result = "";
        for (var nextChar : chars) {
            result += nextChar;
        }
        return result;
    }
 
    public int distinctCharsCount() {
        var differentCharacters = new ArrayList<Character>();
        for (var c : chars) {
            if (differentCharacters.indexOf(c) == -1)
                differentCharacters.add(c);
        }
 
        return differentCharacters.size();
    }
//}
// 
//public class Main {
    public static void main(String[] args) {
        var builder = new MyStringBuilder();
        builder.addToEnd("aa");
        System.out.println(builder.distinctCharsCount()); // 1
        builder.addToStart("bb");
        builder.addToStart("ab");
        System.out.println(builder.distinctCharsCount()); // 2
        System.out.println(builder); // abbbaa
        builder.reverse();
        System.out.println(builder); // abbbaa
        
    }
}