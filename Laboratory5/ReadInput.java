package Laboratory5;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadInput {
    public static String Vt = "";

    ReadInput(HashMap<Character, ArrayList<String>> productions) throws IOException {
        File file = new File("D:\\Univer\\LFPC\\src\\Laboratory5\\input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String st;
        //Determine non-terminal symbols and storage them as keys in the hashmap
        st = reader.readLine();
        for (int i = 2; i < st.length(); i++)
            if (st.charAt(i) >= 'A' && st.charAt(i) <= 'Z' && st.charAt(1) == 'n')
                productions.put(st.charAt(i), new ArrayList<String>());

        //Determine terminal symbols and store them in a string
        st = reader.readLine();
        if (st.charAt(1) == 't'){
            for (int i = 2; i < st.length(); i++)
                if (st.charAt(i) >= 'a' && st.charAt(i) <= 'z')
                    Vt += String.valueOf(st.charAt(i));
        }

        String[] arrOfStr;
        while ((st = reader.readLine()) != null)
            if (st.charAt(0) >= 'A' && st.charAt(0) <= 'Z'){
                arrOfStr = st.split("->",2);
                productions.get(st.charAt(0)).add(arrOfStr[1]);
            }
    }

    public static String getVt(){
        return Vt;
    }

}

