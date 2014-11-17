package company;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

import imports.AttachmentTable;
import imports.AttachmentText;
import imports.LocalEntry;
import imports.User;
import scon.Entry_id_timestamp;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;

public class DBAdapter {
    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////

    // For logging:
    private static final String TAG = "DBAdapter";
    // Separators
    public static final String Comma_Separator = " , ";
    public static final String Semicolon_Separator = " ; ";
    public static final String Bracket_Separator_Left = " ( ";
    public static final String Bracket_Separator_Right = " ) ";
    // Datatypes
    public static final String Typ_Integer =  " integer ";
    public static final String Typ_String =  " string ";
    public static final String Typ_Text =  " text ";
    public static final String Typ_Number = " number ";
    /*
    Table Fields for Table _entry
     */
    public static final String Entry_ID = " Entry_ID ";
    public static final String Entry_Typ = " Entry_Typ ";
    public static final String Entry_Titel = " Entry_Titel ";
    public static final String Entry_Content = " Entry_Content ";
    public static final String Entry_Sync = " Entry_Sync ";
    public static final String Entry_ExperimentID = " Entry_ExperimentID ";
    public static final String Entry_UserID = " Entry_Email ";
    public static final String Entry_RemoteID = " Entry_RemoteID ";
    public static final String Entry_CreationDate =" Entry_CreationDate ";
    public static final String Entry_SyncDate = " Entry_SyncDate ";
    public static final String Entry_ChangeDate = " Entry_ChangeDate ";
    /*
   Table Fields for Table _experiments
    */
    public static final String Experiment_ID = " Experiment_ID ";
    public static final String Experiment_Name = " Experiment_Name ";
    public static final String Experiment_Description = " Experiment_Description ";
    public static final String Experiment_Sync = " Experiment_Sync ";
    public static final String Experiment_UserID = " Experiment_UserID ";
    public static final String Experiment_ProjectID = " Experiment_ProjectID ";
    public static final String Experiment_RemoteID = " Experiment_RemoteID ";
    /*
   Table Fields for Table _user
    */
    public static final String User_ID = " User_ID ";
    public static final String User_EMail = " User_EMail ";
    public static final String User_Password = " User_Password ";
    public static final String User_FName =" User_FName ";
    public static final String User_LName =" User_LName ";
    /*
   Table Fields for Table _project
    */
    public static final String Project_ID = " Project_ID ";
    public static final String Project_Name = " Project_Name ";
    public static final String Project_Description = " Project_Description ";
    public static final String Project_Sync = " Project_Sync ";
    public static final String Project_UserID = " Project_UserID ";
    public static final String Project_RemoteID = " Project_RemoteID ";
    //The Keys for each table
    public static final String[] User_KEYS = new String[] {User_ID, User_EMail, User_Password,User_FName,User_LName};
    public static final String[] Entry_KEYS = new String[] {Entry_ID,Entry_Titel,Entry_Typ,Entry_Content,Entry_Sync,Entry_CreationDate,Entry_ChangeDate,Entry_SyncDate,Entry_RemoteID,Entry_ExperimentID,Entry_UserID};
    public static final String[] Experiment_KEYS = new String[] {Experiment_ID,Experiment_Name,Experiment_Description,Experiment_Sync,Experiment_RemoteID,Experiment_ProjectID};
    public static final String[] Project_KEYS = new String[] {Project_ID,Project_Name,Project_Description,Project_Sync,Project_RemoteID};
    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "Lablet";
    public static final String Table_Entry = " _entry ";
    public static final String Table_User = " _user ";
    public static final String Table_Project = " _project ";
    public static final String Table_Experiment = " _experiment ";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 2;
    //
    private static final String DATABASE_CREATE_USER =
            "CREATE TABLE IF NOT EXISTS" + Table_User
                    + Bracket_Separator_Left + User_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + User_EMail + Typ_String +"not null" + Comma_Separator
                    + User_Password + Typ_String +"not null" + Comma_Separator
                    + User_FName + Typ_String + Comma_Separator
                    + User_LName + Typ_String
                    + Bracket_Separator_Right + Semicolon_Separator;
    //
    private static final String DATABASE_CREATE_PROJECT =
            "CREATE TABLE IF NOT EXISTS" + Table_Project
                    + Bracket_Separator_Left + Project_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + Project_Name + Typ_String +"not null, "
                    + Project_Description + Typ_Text + Comma_Separator
                    + Project_Sync + Typ_Integer + Comma_Separator
                    + Project_RemoteID + Typ_Integer +" UNIQUE "
                    + Bracket_Separator_Right + Semicolon_Separator;
    //
    private static final String DATABASE_CREATE_EXPERIMENT =
            "CREATE TABLE IF NOT EXISTS" + Table_Experiment
                    + Bracket_Separator_Left + Experiment_ID + Typ_Integer +"primary key autoincrement" + Comma_Separator
                    + Experiment_Name + Typ_String +"not null" + Comma_Separator
                    + Experiment_Description + Typ_Text + Comma_Separator
                    + Experiment_Sync + Typ_Integer + Comma_Separator
                    + Experiment_RemoteID + Typ_Integer + " UNIQUE " + Comma_Separator
                    + Experiment_ProjectID + Typ_Integer
                    + Bracket_Separator_Right + Semicolon_Separator;
    //
    private static final String DATABASE_CREATE_Entry =
            "CREATE TABLE IF NOT EXISTS" + Table_Entry
                    + Bracket_Separator_Left + Entry_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + Entry_Titel +Typ_String + Comma_Separator
                    + Entry_Typ + Typ_Integer +  "not null" + Comma_Separator
                    + Entry_Content + Typ_Text + Comma_Separator
                    + Entry_Sync + Typ_Integer + Comma_Separator
                    + Entry_CreationDate + Typ_Number + Comma_Separator
                    + Entry_ChangeDate + Typ_Number + Comma_Separator
                    + Entry_SyncDate + Typ_Number + Comma_Separator
                    + Entry_RemoteID + Typ_Integer + " UNIQUE " + Comma_Separator
                    + Entry_ExperimentID + Typ_Integer +Comma_Separator
                    + Entry_UserID + Typ_String
                    + Bracket_Separator_Right + Semicolon_Separator;
    // Context of application who uses us.

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;
    // ids for the specific rows
    //user table
    public final static int COL_UserID = 0;
    public final static int COL_UserEmail = 1;
    public final static int COL_UserPass = 2;
    public final static int COL_UserFName = 3;
    public final static int COL_UserLName = 4;
    //entry table
    public final static int COL_EntryID = 0;
    public final static int COL_EntryTitle = 1;
    public final static int COL_EntryTyp = 2;
    public final static int COL_EntryContent = 3;
    public final static int COL_EntrySync = 4;
    public final static int COL_EntryCreationDate = 5;
    public final static int COL_EntryChangeDate = 6;
    public final static int COL_EntrySyncDate = 7;
    public final static int COL_EntryRemoteID = 8;
    public final static int COL_EntryExperimentID = 9;
    public final static int COL_EntryUserID = 10;
    // experiment table
    public final static int COL_ExperimentID = 0;
    public final static int COL_ExperimentName=1;
    public final static int COL_ExperimentDescription = 2;
    public final static int COL_ExperimentSync = 3;
    public final static int COL_ExperimentRemoteID = 4;
    public final static int COL_ExperimentProjectID = 5;

