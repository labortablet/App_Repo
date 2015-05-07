package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

import datastructures.Project;
import imports.ActivityRegistry;

/**
 * Created by Grit on 20.08.2014.
 */

public class Gui_DisplayProjectDetails extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_project_details);
        ActivityRegistry.register(this);
        TextView text = (TextView)findViewById(R.id.textView2);
        TextView text2 = (TextView)findViewById(R.id.textView4);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        Project project = (Project) b.getSerializable("Project");
        text.setText(project.get_name());
        text2.setText(project.get_description());
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
}