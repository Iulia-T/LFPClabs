package Laboratory2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class lab2 {

    static String rule;
    static int nonterminals, terminals;
    static char[] listOfTerminals = new char[20];
    static int currentState;
    static String str;

    static HashMap<String, String[]> nfaTable = new HashMap<String, String[]>();


////////////////////////////////////////////////main///////////////////////////////////////////////////////
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // reading input

        System.out.print("Number of non-terminal points: ");
        nonterminals = scanner.nextInt();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter terminal points: ");
        String t = sc.nextLine();
        //System.out.println(t);

        terminals = 0;
        for (int i = 0; i < t.length(); i++)
            if (t.charAt(i)>='a' && t.charAt(i)<='z'){
                listOfTerminals[terminals] = t.charAt(i);
                terminals++;
            }
        //for (int i = 0; i < terminals; i++)
        //System.out.print(listOfTerminals[i] + " ");

        System.out.println("Introduce rules line by line (ex: q0,a = q1). Finish by entering STOP.");

        Scanner in = new Scanner(System.in);
        while (true){
            rule = in.nextLine();
            if (rule.equals("STOP")) break;
            else tableNFA(rule);




        }

    }


/////////////////////////////////////////Building NFA table//////////////////////////////////////////////
    static void tableNFA (String rule){
        String key = rule.substring(0,2);
        String[] value = new String[terminals];
        String assignedNonterminal = null;

        for (int i = 2; i < rule.length(); i++){
            if (rule.charAt(i) == 'q') assignedNonterminal = rule.substring(i, i+2);
        }

        for (int i = 0; i < rule.length(); i++) {
            if (rule.charAt(i) >= 'a' && rule.charAt(i) <= 'z' && rule.charAt(i + 1) == ' ') {
                for (int j = 0; j < terminals; j++)
                    if (rule.charAt(i) == listOfTerminals[j]) {currentState = j; break;}//determinates the path

                //if such key doesn't exist, it creates a new one
                if (!nfaTable.containsKey(key)) {
                    value[currentState] = assignedNonterminal;
                    nfaTable.put(key, value);
                }
                 else {
                    for (int k = 0; k < terminals; k++)
                       if (k!=currentState) value[k] = nfaTable.get(key)[k];

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
                        //value[currentState] = sortingString(value[currentState]);
                    }
                    value[currentState] = currentValue;
                    nfaTable.put(key, value);

                 }
                String x = Arrays.toString(value);
                System.out.println(key + " "+ x);
            }
        }

    }


        static String sortingString(String str){
            String str1 = null;
            String str2 = null;
            String str3 = null;
            String str4 = null;

            for (int j = 3; j < str.length(); j += 2)
                for (int k = 3; k < str.length(); k += 2)
                    if (str.charAt(k) < str.charAt(k-2)) {
                        if (k > 3) str1 = str.substring(0, k-3);
                        str2 = str.substring (k-1, k+1);
                        str3 = str.substring (k-3, k-1);
                        if (str.length() > k+1) str4 = str.substring (k+1);
                    }
            return str1+str2+str3+str4;
        }

}
