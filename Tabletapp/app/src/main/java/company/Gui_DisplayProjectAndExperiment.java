package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import datastructures.Experiment;
import datastructures.Project;
import imports.ActivityRegistry;
import imports.ExpandableListAdapterProjectsAndExperiments;
import datastructures.ProjectExperimentEntry;

public class Gui_DisplayProjectAndExperiment extends Activity {

    private static int experiment_Selected;
    private static int project_Selected;
    private static List<ProjectExperimentEntry> projectExperimentEntries;
    ExpandableListAdapterProjectsAndExperiments listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    LinkedList<Project> projects = new LinkedList<Project>();

    public static int getExperiment_Selected() {
        return experiment_Selected;
    }
    public static int getProject_Selected() {
        return project_Selected;
    }

    public static void setProjectExperimentEntries(List<ProjectExperimentEntry> projectExperimentEntries) {
        Gui_DisplayProjectAndExperiment.projectExperimentEntries = projectExperimentEntries;
    }

    public static List<ProjectExperimentEntry> getProjectExperimentEntries() {
        return projectExperimentEntries;
    }

    @Override

        public void onCreate(Bundle savedInstanceState) {
    /*    try {
            projectExperimentEntries = new AsyncTaskProjectExperimentEntry().execute(Gui_StartActivity.myDb).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        // dummidata
        HashMap<Integer,List<Experiment>> hashMap = new HashMap<Integer, List<Experiment>>();
        projects.add(new Project(0,"testproject0"));
        projects.add(new Project(1,"testproject1"));

        List<Experiment> list = new ArrayList<Experiment>();
        list.add(new Experiment(0,0,"experiment test 1"));
        list.add(new Experiment(1,0,"experiment test 2"));
        list.add(new Experiment(2,0,"experiment test 3"));
        hashMap.put(0,list);
        list.clear();

        list.add(new Experiment(3,1,"experiment test 4"));
        list.add(new Experiment(4,1,"experiment test 5"));
        hashMap.put(1,list);

        // dummidata

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_project_experiment_list);
        ActivityRegistry.register(this);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
       // preparing list data
     //   prepareListData();
       listAdapter = new ExpandableListAdapterProjectsAndExperiments(this, projects, hashMap);
        // setting list adapter
        try {
            expListView.setAdapter(listAdapter);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
        /*
         * Preparing the list data
         */
    /*
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
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_refresh:
                // refresh
              //  Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                // refresh
                Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sync:
                // refresh
                Toast.makeText(getApplicationContext(), "sync", Toast.LENGTH_SHORT).show();
                return true;
            // help action



            default:
                return super.onOptionsItemSelected(item);
        }
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
        intent = new Intent(this, Gui_DisplayEntryList.class);
        startActivity(intent);
    }
    private void startNew_action1(String name,String description){
        Intent intent;
        intent = new Intent(this, Gui_DisplayExperimentDetails.class);
        intent.putExtra("name",name);
        intent.putExtra("description", description);
        startActivity(intent);
    }
    private void startNew_action2(Project project){
        Intent intent;
        intent = new Intent(this, Gui_DisplayProjectDetails.class);
        intent.putExtra("Project", project);

    }    /**
     * On selecting action bar icons
     * */

}
