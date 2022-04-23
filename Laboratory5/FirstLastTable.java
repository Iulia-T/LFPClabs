package Laboratory5;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstLastTable {

    public static HashMap<Character, ArrayList<Character>> firstTable = new HashMap<Character, ArrayList <Character>>();
    public static HashMap<Character, ArrayList<Character>> lastTable = new HashMap<Character, ArrayList <Character>>();


    FirstLastTable(HashMap<Character, ArrayList<String>> productions){

        productions.forEach(
                (key, value) -> {
                    firstTable.put(key, new ArrayList<Character>());
                    lastTable.put(key, new ArrayList<Character>());

                    buildingFirstTable(key, value);
                    buildingLastTable(key, value);
                }
        );
        checkFirstTable(productions);
        checkLastTable(productions);

        System.out.println();
        new SimplePrecedenceMatrix(productions, firstTable, lastTable);

    }

    public void buildingFirstTable(char key, ArrayList<String> value){
        for (int i = 0; i < value.size(); i++){
            if (!firstTable.get(key).contains(value.get(i).charAt(0)))
                firstTable.get(key).add(value.get(i).charAt(0));
        }
    }

    public void checkFirstTable (HashMap<Character, ArrayList<String>> productions){
        firstTable.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i) >= 'A' && value.get(i) <= 'Z' && value.get(i) != key)
                            buildingFirstTable(key, productions.get(value.get(i)));
                    }
                }
        );
        System.out.println("Table of first elements: " + firstTable);
    }

    public void buildingLastTable(char key, ArrayList<String> value){
        for (int i = 0; i < value.size(); i++){
            if (!lastTable.get(key).contains(value.get(i).charAt(value.get(i).length()-1)))
                lastTable.get(key).add(value.get(i).charAt(value.get(i).length()-1));
        }
    }

    public void checkLastTable (HashMap<Character, ArrayList<String>> productions){
        lastTable.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i) >= 'A' && value.get(i) <= 'Z' && value.get(i) != key)
                            buildingLastTable(key, productions.get(value.get(i)));
                    }
                }
        );
        System.out.println("Table of last elements: " + lastTable);
    }
}
