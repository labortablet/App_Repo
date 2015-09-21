package company;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import database.object_level_db;
import datastructures.AttachmentBase;
import datastructures.AttachmentTable;
import datastructures.AttachmentText;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.ProjectExperimentEntry;
import datastructures.User;
import exceptions.SBSBaseException;
import imports.DBAdapter;
import imports.ServersideDatabaseConnectionObject;
import scon.Entry_Remote_Identifier;
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

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    static ServerDatabaseSession SDS;
    public static object_level_db objectlevel_db;
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

    public LocalService() {
    }

    @Override
    public void onCreate() {
        myDb = new DBAdapter(getApplicationContext());
        objectlevel_db = new object_level_db(getApplicationContext());
        super.onCreate();

        // deleteAllSynced();
    }



    public object_level_db getObjectlevel_db() {
        return objectlevel_db;
    }

    public void setUserAndURL(User user, String server) throws MalformedURLException {
        this.user = user;
        this.url = new URL(server);
    }

    public void getProjects() throws SBSBaseException {
        SDS = new ServerDatabaseSession(url, user);
        SDS.start_session();
        LinkedList<RemoteProject> Projects = SDS.get_projects();
        int len = Projects.size();
        for (int j = 0; j < len; j++) {
            objectlevel_db.insert_or_update_project(user, Projects.get((j)));
        }
    }

    public void getExperiments() throws SBSBaseException {
        LinkedList<RemoteExperiment> experiments = SDS.get_experiments();
        int len = experiments.size();
        for (int i = 0; i < len; i++) {
            objectlevel_db.insert_or_update_experiment(user, experiments.get(i));
        }
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
      //  initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms

       // timer.schedule(timerTask,0, 10000); //

    }

    public void getEntry() throws SBSBaseException {
        ArrayList<Long> longs = objectlevel_db.getExperiment_ids_by_user(user);

        LinkedList<Entry_Remote_Identifier> entries;
        int len = longs.size();
        long userid = user.getId();
        for (int i = 0; i < len; i++) {
            entries = SDS.get_last_entry_references(longs.get(i), 5, null);
            for (Entry_Remote_Identifier entry_remote_identifier : entries) {
                objectlevel_db.insert_or_update_entry(SDS.get_entry(entry_remote_identifier), userid);

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
            // SDCO = new ServersideDatabaseConnectionObject(SDS, myDb);
            //int i = new StartUpAsyncTask().execute(SDCO).get();


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


    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

   /* public void initializeTimerTask() {


        timerTask = new TimerTask() {

            public void run() {


                //use a handler to run a toast that shows the current timestamp

                handler.post(new Runnable() {

                    public void run() {
                        try {
                            LinkedList<Entry> entryLinkedList = objectlevel_db.get_unsynced_entries();
                            Entry_Remote_Identifier remote_identifier = new Entry_Remote_Identifier(0, (long) 0);
                            for (Entry entry : entryLinkedList) {
                                Log.d("Entrie infos:", objectlevel_db.get_Remote_ExperimentID(entry.getExperiment_id()) +" , "+ entry.getEntry_time() +" , "+ entry.getTitle() +" , "+ entry.getAttachment());
                                remote_identifier = SDS.send_entry(entry.getExperiment_id(), entry.getEntry_time(), entry.getTitle(), entry.getAttachment());
                               // objectlevel_db.updateEntryAfterSync(entry.getId(), remote_identifier);
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(getApplicationContext(), "Entry " + entry.getTitle() + " was successfully synchronized", duration);
                                toast.show();
                            }
                        } catch (SBSBaseException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();

                        }
                    }

                });

            }

        };

    }*/

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 5; i++) {
                try {

                    LinkedList<Entry> entryLinkedList = objectlevel_db.get_unsynced_entries();
                    Entry_Remote_Identifier remote_identifier = new Entry_Remote_Identifier(0, (long) 0);
                    for (Entry entry : entryLinkedList) {
                        Log.d("Entrie infos:", objectlevel_db.get_Remote_ExperimentID(entry.getExperiment_id()) +" , "+ entry.getEntry_time() +" , "+ entry.getTitle() +" , "+ entry.getAttachment());
                        SDS.send_entry(entry.getExperiment_id(), entry.getEntry_time(), entry.getTitle(), entry.getAttachment());
                        // objectlevel_db.updateEntryAfterSync(entry.getId(), remote_identifier);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), "Entry " + entry.getTitle() + " was successfully synchronized", duration);
                        toast.show();
                    }
                } catch (SBSBaseException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();

                }

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
        protected void onProgressUpdate(Void... values) {}
    }
}



