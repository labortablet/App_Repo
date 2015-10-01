package company;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.util.Locale;

/**
 * Created by Grit on 22.09.2015.
 */
public class Gui_Settings extends Activity {

    EditText editText;
    Spinner spinner;
    Locale myLocale;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_settings);
        getBaseContext().getResources().updateConfiguration(Gui_StartActivity.config, getBaseContext().getResources().getDisplayMetrics());
        editText = (EditText) findViewById(R.id.editText7);
        textView = (TextView) findViewById(R.id.textView14);
        editText.setText(Gui_StartActivity.NumberOfEntries.toString());
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 0) {

                    Toast.makeText(parent.getContext(),
                            "You have selected German", Toast.LENGTH_SHORT)
                            .show();
                    changeLang("de");
                } else if (pos == 1) {

                    Toast.makeText(parent.getContext(),
                            "You have selected English", Toast.LENGTH_SHORT)
                            .show();
                    changeLang("en");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        textView.setText(R.string.settings);
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = Gui_StartActivity.context.getSharedPreferences("com.lablet.PREFERENCE_APP_KEY", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
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

            case R.id.button6:
                if (editText.getText().length() != 0) {
                    Integer number = Integer.parseInt(editText.getText().toString());
                    Gui_StartActivity.NumberOfEntries = number;
                    SharedPreferences appDetails = Gui_StartActivity.context.getSharedPreferences("com.lablet.PREFERENCE_APP_KEY", MODE_PRIVATE);
                    SharedPreferences.Editor editor = appDetails.edit();
                    editor.putInt("NumberOfEntries", number);
                    editor.apply();
                }
                break;
        }
    }


}
