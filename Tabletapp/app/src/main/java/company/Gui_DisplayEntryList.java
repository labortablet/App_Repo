package company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import datastructures.AttachmentText;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.User;
import imports.ActivityRegistry;
import imports.App_Methodes;
import imports.ExpandableListAdapterProjectsAndExperiments;
import datastructures.ProjectExperimentEntry;

/**
 * @author  Grit on 19.07.2014.
 *
 * Shows the Specific Entries of an Experiement
 */
public class Gui_DisplayEntryList extends Activity {

    private Integer experiment_Selected = Gui_DisplayProjectAndExperiment.getExperiment_Selected();
    private Integer project_Selected = Gui_DisplayProjectAndExperiment.getProject_Selected();
    private static List<ProjectExperimentEntry> projectExperimentEntries = Gui_DisplayProjectAndExperiment.getProjectExperimentEntries();
    ExpandableListAdapterProjectsAndExperiments listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listDataDate;
    List<String> debugList;
    private Context _context;



    View convertView ;
    HashMap<String, List<String>> listDataChild;
    public static Integer entry_Selected;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_show);
        LinkedList<Entry> entries = new LinkedList<Entry>();
        entries.add(new Entry(0,new datastructures.User("grit","test"),0,"test Entry 1",new AttachmentText("inhalt von entry 1"),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp()));
        entries.add(new Entry(1,new datastructures.User("grit","test"),0,"test Entry 2",new AttachmentText("inhalt von entry 2"),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp()));
        entries.add(new Entry(2,new datastructures.User("grit","test"),0,"test Entry 3",new AttachmentText("inhalt von entry 3"),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp()));
        entries.add(new Entry(3,new datastructures.User("grit","test"),0,"test Entry 4",new AttachmentText("inhalt von entry 4"),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp(),App_Methodes.generateTimestamp()));

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        Experiment experiment = (Experiment) b.getSerializable("experiment");
        TextView textview1 = (TextView)findViewById(R.id.textview1);
        textview1.setText("Active Experiment: " + experiment.get_name());
        //TODO: funktions call für linkedlist mit entries zugehörig dem experiment
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        //   prepareListData();
      //  listAdapter = new ExpandableListAdapterProjectsAndExperiments(this, projects, hashMap);
        // setting list adapter
        try {
            expListView.setAdapter(listAdapter);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

// get the listview
        this._context = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        expListView.setAdapter(listAdapter);


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
    /**
     * Returns the Selected LocalEntry.
     * @return    ID of Selected entry.
     */
    public static Integer getEntry_Selected() {
        return entry_Selected;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_entry_list, menu);
        return true;

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }  // Standart Android Methoden für apps

    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button: // back
             this.finish();

                break;
            case R.id.button2 :
                ActivityRegistry.finishAll(); // exit button
              System.exit(0);

                break;
            case R.id.button3:      //new entry
                startnew_action();

        }
    }

    /**
     * Starts the new Intent for creating new Entries
     */
    private void startnew_action(){
        Intent intent;
        intent = new Intent(this, Gui_DisplayNewEntrySelection.class);
        startActivity(intent);

    }
    private String usingDateFormatter(long input){
        Date date = new Date(input);
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss z");
        sdf.setCalendar(cal);
        cal.setTime(date);
        return sdf.format(date);

    }
    /**
     * Starts the new Intent for Detail view of LocalEntry
     */
    private void startnew_action1(){
        Intent intent;
        intent = new Intent(this, Gui_DisplayEntryDetails.class);
        startActivity(intent);

    }



}