    // project table
    public final static int COL_ProjectID = 0;
    public final static int COL_ProjectName = 1;
    public final static int COL_ProjectDescription = 2;
    public final static int COL_ProjectSync = 3;
    public final static int COL_ProjectRemoteID = 4;
    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        myDBHelper = new DatabaseHelper(ctx);
    }
    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }
    // Add a new set of values to the database.
    public long insertNewUser(User user) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(User_EMail, user.getUser_email());
        initialValues.put(User_Password, user.getPw_hash());
        initialValues.put(User_FName,user.getFirstname());
        initialValues.put(User_LName,user.getLastname());
        return db.insert(Table_User, null, initialValues);
    }
    public long insertRemoteProject(RemoteProject remoteProject){
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(Project_Name,remoteProject.get_name());
        initialValues.put(Project_Description,remoteProject.get_description());
        initialValues.put(Project_Sync,1);
        initialValues.put(Project_RemoteID,remoteProject.get_id());
        return db.insert(Table_Project,null,initialValues);
    }
    public long insertRemoteExperiment(RemoteExperiment remoteExperiment){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Experiment_Name,remoteExperiment.get_name());
        initialValues.put(Experiment_Description,remoteExperiment.get_description());
        initialValues.put(Experiment_Sync,1);
        initialValues.put(Experiment_ProjectID,remoteExperiment.get_project_id());
        initialValues.put(Experiment_RemoteID,remoteExperiment.get_id());
        return db.insert(Table_Experiment,null,initialValues);
    }
    public long insertRemoteEntry(RemoteEntry remoteEntry){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Entry_Titel,remoteEntry.getTitle());
        initialValues.put(Entry_Typ,remoteEntry.getAttachment_type());
        if(remoteEntry.getAttachment_type() == 1)
        {
            AttachmentText text  = (AttachmentText) remoteEntry.getAttachment();
            initialValues.put(Entry_Content,text.getText());
        }
        else
        {
            AttachmentTable table= (AttachmentTable) remoteEntry.getAttachment();
            initialValues.put(Entry_Content,table.getText());
        }
        initialValues.put(Entry_Sync,1);
        initialValues.put(Entry_ExperimentID,remoteEntry.getExperiment_id());
        initialValues.put(Entry_UserID,remoteEntry.getUser().getUser_email());
        initialValues.put(Entry_RemoteID,remoteEntry.getRemote_id());
        initialValues.put(Entry_CreationDate,remoteEntry.getEntry_time());
        initialValues.put(Entry_SyncDate,remoteEntry.getSync_time());
        initialValues.put(Entry_ChangeDate,remoteEntry.getChange_time());
        return db.insert(Table_Entry,null,initialValues);
    }
    public long insertLocalEntry(LocalEntry localEntry){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Entry_Titel,localEntry.getTitle());
        initialValues.put(Entry_Typ,localEntry.getAttachment_type());
        if(localEntry.getAttachment_type() == 1)
        {
            AttachmentText text  = (AttachmentText) localEntry.getAttachment();
            initialValues.put(Entry_Content,text.getText());
        }
        else
        {
            AttachmentTable table= (AttachmentTable) localEntry.getAttachment();
            initialValues.put(Entry_Content,table.getText());
        }
        initialValues.put(Entry_Sync,0);
        initialValues.put(Entry_ExperimentID,localEntry.getExperiment_id());
        initialValues.put(Entry_UserID,localEntry.getUser().getUser_email());
        initialValues.put(Entry_RemoteID,localEntry.getRemote_id());
        initialValues.put(Entry_CreationDate,localEntry.getEntry_time());
        initialValues.put(Entry_SyncDate,localEntry.getSync_time());
        initialValues.put(Entry_ChangeDate,localEntry.getChange_time());
        return db.insert(Table_Entry,null,initialValues);
    }
    // Delete a row from the database, by rowId (primary key)
    public boolean deleteUserByID(long rowId) {
        String where = User_ID + " = " + rowId;
        return db.delete(Table_User, where, null) != 0;
    }
    public boolean deleteProjectByID(long rowId) {
        String where = Project_ID + " = " + rowId;
        return db.delete(Table_Project, where, null) != 0;
    }
    public boolean deleteExperimentByID(long rowId) {
        String where = Experiment_ID + " = " + rowId;
        return db.delete(Table_Experiment, where, null) != 0;
    }
    public boolean deleteEntryByID(long rowId) {
        String where = Entry_ID + " = " + rowId;
        return db.delete(Table_Entry, where, null) != 0;
    }
    //delets every in the specific table
    public void deleteAllUsers() {
        Cursor c = getAllUserRows();
        long rowId = c.getColumnIndexOrThrow(User_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteUserByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    public void deleteAllProjects() {
        Cursor c = getAllProjectRows();
        long rowId = c.getColumnIndexOrThrow(Project_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteProjectByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    public void deleteAllExperiments() {
        Cursor c = getAllExperimentRows();
        long rowId = c.getColumnIndexOrThrow(Experiment_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteExperimentByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    public void deleteAllEntries() {
        Cursor c = getAllEntryRows();
        long rowId = c.getColumnIndexOrThrow(Entry_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteEntryByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    // Return all data in the database.
    public Cursor getAllUserRows() {
        String where = null;
        Cursor c = 	db.query(true, Table_User, User_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getAllProjectRows() {
        String where = null;
        Cursor c = 	db.query(true, Table_Project, Project_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getAllExperimentRows() {
        String where = null;
        Cursor c = 	db.query(true, Table_Experiment, Experiment_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getAllEntryRows() {
        String where = null;
        Cursor c = 	db.query(true, Table_Entry, Entry_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // Get a specific row (by rowId)
    public Cursor getUserRow(long rowString) {
        String where = User_ID + " = " + rowString;
        Cursor c = 	db.query(true, Table_User, User_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getEntryRow(long rowString) {
        String where = Entry_ID + " = " + rowString;
        Cursor c = 	db.query(true, Table_Entry, Entry_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getProjectRow(long rowString) {
        String where = Project_ID + " = " + rowString;
        Cursor c = 	db.query(true, Table_Project, Project_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getExperimentRow(long rowString) {
        String where = User_ID + " = " + rowString;
        Cursor c = 	db.query(true, Table_Experiment, Experiment_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // Methods for getting all unsynced entries
    public Cursor getAllUnsyncedEntries(){
        String where = Entry_Sync + " != 1";
        Cursor c = 	db.query(true, Table_Entry, Experiment_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }
    //method for getting the actual Entries only the Entry_id_timestamp info's
    public void getAllEntryForCompare(ArrayList<Entry_id_timestamp> arrayList)   {

        for (Entry_id_timestamp anArrayList : arrayList) {
            String where = Entry_RemoteID + " = " + anArrayList.getId();
            Cursor c = 	db.query(true, Table_Entry, Entry_KEYS,
                    where, null, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }

            assert c != null;
            if (c.getLong(DBAdapter.COL_EntryChangeDate) != anArrayList.getLast_change()) {
                //todo: Add the Servcerside session pull for the specific entrie and use the update entry funktion
                // TODO: add the overide in the Projectecperimententry tree for the aktuell entry.
            }
        }

    }
    //update a entrie if a new revision on the server
    public int getUserByEmail(String strings){
        String where = User_EMail + " = " + strings;
        Cursor c = 	db.query(true, Table_User, User_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        else
        {
            this.insertNewUser(new User("","",strings,""));
            c = db.query(true, Table_User, User_KEYS,
                    where, null, null, null, null, null);
        }
        return c.getInt(DBAdapter.COL_UserID);
    }
    // Needs a Remote entry and the User-email
    public boolean updateEntry(RemoteEntry obj,String userEmail){
        String where = Entry_RemoteID + " = " + obj.getRemote_id();
        ContentValues newValues = new ContentValues();
        newValues.put(Entry_Titel,obj.getTitle());
        newValues.put(Entry_Typ,obj.getAttachment_type());
        switch (obj.getAttachment_type()) {
            case 1:
                AttachmentText text = (AttachmentText) obj.getAttachment();
                newValues.put(Entry_Content,text.getText());
            case 2:
                AttachmentTable table =(AttachmentTable) obj.getAttachment();
                newValues.put(Entry_Content,Array2DToString(table.getTable_array()));
        }
        newValues.put(Entry_Sync,1);
        newValues.put(Entry_CreationDate,obj.getEntry_time());
        newValues.put(Entry_ChangeDate,obj.getChange_time());
        newValues.put(Entry_SyncDate,obj.sync_time);
        newValues.put(Entry_RemoteID,obj.remote_id);
        newValues.put(Entry_ExperimentID,obj.getExperiment_id());
        newValues.put(Entry_UserID,getUserByEmail(userEmail));
        return db.update(Table_Entry, newValues, where, null) != 0;
    }

    public String Array2DToString(String[][] strings){
        String finalstr ="";
        for (String[] s : strings) {
            String temp = "";
            for (String string : s) {
                temp += (string + ",");
            }
            finalstr += temp.substring(0, temp.length() - 1);
            finalstr += ";";
        }
        return finalstr;
    }

    public String[][] StringTo2DArray(String strings){
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

    private static int countLetter(String str, String letter) {
        int count = 0;
        for (int pos = -1; (pos = str.indexOf(letter, pos+1)) != -1; count++);
        return count;
    }

    // Change an existing row to be equal to new data.
    //public boolean updateRow(long rowId, String name, int studentNum, String favColour) {
    //   String where = KEY_ROWID + "=" + rowId;
		/*
		 * CHANGE 4:
		 */
    //
    //
    // Create row's data:
    //  ContentValues newValues = new ContentValues();
    //  newValues.put(KEY_NAME, name);
    //   newValues.put(KEY_STUDENTNUM, studentNum);
    //   newValues.put(KEY_FAVCOLOUR, favColour);

    // Insert it into the database.
    //   return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    //  }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_USER);
            _db.execSQL(DATABASE_CREATE_PROJECT);
            _db.execSQL(DATABASE_CREATE_EXPERIMENT);
            _db.execSQL(DATABASE_CREATE_Entry);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + Table_Entry);
            _db.execSQL("DROP TABLE IF EXISTS " + Table_Experiment);
            _db.execSQL("DROP TABLE IF EXISTS " + Table_Project);
            _db.execSQL("DROP TABLE IF EXISTS " + Table_Project);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
