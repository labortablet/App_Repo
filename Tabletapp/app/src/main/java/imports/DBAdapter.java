package imports;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

import datastructures.AttachmentTable;
import datastructures.AttachmentText;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.User;
import scon.Entry_Remote_Identifier;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;


public class DBAdapter {
    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////

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
    public static final String[] Entry_KEYS = new String[]{Entry_ID, Entry_Titel, Entry_Typ, Entry_Content, Entry_Sync, Entry_CreationDate, Entry_ChangeDate, Entry_SyncDate, Entry_RemoteID, Entry_ExperimentID, Entry_UserID};
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
    public static final String[] Experiment_KEYS = new String[]{Experiment_ID, Experiment_Name, Experiment_Description, Experiment_Sync, Experiment_RemoteID, Experiment_ProjectID};
    /*
   Table Fields for Table _user
    */
    public static final String User_ID = " User_ID ";
    public static final String User_EMail = " User_EMail ";
    public static final String User_Password = " User_Password ";
    public static final String User_FName =" User_FName ";
    public static final String User_LName =" User_LName ";
    //The Keys for each table
    public static final String[] User_KEYS = new String[]{User_ID, User_EMail, User_Password, User_FName, User_LName};
    /*
   Table Fields for Table _project
    */
    public static final String Project_ID = " Project_ID ";
    public static final String Project_Name = " Project_Name ";
    public static final String Project_Description = " Project_Description ";
    public static final String Project_Sync = " Project_Sync ";
    public static final String Project_UserID = " Project_UserID ";
    public static final String Project_RemoteID = " Project_RemoteID ";
    public static final String[] Project_KEYS = new String[]{Project_ID, Project_Name, Project_Description, Project_Sync, Project_RemoteID};
    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "LabletDB";
    public static final String Table_Entry = " _entry ";
    //
    private static final String DATABASE_CREATE_Entry =
            "CREATE TABLE IF NOT EXISTS" + Table_Entry
                    + Bracket_Separator_Left + Entry_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + Entry_Titel + Typ_String + Comma_Separator
                    + Entry_Typ + Typ_Integer + "not null" + Comma_Separator
                    + Entry_Content + Typ_Text + Comma_Separator
                    + Entry_Sync + Typ_Integer + Comma_Separator
                    + Entry_CreationDate + Typ_Number + Comma_Separator
                    + Entry_ChangeDate + Typ_Number + Comma_Separator
                    + Entry_SyncDate + Typ_Number + Comma_Separator
                    + Entry_RemoteID + Typ_Integer + " UNIQUE " + Comma_Separator
                    + Entry_ExperimentID + Typ_Integer + Comma_Separator
                    + Entry_UserID + Typ_String
                    + Bracket_Separator_Right + Semicolon_Separator;
    public static final String Table_User = " _user ";
    //
    private static final String DATABASE_CREATE_USER =
            "CREATE TABLE IF NOT EXISTS" + Table_User
                    + Bracket_Separator_Left + User_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + User_EMail + Typ_String + "not null" + Comma_Separator
                    + User_Password + Typ_String + "not null" + Comma_Separator
                    + User_FName + Typ_String + Comma_Separator
                    + User_LName + Typ_String
                    + Bracket_Separator_Right + Semicolon_Separator;
    public static final String Table_Project = " _project ";
    //
    private static final String DATABASE_CREATE_PROJECT =
            "CREATE TABLE IF NOT EXISTS" + Table_Project
                    + Bracket_Separator_Left + Project_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + Project_Name + Typ_String + "not null, "
                    + Project_Description + Typ_Text + Comma_Separator
                    + Project_Sync + Typ_Integer + Comma_Separator
                    + Project_RemoteID + Typ_Integer + " UNIQUE " + Comma_Separator
                    + Project_UserID + Typ_Integer
                    + Bracket_Separator_Right + Semicolon_Separator;
    public static final String Table_Experiment = " _experiment ";
    //
    private static final String DATABASE_CREATE_EXPERIMENT =
            "CREATE TABLE IF NOT EXISTS" + Table_Experiment
                    + Bracket_Separator_Left + Experiment_ID + Typ_Integer + "primary key autoincrement" + Comma_Separator
                    + Experiment_Name + Typ_String + "not null" + Comma_Separator
                    + Experiment_Description + Typ_Text + Comma_Separator
                    + Experiment_Sync + Typ_Integer + Comma_Separator
                    + Experiment_RemoteID + Typ_Integer + " UNIQUE " + Comma_Separator
                    + Experiment_ProjectID + Typ_Integer
                    + Bracket_Separator_Right + Semicolon_Separator;
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;
    // ids for the specific rows
    //user table
    public final static int COL_UserID = 0;
    // Context of application who uses us.
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
    // For logging:
    private static final String TAG = "DBAdapter";
    private static DatabaseHelper myDBHelper;
    private SQLiteDatabase db;
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
    public void insertNewUser(User user) {
        //TODO: We need a REMOTE ID of the user
        // Create row's data:
        SQLiteStatement sqLiteStatement = db.compileStatement("" + "INSERT INTO" + Table_User + " ( "+ User_EMail + "," + User_Password + "," + User_FName + "," +User_LName+ " ) "+ "VALUES (?,?,?,?)" );
        sqLiteStatement.bindString(1, user.getUser_email());
        sqLiteStatement.bindString(2 , String.valueOf(user.getPw_hash()));
        sqLiteStatement.bindString(3, user.getFirstname());
        sqLiteStatement.bindString(4, user.getLastname());
        sqLiteStatement.executeInsert();
    }

