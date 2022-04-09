package Laboratory4;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitProdElimination {

    public UnitProdElimination(HashMap<Character, ArrayList<String>> productions){

        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++)
                        if (value.get(i).length() == 1 && value.get(i).charAt(0) >= 'A' && value.get(i).charAt(0) <= 'Z')
                            value.remove(value.get(i));
                }
        );

        System.out.println("\n" + "After unit productions elimination: " + "\n" + productions);

        new InnaccessibleElimination(productions);
    }
}
