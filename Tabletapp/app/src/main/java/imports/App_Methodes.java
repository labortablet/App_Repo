package imports;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import company.Start;

/**
 * Created by Grit on 19.06.2014.
 */
public class App_Methodes {

    String pattern = "/#/";

    private static int count_column(ArrayList text, String pattern) { // Zähl die anzahl der trennungs Strings

        String temp = (String) text.get(0); // erste zeile aus der Arraylist
        int count = 0;
        Pattern pat = Pattern.compile(Pattern.quote(pattern));
        Matcher m;
        for (m = pat.matcher(temp); m.find(); count++) ;
        return count;
    }

    private static String[][] calc_array(ArrayList text, String pattern) { // Diese funktion Schreibt die Arraylist aus der Tabelle in ein 2D array um
        int i = count_column(text, pattern); // anzahl der Zeilen
        i++; // da ja immer ein eintrag mehr in der Zeile ist, als trennzeichen +1
        String[][] yourArray = new String[i][text.size()];  // array Dynamisch nach länge der arraylist erzuegen

        for (int j = 0; j <= text.size(); j++) {
            String str = (String) text.get(j);  // jeweilige arraylist zeile die bearbeitet wird
            str = str.trim();
            for (int ii = 0; ii <= i; ii++) {
                if (str.contains(pattern)) {
                    yourArray[j][ii] = str.substring(0, str.indexOf(pattern));  // jeden einzelnen eintrag in array speichern
                    str = str.substring(str.indexOf(pattern) + pattern.length(), str.length() - str.indexOf(pattern)); // ersten eintrag im str löschen
                    str = str.trim();
                } else {
                    yourArray[j][ii] = str;
                    break;
                }
            }
        }
        return yourArray;
    }

    public static String[][] get_array(ArrayList text, String pattern) {
        return calc_array(text, pattern);
    }
public static String[][] return2DArray(String string){
        String[] strings = string.split(";");
        String[][] stringreturn = new String[strings.length][strings[0].split(",").length];
        for (int i=0; i < strings.length;i++) {
            String[] strings2 = strings[i].split(",");
            for (int j = 0 ;j <strings[0].split(",").length;j++){
                stringreturn[i][j] = strings2[j];
            }
        }
    return stringreturn;
    }


    public static Long generateTimestamp() {
      return  (System.currentTimeMillis() / 1000L);
    }
    private static int countLetter(String str, String letter) {
        int count = 0;
        for (int pos = -1; (pos = str.indexOf(letter, pos+1)) != -1; count++);
        return count;
    }
    public static String[][] StringTo2DArray(String strings){
        String[] string = strings.split(";");
        int x = countLetter(string[0],",");
        int y = string.length;
        String[][] temp =new String[y][x];
        for (int i = 0; i < y ; i++) {
            for (int j = 0;j < x;j++) {
                int pos = string[y].indexOf(",");
                temp[i][j] = string[y].substring(pos);
                string[y] = string[y].substring(pos+1,strings.length());
            }
        }
        return temp;
    }
    public static String twoDArray2String(String[][] table_array) {
        String finalstr ="";
        for (String[] s : table_array) {
            String temp = "";
            for (String string : s) {
                temp += (string + ",");
            }
            finalstr += temp.substring(0, temp.length() - 1);
            finalstr += ";";
        }
        return finalstr;
    }
    public static String[] StringToArray(String string){
        return string.split(";");
    }
}

/*

    Creation and initialization

    Object[] yourArray = new Object[ARRAY_LENGTH];

    Write access

    yourArray[i]= someArrayList;

    to access elements of internal ArrayList:

    ((ArrayList<YourType>) yourArray[i]).add(elementOfYourType); //or other method

    Read access

    to read array element i as an ArrayList use type casting:

    someElement= (ArrayList<YourType>) yourArray[i];

    for array element i: to read ArrayList element at index j

    arrayListElement= ((ArrayList<YourType>) yourArray[i]).get(j);

*/