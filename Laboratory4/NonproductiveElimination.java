package Laboratory4;

import java.util.ArrayList;
import java.util.HashMap;

public class NonproductiveElimination {

    String productiveTerms1 = "";
    String productiveTerms2;

    public NonproductiveElimination(HashMap<Character, ArrayList<String>> productions){

        firstIteration(productions);
        productiveTerms2 = productiveTerms1;
        secondIteration(productions);


        System.out.println("\n" + productiveTerms2);

    }

    public void firstIteration (HashMap<Character, ArrayList<String>> productions){
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        boolean isProductive = true;
                        for (int j = 0; j < value.get(i).length(); j++)
                            if (value.get(i).charAt(j) >='A' && value.get(i).charAt(j) <='Z'){
                                isProductive = false;
                                break;
                            }
                        if (isProductive) {
                            productiveTerms1 += Character.toString(key);
                            break;
                        }
                    }
                }
        );
    }

    public void secondIteration (HashMap<Character, ArrayList<String>> productions){
        productions.forEach(
                (key, value) -> {
                    boolean wasInFirstIteration = false;
                    for (int z = 0; z < productiveTerms1.length(); z++)
                        if (key == productiveTerms1.charAt(z)) {
                            wasInFirstIteration = true;
                            break;
                        }
                   if (!wasInFirstIteration)
                    for (int i = 0; i < value.size(); i++){
                        boolean isProductive = true;
                        for (int j = 0; j < value.get(i).length(); j++){
                            if (value.get(i).charAt(j) >='A' && value.get(i).charAt(j) <='Z') {
                                for (int x = 0; x < productiveTerms1.length(); x++)
                                    if (value.get(i).charAt(j) == productiveTerms1.charAt(x)) {
                                        isProductive = true;
                                        break;
                                    }
                            }
                            if (!isProductive) break;
                        }
                        if (isProductive) {
                            productiveTerms2 += Character.toString(key);
                            break;
                        }
                    }
                }
        );
    }

}
