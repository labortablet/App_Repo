package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.test1.tabletapp.app.R;

import java.lang.ref.WeakReference;

import imports.ActivityRegistry;

/**
 * Created by Grit on 03.06.2014.
 */
public class Gui_NewKeyboardEntry extends Activity {
    EditText content; // inhalt des entries
    EditText title;
    private WeakReference<LocalService> mSerive = new WeakReference<LocalService>(Gui_StartActivity.mService);
    Long experimentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guinewkeyboardentry);
        ActivityRegistry.register(this);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        assert b != null;

        experimentID = b.getLong("id");

        title = (EditText) findViewById(R.id.editText);
        content = (EditText) findViewById(R.id.editText1);
        content.setGravity(Gravity.CENTER);
    } // Standart Android Methoden für apps

    //public void buttonEventHandler(View v) {  // butten events
/*
        switch (v.getId()) {  // switch ID button

            case R.id.button3:
                if (!(title.getText().toString().trim().isEmpty()) && !(content.getText().toString().trim().isEmpty())) {
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
                    }}
                 else {
                    Popup popup = new Popup();            // Popup für leeren title
                    popup.set_String(R.string.popup3);
                    popup.show(getFragmentManager(), "this");
                }
                // finish button

                break;

        }*/
    //}






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }  // Standart Android Methoden für apps

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }  // Standart Android Methoden für apps
}


