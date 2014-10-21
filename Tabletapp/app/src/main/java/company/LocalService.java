package company;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.LinkedList;

import database.DatabaseHandler;
import exceptions.SBSBaseException;
import imports.AttachmentText;
import imports.User;
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
    private ServerDatabaseSession SDS;
    private  byte[] challange;
    private DatabaseHandler DB_Handler;
    SQLiteDatabase myDB = null;
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    public boolean Create_DB(){
        DB_Handler = new DatabaseHandler(this);
        try {
            myDB = this.openOrCreateDatabase("Lablet.db", MODE_PRIVATE, null);
            return true;
   /* Create a Table in the Database. */


   /* Insert data to a Table*/

        }
        catch (Exception e)
        {
            return false;
        }
    }

    // connection method For connecting with server
   public int connect(String adress,User user){
       this.user = user;
       this.user.setName("Hans","dieter");
       SQLiteDatabase mydatabase = openOrCreateDatabase("Test",MODE_PRIVATE,null);
       URL url = null;
       try {
         url = new URL(adress);
       } catch (MalformedURLException e) {
           e.printStackTrace();
           return 1;
       }
      SDS = new ServerDatabaseSession(url, user.getUser_email(), user.getPw_hash());

     if(isNetworkAvailable()){

         if(isOnline()){
   /*    try {
   //TODO: add chalange function
           challange = SDS.get_challenge();
       } catch (SBSBaseException e) {
           e.printStackTrace();
           return 2;
           */
             return 0;
       }else return 2;}else return 2;


    // }else return 2;
   }
// Method to get all active Projects From the user
    public LinkedList getProjects() throws SBSBaseException {

        LinkedList<RemoteProject> remoteProject_list = new LinkedList<RemoteProject>();// = SDS.get_projects();

        remoteProject_list.add(0,new RemoteProject(1,"project 1" ,"Das ist Project 1"));
        remoteProject_list.add(1,new RemoteProject(2,"project 2" ,"Das ist Project 2"));
        remoteProject_list.add(2,new RemoteProject(3,"project 3" ,"Das ist Project 3"));
        return remoteProject_list;
    }
    // Method to get all active Experiments From the user
    public LinkedList getExperiments() throws SBSBaseException {

        LinkedList<RemoteExperiment> remoteExperiments_list = new LinkedList<RemoteExperiment>(); // SDS.get_experiments();

        remoteExperiments_list.add(0,new RemoteExperiment(1, 1, "Experiment 1", "Inhalt 1"));
        remoteExperiments_list.add(1,new RemoteExperiment(1, 2, "Experiment 2", "Inhalt 2"));
        remoteExperiments_list.add(2,new RemoteExperiment(2, 3, "Experiment 3", "Inhalt 3"));
        remoteExperiments_list.add(3,new RemoteExperiment(2, 4, "Experiment 4", "Inhalt 4"));
        remoteExperiments_list.add(4,new RemoteExperiment(3, 5, "Experiment 5", "Inhalt 5"));
        remoteExperiments_list.add(5,new RemoteExperiment(3, 6, "Experiment 6", "Inhalt 6"));
        return remoteExperiments_list;
    }
    // Method to get all active Entries From the user
    public LinkedList getEntries() throws SBSBaseException {
        //TODO : add entry call function here!
        LinkedList<RemoteEntry> remoteEntries_list = new LinkedList<RemoteEntry>();

        remoteEntries_list.add(0,new RemoteEntry(new AttachmentText("test1") ,1,new Timestamp(System.currentTimeMillis()),1,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), "test1",user));
        remoteEntries_list.add(1,new RemoteEntry(new AttachmentText("test2") ,1,new Timestamp(System.currentTimeMillis()),2,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), "test2",user));
        remoteEntries_list.add(2,new RemoteEntry(new AttachmentText("test3") ,1,new Timestamp(System.currentTimeMillis()),3,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), "test3",user));
        remoteEntries_list.add(3,new RemoteEntry(new AttachmentText("test4") ,1,new Timestamp(System.currentTimeMillis()),4,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), "test4",user));
        remoteEntries_list.add(4,new RemoteEntry(new AttachmentText("test5") ,1,new Timestamp(System.currentTimeMillis()),5,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), "test5",user));
        remoteEntries_list.add(5,new RemoteEntry(new AttachmentText("test6") ,1,new Timestamp(System.currentTimeMillis()),5,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()), "test6",user));

        return remoteEntries_list;
    }

    public LinkedList getLastEntries(int projectID,int experimentID,int entryID,int NumbersToCall){
        LinkedList<RemoteEntry> remoteEntries_list = new LinkedList<RemoteEntry>();

        //TODO: ADD Get last entry function here
        return remoteEntries_list;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }





    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();}

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
