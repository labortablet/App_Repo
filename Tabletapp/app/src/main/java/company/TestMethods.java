package company;

import java.util.ArrayList;

/**
 * Created by Grit on 15.12.2014.
 */
public class TestMethods {

    public static String[][] calcarray(String strings){
        String[] string = strings.split(";");
        int x = string[0].split(",").length;
        int y = string.length;
        String[][] arrayString = new String[y][x];
     /*   for (int i = 0; i < y; i++ )
        {
            for (int j=0;j < x;j++) {
                arrayString[i][j]{}

            }

        }*/
return null;
    }
    public static void main(String[] args) {
     String string = "test,test1,test2;test3,test4,test5;";
        String[] strings = string.split(";");
        String[][] stringreturn = new String[strings.length][strings[0].split(",").length];



        for (int i=0; i < strings.length;i++) {
            String[] strings2 = strings[i].split(",");
          for (int j = 0 ;j <strings[0].split(",").length;j++){
              stringreturn[i][j] = strings2[j];
              System.out.println(stringreturn[i][j]);
          }
        }
        for (int i = 0; stringreturn.length < i ;i++)
       System.out.println(stringreturn[i][0]);
        }
}
