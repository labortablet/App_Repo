package imports;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Grit on 19.06.2014.
 */
public class App_Methodes {

public static String[][] return2DArray(String string)throws IndexOutOfBoundsException{
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

public String getFullPathWithFileName(String Filename){
    return Environment.getExternalStorageDirectory()+File.separator + Filename;
}

    public static void appendLog(String text)
    {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Long generateTimestamp() {
      return  (System.currentTimeMillis() / 1000L);
    }
    private static int countLetter(String str, String letter) {
        int count = 0;
        for (int pos = -1; (pos = str.indexOf(letter, pos+1)) != -1; count++);
        return count;
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