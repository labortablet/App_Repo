package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

import imports.ActivityRegistry;

/**
 * Created by Grit on 20.08.2014.
 */
public class Gui_DisplayProjectDetails extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.project_detail);
        ActivityRegistry.register(this);
        TextView text = (TextView)findViewById(R.id.textView2);
        TextView text2 = (TextView)findViewById(R.id.textView4);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        assert b != null;
        text.setText((String) b.get("name"));
        text2.setText((String) b.get("description"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_show, menu);
        return true;


    }


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
    }

    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button:   // button back

                this.finish();         // back button

                break;

            case R.id.button2:  // button exit

                ActivityRegistry.finishAll(); // exit button
                System.exit(0);
                break;


        }
    }
}