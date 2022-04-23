package Laboratory5;

import java.util.ArrayList;
import java.util.HashMap;

public class SimplePrecedenceMatrix {

    String Vt = ReadInput.getVt();
    int count = 0;
    int position = 1;

    SimplePrecedenceMatrix(HashMap<Character, ArrayList<String>> productions, HashMap<Character, ArrayList<Character>> firstTable, HashMap<Character, ArrayList<Character>> lastTable) {

        countRows(productions);
        buildMatrix(productions, firstTable, lastTable);

    }

    public void countRows(HashMap<Character, ArrayList<String>> productions){
        //Count non-terminal symbols to put in matrix
        productions.forEach(
                (key, value) -> count ++
        );

        //Count terminal symbols to put in matrix

        count += Vt.length();

        //Add 2 more rows (for $ and input data)
        count += 2;
    }

    public void buildMatrix(HashMap<Character, ArrayList<String>> productions, HashMap<Character, ArrayList<Character>> firstTable, HashMap<Character, ArrayList<Character>> lastTable){
        //Initiate matrix
        char[][] matrix = new char[count][count];
        for (int i = 0; i < count; i++)
            for (int j = 0; j < count; j++)
                matrix[i][j] = ' ';

         // in position [0][0] will be stored nothing
        //Add non-terminal symbols into matrix
        productions.forEach(
                (key, value) -> {
                    matrix[0][position] = key;
                    matrix[position][0] = key;
                    position++;
                }
        );

        //Add terminal symbols into matrix
        for (int i = 0; i < Vt.length(); i++){
            matrix[0][position] = Vt.charAt(i);
            matrix[position][0] = Vt.charAt(i);
            position++;
        }

        //Add $ as last element of the matrix
        matrix[0][position] = '$';
        matrix[position][0] = '$';

        insertRelations(matrix, productions, firstTable, lastTable);
        //print the matrix
        for (int i = 0; i < count; i++){
            for (int j = 0; j < count; j++)
                System.out.print(matrix[i][j] + " ");
            System.out.println();
        }

    }

    public void insertRelations(char[][] matrix, HashMap<Character, ArrayList<String>> productions, HashMap<Character, ArrayList<Character>> firstTable, HashMap<Character, ArrayList<Character>> lastTable){

        //insert relations for $ symbol
        for (int i = 1; i < count-1; i++){
            matrix[i][count-1] = '>';
            matrix[count-1][i] = '<';
        }

        equalityRelations(matrix, productions);
        lessRelations(matrix, productions, firstTable);
        greaterRelations(matrix, productions, firstTable, lastTable);
    }

    public void equalityRelations(char[][] matrix, HashMap<Character, ArrayList<String>> productions){
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i).length() > 1){
                            for (int j = 0; j < value.get(i).length()-1; j++)
                                //identify 2 characters, at position j and j+1, which will be equal
                                insertRelation(matrix, '=', value.get(i).charAt(j), value.get(i).charAt(j+1));
                        }
                    }
                }
        );
    }

    public void lessRelations(char[][] matrix, HashMap<Character, ArrayList<String>> productions, HashMap<Character, ArrayList<Character>> firstTable){
        //If a non-terminal symbol has in front a terminal symbol, then t < first(N)
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i).length() > 1){
                            for (int j = 1; j < value.get(i).length(); j++)
                                if (value.get(i).charAt(j) >= 'A' && value.get(i).charAt(j) <= 'Z' && value.get(i).charAt(j-1) >= 'a' && value.get(i).charAt(j-1) <= 'z'){
                                    char newKey = value.get(i).charAt(j);
                                    for ( int x = 0; x < firstTable.get(newKey).size(); x ++){
                                        insertRelation(matrix, '<', value.get(i).charAt(j-1), firstTable.get(newKey).get(x));
                                    }
                                }
                        }
                    }
                }
        );
    }

    public void greaterRelations(char[][] matrix, HashMap<Character, ArrayList<String>> productions, HashMap<Character, ArrayList<Character>> firstTable, HashMap<Character, ArrayList<Character>> lastTable){
        //First Case - If we have a non-terminal succeeded by a terminal, last(N) > t
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i).length() > 1){
                            for (int j = 0; j < value.get(i).length()-1; j++)
                                if (value.get(i).charAt(j) >= 'A' && value.get(i).charAt(j) <= 'Z' && value.get(i).charAt(j+1) >= 'a' && value.get(i).charAt(j+1) <= 'z'){
                                    char newKey = value.get(i).charAt(j);
                                    for ( int x = 0; x < lastTable.get(newKey).size(); x ++){
                                        insertRelation(matrix, '>', lastTable.get(newKey).get(x), value.get(i).charAt(j+1));
                                    }
                                }
                        }
                    }
                }
        );

        //Second Case - If we have a non-terminal, succeeded by a non-terminal, then last(N1) > first(N2) (if first(N2) is terminal)
        productions.forEach(
                (key, value) -> {
                    for (int i = 0; i < value.size(); i++){
                        if (value.get(i).length() > 1){
                            for (int j = 0; j < value.get(i).length()-1; j++)
                                if (value.get(i).charAt(j) >= 'A' && value.get(i).charAt(j) <= 'Z' && value.get(i).charAt(j+1) >= 'A' && value.get(i).charAt(j+1) <= 'Z'){
                                    char newKey = value.get(i).charAt(j);
                                    for ( int x = 0; x < lastTable.get(newKey).size(); x ++){
                                        for ( int y = 0; y < firstTable.get(value.get(i).charAt(j+1)).size(); y++)
                                            if (firstTable.get(value.get(i).charAt(j+1)).get(y) >= 'a' && firstTable.get(value.get(i).charAt(j+1)).get(y) <= 'z')
                                               insertRelation(matrix, '>', lastTable.get(newKey).get(x), firstTable.get(value.get(i).charAt(j+1)).get(y));
                                    }
                                }
                        }
                    }
                }
        );


    }

    public void insertRelation(char [][] matrix, char relation, char symbol1, char symbol2){
        //Find coordinates of the cell to put the relation
        int row = 0, column = 0;
        for (int i = 1; i < count; i++){
            if (matrix[i][0] == symbol1) row = i;
            if (matrix[0][i] == symbol2) column = i;
        }

        if (matrix[row][column] == ' ') matrix[row][column] = relation;
    }
}