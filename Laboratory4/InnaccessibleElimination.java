package Laboratory4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InnaccessibleElimination {

    public String toRemove = "";

    public InnaccessibleElimination (HashMap<Character, ArrayList<String>> productions){
        productions.forEach(
                (key, value) -> {isAccessible(productions, key);}
        );

        for (int i = 0; i < toRemove.length(); i++)
            productions.remove(toRemove.charAt(i));

        System.out.println("\n" + "After innaccessible productions elimination: " + "\n" + productions);

        new NonproductiveElimination(productions);
    }

    public void isAccessible (HashMap<Character, ArrayList<String>> productions, char character){
        AtomicBoolean accessible = new AtomicBoolean(false);
        accessible.set(false);
        productions.forEach(
                (key, value) -> {
                    if (key != character)
                        for (int i = 0; i < value.size(); i++){
                            for (int j = 0; j < value.get(i).length(); j++)
                                if (value.get(i).charAt(j) == character) {
                                    accessible.set(true);
                                    break;
                                }
                            if (accessible.get()) break;
                        }
                }
        );
        if (!accessible.get()) toRemove += character;
    }

}
