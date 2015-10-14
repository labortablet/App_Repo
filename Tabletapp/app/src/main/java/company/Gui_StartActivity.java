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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import database.object_level_db;
import datastructures.User;
import exceptions.SBSBaseException;
import imports.Popup;
import static imports.App_Methodes.appendLog;

/**
 * Created by Grit on 29.05.2014.
 * This is the Application Main Class
 */

public class Gui_StartActivity extends Activity {
    // Variablen Deklaration

    EditText text;
    EditText text2;
    EditText text3;
    CheckBox checkBox;
    CheckBox checkBox2;
    CheckBox checkBox3;
    ProgressBar prgs;
    ProgressTask task;
    String server;
    String email;
    String password;
    private static object_level_db objectlevel_db;
    public static LocalService mService;
    static Context context;
    static boolean mBound = false;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    boolean mIsBound;
    public static Integer NumberOfEntries = 10;
    public static android.content.res.Configuration config;

    /**
     * Konstruktor fuer die Klasse Gui_StartActivity
     */

    public Gui_StartActivity() {
        super();
        mIsBound = false;
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * onCreate Methode fuer die Klasse Gui_StartActivity
     *
     * @param savedInstanceState a Bundle, A mapping from String values to various Parcelable types.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_start_activity);
        loadLocale();

        objectlevel_db = new object_level_db(this);
        text = (EditText) findViewById(R.id.editText);
        text2 = (EditText) findViewById(R.id.editText4);
        text3 = (EditText) findViewById(R.id.editText3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        context = getApplicationContext();

        try {
            set_NumbersOfEntries_saved_inSharedPreferences();
            set_UserDetailsFromSharedPreferences();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.load_error, Toast.LENGTH_SHORT).show();
            appendLog(e.getMessage());
            e.printStackTrace();
        }


    }

    /**
     * Setzt die Anzahl der Angezeigten Entries bei Änderung vom Standardwert 10 Falls dieser beim
     * Letzten Lauf der App in den Settings geändert wurde
     */

    private void set_NumbersOfEntries_saved_inSharedPreferences() {
        SharedPreferences appDetails = context.getSharedPreferences("com.lablet.PREFERENCE_APP_KEY", MODE_PRIVATE);
        if (appDetails.getInt("NumberOfEntries", 10) != 10) {
            NumberOfEntries = appDetails.getInt("NumberOfEntries", 10);
        }
    }

    /**
     * Setzt die User-Infos, Falls diese beim letzter ausführung gespeichert wurden
     */

    private void set_UserDetailsFromSharedPreferences() {
        SharedPreferences userDetails = context.getSharedPreferences("com.lablet.PREFERENCE_FILE_KEY", MODE_PRIVATE);
        if (userDetails.getString("ServerIP", null) != null)
            text.setText(userDetails.getString("ServerIP", null));
        if (userDetails.getString("userName", null) != null) {
            text2.setText(userDetails.getString("userName", null));
            checkBox2.setChecked(true);
        }
        if (userDetails.getString("Password", null) != null) {
            text3.setText(userDetails.getString("Password", null));
            checkBox3.setChecked(true);
        }
    }

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
                try {
                    if (!text.getText().toString().isEmpty() && !text2.getText().toString().isEmpty() && !text3.getText().toString().isEmpty()) { //Abfrage ob textfelder des logins leer sind
                        if (validate(text2.getText().toString())) { // abfrage der korrektheit der email
                            server = text.getText().toString();
                            email = text2.getText().toString();
                            password = text3.getText().toString();
                            showProgress();
                            save_ToSharedPreferences(text.getText().toString(), text2.getText().toString(), text3.getText().toString());
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

    /**
     * Schreibt die Eingegebenen Parameter in die SharedPreferences
     * @param ServerIP Server IP
     * @param E_Mail   E-Mail
     * @param Password Passwort
     */
    private void save_ToSharedPreferences(String ServerIP, String E_Mail, String Password) {
        SharedPreferences settings = getSharedPreferences("com.lablet.PREFERENCE_FILE_KEY", MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();

        if (checkBox.isChecked())
            edit.putString("ServerIP", ServerIP);
        else
            edit.putString("ServerIP", null);

        if (checkBox2.isChecked())
            edit.putString("userName", E_Mail);
        else
            edit.putString("userName", null);

        if (checkBox3.isChecked())
            edit.putString("Password", Password);
        else
            edit.putString("Password", null);

        edit.apply();
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
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }

    @Override
    protected void onDestroy() {
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
     * checking for the validation of the E-Mail
     *
     * @param hex The E-Mail
     */
    public boolean validate(final String hex) {

        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    /**
     * funktion for Closing the app by backpress with popup 2 ask if you want 2 close
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

    private class ProgressTask extends AsyncTask<Integer, Integer, Void> {
        ProgressDialog dialog;

        // The variable is moved here, we only need it here while displaying the
        // progress dialog.


        protected void onPreExecute() {

            dialog = new ProgressDialog(Gui_StartActivity.this);

            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.setTitle("Getting new Data From Server");

            dialog.show();
            // super.onPreExecute(); ///////???????
            //prgs.setMax(100); // set maximum progress to 100.

        }

        protected void onCancelled() {
            prgs.setMax(0); // stop the progress
            Log.v("Progress", "Cancelled");
        }

        protected Void doInBackground(Integer... params) {
            try {
                objectlevel_db.register_user(email, password, new URL(server));
                LinkedList<User> all_users_which_have_login_info = objectlevel_db.get_all_users_with_login();
                mService.setUserAndURL(getActiveUser(all_users_which_have_login_info), server);
                mService.getProjects();
                publishProgress(20);
                mService.getExperiments();
                publishProgress(20);
                mService.getEntry();

            }

            catch (SBSBaseException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            publishProgress(20);
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            dialog.incrementProgressBy(values[0]);
            // increment progress bar by progress value
            //////////////////////setProgress(10);
            //////////////////////prgs.setProgress(prgs.getProgress() + 5); // the bar does not fill 100%
            //   prgs.setProgress(5);
            //      Log.v("Progress","Once");
        }

        protected void onPostExecute(Void result) {
            // async task finished
            Intent intent;
            intent = new Intent(getApplicationContext(), Gui_DisplayProjectAndExperiment.class);
            startActivity(intent);


        }

    }
/*

     runnable = new Runnable() {
            public void run() {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Entry " + string + " was successfully synchronized", duration);
        toast.show();
            }
        };*/


    final static Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msgs) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Entry " + msgs + " was successfully synchronized", duration);
            toast.show();
        }
    };

    public void showProgress() {
        task = new ProgressTask();
        // start progress bar with initial progress 10
        ///////////////////task.execute(10,5,null);
        task.execute(0);

    }

    public void stopProgress() {
        // dialog.incrementProgressBy(progress[0]);
    }

    public static LocalService getmService() {
        return mService;
    }

    public static object_level_db getObjectlevel_db() {

        return objectlevel_db;

    }

    private User getActiveUser(LinkedList<User> users) {

        User user = null;

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUser_email().equals(email)) {

                user = users.get(i);

                break;
            }
        }

        return user;

    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }


    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("com.lablet.PREFERENCE_APP_KEY", MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

}


