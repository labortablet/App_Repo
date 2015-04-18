package company;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.test1.tabletapp.app.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import imports.ActivityRegistry;
import imports.DBAdapter;
import imports.Popup;
import datastructures.User;

/**
 * Created by Grit on 29.05.2014.
 */

public class Gui_StartActivity extends Activity {
    // Variablen Deklaration / Instanzvariable:

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
        myDb = new DBAdapter(this);
        context = getApplicationContext();
        setContentView(R.layout.layout_gui_start_activity);
        ActivityRegistry.register(this);

    } // Standart Android Methoden für apps

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }  // Standart Android Methoden für apps

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }  // Standart Android Methoden für apps


    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button:
                EditText text = (EditText) findViewById(R.id.editText);
                EditText text2 = (EditText) findViewById(R.id.editText2);
                EditText text3 = (EditText) findViewById(R.id.editText3);
                text2.setText("fredi@uni-siegen.de");
                text3.setText("test");



        try {
            if (!text.getText().toString().isEmpty() && !text2.getText().toString().isEmpty() && !text3.getText().toString().isEmpty()) { //Abfrage ob textfelder des logins leer sind


                if (validate(text2.getText().toString())) { // abfrage der korrektheit der email
                    text2.setText("fredi@uni-siegen.de");
                    text3.setText("test");
                    String server = text.getText().toString();
                    String email = text2.getText().toString();
                    String password = text3.getText().toString();
                   mService.setUserAndURL(new User(email, password), server);
                   int i =  mService.connect(server);


                  switch (i) {
                       case 0:

                           Intent intent = new Intent(this, Gui_DisplayProjectAndExperiment.class);
                           startActivity(intent);
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
        } catch (NullPointerException Ignored) {

        } catch (Exception e) {
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
}


