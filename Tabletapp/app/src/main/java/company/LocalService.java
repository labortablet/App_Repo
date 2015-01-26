package company;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import AsyncTasks.StartUpAsyncTask;
import exceptions.SBSBaseException;
import imports.AttachmentBase;
import imports.AttachmentTable;
import imports.AttachmentText;
import imports.Experiment;
import imports.LocalEntry;
import imports.Project;
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
    private ServersideDatabaseConnectionObject SDCO;
    static ServerDatabaseSession SDS;
    private LinkedList<RemoteProject> projects = new LinkedList<RemoteProject>();
    private LinkedList<RemoteExperiment> experiments = new LinkedList<RemoteExperiment>();
    private LinkedList<RemoteEntry> entries = new LinkedList<RemoteEntry>();
    static DBAdapter myDb = Start.myDb;
    TimerTask timerTask;
    private static List<ProjectExperimentEntry> projectExperimentEntries;

    public LocalService() {


       /* if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/


        // myDb.deleteAllEntries();
        //comment from here
        //   Cursor cur = myDb.getAllProjectRows();
        //cur.moveToFirst();
        //if (cur.getCount() == 0)
        //dummiData();


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


    /*
    private void dummiData(){
     try{
        user = new User("Hans@dampf.net","passwd","Hans","Dampf");
        myDb.insertNewUser(new User("Hans@dampf.net","passwd","Hans","Dampf"));
        myDb.insertRemoteProject(new RemoteProject(1, "project 1", "Das ist Project 1"));
        myDb.insertRemoteProject(new RemoteProject(2,"project 2" ,"Das ist Project 2"));
        myDb.insertRemoteProject(new RemoteProject(3,"project 3" ,"Das ist Project 3"));
        myDb.insertRemoteExperiment(new RemoteExperiment(1, 1, "Experiment 1", "Inhalt 1"));
        myDb.insertRemoteExperiment(new RemoteExperiment(1, 2, "Experiment 2", "Inhalt 2"));
        myDb.insertRemoteExperiment(new RemoteExperiment(2, 3, "Experiment 3", "Inhalt 3"));
        myDb.insertRemoteExperiment(new RemoteExperiment(2, 4, "Experiment 4", "Inhalt 4"));
        myDb.insertRemoteExperiment(new RemoteExperiment(3, 5, "Experiment 5", "Inhalt 5"));
        myDb.insertRemoteExperiment(new RemoteExperiment(3, 6, "Experiment 6", "Inhalt 6"));


        myDb.insertRemoteEntry(new RemoteEntry(new AttachmentText("test"), System.currentTimeMillis()/1000,1,System.currentTimeMillis()/1000,System.currentTimeMillis()/1000, "test1",user));
        myDb.insertRemoteEntry(new RemoteEntry(new AttachmentText("test"), App_Methodes.generateTimestamp(),2,App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(), "test2",user));
        myDb.insertRemoteEntry(new RemoteEntry(new AttachmentText("test"), App_Methodes.generateTimestamp(),3,App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(), "test3",user));
        myDb.insertRemoteEntry(new RemoteEntry(new AttachmentText("test"), App_Methodes.generateTimestamp(),4,App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(), "test4",user));
        myDb.insertRemoteEntry(new RemoteEntry(new AttachmentText("test"), App_Methodes.generateTimestamp(),5,App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(), "test5",user));
        myDb.insertRemoteEntry(new RemoteEntry(new AttachmentText("test"), App_Methodes.generateTimestamp(),5,App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(), "test6",user));
    }

    catch (Exception e)
    {
        e.printStackTrace();
    }
    }
    */

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
            myTimer.scheduleAtFixedRate(new MyTask(),60000,1200000);
            return i;
        } else return 2;
    }

    // connection method For connecting with server

    // Method to get all active Projects From the user
    public LinkedList<Project> getProjects() throws SBSBaseException {
        // LinkedList<Project> remoteProject_list = new LinkedList<Project>();// = SDS.get_projects();
        LinkedList<Project> projects1 = new LinkedList<Project>();

    /*    for (RemoteProject project : projects) {
            projects1.add(new Project(project));
        }
        return projects1;*/
        myDb.open();
        return displayProjects(myDb.getAllProjectRows());

        //    remoteProject_list.add(0,new Project(new RemoteProject(1,"project 1" ,"Das ist Project 1")));
        //  remoteProject_list.add(1,new Project(new RemoteProject(2,"project 2" ,"Das ist Project 2")));
        //  remoteProject_list.add(2,new Project(new RemoteProject(3,"project 3" ,"Das ist Project 3")));
        //  return remoteProject_list;
    }

    private LinkedList<Project> displayProjects(Cursor cursor) {
        try {

            LinkedList<Project> remoteProjects = new LinkedList<Project>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    remoteProjects.add(new Project(cursor.getInt(DBAdapter.COL_ProjectID), cursor.getInt(DBAdapter.COL_ProjectRemoteID), cursor.getString(DBAdapter.COL_ProjectName), cursor.getString(DBAdapter.COL_ProjectDescription)));

                } while (cursor.moveToNext());
            }
            // Close the cursor to avoid a resource leak.
            cursor.close();
            return remoteProjects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // Method to get all active Experiments From the user
    public LinkedList<Experiment> getExperiments() throws SBSBaseException {
/*
        LinkedList<Experiment> remoteExperiments_list = new LinkedList<Experiment>(); // SDS.get_experiments();

        remoteExperiments_list.add(0,new RemoteExperiment(1, 1, "Experiment 1", "Inhalt 1"));
        remoteExperiments_list.add(1,new RemoteExperiment(1, 2, "Experiment 2", "Inhalt 2"));
        remoteExperiments_list.add(2,new RemoteExperiment(2, 3, "Experiment 3", "Inhalt 3"));
        remoteExperiments_list.add(3,new RemoteExperiment(2, 4, "Experiment 4", "Inhalt 4"));
        remoteExperiments_list.add(4,new RemoteExperiment(3, 5, "Experiment 5", "Inhalt 5"));
        remoteExperiments_list.add(5,new RemoteExperiment(3, 6, "Experiment 6", "Inhalt 6"));
        return remoteExperiments_list;*/
        return displayExperiments(myDb.getAllExperimentRows());
    }

    private LinkedList<Experiment> displayExperiments(Cursor cursor) {
        try {

            LinkedList<Experiment> experiments = new LinkedList<Experiment>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    experiments.add(new Experiment(cursor.getInt(DBAdapter.COL_ExperimentID), cursor.getInt(DBAdapter.COL_ExperimentRemoteID), cursor.getInt(DBAdapter.COL_ExperimentProjectID), cursor.getString(DBAdapter.COL_ExperimentName), cursor.getString(DBAdapter.COL_ExperimentDescription)));

                } while (cursor.moveToNext());
            }
            // Close the cursor to avoid a resource leak.
            cursor.close();
            return experiments;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get all active Entries From the user
    public LinkedList<LocalEntry> getEntries() throws SBSBaseException {
        return displayEntries(myDb.getAllEntryRows());
    }

    private LinkedList<LocalEntry> displayEntries(Cursor cursor) {
        try {

            LinkedList<LocalEntry> entries;
            entries = new LinkedList<LocalEntry>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    entries.add(
                            new LocalEntry(cursor.getInt(DBAdapter.COL_EntryID),
                                    new User(cursor.getString(DBAdapter.COL_EntryUserID)),
                                    cursor.getInt(DBAdapter.COL_EntryExperimentID),
                                    cursor.getString(DBAdapter.COL_EntryTitle),
                                    AttachmentBase.deserialize(cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getString(DBAdapter.COL_EntryContent)),
                                    cursor.getInt(DBAdapter.COL_EntrySync) == 1,
                                    cursor.getLong(DBAdapter.COL_EntryCreationDate),
                                    cursor.getLong(DBAdapter.COL_EntrySync),
                                    cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                } while (cursor.moveToNext());
            }
            // Close the cursor to avoid a resource leak.
            cursor.close();
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedList getLastEntries(int projectID, int experimentID, int entryID, int NumbersToCall) {
        LinkedList<RemoteEntry> remoteEntries_list = new LinkedList<RemoteEntry>();

        //TODO: ADD Get last entry function here
        return remoteEntries_list;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    /**
     * method for clients
     */
    public Boolean isOnline() {
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
if (cursor.getCount() >0){
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    try {
                        int experiment_ID = cursor.getInt(DBAdapter.COL_EntryExperimentID);
                        Entry_id_timestamp new_entry_info = SDS.send_entry(experiment_ID, cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), cursor.getInt(DBAdapter.COL_EntryTyp), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)));
                        AttachmentBase attachmentBase = new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent));
                        myDb.updateEntryAfterSync(new_entry_info,cursor.getLong(DBAdapter.COL_EntryCreationDate));
                     //   projectExperimentEntries = Project_show.getProjectExperimentEntries();
                       // projectExperimentEntries.get(myDb.getProjectIDByExperimentID(experiment_ID)).getExperimentEntry().get(cursor.getInt(experiment_ID)).getEntriesList().get(projectExperimentEntries.get(myDb.getProjectIDByExperimentID(experiment_ID)).getExperimentEntry().get(cursor.getInt(experiment_ID)).getEntryIDByCreationTimestamp(cursor.getLong(DBAdapter.COL_EntryCreationDate))).setSync(true);
                       // Project_show.setProjectExperimentEntries(projectExperimentEntries);
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








/*
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SVTEST", "Loc service ONBIND");
        return mBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("SVTEST", "Loc service ONUNBIND");
        return super.onUnbind(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Won't run unless it's EXPLICITLY STARTED
        Log.d("SVTEST", "Loc service ONSTARTCOMMAND");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SVTEST", "Loc service ONDESTROY");
    }
/*

    /** method for clients */




    /*
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.

    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
  /*  public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
      CharSequence text = getText(R.string.local_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.uni_siegen, text,
             System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification


        // Set the info for the views that show in the notification panel.


        // Send the notification.
       mNM.notify(NOTIFICATION, notification);
    }

    */
