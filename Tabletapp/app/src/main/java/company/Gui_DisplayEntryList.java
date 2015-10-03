package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

import datastructures.Entry;
import datastructures.Experiment;
import exceptions.SBSBaseException;
import imports.ActivityRegistry;
import imports.ExpandableListAdapterEntries;
import imports.RowAndColumnPopup;

/**
 * @author  Grit on 19.07.2014.
 *
 * Shows the Specific Entries of an Experiement
 */
public class Gui_DisplayEntryList extends Activity {

    ExpandableListAdapterEntries listAdapter;
    ExpandableListView expListView;
    Experiment experiment;
    TextView textview1;
    Reference<LocalService> mservice = new WeakReference<LocalService>(Gui_StartActivity.getmService());
    LocalService service;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_entry_list);
        service = mservice.get();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        experiment = (Experiment) b.getSerializable("experiment");
        prepareList();

        ActivityRegistry.register(this);
    }

    private void prepareList(){
        LinkedList<Entry> entries = new LinkedList<Entry>();
        service.getDB().open();
        try {
            entries = service.getObjectlevel_db().get_entries(service.getUser(), experiment, Gui_StartActivity.NumberOfEntries);
        } catch (SBSBaseException e) {
            e.printStackTrace();
        }
        service.getDB().close();
        textview1 = (TextView)findViewById(R.id.textview1);
        textview1.setText(experiment.get_name());
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapterEntries(this,entries );

        try {
            expListView.setAdapter(listAdapter);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_entry_list, menu);
        return super.onCreateOptionsMenu(menu);

    }  // Standart Android Methoden für apps

    /**
     * Android Lifecycle method
     * After Adding a new LocalEntry Reload the List of Entries
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_refresh:
                return true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), Gui_Settings.class));
            case R.id.sync:
                long tenMinutes = 600000;
                long millis = System.currentTimeMillis() - Gui_StartActivity.mService.getTimeTillSync();
                long secs = (tenMinutes - millis) / 1000;
                long mins = secs / 60;
                Toast.makeText(getApplicationContext(),"Next Sync in"+mins+" Minutes", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_newentry:

                PopupMenu popupMenu = new PopupMenu(Gui_DisplayEntryList.this,textview1);
                popupMenu.getMenuInflater().inflate(R.menu.popupmenu_newentrydesition, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.keyboard:
                             start_New_ActionKeyboard();
                                break;
                            case R.id.table:
                             start_New_ActionTable();
                                break;
                            case R.id.image:
                             start_New_ActionImage();
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();//showing popup menu*/
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }  // Standart Android Methoden für apps
    /**
     * Starts the new Intent for creating new Entries
     */
    private void start_New_ActionKeyboard(){
        Intent intent;
        intent = new Intent(this, Gui_NewKeyboardEntry.class);
        intent.putExtra("experiment",experiment);
        startActivity(intent);
    }
    private void start_New_ActionTable(){
        Intent intent;
        intent = new Intent(this, RowAndColumnPopup.class);
        intent.putExtra("experiment",experiment);
        startActivity(intent);
    }

    /**
     * Starts the new Intent for Detail view of LocalEntry
     */
    private void start_New_ActionImage(){
        Intent intent;
        intent = new Intent(this, Gui_NewImageEntry.class);
        intent.putExtra("experiment",experiment);
        startActivity(intent);
    }



}
