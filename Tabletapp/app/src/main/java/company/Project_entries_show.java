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
  // private static int index_ofproject = Project_show.getChosen_project();
   //ArrayList<ArrayList<Project>> obj =Project_show.getObj();
  ExpandableListView myListView;
    ListAdapter listenAdapter;
   // int word_position = ActivityRegistry.get_wordposition1(temp,Project_show.getChosen_project());

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_entries_show);
        //doBindService();
        ActivityRegistry.register(this);


        textView     = (TextView) findViewById(R.id.textView);
      //  textView.setText(obj.get(0).get(Project_show.getChosen_project()).getProject_Name());




        // Set ExpandableListView values

       // getExpandableListView().setGroupIndicator(null);
      //  getExpandableListView().setDivider(devider);
       // getExpandableListView().setChildDivider(devider);
     //   getExpandableListView().setDividerHeight(1);
      //  registerForContextMenu(getExpandableListView());

         myListView = ( ExpandableListView) findViewById(R.id.ListView);
    //    final ArrayList<String> list = new ArrayList<String>(); // nur gefüllte spalten auswählen, und zur liste hinzufügen

/*
        if(temp[word_position][1] != null) {
            for (int i = 1; temp[word_position].length >= i; i++) {
                if (temp[word_position][i] != null)
                    list.add(temp[word_position][i]);
                else
                    break;
            }
            myListView.setClickable(true);
        }
        else
        {
            list.add("Keine Einträge vorhanden");
            myListView.setClickable(false);
        }
*/
     //   listenAdapter = new ExpandableListAdapter(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(listenAdapter);

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

        switch(v.getId()) {  // switch ID button

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
