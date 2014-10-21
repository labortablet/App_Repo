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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import imports.User;

/**
 * Created by Grit on 29.05.2014.
 */

public class Start extends Activity {
    // Variablen Deklaration / Instanzvariable:

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Start.user = user;
    }
    static LocalService mService;



    private static User user;

    boolean mBound = false;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Matcher matcher;
    private Pattern pattern;
    boolean mServiceConnected = false;
    private LocalService mLocnServ;
    private static String[][] project = new String[10][20];

    boolean mIsBound;

    EditText text;
    EditText text2;
    EditText text3;
    private String Server;
    private String email;
    private String password;

    public Start() {  //Construktor
        super();

        // Initialisierung der Variablen
        mIsBound = false;

        pattern = Pattern.compile(EMAIL_PATTERN);

        project[0][0] = "test";
        project[1][0] = "test1";
        project[2][0] = "test2";
        project[3][0] = null;
        project[4][0] = null;
        project[0][1] = "test";
        project[1][1] = "test1";

        project[0][2] = "Test3";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_show);
        //doBindService();
        ActivityRegistry.register(this);


      // Intent wtdservice = new Intent(this, LocalService.class);
      // startService(wtdservice);


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

            case R.id.button:   // button Login TODO: Loginfunct. von background app aufrufen
                text = (EditText) findViewById(R.id.editText);
                text2 = (EditText) findViewById(R.id.editText2);
                text3 = (EditText) findViewById(R.id.editText3);




        try {
            if (!text.getText().toString().isEmpty() && !text2.getText().toString().isEmpty() && !text3.getText().toString().isEmpty()) { //Abfrage ob textfelder des logins leer sind


                if (validate(text2.getText().toString())) { // abfrage der korrektheit der email

                    Server = text.getText().toString();
                    email = text2.getText().toString();
                    password = text3.getText().toString();
                    int i = 0;
                   user = new User(email, sha265(password));
                    i = mService.connect(Server,user);


                    switch (i) {
                        case 0:
                            start_NewActivity();
                            break;
                        case 1:
                            Popup popup = new Popup();            // Popup für email
                            popup.set_String(R.string.MalformedURLException);
                            popup.show(getFragmentManager(), "this");
                            break;

                        case 2:
                            Popup popup2 = new Popup();            // Popup für email
                            popup2.set_String(R.string.SBSBaseException);
                            popup2.show(getFragmentManager(), "this");
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

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
                break;

            case R.id.button2:   // button exit
                ActivityRegistry.finishAll(); // exit button
                System.exit(0);
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
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


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



    private String sha265(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        //convert the byte to hex format method

    /*    StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
    */



        byte[] byteData;
        byteData = md.digest();

        return Arrays.toString(byteData);

    }




/*
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
*/






















    /** Defines callbacks for service binding, passed to bindService()*/
   /* private ServiceConnection mConnection = new ServiceConnection() {

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
    };*/













    // Diese methoden können bisher noch ignoriert werden sie dienen dazu den service hinterher einzubinden wenn er dann läuft.
   /* private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((LocalService.LocalBinder)service).getService();


            Toast.makeText(Start.this, R.string.local_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            Toast.makeText(Start.this, R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {

        bindService(new Intent(Start.this,
                LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
         mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {

            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
    */
    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }




    public static void set_Project(String[][] Project) {
        project = Project;
    }

    public static String[][] get_Projekts() {
        return project;
    }

    private void start_NewActivity() {

        Intent intent = new Intent(this, Project_show.class);
        startActivity(intent);
    }
}


