package company;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import exceptions.SBSBaseException;
import imports.DBAdapter;
import imports.Popup;
import datastructures.User;
import scon.Entry_Remote_Identifier;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;
import scon.ServerDatabaseSession;


import static imports.App_Methodes.appendLog;

/**
 * Created by Grit on 29.05.2014.
 * This is the Application Main Class
 */

public class Gui_StartActivity extends Activity {
    // Variablen Deklaration / Instanzvariable:
    EditText text ;
    EditText text2;
    EditText text3 ;
    CheckBox checkBox;
    CheckBox checkBox2;
    CheckBox checkBox3;
    ProgressBar prgs;
    ProgressTask task;
    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Gui_StartActivity.user = user;
    }
    public static LocalService mService;
    static Context context;
    public static DBAdapter myDb;
    private static User user;
    boolean mBound = false;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    boolean mIsBound;

    public Gui_StartActivity() {
        super();
        mIsBound = false;
        pattern = Pattern.compile(EMAIL_PATTERN);
 }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_start_activity);
        text = (EditText) findViewById(R.id.editText);
        text2 = (EditText) findViewById(R.id.editText4);
        text3 = (EditText) findViewById(R.id.editText3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        prgs  = (ProgressBar) findViewById(R.id.progress);
        myDb = new DBAdapter(this);
        context = getApplicationContext();

  //      ActivityRegistry.register(this);
        try {
            SharedPreferences userDetails = context.getSharedPreferences("com.lablet.PREFERENCE_FILE_KEY", MODE_PRIVATE);
            if(userDetails.getString("ServerIP", null) != null)
                text.setText(userDetails.getString("ServerIP", null));
                if(userDetails.getString("userName",null)!= null) {
                    text2.setText(userDetails.getString("userName", null));
                     checkBox2.setChecked(true);}
                    if(userDetails.getString("Password", null) != null) {
                        text3.setText(userDetails.getString("Password", null));
                         checkBox3.setChecked(true);}
        }catch (Exception e)
        {
            appendLog(e.getMessage());
            e.printStackTrace();

        }


    } // Standart Android Methoden für apps

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start, menu);
        return super.onCreateOptionsMenu(menu);
    }  // Standart Android Methoden für apps

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }  // Standart Android Methoden für apps


    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button:
                Toast.makeText(this,"addresse gespeichert",Toast.LENGTH_LONG);
        try {
            if (!text.getText().toString().isEmpty() && !text2.getText().toString().isEmpty() && !text3.getText().toString().isEmpty()) { //Abfrage ob textfelder des logins leer sind


                if (validate(text2.getText().toString())) { // abfrage der korrektheit der email
                //    text2.setText("fredi@uni-siegen.de");
              //      text3.setText("test");
                    String server = text.getText().toString();
                    String email = text2.getText().toString();
                    String password = text3.getText().toString();

                   mService.setUserAndURL(new User(email, password,new URL(server)), server);
                   int i =  mService.connect(server);
                   showProgress();

                  switch (i) {
                       case 0:



SharedPreferences settings = getSharedPreferences("com.lablet.PREFERENCE_FILE_KEY", MODE_PRIVATE);
SharedPreferences.Editor edit = settings.edit();

if(checkBox.isChecked())
 edit.putString("ServerIP",text.getText().toString());
  else
   edit.putString("ServerIP", null);

    if(checkBox2.isChecked())
     edit.putString("userName",text2.getText().toString());
      else
       edit.putString("userName",null);

        if(checkBox3.isChecked())
         edit.putString("Password",text3.getText().toString());
          else
           edit.putString("Password",null);

                           edit.apply();
                           startActivity(new Intent(this, Gui_DisplayProjectAndExperiment.class));
                            break;
                       case 1:
                            Popup popup = new Popup();
                            popup.set_String(R.string.MalformedURLException);
                            popup.show(getFragmentManager(), "this");
                            break;
                       case 2:
                            Popup popup2 = new Popup();
                            popup2.set_String(R.string.NoInternet);
                            popup2.show(getFragmentManager(), "this");
                            break;
                       case 3:
                          Popup popup3 = new Popup();
                          popup3.set_String(R.string.SBSBaseException);
                          popup3.show(getFragmentManager(), "this");
                          break;
                    }


                } else {
                    Popup popup2 = new Popup();            // Popup für email
                    popup2.set_String(R.string.popup2);
                    popup2.show(getFragmentManager(), "this");

                }
            } else {
                Popup popup = new Popup();
                popup.set_String(R.string.popup);     // Popup für leere felder
                popup.show(getFragmentManager(), "this");
            }

        } catch (Exception e) {
            appendLog(e.getMessage());
            e.printStackTrace();
        }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mService.deleteAllSynced();
        // Unbind from the service
            if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.finish();
        System.exit(0);
    }


    /**
     * Connection Function for the Service
     */

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

/**
 *checking for the validation of the E-Mail
 * @param hex  The E-Mail
 *
 */
    public boolean validate(final String hex) {

        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    /** funktion for Closing the app by backpress with popup 2 ask if you want 2 close
     *
     */

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("no", null).show();
    }


    private class ProgressTask extends AsyncTask<Integer,Integer,Void>{

        protected void onPreExecute() {
            super.onPreExecute(); ///////???????
            prgs.setMax(100); // set maximum progress to 100.

        }
        protected void onCancelled() {
            prgs.setMax(0); // stop the progress
            Log.v("Progress","Cancelled");
        }
        protected Void doInBackground(Integer... params) {

            ServerDatabaseSession SDS = mService.SDS;

            LinkedList<RemoteProject> projects;
            LinkedList<RemoteExperiment> experiments;
            LinkedList<Entry> entries = new LinkedList<Entry>();
            // try {
            try {
                SDS.start_session();

            projects = SDS.get_projects();
                for (int i=0; i <projects.size();i++)
                {
                    Log.d("project",projects.get(i).getName());
                }
            experiments = SDS.get_experiments();
            RemoteEntry remoteEntry;
         /*   for (int i = 0; experiments.size() > i; i++) {
                LinkedList<Entry_Remote_Identifier> entry_remoteIdentifiers = SDS.get_last_entry_references(experiments.get(i).get_id(), 10, null);
                 for (int j = 0; entry_remoteIdentifiers.size() > j; j++) {
                    remoteEntry = SDS.get_entry(entry_remoteIdentifiers.get(j));

                    entries.add(remoteEntry);
                   Log.d("Attachmentcontent2", entries.get(j).getAttachment().getContent().toString());
                }}*/
            }catch (SBSBaseException e) {
            e.printStackTrace();
        }

            return null;
        }
        protected void onProgressUpdate(Integer... values) {

            // increment progress bar by progress value
            //////////////////////setProgress(10);
            //////////////////////prgs.setProgress(prgs.getProgress() + 5); // the bar does not fill 100%
            prgs.setProgress(5);
            Log.v("Progress","Once");
        }
        protected void onPostExecute(Void result) {
            // async task finished
            Log.v("Progress", "Finished");
        }

    }

    public void showProgress() {
        task = new ProgressTask();
        // start progress bar with initial progress 10
        ///////////////////task.execute(10,5,null);
        task.execute(10);

    }

    public void stopProgress() {
        task.cancel(true);
    }
}


