package Laboratory1;

import java.util.Scanner;

public class lab1 {

    public static void main(String[] args) {

        String vertexes = "";
        char[][] paths = new char[10][3];
        int x = 0;

        System.out.println("Introduce derivation rules line by line. Finish by entering STOP.");

        // reading input
        Scanner scanner = new Scanner(System.in);
        while (true){

            String rule = scanner.nextLine();
            if (rule.equals("STOP")) break;

            for (int i = 0; i < rule.length(); i++){

                //find vertexes
                if (rule.charAt(i) >= 65 && rule.charAt(i) <= 90){
                    boolean vertex = false;   //assume the next vertex won't repeat in the list of vertexes
                    for (int j = 0; j < vertexes.length(); j++){
                        if (vertexes.charAt(j) == rule.charAt(i)) vertex = true;
                    }
                    if (!vertex) vertexes += String.valueOf(rule.charAt(i)); //if the vertex didn't repeat in the list, it is added
                }

                //find paths
                paths[x][0] = rule.charAt(0);
                if (rule.charAt(i) >= 65 && rule.charAt(i) <= 90 && i!=0) paths[x][2] = rule.charAt(i);
                else if (rule.charAt(i) >= 97 && rule.charAt(i) <= 122 && i!=0) paths[x][1] = rule.charAt(i);

            }

            x++;

        }

        //count terminal points
        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (paths[i][0] >= 65 && paths[i][0] <= 90)
                if (paths[i][2]==0) {count++; paths[i][2] = (char)(count+32);}
        }

        //build array of vertixes
        char[] v = new char[vertexes.length()+count];
        for (int i = 0; i < vertexes.length(); i++)
            v[i] = vertexes.charAt(i);
        for (int i = vertexes.length(); i < vertexes.length()+count; i++)
            v[i] =(char)(i - vertexes.length() + 33);

        //build adjacency matrix
        char[][] matrix = new char[vertexes.length()+count][vertexes.length()+count];
        int position1 = 0, position2 = 0;
        for (int i = 0; i < 10; i++){
            if (paths[i][0]!=0)
                for (int j = 0; j < vertexes.length()+count; j++){
                    if (paths[i][0] == v[j]) position1 = j;
                    if (paths[i][2] == v[j]) position2 = j;
                }
            else break;
            matrix[position1][position2] = paths[i][1];
        }

        //read the input string
        System.out.println("Introduce the string to be checked: ");

        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        //check if string belongs to the grammar
        boolean check = false;
        char c;
        int posC = 0;

        for (int i = 0; i < s.length(); i++){
            c = s.charAt(i);
            check = false;
            if (i == 0) {
                for (int j = 0; j < matrix.length; j++)
                    if (c == matrix[i][j]) {
                        check = true;
                        posC = j;
                    }
            }
            else for (int j = 0; j < matrix.length; j++)
                        if (c == matrix[posC][j]) {
                            check = true;
                            posC = j;
                        }
            if (!check) break;
        }

        if (check) System.out.println("The string belongs to the grammar");
        else System.out.println("The string doesn't belong to the grammar");

    }

}
