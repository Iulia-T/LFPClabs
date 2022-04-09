package Laboratory4;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static String Vt = "";
    public static HashMap<Character, ArrayList <String>> productions = new HashMap<Character, ArrayList <String>>();

    public static void main(String[] args) throws Exception {

        File file = new File("D:\\Univer\\LFPC\\src\\Laboratory4\\input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String st;
        //Determine non-terminal symbols and storage them as keys in the hashmap
        st = reader.readLine();
        for (int i = 2; i < st.length(); i++)
            if (st.charAt(i) >= 'A' && st.charAt(i) <='Z')
                productions.put(st.charAt(i), new ArrayList<String>());

        String[] arrOfStr;
        while ((st = reader.readLine()) != null)
            if (st.charAt(0) >= 'A' && st.charAt(0) <= 'Z'){
                arrOfStr = st.split("->",2);
                productions.get(st.charAt(0)).add(arrOfStr[1]);
            }
        System.out.println(productions);

        new EpsilonElimination(productions);
    }

}
