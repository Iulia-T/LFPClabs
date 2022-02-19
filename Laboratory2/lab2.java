    static boolean mapContainsKey;
    static String[][] table1;

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
            if (t.charAt(i)>='a' && t.charAt(i)<='z'){
                listOfTerminals[terminals] = t.charAt(i);
                terminals++;
            }

        System.out.println("Introduce rules line by line (ex: q0,a = q1). Finish by entering STOP.");

        Scanner in = new Scanner(System.in);
        while (true){
            rule = in.nextLine();
            if (rule.equals("STOP")) break;
            else tableNFA(rule);
        }

        drawNFAtable();

        conversion(table1);



    }


/////////////////////////////////////////Building NFA table//////////////////////////////////////////////
    static void tableNFA (String rule){
        key = rule.substring(0,2);
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
                    }
                    value[currentState] = currentValue;
                    nfaTable.put(key, value);

                 }
                //System.out.println(key + " "+ Arrays.toString(value));
            }
        }

    }


//////////////////////////////////Draw NFA Table//////////////////////////////////////////////////
    static void drawNFAtable(){
        //create NFA Table
        table1 = new String[nonterminals+1][terminals+1];
        table1[0][0] = " ";
        for (int i = 1; i < terminals+1; i++)
            table1[0][i] = String.valueOf(listOfTerminals[i-1]);
        for (int i = 1; i < nonterminals+1; i++) {
            table1[i][0] = "q".concat(String.valueOf((char)((i - 1) + 48)));
            for (int j = 1; j < terminals+1; j++)
                table1[i][j] = nfaTable.get(table1[i][0])[j-1];

        }
    }


//////////////////////////////////Convert from NFA to DFA/////////////////////////////////////////////
    static void conversion(String[][] table1) {

        String[] valueOfDFA = new String[terminals];
        for (int i = 0; i < terminals; i++)
            valueOfDFA[i] = String.valueOf(listOfTerminals[i]);
        dfaTable.put(" ", valueOfDFA); //the first line represents the terminal points

        for (int i = 0; i < terminals; i++) {
            valueOfDFA[i] = table1[1][i+1];
            dfaTable.put("q0", valueOfDFA); //this lined is just copied from NFA
        }

        int currentRow = 1;

        mapContainsKey = false;
        while (!mapContainsKey && valueOfDFA[0]!=null){
            constructorDFA(valueOfDFA);
            currentRow++;
        }

    }

    static void constructorDFA(String[] valueOfDFA){

        String[] auxiliarValue = new String[terminals];
        String str;
        String key = null;

        for (int i = 0; i < terminals; i++){
            mapContainsKey = false;
            if (!dfaTable.containsKey(valueOfDFA[i])){
                key = valueOfDFA[i];
                for (int j = 0; j < key.length()/2; j++){
                    str = key.substring(j, j + 2);
                    for (int path = 0; path < terminals; path++){

                        //see if elements don't repeat in a string
                        boolean b = true;
                        for (int k = 1; k < nfaTable.get(str)[path].length(); j += 2)
                            if (auxiliarValue[path].charAt(k) == str.charAt(1)) {
                                b = false;
                                break;
                            }
                        if (b) auxiliarValue[path] += nfaTable.get(str)[path];
                    }
                }
                mapContainsKey = true;
            }
            else mapContainsKey = true;
        }
        dfaTable.put(key,auxiliarValue);

    }
