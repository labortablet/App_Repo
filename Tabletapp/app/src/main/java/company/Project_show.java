package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import exceptions.SBSBaseException;
import imports.Experiment;
import imports.LocalEntry;
import imports.Project;
import scon.RemoteEntry;
import imports.User;
import scon.RemoteExperiment;
import scon.RemoteProject;

public class Project_show extends Activity {

    private static int experiment_Selected;
    private static int project_Selected;
    private User user;


    private static List<ExperimentEntry>  experimentEntries;
    private static List<ExperimentEntry>  experimentEntries1;
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
        Boolean test = Start.mService.Create_DB();
        Toast.makeText(this,test.toString(),Toast.LENGTH_SHORT).show();
        try {
            @SuppressWarnings("unchecked")
            LinkedList<RemoteProject> remoteProject_list = Start.mService.getProjects();
            @SuppressWarnings("unchecked")
            LinkedList<RemoteExperiment> remoteExperiment_list = Start.mService.getExperiments();
            @SuppressWarnings("unchecked")
            LinkedList<RemoteEntry> remoteEntry_list = Start.mService.getEntries();
            ArrayList<LocalEntry> entries ;



            projectExperimentEntries = new ArrayList<ProjectExperimentEntry>();


           for(int i = 0; i < remoteProject_list.size();i++){
                experimentEntries = new ArrayList<ExperimentEntry>();
                projectExperimentEntries.add(new ProjectExperimentEntry(new Project(remoteProject_list.get(i)),experimentEntries));
            }

            for(int i = 0 ;i < remoteExperiment_list.size();i++) {

               for(int j = 0 ; j< remoteProject_list.size();j++){

                    if (projectExperimentEntries.get(j).getProject().get_id().equals(remoteExperiment_list.get(i).get_project_id()))
                    {
                      entries = new ArrayList<LocalEntry>();
                      projectExperimentEntries.get(j).getExperimentEntry().add(new ExperimentEntry(new Experiment(remoteExperiment_list.get(i)), entries));
                      break;
                    }
                }
            }

            for(int i = 0 ; i<remoteEntry_list.size(); i++)
            {        int ID = 0;
                     for(int j = 0 ; j< projectExperimentEntries.size();j++) {
                         for(int k = 0;k< projectExperimentEntries.get(j).getExperimentEntry().size();k++)
                         {
                      if(projectExperimentEntries.get(j).getExperimentEntry().get(k).getExperiments().get_id().equals(remoteEntry_list.get(i).getExperiment_id()))
                      {
                          projectExperimentEntries.get(j).getExperimentEntry().get(k).getEntriesList().add(new LocalEntry(remoteEntry_list.get(i)));

                          break;

                      }
                         }
                    }
                }




        }catch (SBSBaseException e) {
            e.printStackTrace();
        }catch (NullPointerException ignored) {

        }

        user = Start.getUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_show);
        ActivityRegistry.register(this);
        /*
        RemoteExperiment remote;
        List<LocalEntry> entries;
        List<LocalEntry> entries1;
        List<LocalEntry> entries2;
        entries = new ArrayList<LocalEntry>() ;
        entries1 = new ArrayList<LocalEntry>() ;
        entries2 = new ArrayList<LocalEntry>();

        entries1.add(new LocalEntry(1,"hallo","Inhalt 1","","",user,true));
        entries1.add(new LocalEntry(2,"hallo1","Inhalt 2","","",user,true));

        entries2.add(new LocalEntry(1,"1234","Inhalt 1","","",user,true  ));
        entries2.add(new LocalEntry(2,"1342","Inhalt 2","","",user,true));

        entries.add(new LocalEntry(1,"test","","","",user,true));
        entries.add(new LocalEntry(2,"test2","","","",user,true));
        entries.add(new LocalEntry(1,"test3","","","",user,true));
        entries.add(new LocalEntry(2,"test4","","","",user,true));
        experimentEntries = new ArrayList<ExperimentEntry>();
        experimentEntries1 = new ArrayList<ExperimentEntry>();
        remote = new RemoteExperiment(1,1,"Experiment3","beschreibung1");
        experimentEntries1.add(new ExperimentEntry(new Experiment(remote),entries2));

        projectExperimentEntries = new ArrayList<ProjectExperimentEntry>();

        remote = new RemoteExperiment(1,1,"Experiment2","beschreibung1");
        experimentEntries.add(new ExperimentEntry(new Experiment(remote),entries));
        remote = new RemoteExperiment(2,1,"Experiment1","beschreibung2");
        experimentEntries.add(new ExperimentEntry(new Experiment(remote),entries1));


        RemoteProject rempro = new RemoteProject(1,"Project1","Beschreibung 1");
        projectExperimentEntries.add(new ProjectExperimentEntry(new Project(rempro),experimentEntries));
        rempro = new RemoteProject(1,"Project2","Beschreibung 2");
        projectExperimentEntries.add(new ProjectExperimentEntry(new Project(rempro),experimentEntries1));


*/
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
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
        for(int i = 0; i< projectExperimentEntries.size();i++) {

            listDataHeader.add(projectExperimentEntries.get(i).getProject().get_name());
            List<String> list = new ArrayList<String>();

            for (int j = 0; j < projectExperimentEntries.get(i).getExperimentEntry().size(); j++) {

                list.add(projectExperimentEntries.get(i).getExperimentEntry().get(j).getExperiments().get_name());
            }
            listDataChild.put(listDataHeader.get(i), list);

        }



// Listview on child click listener
 /*
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener(){

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                startNew_action2(projectExperimentEntries.get(groupPosition).getProject().get_name(),projectExperimentEntries.get(groupPosition).getProject().get_description());
                return false;
            }
        }); */
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

    private void startNew_action(){
        Intent intent;
        intent = new Intent(this, Entry_show.class);
        startActivity(intent);

    }
    private void startNew_action1(String name,String description){
        Intent intent;
        intent = new Intent(this, Experiment_Details.class);
        intent.putExtra("name",name);
        intent.putExtra("description", description);
        startActivity(intent);

    }
    private void startNew_action2(String name,String description){
        Intent intent;
        intent = new Intent(this, Project_Details.class);
        intent.putExtra("name",name);
        intent.putExtra("description", description);
        startActivity(intent);

    }
}
