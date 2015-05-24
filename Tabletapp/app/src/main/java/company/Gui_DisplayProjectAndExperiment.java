package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import datastructures.Experiment;
import datastructures.Project;
import exceptions.SBSBaseException;
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
    Reference<LocalService> mservice = new WeakReference<LocalService>(Gui_StartActivity.getmService());
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
        LocalService service = mservice.get();

        try {
            projects.addAll(service.getObjectlevel_db().get().get_projects(service.getUser()));
        } catch (SBSBaseException e) {
            e.printStackTrace();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_project_experiment_list);
        ActivityRegistry.register(this);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

       listAdapter = new ExpandableListAdapterProjectsAndExperiments(this, projects,service);


        try {
            expListView.setAdapter(listAdapter);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
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
}
