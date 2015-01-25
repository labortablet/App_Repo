package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import com.example.test1.tabletapp.app.R;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import imports.App_Methodes;
import imports.AttachmentTable;
import imports.AttachmentText;
import imports.LocalEntry;

/**
 * Created by Grit on 03.08.2014.
 */
public class Table_entry extends Activity {

    private TableLayout table;
    private EditText text;
    private View textview;
    private  int cols;
    private int rows;
    private String[][] string_array;
    private EditText[][] textView_array;
    private Integer project_Selected = Project_show.getProject_Selected();
    private Integer experiment_Selected = Project_show.getExperiment_Selected();
    private Calendar calendar;
    private static List<ProjectExperimentEntry> projectExperimentEntries = Project_show.getProjectExperimentEntries();
    public void buttonEventHandler(View v) {  // butten events

        switch(v.getId()) {  // switch ID button

            case R.id.button:
               ActivityRegistry.finishAll();
                System.exit(0);
                break;
            case R.id.button2:
                this.finish();
                break;
            case R.id.button3:


          for(int i = 0;i < rows;i++ )
                {
                    for (int j = 0; j < cols; j++)
                    {
                        try {

                            EditText temp = textView_array[i][j];
                        //    Integer jj =textView_array.length;
                            if(!(temp.getText().toString().isEmpty()))
                            string_array[i][j] = temp.getText().toString();
                        }
                        catch (NullPointerException e)
                        {}
                    }

                }



                if ( !(text.getText().toString().isEmpty()))
            {
                if(!unique_Test(text.getText().toString())){
                try {



                String title1 = text.getText().toString();

//TODO: Fix table entry String title, AttachmentTable attachment,int attachmentTyp,Long  entry_time, User user, boolean sync,int Experiment_id
               LocalEntry table_entry =  new LocalEntry(title1,new AttachmentTable(App_Methodes.twoDArray2String(string_array)),App_Methodes.generateTimestamp(),Start.getUser(),false,projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getExperiments().get_id());
                    Start.myDb.open();
                    Start.myDb.insertLocalEntry(table_entry);
                    Start.myDb.close();
                    projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().add(table_entry);
                    Project_show.setProjectExperimentEntries(projectExperimentEntries);
                    this.finish();

            }
            catch (Exception e )
            {}
            }
                else
                {
                    Popup popup = new Popup();            // Popup für leeren title
                    popup.set_String(R.string.popup4);
                    popup.show(getFragmentManager(),"this");
                }

            }
            else
            {
                Popup popup = new Popup();            // Popup für leeren title
                popup.set_String(R.string.popup3);
                popup.show(getFragmentManager(),"this");
            }


            // finish button

            break;






        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tableentry);
        //doBindService();
        ActivityRegistry.register(this);

        // Receiving the Data
        Intent i = getIntent();
        text = (EditText)findViewById(R.id.editText);
        table = (TableLayout)findViewById(R.id.tableLayout1);


        table.removeAllViews();
        rows = Integer.parseInt(i.getStringExtra("row"));

        cols = Integer.parseInt(i.getStringExtra("column"));
        textView_array = new EditText[rows][cols];
        string_array = new String[rows][cols];
        BuildTable(rows, cols);

    } // Standart Android Methoden für apps


    private void BuildTable(int rows, int cols) {

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));


            // inner for loop
            for (int j = 0; j < cols; j++) {

                EditText tv = new EditText(this);
                textView_array[i][j] = tv;
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));

      //      tv.addTextChangedListener(watcher);

                tv.setPadding(5, 5, 5, 5);

                tv.setText("R " + i + ". C" + j);
           //     watcher.onTextChanged(tv.getText(), i, j, 0);
         //       watcher.afterTextChanged(tv.getText());

                tv.isInEditMode();
               // tv.setOnEditorActionListener();


                row.addView(tv);


            }

            table.addView(row);

        }}

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

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = myFormat.format(new Date()); // Getting the actual date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
