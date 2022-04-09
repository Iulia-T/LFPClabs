package Laboratory4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChomskyTransformation {

    HashMap<Character, ArrayList<String>> chomsky = new HashMap<Character, ArrayList<String>>();
    public String toRemove = "", terminals = "";
    boolean existsTerminal;
    public int countNonterminals = 0;

    public ChomskyTransformation (HashMap<Character, ArrayList<String>> productions){
        //initiating a map for Chomsky Normal Form
        productions.forEach(
                (key, value) -> {
                    chomsky.put(key, new ArrayList<String>());
                }
        );

        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if ((value.get(i).length() == 1 && value.get(i).charAt(0) >= 'a' && value.get(i).charAt(0) <= 'z') ||
                                (value.get(i).length() == 2 && value.get(i).charAt(0) >= 'A' && value.get(i).charAt(0) <= 'Z' && value.get(i).charAt(1) >= 'A' && value.get(i).charAt(1) <= 'Z')){
                            chomsky.get(key).add(value.get(i));
                            productions.get(key).remove(value.get(i));
                            toRemove += Character.toString(key);
                        }
                    }
                }
        );

        removeEmpty(productions);

        findTerminals(productions);

        buildChomsky(productions);

        System.out.println(chomsky);

    }

    public void removeEmpty(HashMap<Character, ArrayList<String>> productions){
        for (int i = 0; i < toRemove.length(); i++)
            if (productions.get(toRemove.charAt(i)).isEmpty()) productions.remove(toRemove.charAt(i));
    }

    public void findTerminals(HashMap<Character, ArrayList<String>> productions){
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                       for (int j = 0; j < value.get(i).length(); j++){
                           existsTerminal = false;
                           if (value.get(i).charAt(j) >= 'a' && value.get(i).charAt(j) <= 'z'){
                               if (!Objects.equals(terminals, "")){
                                   for (int x = 0; x < terminals.length(); x++)
                                       if (value.get(i).charAt(j) == terminals.charAt(x)){
                                           existsTerminal = true;
                                           break;
                                       }
                               }
                               else {terminals = Character.toString(value.get(i).charAt(j)); existsTerminal = true;}
                               if (!existsTerminal) terminals += Character.toString(value.get(i).charAt(j));
                           }
                       }
                    }
                }
        );
    }

    public void buildChomsky(HashMap<Character, ArrayList<String>> productions){
        toRemove = "";
        //Terminals into Chomsky Normal Form
        for (int i = 0; i < terminals.length(); i++){
            char key = (char) ('Z'-countNonterminals);
            countNonterminals++;
            chomsky.put(key, new ArrayList<String>());
            chomsky.get(key).add(Character.toString(terminals.charAt(i)));
        }

        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i).length() == 2){
                            twoCharsProd(productions, key, value, i);
                        }
                    }

                }
        );

        //more than 2 characters
        productions.forEach(
                (key, value) -> {
                    char k;
                    for (int i = 0; i < value.size(); i++){
                        String str;
                        if (value.get(i).length() > 2){
                            str = value.get(i).substring(0,value.get(i).length()-1);
                            k = (char) ('Z' - countNonterminals);
                            countNonterminals++;
                            chomsky.put(k, new ArrayList<String>());
                            chomsky.get(k).add(str);
                            str = Character.toString(k) + Character.toString(value.get(i).charAt(value.get(i).length()-1));
                            int position = chomsky.get(key).indexOf(value.get(i));
                            if (chomsky.get(key).contains(value)) chomsky.get(key).set(position, str);
                            else chomsky.get(key).add(str);

                            //if (value.get(i).charAt(value.get(i).length()-1) >= 'a' && value.get(i).charAt(value.get(i).length()-1) <= 'z')

                        }
                    }
                }
        );
    }

    public void twoCharsProd(HashMap<Character, ArrayList<String>> productions, char key, ArrayList<String> value, int i){
        String str = "";
        if (value.get(i).charAt(0) >= 'a' && value.get(i).charAt(0) <= 'z' && value.get(i).charAt(1) >= 'A' && value.get(i).charAt(1) <= 'Z'){
            for ( char k : chomsky.keySet() )
                for (int j = 0; j < value.size(); j++)
                    if (chomsky.get(k).contains(Character.toString(value.get(i).charAt(0)))) str = Character.toString(k);
            str += Character.toString(value.get(i).charAt(1));
            chomsky.get(key).add(str);
        }
        else if (value.get(i).charAt(0) >= 'a' && value.get(i).charAt(0) <= 'z' && value.get(i).charAt(1) >= 'A' && value.get(i).charAt(1) <= 'Z'){
            str = Character.toString(value.get(i).charAt(0));
            for ( char k : chomsky.keySet() )
                for (int j = 0; j < value.size(); j++)
                    if (chomsky.get(k).contains(Character.toString(value.get(i).charAt(1)))) str += Character.toString(k);
            chomsky.get(key).add(str);
        }


    }

    public void twoCharsChom(HashMap<Character, ArrayList<String>> productions, char key, ArrayList<String> value, int i){
        int position = 0;
        String str = "";
        if (value.get(i).charAt(0) >= 'a' && value.get(i).charAt(0) <= 'z' && value.get(i).charAt(1) >= 'A' && value.get(i).charAt(1) <= 'Z'){
            for ( char k : chomsky.keySet() )
                for (int j = 0; j < value.size(); j++)
                if (chomsky.get(k).contains(Character.toString(value.get(i).charAt(0)))){ str = Character.toString(k); position = j;}
            str += Character.toString(value.get(i).charAt(1));
            chomsky.get(key).set(position,str);
        }
        else if (value.get(i).charAt(0) >= 'a' && value.get(i).charAt(0) <= 'z' && value.get(i).charAt(1) >= 'A' && value.get(i).charAt(1) <= 'Z'){
            str = Character.toString(value.get(i).charAt(0));
            for ( char k : chomsky.keySet() )
                for (int j = 0; j < value.size(); j++)
                    if (chomsky.get(k).contains(Character.toString(value.get(i).charAt(1)))){ str += Character.toString(k); position = j;}
            chomsky.get(key).set(position,str);
        }


    }


}
