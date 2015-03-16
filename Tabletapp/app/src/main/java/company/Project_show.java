package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import AsyncTasks.AsyncTaskProjectExperimentEntry;
import imports.Experiment;
import imports.LocalEntry;
import imports.Project;
import imports.ProjectExperimentEntry;

public class Project_show extends Activity {

    private static int experiment_Selected;
    private static int project_Selected;
    private static List<ProjectExperimentEntry> projectExperimentEntries;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public static int getExperiment_Selected() {
        return experiment_Selected;
    }
    public static int getProject_Selected() {
        return project_Selected;
    }

    public static void setProjectExperimentEntries(List<ProjectExperimentEntry> projectExperimentEntries) {
        Project_show.projectExperimentEntries = projectExperimentEntries;
    }

    public static List<ProjectExperimentEntry> getProjectExperimentEntries() {
        return projectExperimentEntries;
    }

    @Override

        public void onCreate(Bundle savedInstanceState) {
        try {
            projectExperimentEntries = new AsyncTaskProjectExperimentEntry().execute(Start.myDb).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_show);
        ActivityRegistry.register(this);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
       // preparing list data
        prepareListData();
       listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
       expListView.setAdapter(listAdapter);

    }
        /*
         * Preparing the list data
         */
        private void prepareListData() {
            try{
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
        for(int i = 0; i < projectExperimentEntries.size();i++) {

            listDataHeader.add(projectExperimentEntries.get(i).getProject().get_name());
            List<String> list = new ArrayList<String>();

            for (int j = 0; j < projectExperimentEntries.get(i).getExperimentEntry().size(); j++) {

                list.add(projectExperimentEntries.get(i).getExperimentEntry().get(j).getExperiments().get_name());
            }
            listDataChild.put(listDataHeader.get(i), list);

        }}catch (NullPointerException e)
            {
                e.printStackTrace();
            }


// Listview on child click listener

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                experiment_Selected = childPosition;
                project_Selected = groupPosition;
                startNew_action();
                return false;
            }
        });
            expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    long packedPosition = expListView.getExpandableListPosition(position);
                    if (ExpandableListView.getPackedPositionType(packedPosition) ==
                            ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        // get item ID's
                        startNew_action1(projectExperimentEntries.get(ExpandableListView.getPackedPositionGroup(packedPosition)).getExperimentEntry().get(ExpandableListView.getPackedPositionChild(packedPosition)).getExperiments().get_name(), projectExperimentEntries.get(ExpandableListView.getPackedPositionGroup(packedPosition)).getExperimentEntry().get(ExpandableListView.getPackedPositionChild(packedPosition)).getExperiments().get_description());
                    }
                        if (ExpandableListView.getPackedPositionType(packedPosition) ==
                                ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                            startNew_action2(projectExperimentEntries.get(ExpandableListView.getPackedPositionGroup(packedPosition)).getProject().get_name(),projectExperimentEntries.get(ExpandableListView.getPackedPositionGroup(packedPosition)).getProject().get_description());

                        // return true as we are handling the event.
                        return true;
                    }
                    return false;
                }
            });
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
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
    private void startNew_action() {
        Intent intent;
        intent = new Intent(this, Gui_DisplayEntryDetails.class);
        startActivity(intent);
    }
    private void startNew_action1(String name,String description){
        Intent intent;
        intent = new Intent(this, Gui_DisplayExperimentDetails.class);
        intent.putExtra("name",name);
        intent.putExtra("description", description);
        startActivity(intent);
    }
    private void startNew_action2(String name,String description){
        Intent intent;
        intent = new Intent(this, Gui_DisplayProjectDetails.class);
        intent.putExtra("name",name);
        intent.putExtra("description", description);
        startActivity(intent);

    }
}
