package company;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

/**
 * Created by Grit on 16.06.2014.
 */

public class Project_entries_show extends Activity {

   TextView textView;
  ExpandableListView myListView;
    ListAdapter listenAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_entries_show);
        ActivityRegistry.register(this);


        textView     = (TextView) findViewById(R.id.textView);
        myListView = ( ExpandableListView) findViewById(R.id.ListView);
        myListView.setAdapter(listenAdapter);

    }
     // Standart Android Methoden für apps
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
    }

    public void buttonEventHandler(View v) {  // butten events

        switch(v.getId()) {

            case R.id.button :   // button back

                this.finish();
                break;

            case R.id.button2 :   // button exit

                ActivityRegistry.finishAll();

                break;

            case R.id.button3 :   // button new entry

                break;
        }
    }
}
