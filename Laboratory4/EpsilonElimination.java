package Laboratory4;

import java.util.ArrayList;
import java.util.HashMap;

public class EpsilonElimination{

    public HashMap<Character,ArrayList<String>> productions;
    public char eliminate;
    public boolean hasEpsilon = false;

    public EpsilonElimination (HashMap<Character,ArrayList<String>> productions){
        this.productions = productions;

        checkEpsilon();
        while (hasEpsilon) nextEliminations();

        System.out.println("\n" + "After epsilon elimination:" + "\n" + productions);

        new UnitProdElimination(productions);
    }

    public void checkEpsilon(){
        productions.forEach(
                (key, value) -> {
                    if (value.contains("eps")){
                       value.remove("eps");
                       hasEpsilon = true;
                       eliminate = key;
                    }
                }
        );
    }

    public void nextEliminations(){
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++) {
                        checkEachString(i, value);
                    }
                }
        );
        hasEpsilon = false;
        checkEpsilon();
    }

    public void checkEachString(int i, ArrayList<String> value){
        String newString;
        for (int j = 0; j < value.get(i).length(); j++)
            if (value.get(i).charAt(j) == eliminate){
                newString = removeChar(value.get(i), j);
                if (!value.contains(newString)) value.add(newString);
            }
    }

    public String removeChar (String strng, int j){
        String a;
        if (strng.length() == 1) a = "eps";
        else if (j < strng.length()-1) a = strng.substring(0, j) + strng.substring(j+1);
             else a = strng.substring(0, j);
        return a;
    }

}
