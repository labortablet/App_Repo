package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import datastructures.Project;

import exceptions.SBSBaseException;
import imports.ActivityRegistry;
import imports.ExpandableListAdapterProjectsAndExperiments;

public class Gui_DisplayProjectAndExperiment extends Activity {


    ExpandableListAdapterProjectsAndExperiments listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    Reference<LocalService> mservice = new WeakReference<LocalService>(Gui_StartActivity.getmService());
    HashMap<String, List<String>> listDataChild;
    LinkedList<Project> projects = new LinkedList<Project>();





    @Override

        public void onCreate(Bundle savedInstanceState) {
        LocalService service = mservice.get();
   //     service.getDB().open();
//projects.addAll(service.getDB().getAllProjectRowsLinkedList());
     //   service.getDB().close();

        try {

                       projects.addAll(service.getObjectlevel_db().get_projects(service.getUser()));
            for (int i = 0;projects.size() > i;i++){
               Log.d("project NR." + i, projects.get(i).get_name());
            }

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
                startActivity(new Intent(getApplicationContext(), Gui_Settings.class));
                return true;
            case R.id.sync:
                long tenMinutes = 600000;
                long millis = System.currentTimeMillis() - Gui_StartActivity.mService.getTimeTillSync();
                long secs = (tenMinutes - millis) / 1000;
                long mins = secs / 60;
                Toast.makeText(getApplicationContext(),"Next Sync in"+mins+" Minutes", Toast.LENGTH_SHORT).show();
                return true;
            // help action
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
