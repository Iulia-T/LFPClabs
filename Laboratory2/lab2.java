    package Laboratory2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class lab2 {

    static String rule;
    static int nonterminals, terminals;
    static char[] listOfTerminals = new char[20];
    static int currentState;
    static String key, dictionaryNFA = "{", dictionaryDFA = "{", checkSubstring;
    static String[][] table1;
    static String[] valueOfDFA;
    static boolean check;

    static HashMap<String, String[]> nfaTable = new HashMap<String, String[]>();
    static HashMap<String, String[]> dfaTable = new HashMap<String, String[]>();


    ////////////////////////////////////////////////main///////////////////////////////////////////////////////
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // reading input

        System.out.print("Number of non-terminal points: ");
        nonterminals = scanner.nextInt();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter terminal points: ");
        String t = sc.nextLine();

        terminals = 0;
        for (int i = 0; i < t.length(); i++)
            if (t.charAt(i) >= 'a' && t.charAt(i) <= 'z') {
                listOfTerminals[terminals] = t.charAt(i);
                terminals++;
            }

        System.out.println("Introduce rules line by line (ex: q0,a = q1). Finish by entering STOP.");

        Scanner in = new Scanner(System.in);
        while (true) {
            rule = in.nextLine();
            if (rule.equals("STOP")) break;
            else tableNFA(rule);
        }

        drawNFAtable();
        System.out.println();

        conversion(table1);


    }


    /////////////////////////////////////////Building NFA table//////////////////////////////////////////////
    static void tableNFA(String rule) {
        key = rule.substring(0, 2);
        String[] value = new String[terminals];
        String assignedNonterminal = null;

        for (int i = 2; i < rule.length(); i++) {
            if (rule.charAt(i) == 'q') assignedNonterminal = rule.substring(i, i + 2);
        }

        for (int i = 0; i < rule.length(); i++) {
            if (rule.charAt(i) >= 'a' && rule.charAt(i) <= 'z' && rule.charAt(i + 1) == ' ') {
                for (int j = 0; j < terminals; j++)
                    if (rule.charAt(i) == listOfTerminals[j]) {
                        currentState = j;
                        break;
                    }//determinates the path

                //if such key doesn't exist, it creates a new one
                if (!nfaTable.containsKey(key)) {
                    value[currentState] = assignedNonterminal;
                    nfaTable.put(key, value);
                } else {
                    for (int k = 0; k < terminals; k++)
                        if (k != currentState) value[k] = nfaTable.get(key)[k];

                    String currentValue = nfaTable.get(key)[currentState];

                    if (currentValue == null) currentValue = assignedNonterminal;
                    else {
                        //finding if nonterminal values in string don't repeat
                        boolean b = true;
                        for (int j = 1; j < currentValue.length(); j += 2)
                            if (currentValue.charAt(j) == assignedNonterminal.charAt(1)) {
                                b = false;
                                break;
                            }
                        if (b) currentValue += assignedNonterminal;
                    }
                    value[currentState] = currentValue;
                    nfaTable.put(key, value);

                }
                //System.out.println(key + " "+ Arrays.toString(value));
            }
        }

    }


    //////////////////////////////////Draw NFA Table//////////////////////////////////////////////////
    static void drawNFAtable() {
        //create NFA Table
        table1 = new String[nonterminals + 1][terminals + 1];
        table1[0][0] = " ";

        for (int i = 1; i < terminals + 1; i++)
            table1[0][i] = String.valueOf(listOfTerminals[i - 1]);

        for (int i = 1; i < nonterminals + 1; i++) {
            table1[i][0] = "q".concat(String.valueOf((char) ((i - 1) + 48)));
            if (i > 1) dictionaryNFA += ",";
            dictionaryNFA += "'" + table1[i][0] + "':{";

            for (int j = 1; j < terminals + 1; j++){
                table1[i][j] = nfaTable.get(table1[i][0])[j - 1];
                if (j > 1) dictionaryNFA += ",";
                dictionaryNFA += "'" + table1[0][j] + "':'" + table1[i][j] + "'" ;
            }
            dictionaryNFA +="}";
        }
        dictionaryNFA +="}";
        System.out.println(dictionaryNFA);

        for (int i = 0; i < nonterminals + 1; i++) {
            for (int j = 0; j < terminals + 1; j++)
                if (i == 0) System.out.print(table1[i][j] + "     ");
                else if (i == 2) System.out.print(table1[i][j] + "    ");
                else System.out.print(table1[i][j] + "   ");
            System.out.println();
        }
    }


    //////////////////////////////////Convert from NFA to DFA/////////////////////////////////////////////
    static void conversion(String[][] table1) {

        valueOfDFA = new String[terminals];
        for (int i = 0; i < terminals; i++)
            valueOfDFA[i] = String.valueOf(listOfTerminals[i]);
        dfaTable.put(" ", valueOfDFA); //the first line represents the terminal points

        for (int i = 0; i < terminals; i++) {
            valueOfDFA[i] = table1[1][i + 1];
            dfaTable.put("q0", valueOfDFA); //this line is just copied from NFA
        }
        dictionaryDFA += "'q0'";
        for (int i = 0; i < terminals; i++){
            if (i == 0) dictionaryDFA += ":{";
            dictionaryDFA += "'" + listOfTerminals[i] + "':'" + valueOfDFA[i] + "'";
            if (i < terminals-1) dictionaryDFA += ",";
        }
        dictionaryDFA +="}";
        System.out.println("q0" + " " + Arrays.toString(valueOfDFA));

        for(int i = 0; i < terminals; i++)
            if (!dfaTable.containsKey(valueOfDFA[i]))
                constructorDFA (valueOfDFA[i]);
        while (!check) constructorDFA(checkSubstring);
        dictionaryDFA += "}";
        System.out.println(dictionaryDFA);
    }


    //////////////////////////////////Construct DFA table///////////////////////////////////////////////////////
    static void constructorDFA(String key) {

        String str;
        String[] value = new String[terminals];


        for (int i = 0; i < key.length() / 2; i++) {
            str = key.substring(i * 2, i * 2 + 2);
            int rowInTable1 = str.charAt(1) - '0';
            for (int j = 0; j < terminals; j++) {
                if (value[j] == null) {
                    value[j] = table1[rowInTable1 + 1][j + 1];
                } else if (table1[rowInTable1 + 1][j + 1] != null) {
                    //see if elements don't repeat in a string
                    boolean b = true;
                    for (int k = 1; k < value[j].length(); k += 2) {
                        if (table1[rowInTable1 + 1][j + 1] != null) {
                            for (int l = 1; l < table1[rowInTable1 + 1][j + 1].length(); l += 2)
                                if (value[j].charAt(k) == table1[rowInTable1 + 1][j + 1].charAt(l)) {
                                    b = false;
                                    break;
                                }
                        }
                        if (b) value[j] += table1[rowInTable1 + 1][j + 1];
                        value[j] = sortingString(value[j]);
                    }
                }
            }

        }

        dfaTable.put(key, value);
        System.out.println(key + " " + Arrays.toString(value));
        dictionaryDFA += ",'" + key + "'";
        for (int x = 0; x < terminals; x++) {
            if (x == 0) dictionaryDFA += ":{";
            dictionaryDFA += "'" + listOfTerminals[x] + "':'" + value[x] + "'";
            if (x < terminals - 1) dictionaryDFA += ",";
        }
        dictionaryDFA += "}";

        for (int i = 0; i < dictionaryDFA.length(); i++) {
            if (dictionaryDFA.charAt(i) == 39 && dictionaryDFA.charAt(i + 1) == 'q') {
                checkSubstring = "";
                while (dictionaryDFA.charAt(i + 1) != 39) {
                    checkSubstring += String.valueOf(dictionaryDFA.charAt(i + 1));
                    i++;
                }
                if (!dfaTable.containsKey(checkSubstring)) {
                    check = false;
                    constructorDFA(checkSubstring);
                    break;
                } else check = true;
            }
        }
    }

    static String sortingString(String str){
        for (int i = 1; i < str.length()-1; i += 2)
            for (int j = 1; j < str.length()-1; j += 2)
                if (str.charAt(j) > str.charAt(j+2)){
                    String str1 = str.substring(j-1, j+1);
                    String str2 = str.substring(j+2);
                    str = str1 + str2;
                }
        return str;
    }
}