    public void insertProject(LinkedList<RemoteProject> projectLinkedList){
        for (int i = 0; i<projectLinkedList.size();i++){
            RemoteProject project = projectLinkedList.get(i);
       SQLiteStatement sqLiteStatement = db.compileStatement("" + "INSERT INTO" + DBAdapter.Table_Project + " ( "+ Project_Name + "," + Project_Description + "," + Project_RemoteID + " ) "+ "VALUES (?,?,?)" );
            sqLiteStatement.bindString(1, project.getName());
            sqLiteStatement.bindString(2 , project.getDescription());
            sqLiteStatement.bindLong(3, project.getId());
            sqLiteStatement.executeInsert();
        }

    }

    public void insertExperiments(LinkedList<RemoteExperiment> remoteExperiments){
        for (int i = 0; i<remoteExperiments.size();i++){
            RemoteExperiment experiment = remoteExperiments.get(i);
            SQLiteStatement sqLiteStatement = db.compileStatement("" + "INSERT INTO " + Table_Experiment + " ( "+ Experiment_Name + "," + Experiment_Description + "," + Experiment_RemoteID + "," + Experiment_ProjectID + " ) "+ "VALUES (?,?,?,?)" );


            Cursor c = db.rawQuery(" SELECT "+ Project_ID +" FROM "+ Table_Project +" WHERE " + Project_RemoteID + " = ?",new String[]{String.valueOf(experiment.getProject_id())});

            c.moveToFirst();

            sqLiteStatement.bindString(1, experiment.getName());
            sqLiteStatement.bindString(2 , experiment.getDescription());
            sqLiteStatement.bindLong(3, experiment.getId());

            sqLiteStatement.bindLong(4, c.getLong(DBAdapter.COL_ProjectID));
            c.close();
            sqLiteStatement.executeInsert();
        }

    }
    public void insertEntries(LinkedList<RemoteEntry> remoteEntries,User user){

            for (int i = 0; i<remoteEntries.size();i++){
               RemoteEntry remoteEntry = remoteEntries.get(i);

                SQLiteStatement sqLiteStatement = db.compileStatement("" + "INSERT INTO " + DBAdapter.Table_Entry + " ( "+ Entry_Titel + "," + Entry_Typ + "," + Entry_Content + "," + Entry_CreationDate + "," + Entry_ChangeDate + "," + Entry_SyncDate + "," + Entry_RemoteID + "," + Entry_ExperimentID + "," + Entry_UserID + " ) "+ " VALUES (?,?,?,?,?,?,?,?,?) " );
//: TODO fix this entry here

                Cursor c = db.rawQuery(" SELECT "+ Experiment_ID +" FROM "+ Table_Experiment +" WHERE " + Experiment_RemoteID + " = ?",new String[]{String.valueOf(remoteEntry.getExperiment_id())});
                Cursor c1 = db.rawQuery(" SELECT "+ User_ID +" FROM "+ Table_User +" WHERE " + User_EMail + " = ?",new String[]{String.valueOf(user.getUser_email())});

                c.moveToFirst();
                c1.moveToFirst();
                sqLiteStatement.bindString(1,remoteEntry.getTitle());
                sqLiteStatement.bindLong(2, remoteEntry.getAttachment_type());
                sqLiteStatement.bindString(3, remoteEntry.getAttachment_ser());
                sqLiteStatement.bindLong(4, remoteEntry.getEntry_time());
                sqLiteStatement.bindLong(5, remoteEntry.getChange_time());
                sqLiteStatement.bindLong(6, remoteEntry.getSync_time());
                sqLiteStatement.bindLong(7, remoteEntry.getId());
                sqLiteStatement.bindLong(8, c.getLong(DBAdapter.COL_ExperimentID));
//                sqLiteStatement.bindLong(9, c1.getLong(DBAdapter.COL_UserID));
                sqLiteStatement.bindNull(9);
                c.close();
                c1.close();
                sqLiteStatement.executeInsert();


        }

    }

