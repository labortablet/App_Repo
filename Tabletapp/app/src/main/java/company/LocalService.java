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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import AsyncTasks.StartUpAsyncTask;
import exceptions.SBSBaseException;
import imports.AttachmentBase;
import imports.AttachmentText;
import imports.Experiment;
import imports.LocalEntry;
import imports.Project;
import imports.ProjectExperimentEntry;
import imports.ServersideDatabaseConnectionObject;
import imports.User;
import scon.Entry_id_timestamp;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;
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
    static HashMap<Integer,Integer> projectHashMap; // Remote ID, Tree ID
    static HashMap<Integer,Integer> experimentHashMap; // Remote ID,Tree ID
    static DBAdapter myDb = Start.myDb;
    static List<ProjectExperimentEntry> projectExperimentEntries;

    public LocalService() {

// Notice: this is for using a http conn in the gui or service without async task or timer task just for testing not for release
       /* if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
    }

    @Override
    public void onCreate() {
        super.onCreate();
        deleteAllSynced();
    }

    public void setUserAndURL(User user, String server) {
        this.user = user;

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
            SDCO = new ServersideDatabaseConnectionObject(SDS, myDb);
            int i = new StartUpAsyncTask().execute(SDCO).get();

            myTimer.schedule(new MyTask(), 60000);
            return i;
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
                            Entry_id_timestamp new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)));
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
                                Entry_id_timestamp new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)));
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


    private static class AsyncTaskProjectExperimentHashtable extends AsyncTask<List<ProjectExperimentEntry>,Integer,Boolean>{


        @Override
        protected Boolean doInBackground(List<ProjectExperimentEntry>... params) {
            projectHashMap = new HashMap<Integer, Integer>();
            experimentHashMap = new HashMap<Integer, Integer>();
            for (int i = 0;i < projectExperimentEntries.size();i++)
            {
                projectHashMap.put(projectExperimentEntries.get(i).getProject().get_id(),i);
                for (int j = 0;projectExperimentEntries.get(i).getExperimentEntry().size() > j; j++)
                {
                    experimentHashMap.put(projectExperimentEntries.get(i).getExperimentEntry().get(j).getExperiments().get_id(),j);

                }

            }
            return null;
        }
    }
}