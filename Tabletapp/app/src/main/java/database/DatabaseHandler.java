package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Grit on 21.10.2014.
 */
public class DatabaseHandler
{

}




/**

 - * Created by Grit on 19.06.2014.

 - */

        /*

-public class DatabaseHandler extends SQLiteOpenHelper{

-    // All Static variables

-

-

-    private static String Path = new String();

-    private final Context myContext;

-    // Database Name

-    private static final String DB_NAME = "Lablet.db";

-    // table names

-    private static final String DB_Entry = "_Entry";

-    private static final String DB_Project = "_Project";

-    private static final String DB_User = "_User";

-    private static final String DB_TableWizard = "_TableWizard";

-    private static final String DB_Rows = "_Rows";

-

-    // Database Version

-    private static final int DATABASE_VERSION = 1;

-    private SQLiteDatabase db;

-    Cursor cursor;

-

-    // Create SQL Tables

-

-    private static final String Create_Table_Entry =

-            "CREATE TABLE IF NOT EXISTS `_entry` (\n" +

-                    "  `Entry_ID` int(11) NOT NULL AUTO_INCREMENT,\n" +

-                    "  `Entry_Typ` int(11) NOT NULL,\n" +

-                    "  `Entry_Titel` varchar(100) NOT NULL,\n" +

-                    "  `Entry_Content` MEDIUMBLOB NOT NULL,\n" +

-                    "  `Entry_Date` TIMESTAMP NOT NULL,\n" +

-                    "  `Entry_Sync` tinyint(1) NOT NULL,\n" +

-                    "  `Entry_ProjectID` int(11) NOT NULL,\n" +

-                    "  `Entry_UserID` int(11) NOT NULL,\n" +

-                    "   PRIMARY KEY (`Entry_ID`),\n" +

-                    "   FOREIGN KEY (Entry_UserID) \n" +

-                    "   REFERENCES _user(User_ID),\n" +

-                    "   FOREIGN KEY (Entry_ProjectID) \n" +

-                    "   REFERENCES _project(Project_ID)\n" +

-                    " ) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

-

-    private static final String Create_Table_Project =

-            "CREATE TABLE IF NOT EXISTS `_project` (\n" +

-                    "  `Project_ID` int(11) NOT NULL AUTO_INCREMENT,\n" +

-                    "  `Project_Name` varchar(100) NOT NULL,\n" +

-                    "  `Project_Description` text NOT NULL,\n" +

-                    "  `Project_Sync` tinyint(1) NOT NULL,\n" +

-                    "  `Project_UserID` int(11) NOT NULL,\n" +

-                    "   PRIMARY KEY (`Project_ID`),\n" +

-                    "   FOREIGN KEY (Project_UserID) \n" +

-                    "   REFERENCES _user(User_ID)\n" +

-                    " ) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

-

-    private static final String Create_Table_User =

-            "CREATE TABLE IF NOT EXISTS `_user` (\n" +

-                    "   `User_ID` int(11) NOT NULL AUTO_INCREMENT,\n" +

-                    "   `User_EMail` varchar(100) NOT NULL,\n" +

-                    "   `User_Password` varchar(100) NOT NULL,\n" +

-                    "    PRIMARY KEY (`User_ID`)\n" +

-                    "  ) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

-

-    private static final String Create_Table_TableWizard =

-            "CREATE TABLE IF NOT EXISTS `_table` (\n" +

-                    "  `Table_ID` int(11) NOT NULL AUTO_INCREMENT,\n" +

-                    "  `Table_Name` varchar(100) NOT NULL,\n" +

-                    "  `Table_Sync` tinyint(1) NOT NULL,\n" +

-                    "  `Table_UserID` int(11) NOT NULL,\n" +

-                    "   PRIMARY KEY (`Table_ID`),\n" +

-                    "   FOREIGN KEY (Table_UserID) \n" +

-                    "   REFERENCES _user(User_ID)  \n" +

-                    " ) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

-

-    private static final String Create_Table_Rows =

-            "CREATE TABLE IF NOT EXISTS `_rows` (\n" +

-                    "  `Row_ID` int(11) NOT NULL AUTO_INCREMENT,\n" +

-                    "  `Row_Content` MEDIUMBLOB NOT NULL,\n" +

-                    "  `Row_Sync` int(11) NOT NULL,\n" +

-                    "  `Row_UserID` int(11) NOT NULL,\n" +

-                    "  `Row_TableID` int(11) NOT NULL,\n" +

-                    "   PRIMARY KEY (`Row_ID`),\n" +

-                    "   FOREIGN KEY (Row_UserID) \n" +

-                    "   REFERENCES _user(User_ID),  \n" +

-                    "   FOREIGN KEY (Row_TableID)\n" +

-                    "   REFERENCES _table(Table_ID)  \n" +

-                    " ) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

-

-    public DatabaseHandler(Context ctx) {

-        super(ctx, DB_NAME, null, DATABASE_VERSION);

-        Path = Environment.getDataDirectory().getAbsolutePath() + "/" +"data/"+ ctx.getResources().getString(R.string.app_name);

-        this.myContext = ctx;

-

-

-    }

-    // super(ctx, Includes.get_Db_Name(), null, Includes.get_Database_Version());

-    //   Path = Environment.getDataDirectory().getAbsolutePath() + "/" + "data/" + ctx.getResources().getString(R.string.app_name);

-

-

-    public void close() {

-        db.close();

-    }

-

-    @Override

-    public void onCreate(SQLiteDatabase sqLiteDatabase) {

-        try {

-            db = openOrCreateDatabase(DB_NAME, null);

-            db.execSQL(Create_Table_User);

-            //   sqLiteDatabase.execSQL(Create_Table_Project);

-            //  sqLiteDatabase.execSQL(Create_Table_Entry);

-            //  sqLiteDatabase.execSQL(Create_Table_TableWizard);

-            //  sqLiteDatabase.execSQL(Create_Table_Rows);

-        }

-        catch(Exception e){

-            Log.d("Fail","this");

-

-        }

-

-

-    }

-

-    @Override

-    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

-

-    }

-

-

-   public String get_UserID(String email){

-String test = "test";

-        try{

-        db = getWritableDatabase();

-               String User_ID_query = "SELECT User_ID FROM _user WHERE User_E_Mail LIKE " + email + ";";

-        db.execSQL("INSERT INTO `_user`(`User_EMail`, `User_Password`) VALUES ("+test+","+test+") ;");

-        //cursor = db.query(DB_User,new String[]{"User_ID"},"User_EMAIL LIKE " + email,null,null,null,null);

-    //    cursor.moveToFirst();

-     //       return cursor.getString(0);

-            return null;

-        }

-        catch (NullPointerException e)

-

-        {

-            return "Exception";

-        }

-

-    }

-

-    public Array[][] get_Projects(String email){

-

-

-//Cursor mcursor = db.query(true,DB_User,)

-        return null;

-    }

-

-private void delete_DB(SQLiteDatabase sqLiteDatabase)

-{

-    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_NAME);

-}

-

-/*

-    @Override

-    public void onCreate(SQLiteDatabase db) {

-

-        try{

-            db = SQLiteDatabase.openDatabase(Path, null,0);

-            Log.d("opendb", "EXIST");

-            db.close();

-        }

-        catch(SQLiteException e){

-            Log.d("opendb","NOT EXIST");

-

-            db = openOrCreateDatabase(Path, null, null);

-            db.execSQL("CREATE TABLE IF NOT EXISTS LIST(wlist varchar);");

-

-            db.execSQL("INSERT INTO LIST VALUES('খবর');");

-            db.execSQL("INSERT INTO LIST VALUES('কবর');"); //whatever you want

-            db.close();

-        }

-    }*/

        /*

-private boolean db_exist(){

-    SQLiteDatabase checkDB = null;

-    try {

-        checkDB = SQLiteDatabase.openDatabase(Path, null, // true if exists false if not

-                SQLiteDatabase.OPEN_READONLY);

-        checkDB.close();

-    } catch (SQLiteException e) {

-        // database doesn't exist yet.

-    }

-    return checkDB != null ? true : false;

-}*/



        /*

-    @Override

-    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

-

-    }

-}

-

-*/

