package company;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.test1.tabletapp.app.R;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import imports.App_Methodes;
import imports.AttachmentBase;
import imports.AttachmentText;
import imports.LocalEntry;
import imports.Lablet_Functions;

/**
 * Created by Grit on 03.06.2014.
 */
public class Keyboard_entry extends Activity {
    //    TextView text = (TextView) findViewById(R.id.textView);
    EditText content; // inhalt des entries
    private Integer project_Selected = Project_show.getProject_Selected();
    private Integer experiment_Selected = Project_show.getExperiment_Selected();
    private Lablet_Functions lablet = new Lablet_Functions();
   private static LocalService loc = Start.mService;
    EditText title; // titel des entries
    private static List<ProjectExperimentEntry> projectExperimentEntries = Project_show.getProjectExperimentEntries();



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
                   if (!unique_Test(title.getText().toString())) {
                        try {
                            LocalEntry edit = new LocalEntry(title.getText().toString(), new AttachmentText(content.getText().toString().trim()), App_Methodes.generateTimestamp(), Start.mService.getUser(),false,projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getExperiments().get_id());
                            Start.myDb.open();
                            Start.myDb.insertLocalEntry(edit);
                            Start.myDb.close();
                            projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().add(edit);
                            Project_show.setProjectExperimentEntries(projectExperimentEntries);
                            this.finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Popup popup = new Popup();            // Popup für leeren title
                        popup.set_String(R.string.popup4);
                        popup.show(getFragmentManager(), "this");
                    }}
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


