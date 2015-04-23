package company;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;


import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import datastructures.AttachmentTable;
import datastructures.AttachmentText;
import datastructures.Entry;
import datastructures.Experiment;
import imports.ActivityRegistry;
import imports.App_Methodes;
import imports.ExpandableListAdapterEntries;
import datastructures.ProjectExperimentEntry;
import imports.Popup;
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_entry_list);
        LinkedList<Entry> entries = new LinkedList<Entry>();
        LocalService service = mservice.get();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        experiment = (Experiment) b.getSerializable("experiment");
        service.getDB().open();
        entries = service.getDB().getGetEntryByExperiment(experiment);
        service.getDB().close();
        textview1 = (TextView)findViewById(R.id.textview1);
        textview1.setText(experiment.get_name());
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapterEntries(this, entries);

        try {
            expListView.setAdapter(listAdapter);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

// get the listview



        ActivityRegistry.register(this);
    }
         /*
         * Preparing the list data
         */
    /*
    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataDate   = new ArrayList<String>();
        listDataChild  = new HashMap<String, List<String>>();
        debugList = new ArrayList<String>();

        // Adding child data
        try {
            for (int i = 0; i < projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().size(); i++) {
                img.add(projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).isSync());
                listDataHeader.add(projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getTitle());
           /*    if(!projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getEntry_time()) {
                   Long long1 = projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getEntry_time()*1000;
                   listDataDate.add("   entry date:  " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getEntry_time()*1000))); // ("dd/MM/yyyy HH:mm:ss") Für minuten stunden etc ...
               }
                else {
                   listDataDate.add("empty");
               }

                List<String> list = new ArrayList<String>();
                switch (projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getAttachment().getTypeNumber()) {
                    case 1:
                        list.add(projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getAttachment().getContent().toString());

                  break;
                    case 2:
                     Collections.addAll(list, App_Methodes.StringToArray(projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getAttachment().getContent().toString()));
                  break;
                }

expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        long packedPosition = expListView.getExpandableListPosition(position);
                        if (ExpandableListView.getPackedPositionType(packedPosition) ==
                                ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

                            entry_Selected = position;
                            startnew_action1();
                            return true;
                        }
                        return false;
                    }
                });

                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) { // Setting the onclick listener for a Child obj.
                        entry_Selected = groupPosition;
                        startnew_action1();
                        return false;
                    }
                });

                listDataChild.put(listDataHeader.get(i), list);
            }
        } catch (Exception ignored) {
        }
    }*/
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

                Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sync:

                Toast.makeText(getApplicationContext(), "sync", Toast.LENGTH_SHORT).show();
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
        intent.putExtra("id",experiment.get_id());
        startActivity(intent);
    }
    private void start_New_ActionTable(){
        Intent intent;
        intent = new Intent(this, RowAndColumnPopup.class);
        intent.putExtra("id",experiment.get_id());
        startActivity(intent);
    }

    /**
     * Starts the new Intent for Detail view of LocalEntry
     */
    private void start_New_ActionImage(){
        Intent intent;
        intent = new Intent(this, Gui_NewImageEntry.class);
        intent.putExtra("id",experiment.get_id());
        startActivity(intent);
    }



}
