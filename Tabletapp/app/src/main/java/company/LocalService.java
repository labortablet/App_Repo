package company;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;


import database.object_level_db;
import exceptions.NoInternetAvailable;
import exceptions.SBSBaseException;
import datastructures.AttachmentBase;
import datastructures.AttachmentTable;
import datastructures.AttachmentText;
import imports.DBAdapter;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.ProjectExperimentEntry;
import imports.ServersideDatabaseConnectionObject;
import datastructures.User;
import scon.Entry_Remote_Identifier;
import scon.ServerDatabaseSession;


/**
 * Created by Grit on 04.06.2014.
 */
public class LocalService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private User user;
    public static Timer myTimer = new Timer();
    public static Timer alwaysTimer = new Timer();
    private ServersideDatabaseConnectionObject SDCO;
    static ServerDatabaseSession SDS;
    public WeakReference<object_level_db> objectlevel_db;
    private URL url;
    public static DBAdapter myDb;//= Gui_StartActivity.myDb;
    static List<ProjectExperimentEntry> projectExperimentEntries;

    //Hashmaps so we can cache the objects and can update them easily.
    //also, the same entry will only exist as a unique object and will therefore be
    //updated correctly!
  //  private HashMap<Integer, SoftReference<User>> user_local_id2user_object = new HashMap<Integer, SoftReference<User>>();
   // private HashMap<Integer, SoftReference<Project>> project_local_id2project_object = new HashMap<Integer, SoftReference<Project>>();
   // private HashMap<Integer, SoftReference<Experiment>> experiment_local_id2experiment_object = new HashMap<Integer, SoftReference<Experiment>>();
 //   private HashMap<Integer, SoftReference<Entry>> entry_local_id2entry_object = new HashMap<Integer, SoftReference<Entry>>();

  //  private WeakHashMap<User, WeakReference<LinkedList<Project>>> project_list_cache = new WeakHashMap<User, WeakReference<LinkedList<Project>>>();
  //  private WeakHashMap<User, WeakHashMap<Project, WeakReference<LinkedList<Experiment>>>> experiment_list_cache = new WeakHashMap<User, WeakHashMap<Project, WeakReference<LinkedList<Experiment>>>>();
  //  private WeakHashMap<User, WeakHashMap<Experiment, WeakReference<LinkedList<Entry>>>> entry_list_cache = new WeakHashMap<User, WeakHashMap<Experiment, WeakReference<LinkedList<Entry>>>>();

    public LocalService() {}

    @Override
    public void onCreate() {
        myDb = new DBAdapter(getApplicationContext());
        objectlevel_db = new WeakReference<object_level_db>(Gui_StartActivity.getObjectlevel_db());
        super.onCreate();
        deleteAllSynced();
    }
    public WeakReference<object_level_db> getObjectlevel_db()
    {return objectlevel_db;}

    public void setUserAndURL(User user, String server) throws MalformedURLException {
        this.user = user;
        this.url = new URL(server);

    }
    public DBAdapter getDB(){
        return myDb;
    }

    private void openDB() {
        myDb.open();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        closeDB();
    }


    private void closeDB() {
        myDb.close();
    }

    public void deleteAllSynced() {
        this.openDB();
        myDb.deleteAllSyncedProjects();
        myDb.deleteAllSyncedExperiments();
        myDb.deleteAllSyncedEntries();
        this.closeDB();

    }

    public int connect(String adress) throws ExecutionException, InterruptedException {
        URL url;
        try {
            url = new URL(adress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 1;
        }


        if (isOnline()) {
            SDS = new ServerDatabaseSession(url, user);
           // SDCO = new ServersideDatabaseConnectionObject(SDS, myDb);
            //int i = new StartUpAsyncTask().execute(SDCO).get();

            myTimer.schedule(new MyTask(), 60000);
            //return i;
            return 0;
        } else return 2;
    }
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    /**
     * method for clients
     */
    public static Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public User getUser() {
        return user;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

public LinkedList<scon.RemoteProject> getProjects() throws SBSBaseException {
    SDS = new ServerDatabaseSession(url, user);
    SDS.start_session();
    return SDS.get_projects();
}

    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    private static class MyTask extends TimerTask {

        public void run() {
            myDb.open();
            Cursor cursor = myDb.getAllUnsyncedEntries();
            Log.d("TimerStarted", String.valueOf(System.currentTimeMillis()));
            //TODO This is for getting the hashmap for Experiments and Projects
            /*
            projectExperimentEntries = Project_show.getProjectExperimentEntries();

            try {
                new AsyncTaskProjectExperimentHashtable().execute(projectExperimentEntries).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/

            if (cursor.getCount() > 0) {


                if (cursor.moveToFirst()) {
                    do {
                        // Process the data:
                        try {
                            int experiment_ID = cursor.getInt(DBAdapter.COL_EntryExperimentID);
                            Entry_Remote_Identifier new_entry_info;

                            if(cursor.getInt(DBAdapter.COL_EntryTyp) == 1)
                            new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)));
                            else
                            new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent)));


                            AttachmentBase attachmentBase = new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent));
                            myDb.updateEntryAfterSync(new_entry_info, cursor.getLong(DBAdapter.COL_EntryCreationDate));
                            //   projectExperimentEntries = Project_show.getProjectExperimentEntries();
                            // projectExperimentEntries.get(myDb.getProjectIDByExperimentID(experiment_ID)).getExperimentEntry().get(cursor.getInt(experiment_ID)).getEntriesList().get(projectExperimentEntries.get(myDb.getProjectIDByExperimentID(experiment_ID)).getExperimentEntry().get(cursor.getInt(experiment_ID)).getEntryIDByCreationTimestamp(cursor.getLong(DBAdapter.COL_EntryCreationDate))).setSync(true);
                            //- Project_show.setProjectExperimentEntries(projectExperimentEntries);
                        } catch (SBSBaseException e) {
                            e.printStackTrace();
                        }

                    } while (cursor.moveToNext());
                }
            }

            myDb.close();
            alwaysTimer.scheduleAtFixedRate(new MyTaskAlwaysRunning(), 1200000, 1200000);
            this.cancel();
        }

    }

    private static class MyTaskAlwaysRunning extends TimerTask {

        public void run() {
            if (isOnline()) {
                myDb.open();
                Cursor cursor = myDb.getAllUnsyncedEntries();
                Log.d("TimerStarted", String.valueOf(System.currentTimeMillis()));
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            // Process the data:
                            try {
                                int experiment_ID = cursor.getInt(DBAdapter.COL_EntryExperimentID);
                                Entry_Remote_Identifier new_entry_info;
if(cursor.getInt(DBAdapter.COL_EntryTyp) == 1)
                                new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)));
                            else
                                new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent)));

                                AttachmentBase attachmentBase = new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent));

                                myDb.updateEntryAfterSync(new_entry_info, cursor.getLong(DBAdapter.COL_EntryCreationDate));
                                // TODO: ADD The update in the listview here
                                //   projectExperimentEntries = Project_show.getProjectExperimentEntries();
                                // projectExperimentEntries.get(myDb.getProjectIDByExperimentID(experiment_ID)).getExperimentEntry().get(cursor.getInt(experiment_ID)).getEntriesList().get(projectExperimentEntries.get(myDb.getProjectIDByExperimentID(experiment_ID)).getExperimentEntry().get(cursor.getInt(experiment_ID)).getEntryIDByCreationTimestamp(cursor.getLong(DBAdapter.COL_EntryCreationDate))).setSync(true);
                                //- Project_show.setProjectExperimentEntries(projectExperimentEntries);
                            } catch (SBSBaseException e) {
                                e.printStackTrace();
                            }

                        } while (cursor.moveToNext());
                    }
                }
                myDb.close();
            }
        }

    }




    private class DownloadFilesTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Cursor c = myDb.getAllProjectRows();
            if (c.moveToFirst()) {
                do {
                    // Process the data:
              //  project_local_id2project_object.put(c.getInt(DBAdapter.COL_ProjectRemoteID), new SoftReference<Project>(new Project(c.getInt(DBAdapter.COL_ProjectRemoteID),c.getInt(DBAdapter.COL_EntryRemoteID),c.getString(DBAdapter.COL_ProjectName),c.getString(DBAdapter.COL_ProjectDescription))));
                } while (c.moveToNext());
            }
            c = myDb.getAllExperimentRows();
            if (c.moveToFirst()) {
                do {
                    // Process the data:
                //    experiment_local_id2experiment_object.put(c.getInt(DBAdapter.COL_ExperimentID), new SoftReference<Experiment>(new Experiment(c.getInt(DBAdapter.COL_ExperimentID),c.getInt(DBAdapter.COL_ExperimentRemoteID),c.getInt(DBAdapter.COL_ExperimentProjectID),c.getString(DBAdapter.COL_ExperimentName),c.getString(DBAdapter.COL_ExperimentDescription))));
                } while (c.moveToNext());
            }
            c = myDb.getAllEntryRows();
            if (c.moveToFirst()) {
                do {
                    // Process the data:
              //    entry_local_id2entry_object.put(c.getInt(DBAdapter.COL_EntryID), new SoftReference<Entry>(new Entry(c.getInt(DBAdapter.COL_EntryID),new User(c.getString(DBAdapter.COL_EntryUserID),c.getString(DBAdapter.COL_EntryUserID)),c.getInt(DBAdapter.COL_EntryExperimentID),c.getString(DBAdapter.COL_EntryTitle),new AttachmentText(c.getString(DBAdapter.COL_EntryContent)),true,c.getLong(DBAdapter.COL_EntryCreationDate),c.getLong(DBAdapter.COL_EntrySyncDate),c.getLong(DBAdapter.COL_EntryChangeDate))));
                } while (c.moveToNext());
            }

            // Close the cursor to avoid a resource leak.
            c.close();


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
           // setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
         //   showDialog("Downloaded " + result + " bytes");
        }
    }

}
/*
    //Define Sqlite Statements for later use in Prepared Statements
    These are more of an idea and a basis for discussion right now and should not yet be used
    private static final String DATABASE_updateFooString = "SELECT * FROM TEST WHERE id = ?";
    private static final String DATABASE_CREATE_Entries =
            "CREATE TABLE IF NOT EXISTS entries(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "remote_id INTEGER UNIQUE," +
                    "experiment_id INTEGER NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "title STRING NOT NULL," +
                    "date_creation INTEGER NOT NULL," +
                    "date_user INTEGER NOT NULL," +
                    "date_current INTEGER," +
                    "attachment_ref STRING NOT NULL," +
                    "attachment_type INTEGER NOT NULL" +
                    ");";


    private static final String DATABASE_CREATE_USER =
            "CREATE TABLE IF NOT EXISTS users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "remote_id INTEGER UNIQUE," +
                    "lastname STRING," +
                    "firstname STRING," +
                    "login STRING," +
                    "hashed_pw STRING" +
                    ");";

    private static final String DATABASE_CREATE_EXPERIMENT =
            "CREATE TABLE IF NOT EXISTS experiments(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "remote_id INTEGER," +
                    "user_id INTEGER NOT NULL," +
                    "name STRING NOT NULL," +
                    "description STRING," +
                    "date_creation INTEGER" +

                    ");";

    private static final String DATABASE_CREATE_PROJECT =
            "CREATE TABLE IF NOT EXISTS projects(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "remote_id INTEGER," +
                    "user_id INTEGER NOT NULL," +
                    "name STRING NOT NULL," +
                    "description STRING," +
                    "date_creation INTEGER" +
                    ");";
    *

    // Interface to be used by the GUi after binding

    User register_user(String login, String password, URL server){
        return null;
    };

Project register_project(User user, String project_name){
        return null;
        };

        Experiment register_experiment(User user, Project project, String experiment_name){
        return null;
        };

        //FIXME Local Entry??? We should either call them all Local or all without a prefix
        Entry new_Entry(User user, Experiment experiment, String title, AttachmentBase attachment){
        return null;
        };

        LinkedList<Project> match_project(Project project){
        return null;
        };

        LinkedList<Project> match_project(Project project, Project real_project){
        return null;
        };

        LinkedList<Project> match_project(Project project, boolean force_sync) throws NoInternetAvailable {
        return null;
        };

        LinkedList<Project> match_project(Project project, Project real_project, boolean force_sync) throws NoInternetAvailable{
        return null;
        };

        LinkedList<Experiment> merge_experiment_into(Experiment experiment){
        return null;
        };

        LinkedList<Experiment> merge_experiment_into(Experiment experiment, Experiment real_experiment){
        return null;
        };

        LinkedList<Experiment> merge_experiment_into(Experiment experiment, boolean force_sync) throws NoInternetAvailable{
        return null;
        };

        LinkedList<Experiment> merge_experiment_into(Experiment experiment, Experiment real_experiment, boolean force_sync) throws NoInternetAvailable{
        return null;
        };

        Boolean check_user(User user) throws NoInternetAvailable{
        return null;
        };

        LinkedList<Project> get_projects(User user){
        return null;
        };

        LinkedList<Project> get_projects(User user, boolean force_sync) throws NoInternetAvailable{
        return null;
        };

        LinkedList<Experiment> get_experiments(User user, Project project){
        return null;
        };

        LinkedList<Experiment> get_experiments(User user, Project project, boolean force_sync) throws NoInternetAvailable{
        return null;
        };

        LinkedList<Entry> get_entries(User user, Experiment experiment, int number){
        return null;
        };

        LinkedList<Entry> get_entries(User user, Experiment experiment, int number, boolean force_sync) throws NoInternetAvailable{
        return null;
        };
        */