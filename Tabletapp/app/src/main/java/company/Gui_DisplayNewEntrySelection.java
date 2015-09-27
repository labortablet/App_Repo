package company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.test1.tabletapp.app.R;

import imports.ActivityRegistry;

/**
 * Created by Grit on 31.07.2014.
 */
public class Gui_DisplayNewEntrySelection extends Activity{
 private EditText column;
 private EditText row;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry_desition);
        //doBindService();
        ActivityRegistry.register(this);
       column = (EditText) findViewById(R.id.editText);
       row = (EditText) findViewById(R.id.editText1);


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

    public void buttonEventHandler(View v) {  // butten events

        switch(v.getId()) {  // switch ID button
        case R.id.button:
            this.finish();
            break;
            case R.id.button2:
                ActivityRegistry.finishAll();
                System.exit(0);
                break;
            case R.id.button3: // new Keyboard
      start_NewActionKeyboard();
                this.finish();
                break;
            case R.id.button4: // new Table
                try {
                    if (!row.getText().toString().isEmpty() && !column.getText().toString().isEmpty())
                    {
                        start_NewActionTable(row.getText().toString(),column.getText().toString());
                        this.finish();
                    }
                }
                catch (Exception e)
                {

                }

                break;
        }


    }

    private void start_NewActionKeyboard(){

        Intent intent;
        intent = new Intent(this, Gui_NewKeyboardEntry.class);
        startActivity(intent);



    }

    private void start_NewActionTable(String r1,String c1){

        Intent intent;
        intent = new Intent(this, Gui_NewTableEntry.class);
        intent.putExtra("row",r1);
        intent.putExtra("column",c1);
        startActivity(intent);



    }
}
