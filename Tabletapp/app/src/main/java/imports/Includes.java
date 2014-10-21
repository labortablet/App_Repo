package imports;

import android.os.Environment;

/**
 * Created by Grit on 23.06.2014.
 */
public final class Includes {
    private  static String APP_NAME ="Tabletapp";
    private  static String PATH =  Environment.getDataDirectory().getAbsolutePath();
    private  static String DB_NAME = "Lablet_DB" ;
    private static int DATABASE_VERSION = 1;
    public static String get_AppName(){

        return APP_NAME;

    }
    public static String get_Path(){

        return PATH;

    }
    public static int get_Database_Version(){

        return DATABASE_VERSION;

    }
    public static String get_Db_Name(){

        return DB_NAME;

    }
}