    public long insertRemoteProject(Project remoteProject){
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(Project_Name,remoteProject.get_name());
        initialValues.put(Project_Description,remoteProject.get_description());
        initialValues.put(Project_Sync,1);
       // initialValues.put(Project_RemoteID,remoteProject.get_remote_id());
        return db.insert(Table_Project,null,initialValues);
    }
    public Long insertRemoteProject(String project_Name,String project_Description,int project_Id){
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(Project_Name,project_Name);
        initialValues.put(Project_Description,project_Description);
        initialValues.put(Project_Sync,1);
        initialValues.put(Project_RemoteID,project_Id);
        return db.insert(Table_Project,null,initialValues);
    }
    public long insertRemoteExperiment(Experiment remoteExperiment){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Experiment_Name,remoteExperiment.get_name());
        initialValues.put(Experiment_Description,remoteExperiment.get_description());
        initialValues.put(Experiment_Sync,1);
        initialValues.put(Experiment_ProjectID,remoteExperiment.get_project_id());
        initialValues.put(Experiment_RemoteID,remoteExperiment.get_id());
        return db.insert(Table_Experiment,null,initialValues);
    }
    public Long insertRemoteExperiment(String experiment_Name,String experiment_Description,int experiment_Project_Id,int experiment_Id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Experiment_Name,experiment_Name);
        initialValues.put(Experiment_Description,experiment_Description);
        initialValues.put(Experiment_Sync,1);
        initialValues.put(Experiment_ProjectID,experiment_Project_Id);
        initialValues.put(Experiment_RemoteID,experiment_Id);
        return db.insert(Table_Experiment,null,initialValues);
    }

    public Long insertRemoteEntry(Entry remoteEntry) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Entry_Titel,remoteEntry.getTitle());
        initialValues.put(Entry_Typ, remoteEntry.getAttachment().getTypeNumber());
        initialValues.put(Entry_Content, remoteEntry.getAttachment().getContent());
        Log.d("Attachment", remoteEntry.getAttachment().getContent());
        initialValues.put(Entry_Sync,1);
        initialValues.put(Entry_ExperimentID,remoteEntry.getExperiment_id());
        initialValues.put(Entry_UserID,remoteEntry.getUser().getUser_email());
     //   initialValues.put(Entry_RemoteID,remoteEntry.getRemote_id());
        initialValues.put(Entry_CreationDate,remoteEntry.getEntry_time());
        initialValues.put(Entry_SyncDate,remoteEntry.getSync_time());
        initialValues.put(Entry_ChangeDate,remoteEntry.getChange_time());
        return db.insert(Table_Entry, null, initialValues);
    }

    public Long insertRemoteEntry(String title, int typ, String entry_Content, int experiment_id, String user_name, Long entrytime, Long synctime, Long changetime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Entry_Titel, title);
        initialValues.put(Entry_Typ, typ);
        initialValues.put(Entry_Content, entry_Content);
        initialValues.put(Entry_Sync, 1);
        initialValues.put(Entry_ExperimentID, experiment_id);
        initialValues.put(Entry_UserID, user_name);
     //   initialValues.put(Entry_RemoteID, remote_id);
        initialValues.put(Entry_CreationDate, entrytime);
        initialValues.put(Entry_SyncDate, synctime);
        initialValues.put(Entry_ChangeDate, changetime);
        return db.insert(Table_Entry,null,initialValues);
    }
    public Long insertLocalEntry(Entry entry){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Entry_Titel, entry.getTitle());
        initialValues.put(Entry_Typ, entry.getAttachment().getTypeNumber());
        initialValues.put(Entry_Content, entry.getAttachment().getContent().toString());
        initialValues.put(Entry_UserID, entry.getUser().getUser_id());
        initialValues.put(Entry_Sync,0);
        initialValues.put(Entry_ExperimentID, entry.getExperiment_id());
        initialValues.put(Entry_CreationDate, entry.getEntry_time());
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
    //Deletes every in the specific table
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

    //Deletes every what is Already synced
    public void deleteAllSyncedProjects(){
        Cursor c = getAllSyncedProjectRows();
        long rowId = c.getColumnIndexOrThrow(Project_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteProjectByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();

    }
    public void deleteAllSyncedExperiments(){
        Cursor c = getAllSyncedExperimentRows();
        long rowId = c.getColumnIndexOrThrow(Experiment_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteExperimentByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    public void deleteAllSyncedEntries(){
        Cursor c = getAllSyncedEntryRows();
        long rowId = c.getColumnIndexOrThrow(Entry_ID.trim());
        if (c.moveToFirst()) {
            do {
                deleteEntryByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Returns all Synced Data's in the database
    public Cursor getAllSyncedProjectRows() {
        String where = Project_Sync + " = 1";
        Cursor c = 	db.query(true, Table_Project, Project_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getAllSyncedExperimentRows() {
        String where = Experiment_Sync + " = 1";
        Cursor c = 	db.query(true, Table_Experiment, Experiment_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getAllSyncedEntryRows() {
        String where = Entry_Sync + " = 1";
        Cursor c = 	db.query(true, Table_Entry, Entry_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all Data in the Database.
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
    public LinkedList<Project> getAllProjectRowsLinkedList() {
        String where = null;
        Cursor c = 	db.query(true, Table_Project, Project_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        LinkedList<Project> projects = new LinkedList<Project>();
            do {
              projects.add(new Project(c.getLong(DBAdapter.COL_ProjectID),c.getString(DBAdapter.COL_ProjectName),c.getString(DBAdapter.COL_ProjectDescription)));
                // Process the data:
                // remoteProjects.add(new Project(cursor.getInt(DBAdapter.COL_ProjectID), cursor.getInt(DBAdapter.COL_ProjectRemoteID), cursor.getString(DBAdapter.COL_ProjectName), cursor.getString(DBAdapter.COL_ProjectDescription)));

            } while (c.moveToNext());

        // Close the cursor to avoid a resource leak.
        c.close();
        return projects;
    }
    public Cursor getAllProjectsByUser(User user){
       return db.query(Table_Project,Project_KEYS,Entry_UserID + " = ?",new String[]{String.valueOf(user.getId())},null,null,null);
    }

public LinkedList<Experiment> getExperimentByLocalProjectID(Project project){
   // SQLiteStatement sqLiteStatement1 = db.compileStatement(" SELECT "+ Experiment_ID + Experiment_Name+ Experiment_Description +" FROM "+ Table_Experiment +" WHERE " + Experiment_ProjectID + " = ?");
 //sqLiteStatement1.bindLong(1,project.get_id());
    Cursor cursor = db.rawQuery(" SELECT "+ Experiment_ID + "," + Experiment_Name+ "," + Experiment_Description +" FROM "+ Table_Experiment +" WHERE " + Experiment_ProjectID + " = ?",new String[]{String.valueOf(project.get_id())});
   LinkedList<Experiment> experimentLinkedList = new LinkedList<Experiment>();
   cursor.moveToFirst();
   Experiment experiment;
    if(cursor.getCount() > 0 && cursor.moveToFirst()){
    do {
        experiment = new Experiment(cursor.getLong(DBAdapter.COL_ExperimentID),cursor.getString(DBAdapter.COL_ExperimentName),cursor.getString(DBAdapter.COL_ExperimentDescription));
        experimentLinkedList.add(experiment);
    } while (cursor.moveToNext());

    // Close the cursor to avoid a resource leak.
    cursor.close();}

return experimentLinkedList;

}
public int getExperimentCountByProjectLocalID(Long ID){
        Cursor c = db.rawQuery(" SELECT "+ "COUNT(*) FROM "+ Table_Experiment +" WHERE "+ Experiment_ProjectID +"=?",new String[]{String.valueOf(ID)});
        c.moveToFirst();
return c.getInt(0);
    }

    public LinkedList<Entry> getGetEntryByExperiment(Experiment experiment){
        // SQLiteStatement sqLiteStatement1 = db.compileStatement(" SELECT "+ Experiment_ID + Experiment_Name+ Experiment_Description +" FROM "+ Table_Experiment +" WHERE " + Experiment_ProjectID + " = ?");
        //sqLiteStatement1.bindLong(1,project.get_id());
        Cursor cursor = db.rawQuery(" SELECT "+ Entry_ID + "," + Entry_Titel+ "," + Entry_Typ+ "," + Entry_Content + ","+ Entry_CreationDate +"," + Entry_ChangeDate + "," +Entry_SyncDate+ ","+ Entry_ExperimentID+ "," + Entry_UserID +" FROM "+ Table_Entry +" WHERE " + Entry_ExperimentID + " = ?",new String[]{String.valueOf(experiment.get_id())});
        LinkedList<Entry> entryLinkedList = new LinkedList<Entry>();
        cursor.moveToFirst();
        Entry entry;

        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            do {
           //    Cursor cursor1 = db.rawQuery(" SELECT "+ User_FName + ","+ User_LName +" FROM "+ Table_User +" WHERE " + User_ID + " = ?",new String[]{java.lang.String.valueOf(cursor.getLong(DBAdapter.COL_EntryUserID))});

              // cursor1.moveToFirst();

                    if(cursor.getLong(DBAdapter.COL_EntryTyp) == 1) {
                        entry = new Entry(cursor.getLong(DBAdapter.COL_EntryID), new User("Hans","Maier")/*new User(cursor1.getString(DBAdapter.COL_UserFName), cursor1.getString(DBAdapter.COL_UserLName))*/, cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getLong(COL_EntryCreationDate), cursor.getLong((DBAdapter.COL_EntrySyncDate)), cursor.getLong(COL_EntryChangeDate));
                        entryLinkedList.add(entry);
                    }
                        if(cursor.getLong(DBAdapter.COL_EntryTyp) == 2) {
                            entry = new Entry(cursor.getLong(DBAdapter.COL_EntryID),new User("Hans","Maier") /*new User(cursor1.getString(DBAdapter.COL_UserFName), cursor1.getString(DBAdapter.COL_UserLName))*/, cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getLong(COL_EntryCreationDate), cursor.getLong((DBAdapter.COL_EntrySyncDate)), cursor.getLong(COL_EntryChangeDate));
                            entryLinkedList.add(entry);
                        }
                // TODO: ALSO fix USER QUERY IF REMOTE ID IS ADDED
                //TODO: ADD THE OTHER ENTRY TYPES HERE
//cursor1.close();
            } while (cursor.moveToNext());

            // Close the cursor to avoid a resource leak.
            cursor.close();}

        return entryLinkedList;

    }

    //TODO : add a sql query for getting the projects of the specificant user
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
        Cursor c = 	db.query(true, Table_Entry, Entry_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }

    //method for getting the actual Entries only the Entry_id_timestamp info's
    public void getAllEntryForCompare(ArrayList<Entry_Remote_Identifier> arrayList)   {
        for (Entry_Remote_Identifier anArrayList : arrayList) {
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
      //      this.insertNewUser(new User("","",strings,""));
         //   c = db.query(true, Table_User, User_KEYS,
         //           where, null, null, null, null, null);
        }
        return c.getInt(DBAdapter.COL_UserID);
    }

    // Needs a Remote entry and the User-email
    /*
    public boolean updateEntry(RemoteEntry obj,String userEmail){
        String where = Entry_RemoteID + " = " + obj.getRemote_id();
        ContentValues newValues = new ContentValues();
        newValues.put(Entry_Titel,obj.getTitle());
        newValues.put(Entry_Typ,obj.getAttachment().getTypeNumber());
                newValues.put(Entry_Content,obj.getAttachment().toString());
        newValues.put(Entry_Sync,1);
        newValues.put(Entry_CreationDate,obj.getEntry_time());
        newValues.put(Entry_ChangeDate,obj.getChange_time());
        newValues.put(Entry_SyncDate,obj.sync_time);
        newValues.put(Entry_RemoteID,obj.remote_id);
        newValues.put(Entry_ExperimentID,obj.getExperiment_id());
        newValues.put(Entry_UserID,getUserByEmail(userEmail));
        return db.update(Table_Entry, newValues, where, null) != 0;
    }*/

    public boolean updateEntryAfterSync(Entry_Remote_Identifier entry_remoteIdentifier,Long entry_Time){
        String where = Entry_CreationDate + " = " + entry_Time;
        ContentValues newValues = new ContentValues();
        newValues.put(Entry_Sync,1);
        newValues.put(Entry_ChangeDate, entry_remoteIdentifier.getLast_change());
       // newValues.put(Entry_RemoteID,entry_id_timestamp.getId());
        return db.update(Table_Entry, newValues, where, null) != 0;
    }
    public int getProjectIDByExperimentID(int experiment_id)
    {
        String where = Experiment_RemoteID + " = " + experiment_id;
        Cursor c = 	db.query(true, Table_Experiment, Experiment_KEYS,
                where, null, null, null, null, null);
      c.moveToFirst();
       return c.getInt(DBAdapter.COL_ExperimentProjectID);
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
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static DatabaseHelper getInstance(Context context) {

            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (myDBHelper == null) {
                myDBHelper = new DatabaseHelper(context.getApplicationContext());
            }
            return myDBHelper;
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
