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

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import imports.AttachmentTable;
import imports.AttachmentText;

/**
 * @author  Grit on 19.07.2014.
 *
 * Shows the Specific Entries of an Experiement
 */
public class Entry_show extends Activity {

    private Integer experiment_Selected = Project_show.getExperiment_Selected();
    private Integer project_Selected = Project_show.getProject_Selected();
    private static List<ProjectExperimentEntry> projectExperimentEntries = Project_show.getProjectExperimentEntries();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listDataDate;
    List<String> debugList;
    private Context _context;


    private ArrayList<Boolean> img =  new ArrayList<Boolean>();
    View convertView ;
    HashMap<String, List<String>> listDataChild;
    public static Integer entry_Selected;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_show);
        //doBindService();

// get the listview
        this._context = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data

            prepareListData();
        for (String aDebugList : debugList) Log.d("debug",aDebugList);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, img, listDataDate);



        // setting list adapter
        expListView.setAdapter(listAdapter);

        ActivityRegistry.register(this);
    }
         /*
         * Preparing the list data
         */
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

               if(!projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getEntry_time().toString().isEmpty()) {
                   listDataDate.add("   entry date: " + projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getEntry_time());
               }
                else {
                   listDataDate.add("empty");

               }



                List<String> list = new ArrayList<String>();
                switch (projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getAttachment_type()) {
                    case 1:

                        AttachmentText text = (AttachmentText) projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getAttachment();
                        list.add(  text.getText() );
                        debugList.add(text.getText());
                        break;

                    case 2:
                        AttachmentTable table = (AttachmentTable) projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getAttachment();
                        String[][] strings = table.getTable_array();
                        for (String[] s : strings) {
                            String temp = "";
                            for (String string : s) {

                                temp += (string + ",");
                            }
                            temp = temp.substring(0, temp.length() - 1);
                            list.add(temp + ";");
                        }
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
    }
    /**
     * Returns the Selected LocalEntry.
     * @return    ID of Selected entry.
     */
    public static Integer getEntry_Selected() {
        return entry_Selected;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;

    }  // Standart Android Methoden für apps

    /**
     * Android Lifecycle method
     * After Adding a new LocalEntry Reload the List of Entries
     */
    protected void onResume(){
        img.clear();
        listDataChild.clear();
        listDataDate.clear();
        listDataHeader.clear();
        super.onResume();
        projectExperimentEntries = Project_show.getProjectExperimentEntries();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild,img,listDataDate);
        expListView.setAdapter(listAdapter);
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
        intent = new Intent(this, New_Entry_Select.class);
        startActivity(intent);

    }
    /**
     * Starts the new Intent for Detail view of LocalEntry
     */
    private void startnew_action1(){
        Intent intent;
        intent = new Intent(this, Entry_Details.class);
        startActivity(intent);

    }



}
