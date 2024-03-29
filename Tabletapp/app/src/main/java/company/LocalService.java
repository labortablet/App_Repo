package company;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import database.object_level_db;
import datastructures.Entry;
import datastructures.User;
import exceptions.NoSuccess;
import exceptions.SBSBaseException;
import imports.DBAdapter;
import scon.Entry_Remote_Identifier;
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


    static ServerDatabaseSession SDS;
    public static object_level_db objectlevel_db;
    private URL url;
    public static DBAdapter myDb;//= Gui_StartActivity.myDb;
    LinkedList<RemoteProject> Projects;
    LinkedList<RemoteExperiment> experiments;
    LinkedList<Entry_Remote_Identifier> entries;
    ArrayList<Long> longs;
    private long timeTillSync;
    int len = 0;
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

    public LocalService() {
    }

    @Override
    public void onCreate() {
        myDb = new DBAdapter(getApplicationContext());
        objectlevel_db = new object_level_db(getApplicationContext());
        super.onCreate();
    }



    public object_level_db getObjectlevel_db() {
        return objectlevel_db;
    }

    public void setUserAndURL(User user, String server) throws MalformedURLException {
        this.user = user;
        this.url = new URL(server);
    }

    public void getProjects() throws SBSBaseException {
        Log.e("von hier wurden projecte gelesen","x");
        SDS = new ServerDatabaseSession(url, user);
        SDS.start_session();
        Projects = SDS.get_projects();
        len = Projects.size();
        for (int j = 0; j < len; j++) {
        objectlevel_db.insert_or_update_project(user, Projects.get((j)));
        }
        Log.e("bis hier wurden Projecte gelesen","x");
    }

    public void getExperiments() throws SBSBaseException {
        experiments = SDS.get_experiments();
         len = experiments.size();
        for (int i = 0; i < len; i++) {
            objectlevel_db.insert_or_update_experiment(user, experiments.get(i));
        }
    }



    public void getEntry() throws SBSBaseException {
        longs = objectlevel_db.getExperiment_ids_by_user(user);
        len = longs.size();
        long userid = user.getId();
        for (int i = 0; i < len; i++) {
            entries = SDS.get_last_entry_references(longs.get(i), 5, null);
            for (Entry_Remote_Identifier entry_remote_identifier : entries) {
                try {
                    objectlevel_db.insert_or_update_entry(SDS.get_entry(entry_remote_identifier), userid);
                }catch (NoSuccess ex){
                    ex.printStackTrace();

                }
            }
        }
        new LongOperation().execute("");
    }


    public DBAdapter getDB() {
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
            return 0;
        } else return 2;
    }

    public long getTimeTillSync() {
        return timeTillSync;
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



    private class LongOperation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

                try {

                    LinkedList<Entry> entryLinkedList = objectlevel_db.get_unsynced_entries();
                    if (entryLinkedList.size() == 0 && !Gui_StartActivity.mBound){
                       stopSelf();
                    }
                    Entry_Remote_Identifier remote_identifier;
                    for (Entry entry : entryLinkedList) {
                        try {
                             if(entry.getAttachment().getTypeNumber() != 3) {

                                 Log.e("Entrie infos:", objectlevel_db.get_Remote_ExperimentID(entry.getExperiment_id()) + " , " + entry.getEntry_time() + " , " + entry.getTitle() + " , " + entry.getAttachment());

                                 remote_identifier = SDS.send_entry(objectlevel_db.get_Remote_ExperimentID(entry.getExperiment_id()), entry.getEntry_time(), entry.getTitle(), entry.getAttachment());
                                 objectlevel_db.updateEntryAfterSync(entry.getId(), remote_identifier);

                                 //TODO: publish the progress of the sync
                                 //  this.publishProgress(entry.getTitle());
                             }
                        }catch (NoSuccess noSuccess){
                            noSuccess.printStackTrace();

                        }
                        timeTillSync = System.currentTimeMillis();
                        Thread.sleep(10000,0);
                    }
                } catch (SBSBaseException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();

                }


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

       // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
         /*   Gui_StartActivity.runnable = (new Runnable() {
                public void run() {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "Entry was successfully synchronized", duration);
                    toast.show();
                }
            });*/

        }
    }
}



