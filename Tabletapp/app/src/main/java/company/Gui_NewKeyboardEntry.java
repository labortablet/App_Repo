package company;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.test1.tabletapp.app.R;

import java.util.List;

import imports.ActivityRegistry;
import imports.App_Methodes;
import datastructures.AttachmentText;
import datastructures.Entry;

import imports.Popup;
import datastructures.ProjectExperimentEntry;

/**
 * Created by Grit on 03.06.2014.
 */
public class Gui_NewKeyboardEntry extends Activity {
    EditText content; // inhalt des entries
    private Integer project_Selected = Gui_DisplayProjectAndExperiment.getProject_Selected();
    private Integer experiment_Selected = Gui_DisplayProjectAndExperiment.getExperiment_Selected();
   private static LocalService loc = Gui_StartActivity.mService;
    EditText title; // titel des entries
    private static List<ProjectExperimentEntry> projectExperimentEntries = Gui_DisplayProjectAndExperiment.getProjectExperimentEntries();



    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button:

                this.finish();         // back button
                break;


            case R.id.button2:

                ActivityRegistry.finishAll(); // exit button
                System.exit(0);
                break;

            case R.id.button3:
                if (!(title.getText().toString().trim().isEmpty())) {
                  /* if (!unique_Test(title.getText().toString())) {
                        try {
                            Entry edit = new Entry(title.getText().toString(), new AttachmentText(content.getText().toString().trim()), App_Methodes.generateTimestamp(), Gui_StartActivity.mService.getUser(),false,projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getExperiments().get_id());
                            Gui_StartActivity.myDb.open();
                            Gui_StartActivity.myDb.insertLocalEntry(edit);
                            Gui_StartActivity.myDb.close();
                            projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().add(edit);
                            Gui_DisplayProjectAndExperiment.setProjectExperimentEntries(projectExperimentEntries);
                            this.finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Popup popup = new Popup();            // Popup für leeren title
                        popup.set_String(R.string.popup4);
                        popup.show(getFragmentManager(), "this");
                    }*/}
                 else {
                    Popup popup = new Popup();            // Popup für leeren title
                    popup.set_String(R.string.popup3);
                    popup.show(getFragmentManager(), "this");


                }
                // finish button

                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_keyboardentry);
        ActivityRegistry.register(this);

        title = (EditText) findViewById(R.id.editText2);
        content = (EditText) findViewById(R.id.editText);
        content.setGravity(Gravity.CENTER);
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




     private boolean unique_Test(String string) {
        boolean unique = false;


        for (int i = 0;  i < projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().size(); i++) {
            if (projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(i).getTitle().equals(string))
            {
              unique = true;
                break;
            }
            else
                unique = false;
        }
return unique;
    }
}


