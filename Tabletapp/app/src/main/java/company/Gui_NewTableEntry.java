package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import com.example.test1.tabletapp.app.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import datastructures.AttachmentTable;
import datastructures.Experiment;

import imports.ActivityRegistry;
import imports.App_Methodes;
import imports.Popup;

/**
 * Created by Grit on 03.08.2014.
 */
public class Gui_NewTableEntry extends Activity {

    private TableLayout table;
    EditText text;
    EditText text1;
    EditText text2;
    private int cols;
    private int rows;
    Experiment experiment;
    private String[][] string_array;
    Reference<LocalService> ref = new WeakReference<LocalService>(Gui_StartActivity.getmService());
    LocalService service;
    private EditText[][] textView_array;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guinewtableentry);
        //doBindService();
        ActivityRegistry.register(this);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        experiment = (Experiment) b.getSerializable("experiment");
     //   Log.e("Experiment name",experiment.get_name());
        rows = b.getInt("row");
        cols = b.getInt("column");
        // Receiving the Data
        service = ref.get();



        text = (EditText)findViewById(R.id.editText2);

        text1 = (EditText)findViewById(R.id.editText1);

        text2 = (EditText)findViewById(R.id.editText2);
      // text2.setVisibility(View.INVISIBLE);
        table = (TableLayout)findViewById(R.id.tableLayout1);
       // table.setVisibility(View.INVISIBLE);

        table.removeAllViews();
        textView_array = new EditText[rows][cols];
        string_array = new String[rows][cols];

        BuildTable(rows, cols);



    } // Standart Android Methoden für apps



    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button3:
                Log.d("Button 3", "Button finish gedrückt");

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        try {

                            EditText temp = textView_array[i][j];
                            if (!(temp.getText().toString().isEmpty()))
                                string_array[i][j] = temp.getText().toString();
                        } catch (NullPointerException e) {
                        }
                    }

                }
                if (!(text.getText().toString().isEmpty())) {

                    try {



                        long time = App_Methodes.generateTimestamp();
                        service.getObjectlevel_db().new_Entry(service.getUser(),experiment,text.getText().toString(),new AttachmentTable(App_Methodes.twoDArray2String(string_array).trim()),time);
                        this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Popup popup = new Popup();            // Popup für leeren title
                    popup.set_String(R.string.popup4);
                    popup.show(getFragmentManager(), "this");
                }





        // finish button

        break;


    }




    }




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
                tv.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                tv.setPadding(15, 15, 15, 15);
              //  tv.setText("R " + i + ". C" + j); // TODO Delete this to get empty textfields in the table
                tv.isInEditMode();
                row.addView(tv);
                row.setGravity(Gravity.CENTER);

            }
        //  table.setGravity(Gravity.CENTER);
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






}
